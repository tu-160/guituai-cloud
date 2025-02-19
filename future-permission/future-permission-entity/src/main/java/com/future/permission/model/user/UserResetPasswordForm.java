package com.future.permission.model.user;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class UserResetPasswordForm {
    @NotBlank(message = "必填")
    @Schema(description = "用户id")
    private String id;
    @NotBlank(message = "必填")
    @Schema(description = "新密码，需要 MD5 加密后传输")
    private String userPassword;
    @NotBlank(message = "必填")
    @Schema(description = "重复新密码")
    private String validatePassword;
}
