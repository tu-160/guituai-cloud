package com.future.permission.model.authorize;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

import com.future.common.util.treeutil.SumTree;

@Data
public class AuthorizeDataModel extends SumTree {
    private  String id;
    private String fullName;
    private String icon;
    private Boolean showcheck;
    private Integer checkstate;
    private String title;
    private String moduleId;
    private String type;
    private Date creatorTime;
    private String category;
    private boolean disabled;
    private Long sortCode=9999L;
    private String systemId;
}
