package com.future.permission.model.position;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
public class PositionListAllVO {
    private String id;
    private String enCode;
    private String fullName;
    private String organizeId;
    private String type;

}
