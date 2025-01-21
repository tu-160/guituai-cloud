package com.future.module.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.Operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.future.common.base.ActionResult;
import com.future.common.base.Page;
import com.future.common.base.Pagination;
import com.future.common.base.vo.PageListVO;
import com.future.common.base.vo.PaginationVO;
import com.future.common.util.JsonUtil;
import com.future.module.system.UserOnlineApi;
import com.future.module.system.model.UserOnlineModel;
import com.future.module.system.model.UserOnlineVO;
import com.future.module.system.model.online.BatchOnlineModel;
import com.future.module.system.service.UserOnlineService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 在线用户
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2019年9月26日 上午9:18
 */
@Tag(name = "在线用户", description = "Online")
@RestController
@RequestMapping("/OnlineUser")
public class OnlineUserController implements UserOnlineApi {

    @Autowired
    private UserOnlineService userOnlineService;

    /**
     * 列表
     *
     * @param page 关键词
     * @return
     */
    @Operation(summary = "获取在线用户列表")
    @SaCheckPermission("permission.userOnline")
    @GetMapping
    public ActionResult<PageListVO<UserOnlineVO>> list(Pagination page) {
        List<UserOnlineModel> data = userOnlineService.getList(page);
        List<UserOnlineVO> voList= data.stream().map(online->{
            UserOnlineVO vo = JsonUtil.getJsonToBean(online, UserOnlineVO.class);
            vo.setUserId(online.getToken());
            //vo.setUserName(vo.getUserName() + "/" + online.getDevice());
            return vo;
        }).collect(Collectors.toList());
        PaginationVO paginationVO = JsonUtil.getJsonToBean(page, PaginationVO.class);
        return ActionResult.page(voList, paginationVO);
    }

    /**
     * 注销
     *
     * @param token token
     * @return
     */
    @Operation(summary = "强制下线")
    @Parameter(name = "token", description = "token", required = true)
    @SaCheckPermission("permission.userOnline")
    @DeleteMapping("/{token}")
    public ActionResult delete(@PathVariable("token") String token) {
        userOnlineService.delete(token);
        return ActionResult.success("操作成功");
    }

    /**
     * 批量下线用户
     *
     * @param model 在线用户id集合
     * @return ignore
     */
    @Operation(summary = "批量下线用户")
    @Parameter(name = "model", description = "在线用户id集合", required = true)
    @SaCheckPermission("permission.userOnline")
    @DeleteMapping
    public ActionResult clear(@RequestBody BatchOnlineModel model) {
        userOnlineService.delete(model.getIds());
        return ActionResult.success("操作成功");
    }

}
