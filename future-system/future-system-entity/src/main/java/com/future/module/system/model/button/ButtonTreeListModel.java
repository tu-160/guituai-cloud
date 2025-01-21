package com.future.module.system.model.button;


import com.future.common.util.treeutil.SumTree;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ButtonTreeListModel extends SumTree {
    private String  id;
    private String parentId;
    private String fullName;
    private String enCode;
    private String icon;
    private Integer enabledMark;
    private String description;
    private Long sortCode;
}
