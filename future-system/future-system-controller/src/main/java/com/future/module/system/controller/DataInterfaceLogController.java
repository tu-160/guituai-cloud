package com.future.module.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.future.base.controller.SuperController;
import com.future.common.base.ActionResult;
import com.future.common.base.Pagination;
import com.future.common.base.vo.PageListVO;
import com.future.common.base.vo.PaginationVO;
import com.future.common.util.JsonUtil;
import com.future.module.system.entity.DataInterfaceLogEntity;
import com.future.module.system.model.datainterface.DataInterfaceLogVO;
import com.future.module.system.service.DataInterfaceLogService;
import com.future.permission.UserApi;
import com.future.permission.entity.UserEntity;

import java.util.List;

/**
 * 数据接口调用日志控制器
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-06-03
 */
@Tag(description = "DataInterfaceLog", name = "数据接口调用日志")
@RestController
@RequestMapping("/DataInterfaceLog")
public class DataInterfaceLogController extends SuperController<DataInterfaceLogService, DataInterfaceLogEntity> {
    @Autowired
    private DataInterfaceLogService dataInterfaceLogService;
    @Autowired
    private UserApi userApi;

    /**
     * 获取数据接口调用日志列表
     *
     * @param id 主键
     * @param pagination 分页参数
     * @return
     */
    @Operation(summary = "获取数据接口调用日志列表")
    @Parameters({
            @Parameter(name = "id", description = "主键", required = true)
    })
    @SaCheckPermission("systemData.dataInterface")
    @GetMapping("{id}")
    public ActionResult<PageListVO<DataInterfaceLogVO>> getList(@PathVariable("id") String id, Pagination pagination) {
        List<DataInterfaceLogEntity> list = dataInterfaceLogService.getList(id, pagination);
        List<DataInterfaceLogVO> voList = JsonUtil.getJsonToList(list, DataInterfaceLogVO.class);
        for (DataInterfaceLogVO vo : voList) {
            UserEntity entity = userApi.getInfoById(vo.getUserId());
            if (entity!=null){
                vo.setUserId(entity.getRealName() + "/" + entity.getAccount());
            }
        }
        PaginationVO vo = JsonUtil.getJsonToBean(pagination, PaginationVO.class);
        return ActionResult.page(voList, vo);
    }
}
