package com.future.module.system.model.advancedquery;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AdvancedQueryListVO extends AdvancedQuerySchemeForm{
	@Schema(description = "主键")
	private String id;
}
