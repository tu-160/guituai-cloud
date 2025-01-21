package com.future.module.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.Operation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.future.common.base.ActionResult;
import com.future.common.util.JsonUtil;
import com.future.module.system.model.monitor.MonitorListVO;
import com.future.module.system.util.MonitorUtil;


/**
 * 系统监控
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2019年9月27日 上午9:18
 */
@Tag(name = "系统监控", description = "Monitor")
@RestController
@RequestMapping("/Monitor")
public class MonitorController {

    /**
     * 系统监控
     *
     * @return
     */
    @Operation(summary = "系统监控")
    @SaCheckPermission("system.monitor")
    @GetMapping
    public ActionResult<MonitorListVO> list() {
        MonitorUtil monitorUtil = new MonitorUtil();
        MonitorListVO vo = JsonUtil.getJsonToBean(monitorUtil, MonitorListVO.class);
        vo.setTime(System.currentTimeMillis());
        return ActionResult.success(vo);
    }
}
