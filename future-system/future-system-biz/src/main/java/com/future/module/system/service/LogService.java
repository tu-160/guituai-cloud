package com.future.module.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.future.base.service.SuperService;
import com.future.common.base.UserInfo;
import com.future.module.system.entity.LogEntity;
import com.future.module.system.model.logmodel.PaginationLogModel;
import com.future.permission.model.user.UserLogForm;

import java.util.List;
import java.util.Set;

/**
 * 系统日志
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2019年9月27日 上午9:18
 */
public interface LogService extends SuperService<LogEntity> {

    /**
     * 列表
     *
     * @param category  日志分类
     * @param paginationTime 分页条件
     * @return
     */
    List<LogEntity> getList(int category, PaginationLogModel paginationTime);

    /**
     * 信息
     *
     * @param id 主键值
     * @return
     */
    LogEntity getInfo(String id);


    /**
     * 删除
     * @param ids
     * @return
     */
    boolean delete(String[] ids);

    /**
     * 写入日志
     *
     * @param userId    用户Id
     * @param userName  用户名称
     * @param abstracts 摘要
     */
    void writeLogAsync(String userId, String userName, String abstracts, long requestDuration);

    /**
     * 写入日志
     *
     * @param userId    用户Id
     * @param userName  用户名称
     * @param abstracts 摘要
     */
    void writeLogAsync(String userId, String userName, String abstracts, UserInfo userInfo, int loginMark, Integer loginType, long requestDuration);

    /**
     * 请求日志
     *
     * @param logEntity 实体对象
     */
    void writeLogAsync(LogEntity logEntity);

    /**
     * 请求日志
     */
    void deleteHandleLog(String type, Integer userOnline);

    /**
     * 获取操作模块名
     *
     * @return
     */
    Set<String> queryList();
}
