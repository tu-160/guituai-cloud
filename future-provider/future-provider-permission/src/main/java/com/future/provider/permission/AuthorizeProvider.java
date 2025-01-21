package com.future.provider.permission;

import com.future.permission.model.authorize.AuthorizeVO;

/**
 * 使用RPC获取权限
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-04-29
 */
public interface AuthorizeProvider {
    /**
     * 获取权限（菜单、按钮、列表）
     *
     * @param isCache 是否存在redis
     * @return
     */
    AuthorizeVO getAuthorize(boolean isCache);
}
