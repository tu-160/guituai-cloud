package com.future.permission.model.socails;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 流程设计
 *
 * @author Future Platform Group
 * @version v4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2022/7/14 11:02:42
 */
@Data
public class SocialsUserModel {
    /**
     * 主键
     */
    private String id;
    /**
     * 系统用户id
     */
    private String userId;
    /**
     * 第三方类型
     */
    private String socialType;

    /**
     * 第三方uuid
     */
    private String socialId;
    /**
     * 第三方账号
     */
    private String socialName;

    /**
     * 创建时间
     */
    private Date creatorTime;

    /**
     * 描述
     */
    private String description;
}
