package com.future.module.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.future.base.controller.SuperController;
import com.future.common.base.ActionResult;
import com.future.common.base.vo.DownloadVO;
import com.future.common.base.vo.ListVO;
import com.future.common.constant.MsgCode;
import com.future.common.emnus.ModuleTypeEnum;
import com.future.common.exception.DataException;
import com.future.common.util.FileUtil;
import com.future.common.util.JsonUtil;
import com.future.common.util.StringUtil;
import com.future.common.util.treeutil.ListToTreeUtil;
import com.future.common.util.treeutil.SumTree;
import com.future.common.util.treeutil.TreeDotUtils;
import com.future.common.util.type.StringNumber;
import com.future.module.system.DictionaryDataApi;
import com.future.module.system.entity.DictionaryDataEntity;
import com.future.module.system.entity.DictionaryTypeEntity;
import com.future.module.system.model.dictionarydata.*;
import com.future.module.system.model.dictionarytype.DictionaryTypeSelectModel;
import com.future.module.system.model.dictionarytype.DictionaryTypeSelectVO;
import com.future.module.system.service.DictionaryDataService;
import com.future.module.system.service.DictionaryTypeService;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 字典数据
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2019年9月27日 上午9:18
 */
@Tag(name = "数据字典", description = "DictionaryData")
@RestController
@RequestMapping("/DictionaryData")
public class DictionaryDataController extends SuperController<DictionaryDataService, DictionaryDataEntity> implements DictionaryDataApi {

    @Autowired
    private DictionaryDataService dictionaryDataService;
    @Autowired
    private DictionaryTypeService dictionaryTypeService;

    /**
     * 获取数据字典列表
     *
     * @param dictionaryTypeId 数据分类id
     * @param pageDictionaryData 分页模型
     * @return
     */
    @Operation(summary = "获取数据字典列表")
    @Parameters({
            @Parameter(name = "dictionaryTypeId", description = "数据分类id", required = true)
    })
    @GetMapping("/{dictionaryTypeId}")
    public ActionResult bindDictionary(@PathVariable("dictionaryTypeId") String dictionaryTypeId, PageDictionaryData pageDictionaryData) {
        List<DictionaryDataEntity> data = dictionaryDataService.getList(dictionaryTypeId);
        List<DictionaryDataEntity> dataAll = data;
        if (StringUtil.isNotEmpty(pageDictionaryData.getKeyword())) {
            data = data.stream().filter(t -> t.getFullName().contains(pageDictionaryData.getKeyword()) || t.getEnCode().contains(pageDictionaryData.getKeyword())).collect(Collectors.toList());
        }
        if (pageDictionaryData.getIsTree() != null && StringNumber.ONE.equals(pageDictionaryData.getIsTree())) {
            List<DictionaryDataEntity> treeData = JsonUtil.getJsonToList(ListToTreeUtil.treeWhere(data, dataAll), DictionaryDataEntity.class);
            List<DictionaryDataModel> voListVO = JsonUtil.getJsonToList(treeData, DictionaryDataModel.class);
            List<SumTree<DictionaryDataModel>> sumTrees = TreeDotUtils.convertListToTreeDot(voListVO);
            List<DictionaryDataListVO> list = JsonUtil.getJsonToList(sumTrees, DictionaryDataListVO.class);
            ListVO<DictionaryDataListVO> treeVo = new ListVO<>();
            treeVo.setList(list);
            return ActionResult.success(treeVo);
        }
        List<DictionaryDataModel> voListVO = JsonUtil.getJsonToList(data, DictionaryDataModel.class);
        ListVO<DictionaryDataModel> treeVo = new ListVO<>();
        treeVo.setList(voListVO);
        return ActionResult.success(treeVo);
    }


    /**
     * 获取数据字典列表
     *
     * @return
     */
    @Operation(summary = "获取数据字典列表(分类+内容)")
    @GetMapping("/All")
    public ActionResult<ListVO<Map<String, Object>>> allBindDictionary() {
        List<DictionaryTypeEntity> dictionaryTypeList = dictionaryTypeService.getList();
        List<Map<String, Object>> list = new ArrayList<>();
        for (DictionaryTypeEntity dictionaryTypeEntity : dictionaryTypeList) {
            List<DictionaryDataEntity> childNodeList = dictionaryDataService.getList(dictionaryTypeEntity.getId(), true);
            if (dictionaryTypeEntity.getIsTree() != null && dictionaryTypeEntity.getIsTree().compareTo(1) == 0) {
                List<Map<String, Object>> selectList = new ArrayList<>();
                for (DictionaryDataEntity item : childNodeList) {
                    Map<String, Object> ht = new HashMap<>();
                    ht.put("fullName", item.getFullName());
                    ht.put("id", item.getId());
                    ht.put("enCode", item.getEnCode());
                    ht.put("parentId", item.getParentId());
                    selectList.add(ht);
                }
                //==============转换树
                List<SumTree<DictionaryDataAllModel>> list1 = TreeDotUtils.convertListToTreeDot(JsonUtil.getJsonToList(selectList, DictionaryDataAllModel.class));
                List<DictionaryDataAllVO> list2 = JsonUtil.getJsonToList(list1, DictionaryDataAllVO.class);
                //==============
                Map<String, Object> ht_item = new HashMap<>();
                ht_item.put("id", dictionaryTypeEntity.getId());
                ht_item.put("enCode", dictionaryTypeEntity.getEnCode());
                ht_item.put("dictionaryList", list2);
                ht_item.put("isTree", 1);
                list.add(ht_item);
            } else {
                List<Map<String, Object>> selectList = new ArrayList<>();
                for (DictionaryDataEntity item : childNodeList) {
                    Map<String, Object> ht = new HashMap<>();
                    ht.put("enCode", item.getEnCode());
                    ht.put("id", item.getId());
                    ht.put("fullName", item.getFullName());
                    selectList.add(ht);
                }
                Map<String, Object> ht_item = new HashMap<>();
                ht_item.put("id", dictionaryTypeEntity.getId());
                ht_item.put("enCode", dictionaryTypeEntity.getEnCode());
                ht_item.put("dictionaryList", selectList);
                ht_item.put("isTree", 0);
                list.add(ht_item);
            }
        }
        ListVO<Map<String, Object>> vo = new ListVO<>();
        vo.setList(list);
        return ActionResult.success(vo);
    }


    /**
     * 获取数据字典下拉框数据
     *
     * @param dictionaryTypeId 类别主键
     * @param isTree 是否树形
     * @param id 主键
     * @return
     */
    @Operation(summary = "获取数据字典分类下拉框数据")
    @Parameters({
            @Parameter(name = "dictionaryTypeId", description = "数据分类id", required = true),
            @Parameter(name = "isTree", description = "是否树形"),
            @Parameter(name = "id", description = "主键", required = true)
    })
    @GetMapping("/{dictionaryTypeId}/Selector/{id}")
    public ActionResult<ListVO<DictionaryDataSelectVO>> treeView(@PathVariable("dictionaryTypeId") String dictionaryTypeId,
                                                                 @RequestParam(value = "isTree", required = false) String isTree, @PathVariable("id") String id) {

        DictionaryTypeEntity typeEntity = dictionaryTypeService.getInfo(dictionaryTypeId);
        List<DictionaryDataModel> treeList = new ArrayList<>();
        DictionaryDataModel treeViewModel = new DictionaryDataModel();
        treeViewModel.setId("0");
        treeViewModel.setFullName(typeEntity.getFullName());
        treeViewModel.setParentId("-1");
        treeViewModel.setIcon("fa fa-tags");
        treeList.add(treeViewModel);
        if (isTree != null && StringNumber.ONE.equals(isTree)) {
            List<DictionaryDataEntity> data = dictionaryDataService.getList(dictionaryTypeId);
            //过滤子集
            if(!"0".equals(id)){
                data.remove(dictionaryDataService.getInfo(id));
            }
            for (DictionaryDataEntity entity : data) {
                DictionaryDataModel treeModel = new DictionaryDataModel();
                treeModel.setId(entity.getId());
                treeModel.setFullName(entity.getFullName());
                treeModel.setParentId("-1".equals(entity.getParentId()) ? entity.getDictionaryTypeId() : entity.getParentId());
                treeList.add(treeModel);
            }
        }
        List<SumTree<DictionaryDataModel>> sumTrees = TreeDotUtils.convertListToTreeDotFilter(treeList);
        List<DictionaryDataSelectVO> list = JsonUtil.getJsonToList(sumTrees, DictionaryDataSelectVO.class);
        ListVO<DictionaryDataSelectVO> treeVo = new ListVO<>();
        treeVo.setList(list);
        return ActionResult.success(treeVo);
    }

    /**
     * 获取字典分类
     *
     * @param dictionaryTypeId 数据分类id
     * @return
     */
    @Operation(summary = "获取某个字典数据下拉框列表")
    @Parameters({
            @Parameter(name = "dictionaryTypeId", description = "数据分类id", required = true)
    })
    @GetMapping("/{dictionaryTypeId}/Data/Selector")
    public ActionResult<ListVO<DictionaryTypeSelectVO>> selectorOneTreeView(@PathVariable("dictionaryTypeId") String dictionaryTypeId) {
        List<DictionaryDataEntity> data = dictionaryDataService.getList(dictionaryTypeId, true);
        if(data.isEmpty()){
            DictionaryTypeEntity typeEntity = dictionaryTypeService.getInfoByEnCode(dictionaryTypeId);
            if(typeEntity != null){
                data = dictionaryDataService.getList(typeEntity.getId(), true);
            }

        }
        List<DictionaryTypeSelectModel> voListVO = JsonUtil.getJsonToList(data, DictionaryTypeSelectModel.class);
        List<SumTree<DictionaryTypeSelectModel>> sumTrees = TreeDotUtils.convertListToTreeDot(voListVO);
        List<DictionaryTypeSelectVO> list = JsonUtil.getJsonToList(sumTrees, DictionaryTypeSelectVO.class);
        ListVO<DictionaryTypeSelectVO> vo = new ListVO<>();
        vo.setList(list);
        return ActionResult.success(vo);
    }


    /**
     * 获取数据字典信息
     *
     * @param id 主键值
     * @return
     */
    @Operation(summary = "获取数据字典信息")
    @Parameters({
            @Parameter(name = "id", description = "主键值", required = true)
    })
    @GetMapping("/{id}/Info")
    public ActionResult<DictionaryDataInfoVO> info(@PathVariable("id") String id) throws DataException {
        DictionaryDataEntity entity = dictionaryDataService.getInfo(id);
        DictionaryDataInfoVO vo = JsonUtil.getJsonToBeanEx(entity, DictionaryDataInfoVO.class);
        return ActionResult.success(vo);
    }

//    /**
//     * 重复验证（名称）
//     *
//     * @param dictionaryTypeId 类别主键
//     * @param fullName         名称
//     * @param id               主键值
//     * @return
//     */
//    @Operation(summary = "（待定）重复验证（名称）")
//    @GetMapping("/IsExistByFullName")
//    public ActionResult isExistByFullName(String dictionaryTypeId, String fullName, String id) {
//        boolean data = dictionaryDataService.isExistByFullName(dictionaryTypeId, fullName, id);
//        return ActionResult.success(data);
//    }
//
//    /**
//     * 重复验证（编码）
//     *
//     * @param dictionaryTypeId 类别主键
//     * @param enCode           编码
//     * @param id               主键值
//     * @return
//     */
//    @Operation(summary = "（待定）重复验证（编码）")
//    @GetMapping("/IsExistByEnCode")
//    public ActionResult isExistByEnCode(String dictionaryTypeId, String enCode, String id) {
//        boolean data = dictionaryDataService.isExistByEnCode(dictionaryTypeId, enCode, id);
//        return ActionResult.success(data);
//    }


    /**
     * 添加数据字典
     *
     * @param dictionaryDataCrForm 实体对象
     * @return
     */
    @Operation(summary = "添加数据字典")
    @Parameters({
            @Parameter(name = "dictionaryDataCrForm", description = "实体对象", required = true)
    })
    @SaCheckPermission("systemData.dictionary")
    @PostMapping
    public ActionResult create(@RequestBody @Valid DictionaryDataCrForm dictionaryDataCrForm) {
        DictionaryDataEntity entity = JsonUtil.getJsonToBean(dictionaryDataCrForm, DictionaryDataEntity.class);
        if (dictionaryDataService.isExistByFullName(entity.getDictionaryTypeId(), entity.getFullName(), entity.getId())) {
            return ActionResult.fail("字典名称不能重复");
        }
        if (dictionaryDataService.isExistByEnCode(entity.getDictionaryTypeId(), entity.getEnCode(), entity.getId())) {
            return ActionResult.fail("字典编码不能重复");
        }
        dictionaryDataService.create(entity);
        return ActionResult.success("新建成功");
    }

    /**
     * 修改数据字典
     *
     * @param dictionaryDataUpForm 实体对象
     * @param id                   主键值
     * @return
     */
    @Operation(summary = "修改数据字典")
    @Parameters({
            @Parameter(name = "id", description = "主键值", required = true),
            @Parameter(name = "dictionaryDataUpForm", description = "实体对象", required = true)
    })
    @SaCheckPermission("systemData.dictionary")
    @PutMapping("/{id}")
    public ActionResult update(@PathVariable("id") String id, @RequestBody @Valid DictionaryDataUpForm dictionaryDataUpForm) {
        DictionaryDataEntity entity = JsonUtil.getJsonToBean(dictionaryDataUpForm, DictionaryDataEntity.class);
        if (dictionaryDataService.isExistByFullName(entity.getDictionaryTypeId(), entity.getFullName(), id)) {
            return ActionResult.fail("字典名称不能重复");
        }
        if (dictionaryDataService.isExistByEnCode(entity.getDictionaryTypeId(), entity.getEnCode(), id)) {
            return ActionResult.fail("字典编码不能重复");
        }
        boolean flag = dictionaryDataService.update(id, entity);
        if (flag == false) {
            return ActionResult.success("更新失败，数据不存在");
        }
        return ActionResult.success("更新成功");

    }

    /**
     * 删除数据字典
     *
     * @param id 主键值
     * @return
     */
    @Operation(summary = "删除数据字典")
    @Parameters({
            @Parameter(name = "id", description = "主键值", required = true)
    })
    @SaCheckPermission("systemData.dictionary")
    @DeleteMapping("/{id}")
    public ActionResult delete(@PathVariable("id") String id) {
        DictionaryDataEntity entity = dictionaryDataService.getInfo(id);
        if (entity != null) {
            if (dictionaryDataService.isExistSubset(entity.getId())) {
                return ActionResult.fail("字典类型下面有字典值禁止删除");
            }
            dictionaryDataService.delete(entity);
            return ActionResult.success("删除成功");
        }
        return ActionResult.fail("删除失败，数据不存在");
    }

    /**
     * 更新字典状态
     *
     * @param id 主键值
     * @return
     */
    @Operation(summary = "更新字典状态")
    @Parameters({
            @Parameter(name = "id", description = "主键值", required = true)
    })
    @SaCheckPermission("systemData.dictionary")
    @PutMapping("/{id}/Actions/State")
    public ActionResult update(@PathVariable("id") String id) {
        DictionaryDataEntity entity = dictionaryDataService.getInfo(id);
        if (entity != null) {
            if (entity.getEnabledMark() == 1) {
                entity.setEnabledMark(0);
            } else {
                entity.setEnabledMark(1);
            }
            boolean flag = dictionaryDataService.update(entity.getId(), entity);
            if (flag == false) {
                return ActionResult.success("更新失败，数据不存在");
            }
        }
        return ActionResult.success("更新成功");
    }

    /**
     * 数据字典导出功能
     *
     * @param id 接口id
     */
    @Operation(summary = "导出数据字典数据")
    @Parameters({
            @Parameter(name = "id", description = "主键值", required = true)
    })
    @SaCheckPermission("systemData.dictionary")
    @GetMapping("/{id}/Actions/Export")
    public ActionResult exportFile(@PathVariable("id") String id) {
        DownloadVO downloadVO = dictionaryDataService.exportData(id);
        return ActionResult.success(downloadVO);
    }

    /**
     * 数据字典导入功能
     *
     * @param multipartFile 文件
     * @return
     * @throws DataException
     */
    @Operation(summary = "数据字典导入功能")
    @SaCheckPermission("systemData.dictionary")
    @PostMapping(value = "/Actions/Import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ActionResult importFile(@RequestPart("file") MultipartFile multipartFile,
                                   @RequestParam("type") Integer type) throws DataException {
        //判断是否为.json结尾
        if (FileUtil.existsSuffix(multipartFile, ModuleTypeEnum.SYSTEM_DICTIONARYDATA.getTableName())) {
            return ActionResult.fail(MsgCode.IMP002.get());
        }
        try {
            //获取文件内容
            String fileContent = FileUtil.getFileContent(multipartFile);
            DictionaryExportModel exportModel = JsonUtil.getJsonToBean(fileContent, DictionaryExportModel.class);
            List<DictionaryTypeEntity> list = exportModel.getList();
            //父级分类id不存在的话，直接抛出异常
            //如果分类只有一个
            if (list.size() == 1 && !"-1".equals(list.get(0).getParentId()) && dictionaryTypeService.getInfo(list.get(0).getParentId()) == null) {
                return ActionResult.fail("导入失败，查询不到上级分类");
            }
            //如果有多个需要验证分类是否存在
            if (list.stream().filter(t -> "-1".equals(t.getParentId())).count() < 1) {
                boolean exist = false;
                for (DictionaryTypeEntity dictionaryTypeEntity : list) {
                    //判断父级是否存在
                    if (dictionaryTypeService.getInfo(dictionaryTypeEntity.getParentId()) != null) {
                        exist = true;
                    }
                }
                if (!exist) {
                    return ActionResult.fail("导入失败，查询不到上级分类");
                }
            }
            //判断数据是否存在
            return dictionaryDataService.importData(exportModel, type);
        } catch (Exception e) {
            throw new DataException(MsgCode.IMP004.get());
        }
    }



    /**
     * 获取字典数据信息列表
     */
    @Override
    @GetMapping("/getList/{dictionary}")
    public List<DictionaryDataEntity> getList(@PathVariable("dictionary") String dictionary) {
        return dictionaryDataService.getList(dictionary);
    }

    /**
     * 获取数据字典信息
     *
     * @param id 主键值
     * @return
     */
    @Override
    @GetMapping("/getInfo/{id}")
    public DictionaryDataEntity getInfo(@PathVariable("id") String id) {
        return dictionaryDataService.getInfo(id);
    }

    /**
     * 获取字典数据信息列表
     *
     * @param typeCode 字典分类code
     * @param dataCode 字典数据code
     * @return
     * @throws DataException
     */
    @Override
    @GetMapping("/getByTypeDataCode")
    public ActionResult<DictionaryDataEntity> getByTypeDataCode(@RequestParam("typeCode") String typeCode, @RequestParam("dataCode") String dataCode) throws DataException {
        DictionaryDataEntity entity = dictionaryDataService.getByTypeDataCode(typeCode, dataCode);
        return ActionResult.success(entity);
    }

    @Override
    @GetMapping("/getDicList/{dictionaryTypeId}")
    public List<DictionaryDataEntity> getDicList(@PathVariable("dictionaryTypeId") String dictionaryTypeId) {
        return dictionaryDataService.getDicList(dictionaryTypeId);
    }

    @Override
    @GetMapping("/getDicList/{dictionaryTypeId}/{enable}")
    public List<DictionaryDataEntity> getList(@PathVariable("dictionaryTypeId") String dictionaryTypeId, @PathVariable("enable") String enable) {
        if ("true".equals(enable)) {
            return dictionaryDataService.getList(dictionaryTypeId, true);
        }
        return dictionaryDataService.getList(dictionaryTypeId, false);
    }

    /**
     * 获取字典数据信息列表
     *
     * @param typeCode 字典分类code
     * @return
     */
    @Override
    @GetMapping("/getListByTypeDataCode/{typeCode}")
    public ActionResult<List<DictionaryDataEntity>> getListByTypeDataCode(@PathVariable("typeCode") String typeCode) {
        List<DictionaryDataEntity> list = dictionaryDataService.getListByTypeDataCode(typeCode);
        return ActionResult.success(list);
    }

    @Override
    @GetMapping("/getListByCode")
    public List<DictionaryDataEntity> getListByCode(@RequestParam("typeCode") String typeCode) {
        DictionaryTypeEntity dictionaryTypeEntity = dictionaryTypeService.getInfoByEnCode(typeCode);
        if (dictionaryTypeEntity != null) {
            return dictionaryDataService.getList(dictionaryTypeEntity.getId());
        }
        return new ArrayList();
    }

    @Override
    @PostMapping("/getDicList")
    public List<DictionaryDataEntity> getDictionName(@RequestBody List<String> id){
        return dictionaryDataService.getDictionName(id);
    }

    @Override
    @GetMapping("/getSwapInfo/{value}/{parentId}")
    public DictionaryDataEntity getSwapInfo(@PathVariable("value") String value,@PathVariable("parentId") String parentId){
        return dictionaryDataService.getSwapInfo(value,parentId);
    }

}
