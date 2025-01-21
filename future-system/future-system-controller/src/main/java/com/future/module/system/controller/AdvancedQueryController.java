package com.future.module.system.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.future.base.controller.SuperController;
import com.future.common.base.ActionResult;
import com.future.common.base.UserInfo;
import com.future.common.base.vo.ListVO;
import com.future.common.constant.MsgCode;
import com.future.common.exception.DataException;
import com.future.common.util.JsonUtil;
import com.future.common.util.JsonUtilEx;
import com.future.common.util.UserProvider;
import com.future.module.system.entity.AdvancedQueryEntity;
import com.future.module.system.model.advancedquery.AdvancedQueryListVO;
import com.future.module.system.model.advancedquery.AdvancedQuerySchemeForm;
import com.future.module.system.service.AdvancedQueryService;

import javax.validation.Valid;
import java.util.List;

/**
 * 高级查询方案管理
 *
 * @author Future Platform Group
 * @version v4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2022/5/30
 */
@Tag(name = "高级查询方案管理", description = "AdvancedQuery")
@RestController
@RequestMapping("/AdvancedQuery")
public class AdvancedQueryController extends SuperController<AdvancedQueryService, AdvancedQueryEntity> {

	@Autowired
	private AdvancedQueryService queryService;
	@Autowired
	private UserProvider userProvider;

	/**
	 * 新建
	 *
	 * @param advancedQuerySchemeForm 实体对象
	 * @return
	 */
	@Operation(summary = "新建方案")
	@Parameters({
			@Parameter(name = "advancedQuerySchemeForm", description = "实体对象", required = true)
	})
	@PostMapping
	public ActionResult create(@RequestBody @Valid AdvancedQuerySchemeForm advancedQuerySchemeForm) {
		AdvancedQueryEntity entity = JsonUtil.getJsonToBean(advancedQuerySchemeForm, AdvancedQueryEntity.class);
		queryService.create(entity);
		return ActionResult.success(MsgCode.SU001.get());
	}

	/**
	 * 修改方案
	 *
	 * @param id 主键
	 * @param advancedQuerySchemeForm 实体对象
	 * @return
	 */
	@Operation(summary = "修改方案")
	@Parameters({
			@Parameter(name = "id", description = "主键", required = true),
			@Parameter(name = "advancedQuerySchemeForm", description = "实体对象", required = true)
	})
	@PutMapping("/{id}")
	public ActionResult update(@PathVariable("id") String id, @RequestBody @Valid AdvancedQuerySchemeForm advancedQuerySchemeForm) {
		AdvancedQueryEntity entity = JsonUtil.getJsonToBean(advancedQuerySchemeForm, AdvancedQueryEntity.class);
		entity.setId(id);
		queryService.updateById(entity);
		return ActionResult.success(MsgCode.SU004.get());
	}

	/**
	 * 删除
	 *
	 * @param id 主键值
	 * @return ignore
	 */
	@Operation(summary = "删除方案")
	@Parameters({
			@Parameter(name = "id", description = "主键", required = true)
	})
	@DeleteMapping("/{id}")
	public ActionResult delete(@PathVariable("id") String id) {
		UserInfo userInfo = userProvider.get();
		AdvancedQueryEntity entity = queryService.getInfo(id,userInfo.getUserId());
		if (entity != null) {
			queryService.removeById(entity);
			return ActionResult.success(MsgCode.SU003.get());
		}
		return ActionResult.fail(MsgCode.FA003.get());
	}

	/**
	 * 列表
	 *
	 * @param moduleId 功能主键
	 * @return ignore
	 */
	@Operation(summary = "方案列表")
	@Parameters({
			@Parameter(name = "moduleId", description = "功能主键", required = true)
	})
	@GetMapping("/{moduleId}/List")
	public ActionResult<ListVO<AdvancedQueryListVO>> list(@PathVariable("moduleId") String moduleId) {
		UserInfo userInfo = userProvider.get();
		List<AdvancedQueryEntity> data = queryService.getList(moduleId,userInfo);
		List<AdvancedQueryListVO> list = JsonUtil.getJsonToList(data, AdvancedQueryListVO.class);
		ListVO<AdvancedQueryListVO> vo = new ListVO<>();
		vo.setList(list);
		return ActionResult.success(vo);
	}
	/**
	 * 信息
	 *
	 * @param id 主键值
	 * @return ignore
	 * @throws DataException ignore
	 */
	@Operation(summary = "获取方案信息")
	@Parameters({
			@Parameter(name = "id", description = "主键值", required = true)
	})
	@GetMapping("/{id}")
	public ActionResult<AdvancedQuerySchemeForm> info(@PathVariable("id") String id) throws DataException {
		UserInfo userInfo = userProvider.get();
		AdvancedQueryEntity entity = queryService.getInfo(id,userInfo.getUserId());
		AdvancedQuerySchemeForm vo = JsonUtilEx.getJsonToBeanEx(entity, AdvancedQuerySchemeForm.class);
		return ActionResult.success(vo);
	}

}
