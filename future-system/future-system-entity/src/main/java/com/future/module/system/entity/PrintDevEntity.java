package com.future.module.system.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.future.common.base.entity.SuperExtendEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 打印模板-实体类
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2019年9月30日
 */
@Data
@EqualsAndHashCode
@TableName("base_print_template")
public class PrintDevEntity extends SuperExtendEntity.SuperExtendDEEntity<String> {

    private static final long serialVersionUID = 1L;

    /**
     * 名称
     */
    @TableField("F_FULL_NAME")
    private String fullName;

    /**
     * 编码
     */
    @TableField("F_EN_CODE")
    private String enCode;

    /**
     * 分类
     */
    @TableField("F_CATEGORY")
    private String category;

    /**
     * 类型(1-流程表单 2-功能表单)
     */
    @TableField("F_TYPE")
    private Integer type;

    /**
     * 连接数据 _id
     */
    @TableField("F_DB_LINK_ID")
    private String dbLinkId;

    /**
     * sql语句
     */
    @TableField("F_SQL_TEMPLATE")
    private String sqlTemplate;

    /**
     * 左侧字段
     */
    @TableField("F_LEFT_FIELDS")
    private String leftFields;

    /**
     * 打印模板
     */
    @TableField("F_PRINT_TEMPLATE")
    private String printTemplate;

    /**
     * 纸张参数
     */
    @TableField("F_PAGE_PARAM")
    private String pageParam;
}
