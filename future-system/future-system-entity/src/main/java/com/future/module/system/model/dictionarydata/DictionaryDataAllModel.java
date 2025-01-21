package com.future.module.system.model.dictionarydata;

import com.future.common.util.treeutil.SumTree;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class DictionaryDataAllModel extends SumTree {
    private String  fullName;
    private String  enCode;
}
