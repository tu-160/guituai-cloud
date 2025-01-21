package com.future.permission.model.organize;

import com.alibaba.fastjson.annotation.JSONField;
import com.future.common.util.treeutil.SumTree;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;


@Data
public class OrganizeModel extends SumTree {
    private String fullName;
    private String enCode;
    private Long creatorTime;
    private String manager;
    private String description;
    private int enabledMark;
    private String icon;
    @JSONField(name="category")
    private String  type;
    private long sortCode;
    private String organizeIdTree;
    private String organize;

    @Schema(description ="组织id树")
    private List<String> organizeIds;

    private String lastFullName;
}
