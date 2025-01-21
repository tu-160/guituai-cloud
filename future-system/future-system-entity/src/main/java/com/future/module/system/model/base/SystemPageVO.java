package com.future.module.system.model.base;

import com.future.common.base.Page;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 类功能
 *
 * @author Future Platform Group
 * @version v4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2023-02-27
 */
@Data
public class SystemPageVO extends Page {

    private String enabledMark;

    private Boolean selector;
}
