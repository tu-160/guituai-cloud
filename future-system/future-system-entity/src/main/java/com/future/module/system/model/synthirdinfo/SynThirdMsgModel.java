package com.future.module.system.model.synthirdinfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 第三方工具的对象同步表
 *
 * @@version V4.0.0
 * @@copyright 直方信息科技有限公司
 * @@author Future Platform Group
 * @date 2021/4/26 16:18
 */
@Data
public class SynThirdMsgModel {
    private String sysObjId;
    private String thirdObjId;
    private Integer isSynOk;
    private String errorMsg;
    private String synstate;
}
