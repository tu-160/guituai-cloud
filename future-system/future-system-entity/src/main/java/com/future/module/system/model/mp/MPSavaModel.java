package com.future.module.system.model.mp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

import com.future.module.system.entity.SysConfigEntity;

@Data
public class MPSavaModel {
    private List<SysConfigEntity> entitys;

    public MPSavaModel(List<SysConfigEntity> entitys) {
        this.entitys = entitys;
    }
}
