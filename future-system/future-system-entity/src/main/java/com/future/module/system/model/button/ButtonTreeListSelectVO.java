package com.future.module.system.model.button;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class ButtonTreeListSelectVO {
    private String id;
    private String parentId;
    private String fullName;
    private String icon;
    private List<ButtonTreeListModel> children;
}
