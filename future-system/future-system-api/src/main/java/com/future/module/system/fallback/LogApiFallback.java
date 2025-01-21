package com.future.module.system.fallback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import com.future.common.base.vo.PageListVO;
import com.future.common.util.type.IntegerNumber;
import com.future.module.system.LogApi;
import com.future.module.system.entity.LogEntity;
import com.future.module.system.model.logmodel.PaginationLogModel;
import com.future.module.system.model.logmodel.WriteLogModel;
import com.future.permission.model.user.UserLogForm;

import java.util.ArrayList;
import java.util.List;

/**
 * 调用系统日志Api降级处理
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-03-24
 */
@Component
@Slf4j
public class LogApiFallback implements LogApi {

    @Override
    public void writeLogAsync(WriteLogModel writeLogModel) {
        log.error("写入日志失败：租户 {}, 用户ID {}, 用户名 {}", writeLogModel.getUserInfo().getTenantId(), writeLogModel.getUserId(), writeLogModel.getUserName());
    }

    @Override
    public void writeLogRequest(LogEntity logEntity) {

    }

    @Override
    public PageListVO<LogEntity> getList(PaginationLogModel pagination) {
        return new PageListVO<>();
    }
}
