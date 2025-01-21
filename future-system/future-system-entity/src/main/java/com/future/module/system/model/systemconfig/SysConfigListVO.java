package com.future.module.system.model.systemconfig;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class SysConfigListVO {
    List<SysConfigModel> list;
}
