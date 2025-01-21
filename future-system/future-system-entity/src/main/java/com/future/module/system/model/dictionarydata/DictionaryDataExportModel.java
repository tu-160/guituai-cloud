package com.future.module.system.model.dictionarydata;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 数据字典数据模板
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-06-11
 */
@Data
public class DictionaryDataExportModel implements Serializable {
    /**
     * 主键
     */
    private String id;

    /**
     * 上级
     */
    private String parentId;

    /**
     * 名称
     */
    private String fullName;

    /**
     * 编码
     */
    private String enCode;

    /**
     * 拼音
     */
    private String simpleSpelling;

    /**
     * 默认
     */
    private Integer isDefault;

    /**
     * 描述
     */
    private String description;

    /**
     * 排序码
     */
    private Long sortCode;

    /**
     * 有效标志
     */
    private Integer enabledMark;

    /**
     * 创建时间
     */
    private Date creatorTime;

    /**
     * 创建用户
     */
    private String creatorUserId;

    /**
     * 修改时间
     */
    private Date lastModifyTime;

    /**
     * 修改用户
     */
    private String lastModifyUserId;

    /**
     * 删除标志
     */
    private Integer deleteMark;

    /**
     * 删除时间
     */
    private Date deleteTime;

    /**
     * 删除用户
     */
    private String deleteUserId;

    /**
     * 类别主键
     */
    private String dictionaryTypeId;
}
