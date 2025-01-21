package com.future.permission.model.authorize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class AuthorizeDataValuesVO {
    List<AuthorizeDataReturnModel> list;
    List<String> ids;
    List<String> all;
}
