package com.future.module.system.model.printdev.query;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 打印模板-数查询对象
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2019年9月30日
 */
@Data
public class PrintDevFieldsQuery {

    /**
     * sql语句
     */
    @NotBlank(message = "必填")
    @Schema(description ="sql语句")
    private String sqlTemplate;

    /**
     * 连接id
     */
    @NotBlank(message = "必填")
    @Schema(description ="连接id")
    private String dbLinkId;

}
