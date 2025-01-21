package com.future.permission.model.authorize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AuthorizeDataVO {
//    private AuthorizeDataReturnVO menu;
    private AuthorizeDataReturnVO module;
    private AuthorizeDataReturnVO button;
    private AuthorizeDataReturnVO column;
    private AuthorizeDataReturnVO resource;
    private AuthorizeDataReturnVO form;

}
