package com.future.module.system.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.future.base.controller.SuperController;
import com.future.common.base.ActionResult;
import com.future.common.base.vo.ListVO;
import com.future.common.base.vo.PageListVO;
import com.future.common.base.vo.PaginationVO;
import com.future.common.constant.MsgCode;
import com.future.common.util.JsonUtil;
import com.future.common.util.RandomUtil;
import com.future.module.system.entity.CommonWordsEntity;
import com.future.module.system.model.commonword.ComWordsPagination;
import com.future.module.system.model.commonword.CommonWordsForm;
import com.future.module.system.model.commonword.CommonWordsVO;
import com.future.module.system.service.CommonWordsService;

import java.util.List;

/**
 * 常用语控制类
 *
 * @author Future Platform Group
 * @version v4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2023-01-06
 */
@Tag(name = "审批常用语", description = "commonWords")
@RestController
@RequestMapping("/CommonWords")
public class CommonWordsController extends SuperController<CommonWordsService, CommonWordsEntity> {

    @Autowired
    private CommonWordsService commonWordsService;


    /**
     * 列表
     *
     * @param comWordsPagination 页面参数对象
     * @return 列表结果集
     */
    @Operation(summary = "当前系统应用列表")
    @GetMapping()
    public ActionResult<PageListVO<CommonWordsVO>> getList(ComWordsPagination comWordsPagination) {
        List<CommonWordsEntity> entityList = commonWordsService.getSysList(comWordsPagination, false);
        List<CommonWordsVO> voList = JsonUtil.getJsonToList(entityList, CommonWordsVO.class);
        return ActionResult.page(voList, JsonUtil.getJsonToBean(comWordsPagination, PaginationVO.class));
    }

    @Operation(summary = "获取信息")
    @GetMapping("/{id}")
    public ActionResult<CommonWordsVO> getInfo(@PathVariable String id) {
        CommonWordsEntity entity = commonWordsService.getById(id);
        CommonWordsVO vo = JsonUtil.getJsonToBean(entity, CommonWordsVO.class);
        return ActionResult.success(vo);
    }

    @Operation(summary = "下拉列表")
    @GetMapping("/Selector")
    public ActionResult<ListVO<CommonWordsVO>> getSelect(String type) {
        List<CommonWordsVO> voList = JsonUtil.getJsonToList(commonWordsService.getListModel(type), CommonWordsVO.class);
        return ActionResult.success(new ListVO<>(voList));
    }

    @Operation(summary = "新建")
    @PostMapping("")
    public ActionResult create(@RequestBody CommonWordsForm commonWordsForm) {
        CommonWordsEntity entity = JsonUtil.getJsonToBean(commonWordsForm, CommonWordsEntity.class);
        entity.setId(RandomUtil.uuId());
        commonWordsService.save(entity);
        return ActionResult.success(MsgCode.SU001.get());
    }

    @Operation(summary = "修改")
    @PutMapping("/{id}")
    public ActionResult update(@RequestBody CommonWordsForm commonWordsForm) {
        CommonWordsEntity entity = JsonUtil.getJsonToBean(commonWordsForm, CommonWordsEntity.class);
        entity.setId(commonWordsForm.getId());
        commonWordsService.updateById(entity);
        return ActionResult.success(MsgCode.SU004.get());
    }

    @Operation(summary = "删除")
    @DeleteMapping("/{id}")
    public ActionResult delete(@PathVariable String id) {
        //对象存在判断
        if (commonWordsService.getById(id) != null) {
            commonWordsService.removeById(id);
            return ActionResult.success(MsgCode.SU003.get());
        } else {
            return ActionResult.fail(MsgCode.FA003.get());
        }
    }

}
