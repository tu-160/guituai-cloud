package com.future.module.system.model.synthirdinfo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 企业微信的模型
 *
 * @@version V4.0.0
 * @@copyright 直方信息科技有限公司
 * @@author Future Platform Group
 * @date 2021/5/25 14:18
 */
@Data
public class QyWebChatModel {
    @Schema(description = "CorpId")
    private String qyhCorpId;
    @Schema(description = "AgentId")
    private String qyhAgentId;
    @Schema(description = "AgentSecret")
    private String qyhAgentSecret;
    @Schema(description = "CorpSecret")
    private String qyhCorpSecret;
}
