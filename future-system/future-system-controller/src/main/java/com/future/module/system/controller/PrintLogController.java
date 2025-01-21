package com.future.module.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.future.common.base.ActionResult;
import com.future.common.base.UserInfo;
import com.future.common.base.vo.PaginationVO;
import com.future.common.util.*;
import com.future.module.system.entity.PrintLogEntity;
import com.future.module.system.model.printdev.vo.PrintLogVO;
import com.future.module.system.model.printlog.PrintLogInfo;
import com.future.module.system.model.printlog.PrintLogQuery;
import com.future.module.system.service.PrintLogService;
import com.future.permission.UserApi;
import com.future.permission.entity.UserEntity;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.Operation;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Tag(name = "打印模板日志", description = "PrintLogController")
@RestController
@RequestMapping("/printLog")
public class PrintLogController {
    @Autowired
    private PrintLogService printLogService;

    @Autowired
    private UserProvider userProvider;

    @Autowired
    private UserApi userApi;

    /**
     * 获取列表
     *
     * @param page 分页模型
     * @return
     */
    @Operation(summary = "获取列表")
    @Parameters({
            @Parameter(name = "id", description = "打印模板ID", required = true)
    })
    @SaCheckPermission("system.printDev")
    @GetMapping("/{id}")
    public ActionResult<?> list(@PathVariable("id") String printId, PrintLogQuery page) {
        List<PrintLogEntity> records  = printLogService.getListId(printId, page);
        List<PrintLogVO> list = new ArrayList<>(records.size());
        PaginationVO paginationVO = JsonUtil.getJsonToBean(page, PaginationVO.class);
        // 转化名称
        List<String> collect = records.stream().map(PrintLogEntity::getCreatorUserId).filter(Objects::nonNull).collect(Collectors.toList());
        if (collect.size() > 0) {
            List<UserEntity> userEntityList = userApi.getUserName(collect);
            Map<String, UserEntity> map = userEntityList.stream().collect(Collectors.toMap(UserEntity::getId, Function.identity()));
            for (PrintLogEntity record : records) {
                PrintLogVO vo = JsonUtil.getJsonToBean(record, PrintLogVO.class);
                UserEntity userEntity = map.get(record.getCreatorUserId());
                if (userEntity != null) {
                    vo.setPrintMan(userEntity.getRealName() + "/" + userEntity.getAccount());
                }
                vo.setPrintTime(ObjectUtil.isNotNull(record.getCreatorTime()) ? record.getCreatorTime().getTime() : null);
                list.add(vo);
            }
        }

        return ActionResult.page(list, paginationVO);
    }

    /**
     * 保存信息
     *
     * @param info 实体对象
     * @return
     */
    @Operation(summary = "保存信息")
    @Parameters({
            @Parameter(name = "info", description = "实体对象", required = true)
    })
    @SaCheckPermission("system.printDev")
    @PostMapping("save")
    public ActionResult<?> save(@RequestBody @Validated PrintLogInfo info) {
        PrintLogEntity printLogEntity = BeanUtil.copyProperties(info, PrintLogEntity.class);
        UserInfo userInfo = userProvider.get();

        printLogEntity.setId(RandomUtil.uuId());
        printLogEntity.setCreatorTime(new Date());
        printLogEntity.setCreatorUserId(userInfo.getUserId());
        printLogService.save(printLogEntity);
        return ActionResult.success("保存成功");
    }


}
