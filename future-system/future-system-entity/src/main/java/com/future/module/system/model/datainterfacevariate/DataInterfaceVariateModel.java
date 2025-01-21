package com.future.module.system.model.datainterfacevariate;

import lombok.Data;

import javax.validation.constraints.NotNull;

import com.future.common.util.treeutil.SumTree;

import java.io.Serializable;

@Data
public class DataInterfaceVariateModel extends SumTree implements Serializable {
    @NotNull(message = "接口id不能为空")
    private String interfaceId;
    @NotNull(message = "参数名称不能为空")
    private String fullName;
    private String expression;
    private String value;
}
