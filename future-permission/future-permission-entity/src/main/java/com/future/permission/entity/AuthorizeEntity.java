package com.future.permission.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.future.common.base.entity.SuperExtendEntity;

import lombok.Data;

import java.util.Date;

/**
 * 操作权限
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2019年9月26日 上午9:18
 */
@Data
@TableName("base_authorize")
public class AuthorizeEntity extends SuperExtendEntity<String> {

    /**
     * 项目类型
     */
    @TableField("f_item_type")
    private String itemType;

    /**
     * 项目主键
     */
    @TableField("f_item_id")
    private String itemId;

    /**
     * 对象类型
     */
    @TableField("f_object_type")
    private String objectType;

    /**
     * 对象主键
     */
    @TableField("f_object_id")
    private String objectId;

}
