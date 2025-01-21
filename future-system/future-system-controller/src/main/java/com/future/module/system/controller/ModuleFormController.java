package com.future.module.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.future.base.controller.SuperController;
import com.future.common.base.ActionResult;
import com.future.common.base.Pagination;
import com.future.common.base.vo.ListVO;
import com.future.common.constant.MsgCode;
import com.future.common.exception.DataException;
import com.future.common.model.visualJson.FieLdsModel;
import com.future.common.model.visualJson.FormCloumnUtil;
import com.future.common.model.visualJson.FormDataModel;
import com.future.common.model.visualJson.TableModel;
import com.future.common.model.visualJson.analysis.*;
import com.future.common.util.JsonUtil;
import com.future.common.util.JsonUtilEx;
import com.future.common.util.StringUtil;
import com.future.module.system.ModuleFormApi;
import com.future.module.system.entity.ModuleEntity;
import com.future.module.system.entity.ModuleFormEntity;
import com.future.module.system.model.form.*;
import com.future.module.system.model.module.PropertyJsonModel;
import com.future.module.system.service.ModuleFormService;
import com.future.module.system.service.ModuleService;
import com.future.visualdev.VisualdevApi;
import com.future.visualdev.entity.VisualdevEntity;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 表单权限
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @author Future Platform Group
* @date 2021-09-14
 */
@Tag(name = "表单权限", description = "ModuleForm")
@RestController
@RequestMapping("/ModuleForm")
public class ModuleFormController extends SuperController<ModuleFormService, ModuleFormEntity> implements ModuleFormApi {

    @Autowired
    private ModuleFormService moduleFormService;
    @Autowired
    private ModuleService moduleService;
    @Autowired
    private VisualdevApi visualdevApi;

    /**
     * 获取表单权限列表
     *
     * @param moduleId   功能主键
     * @param pagination 分页参数
     * @return ignore
     */
    @Operation(summary = "获取表单权限列表")
    @Parameters({
            @Parameter(name = "moduleId", description = "功能主键", required = true)
    })
    @SaCheckPermission("system.menu")
    @GetMapping("/{moduleId}/Fields")
    public ActionResult<ListVO<ModuleFormListVO>> getList(@PathVariable("moduleId") String moduleId, Pagination pagination) {
        List<ModuleFormEntity> list = moduleFormService.getList(moduleId, pagination);
        List<ModuleFormListVO> voList = JsonUtil.getJsonToList(list, ModuleFormListVO.class);
        voList.stream().forEach(t-> {
            String enCode = t.getEnCode();
            if (StringUtil.isNotEmpty(enCode)) {
                if (enCode.contains("-")){
                    enCode = enCode.substring(enCode.indexOf("-")+1);
                }
                t.setEnCode(enCode.replace("future_" + t.getBindTable() + "_future_", ""));
            }
        });
        ListVO vo = new ListVO<>();
        vo.setList(voList);
        return ActionResult.success(vo);
    }

    /**
     * 菜单数据权限
     *
     * @param moduleId 功能主键
     * @return ignore
     */
    @Operation(summary = "菜单数据权限")
    @Parameters({
            @Parameter(name = "moduleId", description = "功能主键", required = true)
    })
    @SaCheckPermission("system.menu")
    @GetMapping("/{moduleId}/FieldList")
    public ActionResult<List<Map<String, String>>> fieldList(@PathVariable("moduleId") String moduleId) {
        List<Map<String, String>> list = new ArrayList<>();
        // 得到菜单id
        ModuleEntity entity = moduleService.getInfo(moduleId);
        PropertyJsonModel model = JsonUtil.getJsonToBean(entity.getPropertyJson(), PropertyJsonModel.class);
        if (model == null) {
            model = new PropertyJsonModel();
        }
        // 得到bean
        VisualdevEntity info = visualdevApi.getInfo(model.getModuleId());
        if (info != null) {
            FormDataModel formDataModel = JsonUtil.getJsonToBean(String.valueOf(info.getFormData()), FormDataModel.class);
            List<FieLdsModel> fieLdsModelList = JsonUtil.getJsonToList(formDataModel.getFields(), FieLdsModel.class);
            RecursionForm recursionForm = new RecursionForm();
            recursionForm.setList(fieLdsModelList);
            recursionForm.setTableModelList(JsonUtil.getJsonToList(String.valueOf(info.getVisualTables()), TableModel.class));
            List<FormAllModel> formAllModel = new ArrayList<>();
            FormCloumnUtil.recursionForm(recursionForm, formAllModel);
            for (FormAllModel allModel : formAllModel) {
                if (FormEnum.table.getMessage().equals(allModel.getFutureKey())) {
                    FormColumnTableModel childList = allModel.getChildList();
                    Map<String, String> map1 = new HashedMap<>();
                    map1.put("field", childList.getTableModel());
                    map1.put("fieldName", childList.getLabel());
                    list.add(map1);
                } else if (FormEnum.mast.getMessage().equals(allModel.getFutureKey())) {
                    FormColumnModel formColumnModel = allModel.getFormColumnModel();
                    FieLdsModel fieLdsModel = formColumnModel.getFieLdsModel();
                    if (StringUtil.isNotEmpty(fieLdsModel.getVModel())) {
                        Map<String, String> map1 = new HashedMap<>();
                        map1.put("field", fieLdsModel.getVModel());
                        map1.put("fieldName", fieLdsModel.getConfig().getLabel());
                        list.add(map1);
                    }
                } else if (FormEnum.mastTable.getMessage().equals(allModel.getFutureKey())) {
                    FormMastTableModel formColumnModel = allModel.getFormMastTableModel();
                    FieLdsModel fieLdsModel = formColumnModel.getMastTable().getFieLdsModel();
                    if (StringUtil.isNotEmpty(fieLdsModel.getVModel())) {
                        Map<String, String> map1 = new HashedMap<>();
                        map1.put("field", fieLdsModel.getVModel());
                        map1.put("fieldName", fieLdsModel.getConfig().getLabel());
                        list.add(map1);
                    }
                }
            }
        }
        return ActionResult.success(list);
    }

    /**
     * 获取表单权限信息
     *
     * @param id 主键值
     * @return ignore
     * @throws DataException ignore
     */
    @Operation(summary = "获取表单权限信息")
    @Parameters({
            @Parameter(name = "id", description = "主键值", required = true)
    })
    @GetMapping("/{id}")
    public ActionResult<ModuleFormInfoVO> info(@PathVariable("id") String id) throws DataException {
        ModuleFormEntity entity = moduleFormService.getInfo(id);
            String enCode = entity.getEnCode();
            if (StringUtil.isNotEmpty(enCode)) {
                if (enCode.contains("-") && entity.getFieldRule()==2){
                    enCode = enCode.substring(enCode.indexOf("-")+1);
                    entity.setEnCode(enCode);
                }
                if (entity.getFieldRule()==1 && entity.getBindTable()!=null){
                    entity.setEnCode(enCode.replace("future_" + entity.getBindTable() + "_future_", ""));
                }
            }
        ModuleFormInfoVO vo = JsonUtilEx.getJsonToBeanEx(entity, ModuleFormInfoVO.class);
        return ActionResult.success(vo);
    }

    /**
     * 新建表单权限
     *
     * @param moduleFormCrForm 实体对象
     * @return ignore
     */
    @Operation(summary = "新建表单权限")
    @Parameters({
            @Parameter(name = "moduleFormCrForm", description = "实体对象", required = true)
    })
    @SaCheckPermission("system.menu")
    @PostMapping
    public ActionResult create(@RequestBody @Valid ModuleFormCrForm moduleFormCrForm) {
        ModuleEntity moduleEntity = moduleService.getInfo(moduleFormCrForm.getModuleId());
        ModuleFormEntity entity = JsonUtil.getJsonToBean(moduleFormCrForm, ModuleFormEntity.class);

        if (moduleEntity != null){
            PropertyJsonModel model = JsonUtil.getJsonToBean(moduleEntity.getPropertyJson(), PropertyJsonModel.class);
            if (model == null) {
                model = new PropertyJsonModel();
            }
            if (entity.getFieldRule() == 1 && StringUtil.isNotEmpty(moduleFormCrForm.getBindTable())) {
                String enCode = "future_" + moduleFormCrForm.getBindTable() + "_future_" + entity.getEnCode();
                entity.setEnCode(enCode);
            }

            if (entity.getFieldRule() == 2 && StringUtil.isNotEmpty(moduleFormCrForm.getChildTableKey())) {
                // 得到bean
//                Object bean = SpringContext.getBean("visualdevServiceImpl");
//                Object method = ReflectionUtil.invokeMethod(bean, "getTableNameToKey", new Class[]{String.class}, new Object[]{model.getModuleId()});
//                Map<String, Object> map = JsonUtil.entityToMap(method);
//
//                String enCode = map.get(moduleFormCrForm.getBindTable().toLowerCase()) + "-" + entity.getEnCode();
                String enCode = moduleFormCrForm.getChildTableKey() + "-" + entity.getEnCode();
                entity.setEnCode(enCode);
            }
        }
        if (moduleFormService.isExistByEnCode(entity.getModuleId(), entity.getEnCode(), entity.getId())) {
            return ActionResult.fail("字段名称不能重复");
        }
        moduleFormService.create(entity);
        return ActionResult.success(MsgCode.SU001.get());
    }

    /**
     * 更新表单权限
     *
     * @param id               主键值
     * @param moduleFormUpForm 实体对象
     * @return ignore
     */
    @Operation(summary = "更新表单权限")
    @Parameters({
            @Parameter(name = "id", description = "主键值", required = true),
            @Parameter(name = "moduleFormUpForm", description = "实体对象", required = true)
    })
    @SaCheckPermission("system.menu")
    @PutMapping("/{id}")
    public ActionResult update(@PathVariable("id") String id, @RequestBody @Valid ModuleFormUpForm moduleFormUpForm) {
        ModuleEntity moduleEntity = moduleService.getInfo(moduleFormUpForm.getModuleId());
        ModuleFormEntity entity = JsonUtil.getJsonToBean(moduleFormUpForm, ModuleFormEntity.class);
        if (moduleEntity != null){
            PropertyJsonModel model = JsonUtil.getJsonToBean(moduleEntity.getPropertyJson(), PropertyJsonModel.class);
            if (model == null) {
                model = new PropertyJsonModel();
            }
            if (entity.getFieldRule() == 1 && StringUtil.isNotEmpty(moduleFormUpForm.getBindTable())) {
                String enCode = "future_" + moduleFormUpForm.getBindTable() + "_future_" + entity.getEnCode();
                entity.setEnCode(enCode);
            }

            if (entity.getFieldRule() == 2 && StringUtil.isNotEmpty(moduleFormUpForm.getChildTableKey())) {
//                // 得到bean
//                Object bean = SpringContext.getBean("visualdevServiceImpl");
//                Object method = ReflectionUtil.invokeMethod(bean, "getTableNameToKey", new Class[]{String.class}, new Object[]{model.getModuleId()});
//                Map<String, Object> map = JsonUtil.entityToMap(method);
//
//                String enCode = map.get(moduleFormUpForm.getBindTable().toLowerCase()) + "-" + entity.getEnCode();
                String enCode = moduleFormUpForm.getChildTableKey() + "-" + entity.getEnCode();
                entity.setEnCode(enCode);
            }
        }
        if (moduleFormService.isExistByEnCode(entity.getModuleId(), entity.getEnCode(), id)) {
            return ActionResult.fail("字段名称不能重复");
        }
        boolean flag = moduleFormService.update(id, entity);
        if (!flag) {
            return ActionResult.success(MsgCode.FA002.get());
        }
        return ActionResult.success(MsgCode.SU004.get());
    }

    /**
     * 删除表单权限
     *
     * @param id 主键值
     * @return
     */
    @Operation(summary = "删除表单权限")
    @Parameters({
            @Parameter(name = "id", description = "主键值", required = true)
    })
    @SaCheckPermission("system.menu")
    @DeleteMapping("/{id}")
    public ActionResult delete(@PathVariable("id") String id) {
        ModuleFormEntity entity = moduleFormService.getInfo(id);
        if (entity != null) {
            moduleFormService.delete(entity);
            return ActionResult.success(MsgCode.SU003.get());
        }
        return ActionResult.fail(MsgCode.FA003.get());
    }

    /**
     * 批量新建
     *
     * @param formBatchForm 批量表单模型
     * @return
     */
    @Operation(summary = "批量新建表单权限")
    @Parameters({
            @Parameter(name = "formBatchForm", description = "批量表单模型", required = true)
    })
    @SaCheckPermission("system.menu")
    @PostMapping("/Actions/Batch")
    public ActionResult batchCreate(@RequestBody @Valid FormBatchForm formBatchForm) {
        List<ModuleFormEntity> entitys = formBatchForm.getFormJson() != null ? JsonUtil.getJsonToList(formBatchForm.getFormJson(), ModuleFormEntity.class) : new ArrayList<>();
        List<String> name = new ArrayList<>();
        for (ModuleFormEntity entity : entitys) {
            if (entity.getFieldRule() == 1 ) {
                String enCode = "future_" + entity.getBindTable() + "_future_" + entity.getEnCode();
                entity.setEnCode(enCode);
            }
            if (entity.getFieldRule() == 2 ) {
                String enCode = entity.getChildTableKey() + "-" + entity.getEnCode();
                entity.setEnCode(enCode);
            }
            entity.setModuleId(formBatchForm.getModuleId());
            if (moduleFormService.isExistByEnCode(entity.getModuleId(), entity.getEnCode(), null)) {
                return ActionResult.fail(MsgCode.EXIST002.get());
            }
            if (name.contains(entity.getEnCode())) {
                return ActionResult.fail(MsgCode.EXIST002.get());
            }
            name.add(entity.getEnCode());
        }
        moduleFormService.create(entitys);
        return ActionResult.success(MsgCode.SU001.get());
    }

    /**
     * 更新表单权限状态
     *
     * @param id 主键值
     * @return
     */
    @Operation(summary = "更新表单权限状态")
    @Parameters({
            @Parameter(name = "id", description = "主键值", required = true)
    })
    @SaCheckPermission("system.menu")
    @PutMapping("/{id}/Actions/State")
    public ActionResult upState(@PathVariable("id") String id) {
        ModuleFormEntity entity = moduleFormService.getInfo(id);
        entity.setEnabledMark("1".equals(String.valueOf(entity.getEnabledMark())) ? 0 : 1);
        boolean flag = moduleFormService.update(id, entity);
        if (!flag) {
            return ActionResult.success(MsgCode.FA002.get());
        }
        return ActionResult.success(MsgCode.SU004.get());
    }

    @Override
    @GetMapping("/getList")
    public List<ModuleFormEntity> getList() {
        return moduleFormService.getList();
    }

    @Override
    @PostMapping("/getListByModuleId")
    public List<ModuleFormEntity> getListByModuleId(@RequestBody List<String> ids) {
        return moduleFormService.getListByModuleId(ids);
    }

    @Override
    @PostMapping("/getListByIds")
    public List<ModuleFormEntity> getListByIds(@RequestBody List<String> ids) {
        return moduleFormService.getListByIds(ids);
    }
}
