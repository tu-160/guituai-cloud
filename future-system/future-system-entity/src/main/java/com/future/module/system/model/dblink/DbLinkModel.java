package com.future.module.system.model.dblink;

import com.alibaba.fastjson.annotation.JSONField;
import com.future.common.util.treeutil.SumTree;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class DbLinkModel extends SumTree {
    private String fullName;
    private String dbType;
    private String host;
    private String port;
    private Long creatorTime;
    @JSONField(name = "creatorUserId")
    private String creatorUser;
    private String id;
    private Long lastModifyTime;
    @JSONField(name = "lastModifyUserId")
    private String lastModifyUser;
    private Integer enabledMark;
    private Long sortCode;
    private Long num;
}
