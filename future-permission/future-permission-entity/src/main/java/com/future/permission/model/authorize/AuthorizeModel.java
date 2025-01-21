package com.future.permission.model.authorize;

import com.future.common.util.treeutil.SumTree;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
public class AuthorizeModel extends SumTree {
    private String id;
    private String fullName;
    private String icon;

    private long sortCode;
}
