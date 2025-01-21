package com.future.module.system.model.datainterface;

import com.future.common.util.treeutil.SumTree;

import lombok.Data;

@Data
public class DataInterfaceTreeModel extends SumTree {
//    private String id;
//    private String parentId;
    private String fullName;
    private String category;
}
