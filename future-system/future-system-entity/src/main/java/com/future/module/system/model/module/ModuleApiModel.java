package com.future.module.system.model.module;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ModuleApiModel implements Serializable {
    private Boolean filterFlowWork = false;
    private List<String> moduleAuthorize;
    private List<String> moduleUrlAddressAuthorize;
    private Boolean singletonOrg = false;
}
