package com.future.permission.util;

import java.util.*;

import com.future.common.constant.MsgCode;
import com.future.common.exception.DataException;
import com.future.common.util.StringUtil;
import com.future.permission.entity.OrganizeEntity;
import com.future.permission.service.*;

/**
 * 类功能
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2022/1/27
 */
public class PermissionUtil {

    /**
     * 递归取组织结构id
     * @param organizeInfo 组织信息集合
     * @param organizeId 组织id
     * @param infoType 信息类型 1:id 2:fullName
     */
    private static LinkedList<String> getOrganizeInfos(LinkedList<String> organizeInfo, String organizeId, Integer infoType, OrganizeService organizeService){
        OrganizeEntity infoEntity = organizeService.getInfo(organizeId);
        if(infoEntity != null){
            organizeInfo.add(infoType.equals(1) ? organizeId : infoEntity.getFullName());
            // -1 为顶级节点
            if(infoEntity != null && !"-1".equals(infoEntity.getParentId())){
                getOrganizeInfos(organizeInfo, infoEntity.getParentId(), infoType, organizeService);
            }else {
                // 结束时，进行倒序排列
                Collections.reverse(organizeInfo);
            }
        }
        return organizeInfo;
    }

    public static List<LinkedList<String>> getOrgIdsTree(List<String> organizeIds, Integer infoType, OrganizeService organizeService){
        List<LinkedList<String>> organizeIdsTree = new ArrayList<>();
        organizeIds.forEach(id->{
            organizeIdsTree.add(getOrganizeInfos(new LinkedList<>(), id , infoType, organizeService));
        });
        return organizeIdsTree;
    }

    /**
     * 获取组名连接信息
     * @param organizeIds 组织id集合
     * @return 组织链式信息
     */
    public static String getLinkInfoByOrgId(List<String> organizeIds, OrganizeService organizeService){
        StringBuilder organizeInfoVo = new StringBuilder();
        for(String id : organizeIds) {
            if(id != null){
                StringBuilder organizeInfo = new StringBuilder();
                for (String name : getOrganizeInfos(new LinkedList<>(), id, 2, organizeService)) {
                    organizeInfo.append(name).append("/");
                }
                // 去除最后一个斜杠
                if(organizeInfo.length() > 0){
                    organizeInfo = new StringBuilder(organizeInfo.substring(0, organizeInfo.length()-1));
                }
                organizeInfo.append(",");
                organizeInfoVo.append(organizeInfo);
            }
        }
        return organizeInfoVo.toString();
    }

    /**
     * 获取组名连接信息
     * @param organizeId 组织id
     * @return 组织链式信息
     */
    public static String getLinkInfoByOrgId(String organizeId, OrganizeService organizeService){
        return getLinkInfoByOrgId(Collections.singletonList(organizeId), organizeService);
    }

    /**
     * 去掉尾部的封号
     */
    public static String getLinkInfoByOrgId(String organizeId, OrganizeService organizeService, Boolean separateFlag){
        String linkInfo = getLinkInfoByOrgId(organizeId, organizeService);
        if (StringUtil.isEmpty(linkInfo)){
            return linkInfo;
        }
        if(!separateFlag){
            linkInfo = linkInfo.substring(0, linkInfo.length() - 1);
        }
        return linkInfo;
    }

    public static List<String> getOrgIdsByFormTree(OrganizeService organizeService, List<List<String>> organizeIdsTree) throws DataException {
        List<String> orgIds = new ArrayList<>();
        for (List<String> organizeIds : organizeIdsTree) {
            // 组织id数组树最后一个数组最后一个id，是需要储存的id
            String organizeId = organizeIds.get(organizeIds.size() - 1);
            orgIds.add(organizeId);
        }
        // 判断每个OrgId的有效性
        int count = organizeService.listByIds(orgIds).size();
        if(count != orgIds.size()){
            throw new DataException(MsgCode.FA026.get());
        }
        return orgIds;
    }






}
