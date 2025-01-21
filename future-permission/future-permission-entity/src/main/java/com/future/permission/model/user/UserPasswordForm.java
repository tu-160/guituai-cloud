package com.future.permission.model.user;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserPasswordForm {
    private String oldPassword;
    private String password;
    private String code;
}
