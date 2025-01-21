package com.future.permission.model.user;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 超级管理员设置表单参数
 *
 * @author Future Platform Group
 * @version v4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2022/2/23
 */
@Data
public class UserUpAdminForm implements Serializable {

    @Schema(description ="超级管理id集合")
    List<String> adminIds;

}
