package com.future.module.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.util.ObjectUtil;

import com.future.common.base.ActionResult;
import com.future.common.base.Pagination;
import com.future.common.base.vo.PageListVO;
import com.future.common.base.vo.PaginationVO;
import com.future.common.model.visualJson.TableModel;
import com.future.common.util.*;
import com.future.common.util.context.SpringContext;
import com.future.database.model.dbfield.DbFieldModel;
import com.future.module.system.entity.ModuleDataAuthorizeLinkEntity;
import com.future.module.system.entity.ModuleEntity;
import com.future.module.system.model.dbtable.vo.DbFieldVO;
import com.future.module.system.model.module.PropertyJsonModel;
import com.future.module.system.model.moduledataauthorize.DataAuthorizeLinkForm;
import com.future.module.system.model.moduledataauthorize.DataAuthorizeTableNameVO;
import com.future.module.system.service.DbTableService;
import com.future.module.system.service.ModuleDataAuthorizeLinkDataService;
import com.future.module.system.service.ModuleService;
import com.future.visualdev.VisualdevApi;
import com.future.visualdev.entity.VisualdevEntity;
import com.google.common.base.CaseFormat;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.Operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 数据权限字段管理 数据连接
 *
 * @author Future Platform Group
 * @version v4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2022/6/7
 */
@Tag(name = "数据权限字段管理数据连接" , description = "ModuleDataAuthorizeLink")
@RestController
@RequestMapping("/ModuleDataAuthorizeLink")
public class ModuleDataAuthorizeLinkController {

	@Autowired
	private ModuleDataAuthorizeLinkDataService linkDataService;
	@Autowired
	private ModuleService moduleService;
	@Autowired
	private DbTableService dbTableService;
	@Autowired
	private VisualdevApi visualdevApi;

	/**
	 * 页面参数
	 *
	 * @param linkForm 页面参数
	 * @return
	 */
	@Operation(summary = "保存编辑数据连接")
	@Parameters({
			@Parameter(name = "linkForm", description = "页面参数", required = true)
	})
	@SaCheckPermission("system.menu")
	@PostMapping("/saveLinkData")
	public ActionResult saveLinkData(@RequestBody @Valid DataAuthorizeLinkForm linkForm) {
		ModuleDataAuthorizeLinkEntity linkDataEntity = JsonUtil.getJsonToBean(linkForm, ModuleDataAuthorizeLinkEntity.class);
		if (StringUtil.isEmpty(linkDataEntity.getId())) {
			linkDataEntity.setId(RandomUtil.uuId());
			linkDataService.save(linkDataEntity);
			return ActionResult.success("保存成功");
		} else {
			linkDataService.updateById(linkDataEntity);
			return ActionResult.success("更新成功");
		}
	}

	/**
	 * 获取表名
	 *
	 * @param menuId 菜单id
	 * @param type 分类
	 * @return
	 */
	@Operation(summary = "获取表名")
	@Parameters({
			@Parameter(name = "menuId", description = "菜单id", required = true),
			@Parameter(name = "type", description = "分类", required = true)
	})
	@SaCheckPermission("system.menu")
	@GetMapping("/getVisualTables/{menuId}/{type}")
	public ActionResult<DataAuthorizeTableNameVO> getVisualTables(@PathVariable("menuId") String menuId, @PathVariable("type") Integer type) {
		ModuleEntity info = moduleService.getInfo(menuId);
		DataAuthorizeTableNameVO vo = null;
		if (ObjectUtil.isNotNull(info)) {
			PropertyJsonModel model = JsonUtil.getJsonToBean(info.getPropertyJson(), PropertyJsonModel.class);
			if (model == null) {
				model = new PropertyJsonModel();
			}
			//功能
			if (info.getType() == 3) {
				// 得到bean
				VisualdevEntity visualdevEntity = visualdevApi.getInfo(model.getModuleId());
				if (visualdevEntity != null) {
					List<TableModel> tables = JsonUtil.getJsonToList(visualdevEntity.getVisualTables(), TableModel.class);
					List<String> collect = tables.stream().map(t -> t.getTable()).collect(Collectors.toList());
					vo = DataAuthorizeTableNameVO.builder().linkTables(collect).linkId(visualdevEntity.getDbLinkId()).build();
				}
			} else {
				ModuleDataAuthorizeLinkEntity linkDataEntity = linkDataService.getLinkDataEntityByMenuId(menuId,type);
				String linkTables = linkDataEntity.getLinkTables();
				List<String> tables = StringUtil.isNotEmpty(linkTables) ? Arrays.asList(linkTables.split(",")) : new ArrayList<>();
				vo = DataAuthorizeTableNameVO.builder().linkTables(tables).linkId(linkDataEntity.getLinkId()).build();
			}
		}
		return ActionResult.success(vo);
	}

	/**
	 * 数据连接信息
	 *
	 * @param menudId 菜单id
	 * @param type 分类
	 * @return
	 */
	@Operation(summary = "数据连接信息")
	@Parameters({
			@Parameter(name = "menudId", description = "菜单id", required = true),
			@Parameter(name = "type", description = "分类", required = true)
	})
	@SaCheckPermission("system.menu")
	@GetMapping("/getInfo/{menudId}/{type}")
	public ActionResult<DataAuthorizeLinkForm> getInfo(@PathVariable("menudId") String menudId,@PathVariable("type") Integer type) {
		ModuleDataAuthorizeLinkEntity linkDataEntity = linkDataService.getLinkDataEntityByMenuId(menudId,type);
		DataAuthorizeLinkForm linkForm = JsonUtil.getJsonToBean(linkDataEntity, DataAuthorizeLinkForm.class);
		return ActionResult.success(linkForm);
	}

	/**
	 * 表名获取数据表字段
	 *
	 * @param linkId 连接id
	 * @param tableName 表名
	 * @param menuType 菜单类型
	 * @param dataType 数据类型
	 * @param pagination 分页模型
	 * @return
	 * @throws Exception
	 */
	@Operation(summary = "表名获取数据表字段")
	@Parameters({
			@Parameter(name = "linkId", description = "连接id", required = true),
			@Parameter(name = "tableName", description = "表名", required = true),
			@Parameter(name = "menuType", description = "菜单类型", required = true),
			@Parameter(name = "dataType", description = "数据类型", required = true)
	})
	@SaCheckPermission("system.menu")
	@GetMapping("/{linkId}/Tables/{tableName}/Fields/{menuType}/{dataType}")
	public ActionResult<PageListVO<Object>> getTableInfoByTableName(@PathVariable("linkId") String linkId, @PathVariable("tableName") String tableName, @PathVariable("menuType") Integer menuType, @PathVariable("dataType") Integer dataType, Pagination pagination) throws Exception {
		List<DbFieldModel> data = dbTableService.getFieldList(linkId, tableName);
		List<DbFieldVO> vos = JsonUtil.getJsonToList(data, DbFieldVO.class);
		if (StringUtil.isNotEmpty(pagination.getKeyword())) {
			vos = vos.stream().filter(vo -> {
				boolean ensure;
				String fieldName = vo.getFieldName();
				fieldName = Optional.ofNullable(fieldName).orElse("");
				ensure = fieldName.toLowerCase().contains(pagination.getKeyword().toLowerCase()) || vo.getField().toLowerCase().contains(pagination.getKeyword().toLowerCase());
				return ensure;
			}).collect(Collectors.toList());
		}
		if (menuType==2 && dataType!=3){
			for (DbFieldVO vo : vos){
				String name = vo.getField().toLowerCase().replaceAll("f_", "");
				vo.setField(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name));
			}
		}
		List listPage = PageUtil.getListPage((int) pagination.getCurrentPage(), (int) pagination.getPageSize(), vos);
		PaginationVO paginationVO = JsonUtil.getJsonToBean(pagination, PaginationVO.class);
		paginationVO.setTotal(vos.size());
		return ActionResult.page(listPage,paginationVO);
	}
}

