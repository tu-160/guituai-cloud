package com.future.module.system.model.dictionarytype;

import com.future.common.util.treeutil.SumTree;

import lombok.Data;

@Data
public class DictionaryTypeModel extends SumTree {
    private String id;
    private String parentId;
    private String fullName;
    private Integer isTree;
    private String enCode;
    private long sortCode;
    private String category;
}
