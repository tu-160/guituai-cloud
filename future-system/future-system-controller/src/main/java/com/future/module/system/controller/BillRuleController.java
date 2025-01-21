package com.future.module.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.future.base.controller.SuperController;
import com.future.common.annotation.HandleLog;
import com.future.common.base.ActionResult;
import com.future.common.base.vo.DownloadVO;
import com.future.common.base.vo.PageListVO;
import com.future.common.base.vo.PaginationVO;
import com.future.common.constant.FileTypeConstant;
import com.future.common.constant.MsgCode;
import com.future.common.emnus.ModuleTypeEnum;
import com.future.common.exception.DataException;
import com.future.common.util.FileUtil;
import com.future.common.util.JsonUtil;
import com.future.common.util.StringUtil;
import com.future.common.util.UserProvider;
import com.future.module.file.FileApi;
import com.future.module.file.model.ExportModel;
import com.future.module.system.BillRuleApi;
import com.future.module.system.entity.BillRuleEntity;
import com.future.module.system.entity.DictionaryDataEntity;
import com.future.module.system.model.billrule.*;
import com.future.module.system.service.BillRuleService;
import com.future.module.system.service.DictionaryDataService;
import com.future.permission.UserApi;
import com.future.permission.entity.UserEntity;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * 单据规则
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2019年9月27日 上午9:18
 */
@Tag(name = "单据规则", description = "BillRule")
@RestController
@RequestMapping("/BillRule")
public class BillRuleController extends SuperController<BillRuleService, BillRuleEntity> implements BillRuleApi {

    @Autowired
    private FileApi fileApi;
    @Autowired
    private UserProvider userProvider;
    @Autowired
    private BillRuleService billRuleService;
    @Autowired
    private UserApi userApi;
    @Autowired
    private DictionaryDataService dictionaryDataService;

    /**
     * 列表
     *
     * @param pagination 分页模型
     * @return
     */
    @HandleLog(moduleName = "单据规则", requestMethod = "查询")
    @Operation(summary = "获取单据规则列表(带分页)")
    @SaCheckPermission("system.billRule")
    @GetMapping
    public ActionResult<PageListVO<BillRuleListVO>> list(BillRulePagination pagination) {
        List<BillRuleEntity> list = billRuleService.getList(pagination);
        List<BillRuleListVO> listVO = new ArrayList<>();
        list.forEach(entity->{
            BillRuleListVO vo = JsonUtil.getJsonToBean(entity, BillRuleListVO.class);
            if(StringUtil.isNotEmpty(entity.getCategory())){
                DictionaryDataEntity dataEntity = dictionaryDataService.getInfo(entity.getCategory());
                vo.setCategory(dataEntity != null ? dataEntity.getFullName() : null);
            }

            UserEntity userEntity = userApi.getInfoById(entity.getCreatorUserId());
            if(userEntity != null){
                vo.setCreatorUser(userEntity.getRealName() + "/" + userEntity.getAccount());
            }
            listVO.add(vo);
        });
        PaginationVO paginationVO = JsonUtil.getJsonToBean(pagination, PaginationVO.class);
        return ActionResult.page(listVO, paginationVO);
    }

    /**
     * 列表
     *
     * @param pagination 分页模型
     * @return
     */
    @HandleLog(moduleName = "单据规则", requestMethod = "查询")
    @Operation(summary = "获取单据规则下拉框")
    @GetMapping("/Selector")
    public ActionResult<PageListVO<BillRuleListVO>> selectList(BillRulePagination pagination) {
        List<BillRuleEntity> list = billRuleService.getListByCategory(pagination.getCategoryId(),pagination);
        List<BillRuleListVO> listVO = new ArrayList<>();
        list.forEach(entity->{
            BillRuleListVO vo = JsonUtil.getJsonToBean(entity, BillRuleListVO.class);
            if(StringUtil.isNotEmpty(entity.getCategory())){
                DictionaryDataEntity dataEntity = dictionaryDataService.getInfo(entity.getCategory());
                vo.setCategory(dataEntity != null ? dataEntity.getFullName() : null);
            }

            UserEntity userEntity = userApi.getInfoById(entity.getCreatorUserId());
            if(userEntity != null){
                vo.setCreatorUser(userEntity.getRealName() + "/" + userEntity.getAccount());
            }
            listVO.add(vo);
        });
        PaginationVO paginationVO = JsonUtil.getJsonToBean(pagination, PaginationVO.class);
        return ActionResult.page(listVO, paginationVO);
    }


    /**
     * 更新组织状态
     *
     * @param id 主键值
     * @return
     */
    @HandleLog(moduleName = "单据规则", requestMethod = "修改")
    @Operation(summary = "更新单据规则状态")
    @Parameters({
            @Parameter(name = "id", description = "主键值", required = true)
    })
    @SaCheckPermission("system.billRule")
    @PutMapping("/{id}/Actions/State")
    public ActionResult update(@PathVariable("id") String id) {
        BillRuleEntity entity = billRuleService.getInfo(id);
        if (entity != null) {
            if (entity.getEnabledMark() == 1) {
                entity.setEnabledMark(0);
            } else {
                entity.setEnabledMark(1);
            }
            billRuleService.update(entity.getId(), entity);
            return ActionResult.success("更新成功");
        }
        return ActionResult.fail("更新失败，数据不存在");
    }

    /**
     * 信息
     *
     * @param id 主键值
     * @return
     */
    @HandleLog(moduleName = "单据规则", requestMethod = "查询")
    @Operation(summary = "获取单据规则信息")
    @Parameters({
            @Parameter(name = "id", description = "主键值", required = true)
    })
    @SaCheckPermission("system.billRule")
    @GetMapping("/{id}")
    public ActionResult info(@PathVariable("id") String id) throws DataException {
        BillRuleEntity entity = billRuleService.getInfo(id);
        BillRuleInfoVO vo = JsonUtil.getJsonToBeanEx(entity, BillRuleInfoVO.class);
        return ActionResult.success(vo);
    }

    /**
     * 获取单据流水号
     *
     * @param enCode 参数编码
     * @return
     */
    @HandleLog(moduleName = "单据规则", requestMethod = "查询")
    @Operation(summary = "获取单据流水号(工作流调用)")
    @Parameters({
            @Parameter(name = "enCode", description = "参数编码", required = true)
    })
    @GetMapping("/BillNumber/{enCode}")
    public ActionResult GetBillNumber(@PathVariable("enCode") String enCode) throws DataException {
        String data = billRuleService.getBillNumber(enCode, true);
        return ActionResult.success("获取成功", data);
    }

    /**
     * 新建
     *
     * @param billRuleCrForm 实体对象
     * @return
     */
    @HandleLog(moduleName = "单据规则", requestMethod = "新增")
    @Operation(summary = "添加单据规则")
    @Parameters({
            @Parameter(name = "billRuleCrForm", description = "实体对象", required = true)
    })
    @SaCheckPermission("system.billRule")
    @PostMapping
    public ActionResult create(@RequestBody @Valid BillRuleCrForm billRuleCrForm) {
        BillRuleEntity entity = JsonUtil.getJsonToBean(billRuleCrForm, BillRuleEntity.class);
        if (billRuleService.isExistByFullName(entity.getFullName(), entity.getId())) {
            return ActionResult.fail("名称不能重复");
        }
        if (billRuleService.isExistByEnCode(entity.getEnCode(), entity.getId())) {
            return ActionResult.fail("编码不能重复");
        }
        billRuleService.create(entity);
        return ActionResult.success("新建成功");
    }

    /**
     * 更新
     *
     * @param billRuleUpForm 实体对象
     * @param id             主键值
     * @return
     */
    @HandleLog(moduleName = "单据规则", requestMethod = "修改")
    @Operation(summary = "修改单据规则")
    @Parameters({
            @Parameter(name = "id", description = "主键值", required = true),
            @Parameter(name = "billRuleUpForm", description = "实体对象", required = true)
    })
    @SaCheckPermission("system.billRule")
    @PutMapping("/{id}")
    public ActionResult update(@PathVariable("id") String id, @RequestBody BillRuleUpForm billRuleUpForm) {
        BillRuleEntity entity = JsonUtil.getJsonToBean(billRuleUpForm, BillRuleEntity.class);
        if (billRuleService.isExistByFullName(entity.getFullName(), id)) {
            return ActionResult.fail("名称不能重复");
        }
        if (billRuleService.isExistByEnCode(entity.getEnCode(), id)) {
            return ActionResult.fail("编码不能重复");
        }
        boolean flag = billRuleService.update(id, entity);
        if (flag == false) {
            return ActionResult.fail("更新失败，数据不存在");
        }
        return ActionResult.success("更新成功");
    }

    /**
     * 删除
     *
     * @param id 主键值
     * @return
     */
    @HandleLog(moduleName = "单据规则", requestMethod = "删除")
    @Operation(summary = "删除单据规则")
    @Parameters({
            @Parameter(name = "id", description = "主键值", required = true)
    })
    @SaCheckPermission("system.billRule")
    @DeleteMapping("/{id}")
    public ActionResult delete(@PathVariable("id") String id) {
        BillRuleEntity entity = billRuleService.getInfo(id);
        if (entity != null) {
            if (!StringUtils.isEmpty(entity.getOutputNumber())) {
                return ActionResult.fail("单据已经被使用,不允许被删除");
            } else {
                billRuleService.delete(entity);
                return ActionResult.success("删除成功");
            }
        }
        return ActionResult.fail("删除失败，数据不存在");
    }

    /**
     * 获取单据缓存
     *
     * @param enCode 参数编码
     * @return
     */
    @Override
    @GetMapping("/useBillNumber/{enCode}")
    public ActionResult useBillNumber(@PathVariable("enCode") String enCode) {
        billRuleService.useBillNumber(enCode);
        return ActionResult.success();
    }

    /**
     * 获取单据流水号
     *
     * @param enCode 参数编码
     * @return
     */
    @Override
    @GetMapping("/getBillNumber/{enCode}")
    public ActionResult getBillNumber(@PathVariable("enCode") String enCode) throws DataException {
        Object data = billRuleService.getBillNumber(enCode, false);
        return ActionResult.success(data);
    }

    /**
     * 导出单据规则
     * @param id 打印模板id
     */
    @HandleLog(moduleName = "单据规则", requestMethod = "导出")
    @Operation(summary = "导出")
    @Parameters({
            @Parameter(name = "id", description = "主键值", required = true)
    })
    @SaCheckPermission("system.billRule")
    @GetMapping("/{id}/Actions/Export")
    public ActionResult<DownloadVO> export(@PathVariable String id){
        BillRuleEntity entity = billRuleService.getInfo(id);
        //导出文件
        DownloadVO downloadVO=fileApi.exportFile(new ExportModel(entity,fileApi.getPath(FileTypeConstant.TEMPORARY),UserProvider.getToken(), entity.getFullName(), ModuleTypeEnum.SYSTEM_BILLRULE.getTableName()));
        return ActionResult.success(downloadVO);
    }

    /**
     * 导入单据规则
     * @param multipartFile 备份json文件
     * @return 执行结果标识
     */
    @HandleLog(moduleName = "单据规则", requestMethod = "导入")
    @Operation(summary = "导入")
    @SaCheckPermission("system.billRule")
    @PostMapping(value = "/Actions/Import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ActionResult importData(@RequestPart("file") MultipartFile multipartFile,
                                   @RequestParam("type") Integer type) throws DataException {
        //判断是否为.json结尾
        if (FileUtil.existsSuffix(multipartFile, ModuleTypeEnum.SYSTEM_BILLRULE.getTableName())) {
            return ActionResult.fail(MsgCode.IMP002.get());
        }
        try {
            String fileContent = FileUtil.getFileContent(multipartFile);
            BillRuleEntity entity = JsonUtil.getJsonToBean(fileContent, BillRuleEntity.class);
            return billRuleService.ImportData(entity, type);
        } catch (Exception e) {
            throw new DataException(MsgCode.IMP004.get());
        }

    }

}
