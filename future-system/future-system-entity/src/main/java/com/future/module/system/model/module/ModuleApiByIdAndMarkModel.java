package com.future.module.system.model.module;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModuleApiByIdAndMarkModel implements Serializable {
    private int mark;
    private String id;
    private List<String> moduleAuthorize;
    private List<String> moduleUrlAddressAuthorize;
}
