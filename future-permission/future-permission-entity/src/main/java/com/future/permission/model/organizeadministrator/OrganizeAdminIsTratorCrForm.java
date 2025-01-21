package com.future.permission.model.organizeadministrator;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * 机构分级管理员
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2019年9月26日 上午9:18
 */
@Data
public class OrganizeAdminIsTratorCrForm implements Serializable {

    /**
     * 用户主键
     **/
    @Schema(description = "用户主键")
    @NotBlank(message = "管理员不能为空")
    private String userId;

    /**
     * 分级管理员模型集合
     */
    @Schema(description = "分级管理员模型集合")
    private List<OrganizeAdministratorCrModel> orgAdminModel;


    @Schema(description = "菜单集合")
    private List<String> moduleIds;
    @Schema(description = "应用集合")
    private List<String> systemIds;

}
