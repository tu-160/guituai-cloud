package com.future.module.oauth.model.app;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021/5/14 8:47
 */
@Data
public class AppDataModel {
    private String id;
    private String enCode;
    private String fullName;
    private String type;
    private String icon;
    private String iconBackground;
}
