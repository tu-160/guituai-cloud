package com.future.module.system.model.dictionarytype;

import com.future.common.util.treeutil.SumTree;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class DictionaryTypeSelectModel extends SumTree {
    private String id;
    private String parentId;
    private String fullName;
    private String enCode;
}
