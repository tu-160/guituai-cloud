package com.future.provider.system;

import org.springframework.web.bind.annotation.RequestBody;

import com.future.module.system.entity.LogEntity;

import java.util.List;

/**
 * 日志
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-04-29
 */
public interface LogProvider {
    /**
     * 写入日志
     *
     * @param userId    用户Id
     * @param userName  用户名称
     * @param abstracts 摘要
     */
    void writeLogAsync(String dbId, String userId, String userName, String account, String abstracts);

    /**
     * 写入请求日志
     * @param logEntity
     */
    void writeLogRequest(@RequestBody LogEntity logEntity);

}
