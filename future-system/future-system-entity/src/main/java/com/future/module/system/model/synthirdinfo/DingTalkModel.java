package com.future.module.system.model.synthirdinfo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 钉钉发送信息配置模型
 *
 * @@version V4.0.0
 * @@copyright 直方信息科技有限公司
 * @@author Future Platform Group
 * @date 2021/4/22 14:12
 */
@Data
public class DingTalkModel {
//    private String dingAppKey;
//    private String dingAppSecret;
    @Schema(description = "SynAppKey")
    private String dingSynAppKey;
    @Schema(description = "SynAppSecret")
    private String dingSynAppSecret;
    @Schema(description = "AgentId")
    private String dingAgentId;
}
