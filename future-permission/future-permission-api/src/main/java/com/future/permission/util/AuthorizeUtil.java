package com.future.permission.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.future.common.base.UserInfo;
import com.future.common.constant.PlatformConst;
import com.future.common.emnus.SearchMethodEnum;
import com.future.common.model.visualJson.FieLdsModel;
import com.future.common.model.visualJson.config.ConfigModel;
import com.future.common.util.*;
import com.future.database.model.superQuery.SuperJsonModel;
import com.future.database.model.superQuery.SuperQueryJsonModel;
import com.future.module.system.model.resource.ResourceModel;
import com.future.permission.*;
import com.future.permission.entity.OrganizeEntity;
import com.future.permission.model.authorize.AuthorizeConditionEnum;
import com.future.permission.model.authorize.AuthorizeVO;
import com.future.permission.model.authorize.ConditionModel;
import com.future.reids.util.RedisUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 获取权限信息工具类
 *
 * @author Future Platform Group
 * @version v4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2022/12/13 16:30:41
 */
@Slf4j
@Component
public class AuthorizeUtil {

    @Autowired
    public OrganizeApi organizeApi;
    @Autowired
    private UserProvider userProvider;
    @Autowired
    private OrganizeAdminTratorApi organizeAdminIsTratorApi;
    @Autowired
    private UserSettingApi userSettingApi;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private CacheKeyUtil cacheKeyUtil;

    public AuthorizeVO getAuthorize() {
        AuthorizeVO authorize = userSettingApi.getAuthorize();
        return authorize;
    }

    public List<SuperJsonModel> getConditionSql(String moduleId) {
        List<SuperJsonModel> list = new ArrayList<>();
        UserInfo userInfo = userProvider.get();
        String reidsKey = cacheKeyUtil.getUserAuthorize() + moduleId + "_" + userInfo.getUserId();
        long time = 60 * 5;
        if (redisUtil.exists(reidsKey)) {
            return JsonUtil.getJsonToList(redisUtil.getString(reidsKey).toString(), SuperJsonModel.class);
        }
        AuthorizeVO model = this.getAuthorize();
        if(model == null){
            redisUtil.insert(reidsKey, JsonUtil.getObjectToString(new ArrayList<>()), time);
            return new ArrayList<>();
        }
        List<ResourceModel> resourceListAll = model.getResourceList().stream().filter(m -> m.getModuleId().equals(moduleId)).collect(Collectors.toList());
        //先遍历一次 查找其中有没有全部方案
        boolean isAll = resourceListAll.stream().filter(item -> "future_alldata".equals(item.getEnCode())).count() > 0;
        //未分配权限方案
        if (isAll || userInfo.getIsAdministrator()) {
            SuperJsonModel superJsonModel = new SuperJsonModel();
            list.add(superJsonModel);
            redisUtil.insert(reidsKey, JsonUtil.getObjectToString(list), time);
            return list;
        }
        Map<String, List<ResourceModel>> authorizeMap = resourceListAll.stream().filter(t->StringUtil.isNotEmpty(t.getObjectId())).collect(Collectors.groupingBy(ma -> ma.getObjectId()));
        int num = 0;
        //方案
        for (String key : authorizeMap.keySet()) {
            List<ResourceModel> resourceList = authorizeMap.get(key);
            boolean authorizeLogic = num == 0;
            for (ResourceModel item : resourceList) {
                String matchLogic = StringUtil.isNotEmpty(item.getMatchLogic()) ? item.getMatchLogic() : SearchMethodEnum.And.getSymbol();
                List<SuperQueryJsonModel> conditionList = new ArrayList<>();
                List<ConditionModel> conditionModelList = JsonUtil.getJsonToList(item.getConditionJson(), ConditionModel.class);
                //分组
                for (ConditionModel conditionModel : conditionModelList) {
                    String logic = conditionModel.getLogic();
                    List<FieLdsModel> groupList = new ArrayList<>();
                    //条件
                    for (ConditionModel.ConditionItemModel fieldItem : conditionModel.getGroups()) {
                        //当前用户
                        String itemValue = fieldItem.getValue();
                        SearchMethodEnum itemMethod = SearchMethodEnum.getSearchMethod(fieldItem.getOp());
                        String itemField = fieldItem.getField();
                        String table = fieldItem.getBindTable();
                        if (itemField.contains(".") && itemField.split("\\.").length == 2) {
                            table = itemField.split("\\.")[0];
                            itemField = itemField.split("\\.")[1];
                        }
                        String bindTable = table;
                        String vModel = itemField;
                        if (AuthorizeConditionEnum.USER.getCondition().equals(itemValue)) {
                            itemValue = userInfo.getUserId();
                        }
                        //当前组织
                        if (AuthorizeConditionEnum.ORGANIZE.getCondition().equals(itemValue)) {
                            List<List<String>> orgAllPathList = getOrgAllPathList(new ArrayList() {{
                                add(userInfo.getOrganizeId());
                            }});
                            if(CollectionUtils.isNotEmpty(orgAllPathList)){
                                itemValue = JsonUtil.getObjectToString(orgAllPathList.get(0));
                            }else{
                                itemValue = "";
                            }
                        }
                        //当前组织及子组织
                        if (AuthorizeConditionEnum.ORGANIZEANDUNDER.getCondition().equals(itemValue)) {
                            String orgId = userInfo.getOrganizeId();
                            if (StringUtil.isNotEmpty(userInfo.getDepartmentId())) {
                                orgId = userInfo.getDepartmentId();
                            }
                            List<String> underOrganizations = organizeApi.getUnderOrganizations(orgId);
                            underOrganizations.add(orgId);
                            itemValue = getOrgAllPath(underOrganizations);
                        }
                        //当前用户及下属
                        if (AuthorizeConditionEnum.USERANDUNDER.getCondition().equals(itemValue)) {
                            List<String> subOrganizeIds = new ArrayList<>();
                            if (userInfo.getSubordinateIds().size() > 0) {
                                subOrganizeIds = userInfo.getSubordinateIds();
                            }
                            subOrganizeIds.add(userInfo.getUserId());
                            itemValue = JsonUtil.getObjectToString(subOrganizeIds);
                        }
                        //分管组织
                        if (AuthorizeConditionEnum.BRANCHMANAGEORG.getCondition().equals(itemValue)) {
                            List<String> allIdList = organizeAdminIsTratorApi.getOrganizeUserList(PlatformConst.CURRENT_ORG_SUB);
                            itemValue = getOrgAllPath(allIdList);
                        }
                        FieLdsModel fieLdsModel = new FieLdsModel();
                        ConfigModel config = new ConfigModel();
                        config.setFutureKey(fieldItem.getConditionText());
                        config.setTableName(bindTable);
                        fieLdsModel.setConfig(config);
                        fieLdsModel.setSymbol(itemMethod.getSymbol());
                        fieLdsModel.setVModel(vModel);
                        fieLdsModel.setId(vModel);
                        fieLdsModel.setFieldValue(itemValue);
                        groupList.add(fieLdsModel);
                    }
                    //搜索条件
                    SuperQueryJsonModel queryJsonModel = new SuperQueryJsonModel();
                    queryJsonModel.setGroups(groupList);
                    queryJsonModel.setLogic(logic);
                    conditionList.add(queryJsonModel);
                }
                if (conditionList.size() > 0) {
                    SuperJsonModel superJsonModel = new SuperJsonModel();
                    superJsonModel.setMatchLogic(matchLogic);
                    superJsonModel.setConditionList(conditionList);
                    superJsonModel.setAuthorizeLogic(authorizeLogic);
                    list.add(superJsonModel);
                }
            }
            num += list.size() > 0 ? 1 : 0;
        }
        redisUtil.insert(reidsKey, JsonUtil.getObjectToString(list), time);
        return list;
    }

    /**
     * 获取组织全路径
     * @param allIdList  组织id列表。
     * @return
     */
    private String getOrgAllPath(List<String> allIdList) {
        List<List<String>> orgAllPathList = getOrgAllPathList(allIdList);
        return JsonUtil.getObjectToString(orgAllPathList);
    }

    /**
     * 获取组织全路径
     * @param allIdList  组织id列表。
     * @return
     */
    private List<List<String>> getOrgAllPathList(List<String> allIdList) {
        List<List<String>> resOrg=new ArrayList<>();
        for(String itemOrg: allIdList){
            OrganizeEntity organizeEntity =organizeApi.getInfoById(itemOrg);
            if (organizeEntity != null) {
                if (StringUtil.isNotEmpty(organizeEntity.getOrganizeIdTree())) {
                    String[] split = organizeEntity.getOrganizeIdTree().split(",");
                    if(split.length > 0){
                        resOrg.add(Arrays.asList(split));
                    }
                }
            }
        }
        return resOrg;
    }
}
