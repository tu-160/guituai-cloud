package com.future.permission.model.user;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class UserModifyPasswordForm {
    @NotBlank(message = "必填")
    @Schema(description ="旧密码,需要 MD5 加密后传输")
    private String oldPassword;
    @NotBlank(message = "必填")
    @Schema(description ="新密码")
    private String password;
    @NotBlank(message = "必填")
    @Schema(description ="验证码")
    private String code;
    @NotBlank(message = "必填")
    @Schema(description ="验证码标识")
    private String timestamp;
}
