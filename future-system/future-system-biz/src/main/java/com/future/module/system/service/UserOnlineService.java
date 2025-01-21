package com.future.module.system.service;


import java.util.List;

import com.future.common.base.Page;
import com.future.common.base.Pagination;
import com.future.module.system.model.UserOnlineModel;

/**
 * 在线用户
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2019年9月26日 上午9:18
 */
public interface UserOnlineService {

    /**
     * 列表
     *
     * @return
     */
    List<UserOnlineModel> getList(Pagination page);

    /**
     * 删除
     *
     * @param tokens 主键值
     */
    void delete(String... tokens);
}
