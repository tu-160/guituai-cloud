package com.future.module.system.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.future.common.base.entity.SuperExtendEntity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 数据接口调用日志
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-06-03
 */
@Data
@TableName("base_data_interface_log")
public class DataInterfaceLogEntity extends SuperExtendEntity<String> implements Serializable {

    /**
     * 调用接口id
     */
    @TableField("f_invok_id")
    private String invokId;

    /**
     * 调用时间
     */
    @TableField(value = "f_invok_time", fill = FieldFill.INSERT)
    private Date invokTime;

    /**
     * 调用者id
     */
    @TableField("f_user_id")
    private String userId;

    /**
     * 请求ip
     */
    @TableField("f_invok_ip")
    private String invokIp;

    /**
     * 请求设备
     */
    @TableField("f_invok_device")
    private String invokDevice;

    /**
     * 请求类型
     */
    @TableField("f_invok_type")
    private String invokType;

    /**
     * 请求耗时
     */
    @TableField("f_invok_waste_time")
    private Integer invokWasteTime;

    /**
     * 接口授权AppId
     */
    @TableField("f_oauth_app_id")
    private String oauthAppId;

}
