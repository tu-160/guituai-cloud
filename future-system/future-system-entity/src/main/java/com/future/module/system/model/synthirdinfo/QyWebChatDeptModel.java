package com.future.module.system.model.synthirdinfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 企业微信获取部门的对象模型
 *
 * @@version V4.0.0
 * @@copyright 直方信息科技有限公司
 * @@author Future Platform Group
 * @date 2021/4/25 11:10
 */
@Data
public class QyWebChatDeptModel {
    /**
     * 部门ID
     */
    private Integer id;
    /**
     * 部门中文名称
     */
    private String name;
    /**
     * 部门英文名称
     */
    private String name_en;
    /**
     * 部门的上级部门
     */
    private Integer parentid;
    /**
     * 部门排序
     */
    private Integer order;
}
