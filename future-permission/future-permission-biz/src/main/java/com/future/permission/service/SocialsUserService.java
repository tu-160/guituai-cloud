package com.future.permission.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.future.base.service.SuperService;
import com.future.permission.entity.SocialsUserEntity;

import java.util.List;

/**
 * 流程设计
 *
 * @author Future Platform Group
 * @version v4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2022/7/14 9:33:16
 */
public interface SocialsUserService extends SuperService<SocialsUserEntity> {
    /**
     * 查询用户授权列表
     * @param
     * @return
     * @copyright 直方信息科技有限公司
     * @date 2022/7/14
     */
    List<SocialsUserEntity> getListByUserId(String userId);

    /**
     * 查询用户授权列表
     * @param
     * @return
     * @copyright 直方信息科技有限公司
     * @date 2022/7/14
     */
    List<SocialsUserEntity> getUserIfnoBySocialIdAndType(String socialId,String socialType);

    /**
     * 查询用户授权列表
     * @param
     * @return
     * @copyright 直方信息科技有限公司
     * @date 2022/7/14
     */
    List<SocialsUserEntity> getListByUserIdAndSource(String userId,String socialType);

    /**
     *  根据第三方账号账号类型和id获取用户第三方绑定信息
     * @param socialId 第三方账号id
     * @return
     */
    SocialsUserEntity getInfoBySocialId(String socialId,String socialType);
}
