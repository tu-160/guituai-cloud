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
 * 数据备份
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2019年9月27日 上午9:18
 */
@Data
//@TableName("base_dbbackup")
public class DbBackupEntity extends SuperExtendEntity.SuperExtendDEEntity<String> implements Serializable {

    /**
     * 备份库名
     */
    @TableField("F_BACKUPDBNAME")
    private String backupDbName;

    /**
     * 备份时间
     */
    @TableField("F_BACKUPTIME")
    private Date backupTime;

    /**
     * 文件名称
     */
    @TableField("F_FILENAME")
    private String fileName;

    /**
     * 文件大小
     */
    @TableField("F_FILESIZE")
    private String fileSize;

    /**
     * 文件路径
     */
    @TableField("F_FILEPATH")
    private String filePath;

}
