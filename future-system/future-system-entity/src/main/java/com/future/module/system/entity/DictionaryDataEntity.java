package com.future.module.system.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.future.common.base.entity.SuperExtendEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 字典数据
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2019年9月27日 上午9:18
 */
@Data
@TableName("base_dictionary_data")
public class DictionaryDataEntity extends SuperExtendEntity.SuperExtendDEEntity<String> implements Serializable {

    /**
     * 上级
     */
    @TableField("f_parent_id")
    private String parentId;

    /**
     * 名称
     */
    @TableField("f_full_name")
    private String fullName;

    /**
     * 编码
     */
    @TableField("f_en_code")
    private String enCode;

    /**
     * 拼音
     */
    @TableField("f_simple_spelling")
    private String simpleSpelling;

    /**
     * 默认
     */
    @TableField("f_is_default")
    private Integer isDefault;

    /**
     * 类别主键
     */
    @TableField("f_dictionary_type_id")
    private String dictionaryTypeId;

}
