package com.future.permission.model.sign;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 个人签名
 *
 * @author Future Platform Group
 * @copyright 直方信息科技有限公司
 * @date 2022年9月2日 上午9:18
 */
@Data
public class SignForm {

    @Schema(description ="签名图片")
    private String signImg;
    @Schema(description ="状态")
    private Integer isDefault;

}
