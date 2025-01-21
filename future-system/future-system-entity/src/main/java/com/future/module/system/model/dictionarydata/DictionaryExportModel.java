package com.future.module.system.model.dictionarydata;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.future.module.system.entity.DictionaryTypeEntity;

/**
 * 数据字典导入导出模板
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-06-11
 */
@Data
public class DictionaryExportModel implements Serializable {

    /**
     * 字典分类
     */
    private List<DictionaryTypeEntity> list = new ArrayList<>();

    /**
     * 数据集合
     */
    private List<DictionaryDataExportModel> modelList = new ArrayList<>();

}
