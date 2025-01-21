package com.future.module.system.model.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SystemApiByIdsModel implements Serializable {
    private List<String> ids = new ArrayList<>();
    private List<String> moduleAuthorize;
}
