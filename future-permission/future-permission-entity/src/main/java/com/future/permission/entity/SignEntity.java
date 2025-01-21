package com.future.permission.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.future.common.base.entity.SuperEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 个人签名
 *
 * @author Future Platform Group
 * @copyright 直方信息科技有限公司
 * @date 2022年9月2日 上午9:18
 */
@Data
@TableName("base_sign_img")
public class SignEntity extends SuperEntity<String> {

    /**
     * 签名图片
     */
    @TableField("F_SIGN_IMG")
    private String signImg;

    /**
     * 是否默认
     */
    @TableField("F_IS_DEFAULT")
    private Integer isDefault;
}

