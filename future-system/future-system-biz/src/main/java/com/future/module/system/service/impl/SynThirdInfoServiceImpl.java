package com.future.module.system.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dingtalk.api.response.OapiV2DepartmentGetResponse;
import com.future.base.service.SuperServiceImpl;
import com.future.common.util.JsonUtil;
import com.future.common.util.RandomUtil;
import com.future.common.util.StringUtil;
import com.future.module.system.entity.SynThirdInfoEntity;
import com.future.module.system.mapper.SynThirdInfoMapper;
import com.future.module.system.model.synthirdinfo.DingTalkDeptModel;
import com.future.module.system.model.synthirdinfo.SynThirdTotal;
import com.future.module.system.service.SynThirdDingTalkService;
import com.future.module.system.service.SynThirdInfoService;
import com.future.module.system.util.SynDingTalkUtil;
import com.future.module.system.util.SynThirdConsts;
import com.future.permission.OrganizeApi;
import com.future.permission.UserApi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 第三方工具的公司-部门-用户同步表模型
 *
 * @@version V4.0.0
 * @@copyright 直方信息科技有限公司
 * @@author Future Platform Group
 * @date 2021/4/23 17:33
 */
@Service
public class SynThirdInfoServiceImpl extends SuperServiceImpl<SynThirdInfoMapper, SynThirdInfoEntity> implements SynThirdInfoService {
    @Autowired
    private OrganizeApi organizeApi;
    @Autowired
    private UserApi userApi;
    @Autowired
    SynThirdDingTalkService synThirdDingTalkService;

    @Override
    public List<SynThirdInfoEntity> getList(String thirdType, String dataType) {
        QueryWrapper<SynThirdInfoEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().and(t->t.eq(SynThirdInfoEntity::getThirdType, Integer.valueOf(thirdType)));
        queryWrapper.lambda().and(t->t.eq(SynThirdInfoEntity::getDataType, Integer.valueOf(dataType)));
        queryWrapper.lambda().orderByAsc(SynThirdInfoEntity::getCreatorTime);
        return this.list(queryWrapper);
    }

    @Override
    public SynThirdInfoEntity getInfo(String id) {
        QueryWrapper<SynThirdInfoEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SynThirdInfoEntity::getId,id);
        return this.getOne(queryWrapper);
    }

    @Override
    public void create(SynThirdInfoEntity entity) {
        this.save(entity);
    }

    @Override
    public boolean update(String id, SynThirdInfoEntity entity) {
        entity.setId(id);
        return this.updateById(entity);
    }

    @Override
    public void delete(SynThirdInfoEntity entity) {
        if(entity!=null){
            this.removeById(entity.getId());
        }
    }

    @Override
    public SynThirdInfoEntity getInfoBySysObjId(String thirdType,String dataType,String id) {
        QueryWrapper<SynThirdInfoEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().and(t->t.eq(SynThirdInfoEntity::getThirdType,thirdType));
        queryWrapper.lambda().and(t->t.eq(SynThirdInfoEntity::getDataType,dataType));
        queryWrapper.lambda().and(t->t.eq(SynThirdInfoEntity::getSysObjId,id));
        return this.getOne(queryWrapper);
    }

    @Override
    public SynThirdTotal getSynTotal(String thirdType, String dataType) {
        String synType = dataType.equals(SynThirdConsts.DATA_TYPE_ORG) ? "组织" : "用户";
        Integer recordTotal = 0;
        Long synSuccessCount = 0L;
        Long synFailCount = 0L;
        Long unSynCount = 0L;
        Date synDate = null;

        // 获取列表数据
        List<SynThirdInfoEntity> synList = getList(thirdType, dataType).stream().filter(t->t.getLastModifyTime() != null).collect(Collectors.toList());
        if(synList!=null && synList.size()>0){
            synSuccessCount = synList.stream().filter(t -> t.getEnabledMark().equals(SynThirdConsts.SYN_STATE_OK)).count();
            synFailCount = synList.stream().filter(t -> t.getEnabledMark().equals(SynThirdConsts.SYN_STATE_FAIL)).count();
            unSynCount = synList.stream().filter(t -> t.getEnabledMark().equals(SynThirdConsts.SYN_STATE_NO)).count();
            synDate = synList.stream().max(Comparator.comparing(u -> u.getLastModifyTime())).isPresent() ? synList.stream().max(Comparator.comparing(u -> u.getLastModifyTime())).get().getLastModifyTime() : null;
        }

        // 获取本系统的组织、用户表的记录数
        if(dataType.equals(SynThirdConsts.DATA_TYPE_ORG)){
            // 获取组织(公司和部门)的记录数
            recordTotal = organizeApi.getList().size();
        }else{
            // 获取用户的记录数
            recordTotal = userApi.getList(false).size();
        }

        // 写入同步统计模型对象
        SynThirdTotal synThirdTotal = new SynThirdTotal();
        synThirdTotal.setSynType(synType);
        synThirdTotal.setRecordTotal(recordTotal);
        synThirdTotal.setSynSuccessCount(synSuccessCount);
        synThirdTotal.setSynFailCount(synFailCount);
        synThirdTotal.setUnSynCount(unSynCount);
        synThirdTotal.setSynDate(synDate);

        return synThirdTotal;
    }

    @Override
    public List<SynThirdInfoEntity> syncThirdInfoByType(String thirdToSysType, String dataTypeOrg, String SysToThirdType) {

        HashMap<String,String> typeMap = new HashMap<>();
        typeMap.put(SysToThirdType,thirdToSysType);
        typeMap.put(thirdToSysType,SysToThirdType);

        List<SynThirdInfoEntity> synThirdInfoList = this.getList(thirdToSysType, dataTypeOrg);
        List<SynThirdInfoEntity> synThirdInfoDingList = this.getList( typeMap.get(thirdToSysType), dataTypeOrg);


        List<String> collectSource = synThirdInfoList.stream().filter(t -> StringUtil.isBlank(t.getThirdObjId()) || StringUtil.isBlank(t.getSysObjId())).map(t->t.getId()).collect(Collectors.toList());
        List<String> collectTarget = synThirdInfoDingList.stream().filter(t -> StringUtil.isBlank(t.getThirdObjId()) || StringUtil.isBlank(t.getSysObjId())).map(t->t.getId()).collect(Collectors.toList());
        List<String> deleteList = new ArrayList<>();
        deleteList.addAll(collectSource);
        deleteList.addAll(collectTarget);
//        List<String> fails = this.selectAllFail();
//        deleteList.addAll(fails);
        if(!deleteList.isEmpty()){

            this.getBaseMapper().deleteBatchIds(deleteList);
        }


        synThirdInfoList = this.getList(thirdToSysType, dataTypeOrg);
        synThirdInfoDingList = this.getList( typeMap.get(thirdToSysType), dataTypeOrg);
        // 记录已经存在的组合
        HashMap<String,Boolean> existingMap =  new HashMap<>();
        synThirdInfoList.forEach(k->{
            String tag = k.getThirdType() + "-" + k.getDataType() + "-" +k.getSysObjId() +"-"+k.getThirdObjId();
            existingMap.put(tag,true);
        });
        synThirdInfoDingList.forEach(k->{
            String tag = k.getThirdType() + "-" + k.getDataType() + "-" +k.getSysObjId() +"-"+k.getThirdObjId();
            existingMap.put(tag,true);
        });


        HashMap<String, SynThirdInfoEntity> mapSource = new HashMap<>();
        HashMap<String, SynThirdInfoEntity> mapTarget = new HashMap<>();
        String tag = "";
        for(SynThirdInfoEntity entity :synThirdInfoList){
//            if(collectSource.size()>0 && !collectSource.contains(entity.getId())){
            tag =entity.getSysObjId() +"-" + entity.getThirdObjId();
            mapSource.put(tag,entity);
//            }
        }
        for(SynThirdInfoEntity entity :synThirdInfoDingList){
//            if(collectTarget.size()>0 && !collectTarget.contains(entity.getId())){
            tag =entity.getSysObjId() +"-" + entity.getThirdObjId();
            mapTarget.put(tag,entity);
//            }
        }

        // 同步记录
        List<SynThirdInfoEntity> synThirdInfoAddList = new ArrayList<>();
        SynThirdInfoEntity addEntity = null;
        if(mapSource.size()==0 && mapTarget.size()==0){
            return new ArrayList<>();
        }else if (mapSource.size()>0 && mapTarget.size()==0){
            for(String key : mapSource.keySet()){
                SynThirdInfoEntity synThirdInfoEntity = mapSource.get(key);
                addEntity = JsonUtil.getJsonToBean(synThirdInfoEntity,SynThirdInfoEntity.class);
                addEntity.setId(RandomUtil.uuId());
                addEntity.setThirdType(Integer.valueOf(typeMap.get(thirdToSysType)));
                synThirdInfoAddList.add(addEntity);
            }

        }else if (mapSource.size()==0 && mapTarget.size()>0){
            for(String key : mapTarget.keySet()){
                SynThirdInfoEntity synThirdInfoEntity = mapTarget.get(key);
                addEntity = JsonUtil.getJsonToBean(synThirdInfoEntity,SynThirdInfoEntity.class);
                addEntity.setId(RandomUtil.uuId());
                addEntity.setThirdType(Integer.valueOf(thirdToSysType));
                synThirdInfoAddList.add(addEntity);
            }
        }else{
            for(String key : mapSource.keySet()){
                if(!mapTarget.containsKey(key)){
                    SynThirdInfoEntity synThirdInfoEntity = mapSource.get(key);
                    addEntity = JsonUtil.getJsonToBean(synThirdInfoEntity,SynThirdInfoEntity.class);
                    addEntity.setId(RandomUtil.uuId());
                    addEntity.setThirdType(Integer.valueOf(typeMap.get(thirdToSysType)));
                    synThirdInfoAddList.add(addEntity);
                }
            }
            for(String key : mapTarget.keySet()){
                if(!mapSource.containsKey(key)){
                    SynThirdInfoEntity synThirdInfoEntity = mapTarget.get(key);
                    addEntity = JsonUtil.getJsonToBean(synThirdInfoEntity,SynThirdInfoEntity.class);
                    addEntity.setId(RandomUtil.uuId());
                    addEntity.setThirdType(Integer.valueOf(thirdToSysType));
                    synThirdInfoAddList.add(addEntity);
                }
            }

        }

        ArrayList<SynThirdInfoEntity> addList = new ArrayList<>();
        if(synThirdInfoAddList.size() > 0 ){
            // 过滤
            synThirdInfoAddList.forEach(k->{
                String addTag = k.getThirdType() + "-" + k.getDataType() + "-" +k.getSysObjId() +"-"+k.getThirdObjId();
                if (existingMap.get(addTag)==null) {
                    addList.add(k);
                }
            });
            this.saveBatch(addList);
        }
        // 查找对应的数据
        synThirdInfoList = this.getList(thirdToSysType, dataTypeOrg);
        return synThirdInfoList;
    }

    private List<String> selectAllFail() {
        QueryWrapper<SynThirdInfoEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().and(t->t.eq(SynThirdInfoEntity::getEnabledMark,"2"));
        List<SynThirdInfoEntity> lists = this.getBaseMapper().selectList(queryWrapper);
        return lists.stream().map(t->t.getId()).collect(Collectors.toList());
    }

    @Override
    public void initBaseDept(Long dingRootDeptId, String access_token, String thirdType) {
        final String sysByThird = this.getSysByThird("1");
        // 判断是否在中间表存在
        JSONObject retMsg = new JSONObject();

        if(StringUtil.isBlank(sysByThird)){
            if("22".equals(thirdType)){
                retMsg = SynDingTalkUtil.getDepartmentInfo(SynThirdConsts.DING_ROOT_DEPT_ID, access_token);
                OapiV2DepartmentGetResponse.DeptGetResponse departmentInfo = (OapiV2DepartmentGetResponse.DeptGetResponse) retMsg.get("departmentInfo");
                DingTalkDeptModel model = JsonUtil.getJsonToBean(departmentInfo, DingTalkDeptModel.class);
                retMsg = synThirdDingTalkService.createDepartmentDingToSys(true, model, access_token);
            }
//            if("11".equals(thirdType)){
//                retMsg = SynDingTalkUtil.getDepartmentInfo(SynThirdConsts.QY_ROOT_DEPT_ID, access_token);
//                OapiV2DepartmentGetResponse.DeptGetResponse departmentInfo = (OapiV2DepartmentGetResponse.DeptGetResponse) retMsg.get("departmentInfo");
//                DingTalkDeptModel model = JsonUtil.getJsonToBean(departmentInfo, DingTalkDeptModel.class);
//                retMsg = synThirdDingTalkService.createDepartmentDingToSys(true, model, access_token);
//            }
        }
    }

    @Override
    public boolean getBySysObjId(String id) {
        QueryWrapper<SynThirdInfoEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SynThirdInfoEntity::getEnabledMark,"1");
        queryWrapper.lambda().eq(SynThirdInfoEntity::getSysObjId,id);
        List<SynThirdInfoEntity> list = this.getBaseMapper().selectList(queryWrapper);
        if(list!=null && list.size() > 0){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public String getSysByThird(String valueOf) {
        QueryWrapper<SynThirdInfoEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().isNotNull(SynThirdInfoEntity::getSysObjId);
        queryWrapper.lambda().eq(SynThirdInfoEntity::getThirdObjId,valueOf);
        List<SynThirdInfoEntity> list = this.getBaseMapper().selectList(queryWrapper);
        if(list!=null && !list.isEmpty()){
            return list.get(0).getSysObjId();
        }
        return null;
    }

    @Override
    public SynThirdInfoEntity getInfoByThirdObjId(String thirdType,String dataType,String thirdObjId) {
        QueryWrapper<SynThirdInfoEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().and(t->t.eq(SynThirdInfoEntity::getThirdType,thirdType));
        queryWrapper.lambda().and(t->t.eq(SynThirdInfoEntity::getDataType,dataType));
        queryWrapper.lambda().and(t->t.eq(SynThirdInfoEntity::getThirdObjId,thirdObjId));
        return this.getOne(queryWrapper);
    }

}
