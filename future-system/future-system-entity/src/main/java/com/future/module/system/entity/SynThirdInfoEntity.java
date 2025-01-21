package com.future.module.system.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.future.common.base.entity.SuperExtendEntity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 第三方工具的公司-部门-用户同步表模型
 *
 * @@version V4.0.0
 * @@copyright 直方信息科技有限公司
 * @@author Future Platform Group
 * @date 2021/4/23 17:06
 */
@Data
@TableName("base_syn_third_info")
public class SynThirdInfoEntity extends SuperExtendEntity.SuperExtendDEEntity<String> implements Serializable {

    /**
     * 第三方类型(1:企业微信;2:钉钉)
     */
    @TableField("F_THIRD_TYPE")
    private Integer thirdType;

    /**
     * 数据类型(1:组织(公司与部门);2:用户)
     */
    @TableField("F_DATA_TYPE")
    private Integer dataType;

    /**
     * 系统对象ID(公司ID、部门ID、用户ID)
     */
    @TableField("F_SYS_OBJ_ID")
    private String sysObjId;

    /**
     * 第三对象ID(公司ID、部门ID、用户ID)
     */
    @TableField("F_THIRD_OBJ_ID")
    private String thirdObjId;

}
