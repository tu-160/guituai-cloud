package com.future.module.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.future.common.base.ActionResult;
import com.future.common.base.Page;
import com.future.common.base.vo.DownloadVO;
import com.future.common.base.vo.ListVO;
import com.future.common.constant.FileTypeConstant;
import com.future.common.constant.MsgCode;
import com.future.common.emnus.ModuleTypeEnum;
import com.future.common.exception.DataException;
import com.future.common.util.FileUtil;
import com.future.common.util.JsonUtil;
import com.future.common.util.RandomUtil;
import com.future.common.util.UserProvider;
import com.future.database.util.TableUtil;
import com.future.module.file.FileApi;
import com.future.module.file.model.ExportModel;
import com.future.module.system.entity.DataInterfaceEntity;
import com.future.module.system.entity.DataInterfaceVariateEntity;
import com.future.module.system.model.datainterfacevariate.DataInterfaceVariateListVO;
import com.future.module.system.model.datainterfacevariate.DataInterfaceVariateModel;
import com.future.module.system.model.datainterfacevariate.DataInterfaceVariateSelectorVO;
import com.future.module.system.model.datainterfacevariate.DataInterfaceVariateVO;
import com.future.module.system.service.DataInterfaceService;
import com.future.module.system.service.DataInterfaceVariateService;
import com.future.permission.UserApi;
import com.future.permission.entity.UserEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 数据接口变量
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-03-15 10:29
 */
@Tag(name = "数据接口变量", description = "DataInterfaceVariate")
@RestController
@RequestMapping(value = "/DataInterfaceVariate")
public class DataInterfaceVariateController {

    @Autowired
    private DataInterfaceVariateService dataInterfaceVariateService;
    @Autowired
    private UserApi userApi;
    @Autowired
    private DataInterfaceService dataInterfaceService;
    @Autowired
    private FileApi fileApi;

    /**
     * 获取数据接口变量
     *
     * @param id 主键
     * @return
     */
    @Operation(summary = "获取数据接口变量")
    @SaCheckPermission("systemData.dataInterface")
    @Parameter(name = "id", description = "自然主键")
    @GetMapping("/{id}")
    public ActionResult<ListVO<DataInterfaceVariateListVO>> list(@PathVariable("id") String id, Page page) {
        List<DataInterfaceVariateListVO> list = new ArrayList<>();
        List<DataInterfaceVariateEntity> data = dataInterfaceVariateService.getList(id, page);
        data.forEach(t -> {
            DataInterfaceVariateListVO vo = new DataInterfaceVariateListVO();
            vo.setId(t.getId());
            vo.setInterfaceId(t.getInterfaceId());
            vo.setFullName(t.getFullName());
            vo.setValue(t.getValue());
            vo.setCreatorTime(t.getCreatorTime() != null ? t.getCreatorTime().getTime() : null);
            vo.setLastModifyTime(t.getLastModifyTime() != null ? t.getLastModifyTime().getTime() : null);
            UserEntity userEntity = userApi.getInfoById(t.getCreatorUserId());
            vo.setCreatorUser(userEntity != null ? userEntity.getRealName() + "/" + userEntity.getAccount() : null);
            list.add(vo);
        });
        ListVO<DataInterfaceVariateListVO> listVO = new ListVO<>();
        listVO.setList(list);
        return ActionResult.success(listVO);
    }

    /**
     * 下拉列表
     *
     * @return
     */
    @Operation(summary = "下拉列表")
    @SaCheckPermission("systemData.dataInterface")
    @GetMapping("/Selector")
    public ActionResult<List<DataInterfaceVariateSelectorVO>> selector() {
        List<DataInterfaceVariateEntity> data = dataInterfaceVariateService.getList(null, null);
        List<DataInterfaceEntity> list = dataInterfaceService.getList(data.stream().map(DataInterfaceVariateEntity::getInterfaceId).collect(Collectors.toList()));
        List<DataInterfaceVariateSelectorVO> jsonToList = JsonUtil.getJsonToList(list, DataInterfaceVariateSelectorVO.class);
        jsonToList.forEach(t -> {
            t.setParentId("-1");
            t.setType(0);
        });
        jsonToList.forEach(t -> {
            List<DataInterfaceVariateEntity> collect = data.stream().filter(variateEntity -> t.getId().equals(variateEntity.getInterfaceId())).collect(Collectors.toList());
            List<DataInterfaceVariateSelectorVO> selectorVOS = JsonUtil.getJsonToList(collect, DataInterfaceVariateSelectorVO.class);
            selectorVOS.forEach(selectorVO -> {
                selectorVO.setParentId(t.getId());
                selectorVO.setType(1);
            });
            t.setChildren(selectorVOS);
        });
        return ActionResult.success(jsonToList);
    }

    /**
     * 详情
     *
     * @param id 主键
     * @return
     */
    @Operation(summary = "详情")
    @SaCheckPermission("systemData.dataInterface")
    @Parameter(name = "id", description = "自然主键")
    @GetMapping("/{id}/Info")
    public ActionResult<DataInterfaceVariateVO> info(@PathVariable("id") String id) {
        DataInterfaceVariateEntity entity = dataInterfaceVariateService.getInfo(id);
        DataInterfaceVariateVO vo = JsonUtil.getJsonToBean(entity, DataInterfaceVariateVO.class);
        return ActionResult.success(vo);
    }

    /**
     * 导出
     *
     * @param id 自然主键
     * @return
     */
    @Operation(summary = "导出")
    @SaCheckPermission("systemData.dataInterface")
    @Parameter(name = "id", description = "自然主键")
    @GetMapping("/{id}/Actions/Export")
    public ActionResult<DownloadVO> export(@PathVariable("id") String id) {
        DataInterfaceVariateEntity entity = dataInterfaceVariateService.getInfo(id);
        //导出文件
        DownloadVO downloadVO = fileApi.exportFile(
                new ExportModel(entity, fileApi.getPath(FileTypeConstant.TEMPORARY),
                        UserProvider.getLoginUserId() != null ? UserProvider.getLoginUserId() : "",
                        entity.getFullName(), ModuleTypeEnum.SYSTEM_DATAINTEFASE_VARIATE.getTableName()));
        return ActionResult.success(downloadVO);
    }

    /**
     * 添加
     *
     * @param dataInterfaceVariateModel 模型
     * @return
     */
    @Operation(summary = "添加")
    @SaCheckPermission("systemData.dataInterface")
    @Parameter(name = "dataInterfaceVariateModel", description = "模型")
    @PostMapping
    public ActionResult<String> create(@RequestBody DataInterfaceVariateModel dataInterfaceVariateModel) {
        DataInterfaceVariateEntity entity = JsonUtil.getJsonToBean(dataInterfaceVariateModel, DataInterfaceVariateEntity.class);
        if (entity.getFullName().contains("@")) {
            return ActionResult.fail("变量名不能包含敏感字符");
        }
        if (dataInterfaceVariateService.isExistByFullName(entity)) {
            return ActionResult.fail("变量名已存在");
        }
        dataInterfaceVariateService.create(entity);
        return ActionResult.success(MsgCode.SU001.get());
    }

    /**
     * 修改
     *
     * @param id 自然主键
     * @param dataInterfaceVariateModel 模型
     * @return
     */
    @Operation(summary = "修改")
    @SaCheckPermission("systemData.dataInterface")
    @Parameters({
            @Parameter(name = "id", description = "自然主键"),
            @Parameter(name = "dataInterfaceVariateModel", description = "模型")
    })
    @PutMapping("/{id}")
    public ActionResult<String> update(@PathVariable("id") String id, @RequestBody DataInterfaceVariateModel dataInterfaceVariateModel) {
        DataInterfaceVariateEntity entity = JsonUtil.getJsonToBean(dataInterfaceVariateModel, DataInterfaceVariateEntity.class);
        if (entity.getFullName().contains("@")) {
            return ActionResult.fail("变量名不能包含敏感字符");
        }
        entity.setId(id);
        if (dataInterfaceVariateService.isExistByFullName(entity)) {
            return ActionResult.fail("变量名已存在");
        }
        dataInterfaceVariateService.update(entity);
        return ActionResult.success(MsgCode.SU004.get());
    }

    /**
     * 删除
     *
     * @param id 自然主键
     * @return
     */
    @Operation(summary = "删除")
    @SaCheckPermission("systemData.dataInterface")
    @Parameters({
            @Parameter(name = "id", description = "自然主键")
    })
    @DeleteMapping("/{id}")
    public ActionResult<String> delete(@PathVariable("id") String id) {
        DataInterfaceVariateEntity entity = dataInterfaceVariateService.getInfo(id);
        if (entity == null) {
            return ActionResult.fail(MsgCode.FA001.get());
        }
        dataInterfaceVariateService.delete(entity);
        return ActionResult.success(MsgCode.SU003.get());
    }

    /**
     * 导入
     *
     * @param multipartFile 文件
     * @return
     */
    @Operation(summary = "导入")
    @SaCheckPermission("systemData.dataInterface")
    @PostMapping(value = "/Actions/Import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ActionResult<String> delete(@RequestPart("file") MultipartFile multipartFile) {
        //判断是否为.json结尾
        if (FileUtil.existsSuffix(multipartFile, ModuleTypeEnum.SYSTEM_DATAINTEFASE_VARIATE.getTableName())) {
            return ActionResult.fail("导入文件格式错误");
        }
        //读取文件内容
        String fileContent = FileUtil.getFileContent(multipartFile);
        try {
            DataInterfaceVariateEntity entity = JsonUtil.getJsonToBean(fileContent, DataInterfaceVariateEntity.class);
            if (dataInterfaceVariateService.getInfo(entity.getId()) == null &&
                    !dataInterfaceVariateService.isExistByFullName(entity)) {
                dataInterfaceVariateService.create(entity);
                return ActionResult.success("导入成功");
            }
        } catch (Exception e) {
            throw new DataException("导入失败，数据有误");
        }
        return ActionResult.fail("数据已存在");
    }

    /**
     * 复制
     *
     * @param id 自然主键
     * @return
     */
    @Operation(summary = "复制")
    @SaCheckPermission("systemData.dataInterface")
    @Parameter(name = "id", description = "自然主键", required = true)
    @PostMapping("/{id}/Actions/Copy")
    public ActionResult<String> copy(@PathVariable("id") String id) {
        String copyNum = UUID.randomUUID().toString().substring(0, 5);
        DataInterfaceVariateEntity entity = dataInterfaceVariateService.getInfo(id);
        entity.setFullName(entity.getFullName() + ".副本" + copyNum);
        if(entity.getFullName().length() > 50) return ActionResult.fail(MsgCode.COPY001.get());
        entity.setLastModifyTime(null);
        entity.setLastModifyUserId(null);
        dataInterfaceVariateService.create(entity);
        return ActionResult.success(MsgCode.SU007.get());
    }

}
