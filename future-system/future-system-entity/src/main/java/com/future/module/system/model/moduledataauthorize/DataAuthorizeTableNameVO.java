package com.future.module.system.model.moduledataauthorize;


import lombok.Builder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DataAuthorizeTableNameVO {
	@Schema(description = "连接id")
	private String linkId;
	@Schema(description = "连接表集合")
	private List<String> linkTables;
}
