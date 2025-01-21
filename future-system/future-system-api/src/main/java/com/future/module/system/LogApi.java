package com.future.module.system;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.future.common.base.vo.PageListVO;
import com.future.feign.utils.FeignName;
import com.future.module.system.entity.LogEntity;
import com.future.module.system.fallback.LogApiFallback;
import com.future.module.system.model.logmodel.PaginationLogModel;
import com.future.module.system.model.logmodel.WriteLogModel;
import com.future.permission.model.user.UserLogForm;

import java.util.List;

/**
 * 调用系统日志Api
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-03-24
 */
@FeignClient(name = FeignName.SYSTEM_SERVER_NAME, fallback = LogApiFallback.class, path = "/Log")
public interface LogApi {
    /**
     * 写入日志
     *
     * @param writeLogModel
     */
    @PostMapping("/writeLogAsync")
    void writeLogAsync(@RequestBody WriteLogModel writeLogModel);

    /**
     * 写入请求日志
     */
    @PostMapping("/writeLogRequest")
    void writeLogRequest(@RequestBody LogEntity logEntity);

    /**
     * 获取系统日志列表
     *
     * @param pagination
     * @return
     */
    @PostMapping("/getList")
    PageListVO<LogEntity> getList(@RequestBody PaginationLogModel pagination);
}
