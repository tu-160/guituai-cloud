package com.future.module.system;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import com.future.common.base.ActionResult;
import com.future.common.exception.DataException;
import com.future.feign.utils.FeignName;
import com.future.module.system.entity.DictionaryDataEntity;
import com.future.module.system.fallback.DictionaryDataApiFallback;

import java.util.List;

/**
 * 调用数据字典Api
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-03-24
 */
@FeignClient(name = FeignName.SYSTEM_SERVER_NAME, fallback = DictionaryDataApiFallback.class, path = "/DictionaryData")
public interface DictionaryDataApi {
    /**
     * 获取字典数据信息列表
     *
     * @param dictionary
     * @return
     */
    @GetMapping("/getList/{dictionary}")
    List<DictionaryDataEntity> getList(@PathVariable("dictionary") String dictionary);

    /**
     * 获取字典数据信息
     *
     * @param id
     * @return
     */
    @GetMapping("/getInfo/{id}")
    DictionaryDataEntity getInfo(@PathVariable("id") String id);

    /**
     * 获取字典数据信息列表
     *
     * @param typeCode 字典分类code
     * @return
     */
    @GetMapping("/getListByTypeDataCode/{typeCode}")
    ActionResult<List<DictionaryDataEntity>> getListByTypeDataCode(@PathVariable("typeCode") String typeCode);

    /**
     * 获取字典数据信息列表
     *
     * @param typeCode 字典分类code
     * @return
     */
    @GetMapping("/getListByCode")
    List<DictionaryDataEntity> getListByCode(@RequestParam("typeCode") String typeCode);

    /**
     * 获取字典数据信息列表
     *
     * @param typeCode 字典分类code
     * @param dataCode 字典数据code
     * @return
     * @throws DataException
     */
    @GetMapping("/getByTypeDataCode")
    ActionResult<DictionaryDataEntity> getByTypeDataCode(@RequestParam("typeCode") String typeCode, @RequestParam("dataCode") String dataCode) throws DataException;

    /**
     * 通过数据字典id获取数据字典信息
     *
     * @param dictionaryTypeId
     * @return
     */
    @GetMapping("/getDicList/{dictionaryTypeId}")
    List<DictionaryDataEntity> getDicList(@PathVariable("dictionaryTypeId") String dictionaryTypeId);

    /**
     * 通过数据分类id获取id，可选择是否获取有效的数据
     *
     * @param dictionaryTypeId
     * @param enable
     * @return
     */
    @GetMapping("/getDicList/{dictionaryTypeId}/{enable}")
    List<DictionaryDataEntity> getList(@PathVariable("dictionaryTypeId") String dictionaryTypeId, @PathVariable("enable") String enable);


    /**
     * 通过数据字典id获取数据字典信息
     *
     * @return
     */
    @PostMapping("/getDicList")
    List<DictionaryDataEntity> getDictionName(@RequestBody List<String> id);

    /**
     * 代码生成器 数据字典转换
     * @param value
     * @param value
     * @return
     */
    @GetMapping("/getSwapInfo/{value}/{parentId}")
    DictionaryDataEntity getSwapInfo(@PathVariable("value") String value,@PathVariable("parentId") String parentId);

}
