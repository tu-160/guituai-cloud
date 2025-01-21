package com.future.module.system.model.systemconfig;

import lombok.Data;

import java.util.List;

@Data
public class SaveSysConfigForm {
    List<SysConfigModel>   configList;
}
