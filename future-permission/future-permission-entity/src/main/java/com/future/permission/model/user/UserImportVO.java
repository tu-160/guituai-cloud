package com.future.permission.model.user;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-12-18
 */
@Data
public class UserImportVO implements Serializable {
    /**
     * 导入成功条数
     */
    @Schema(description = "导入成功条数")
    private int snum;
    /**
     * 导入失败条数
     */
    @Schema(description = "导入失败条数")
    private int fnum;
    /**
     * 导入结果状态(0,成功  1，失败)
     */
    @Schema(description = "导入结果状态(0,成功  1，失败)")
    private int resultType;
    /**
     * 失败结果
     */
    @Schema(description = "失败结果")
    private List<UserExportExceptionVO> failResult;

}
