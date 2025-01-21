package com.future.module.system.model.dblink;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 数据连接表单对象
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021/3/12 15:31
 */
@Data
public class DbLinkCreUpForm extends DbLinkBaseForm {

    @Schema(description ="有效标识")
    @NotNull(message = "必填")
    private boolean enabledMark;

}
