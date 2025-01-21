package com.future.module.system.model.column;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ModuleColumnUpForm {
    private String creatorUserId;

    private Integer enabledMark;

    private String fullName;

    private String description;

    private Long sortCode;

    private String enCode;

    private String creatorTime;

    private String moduleId;

    private String bindTable;

    private String bindTableName;

    private Integer fieldRule;

    private String tableName;

    private String childTableKey;
}
