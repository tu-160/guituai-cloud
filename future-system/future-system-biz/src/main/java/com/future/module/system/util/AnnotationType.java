package com.future.module.system.util;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 数据接口支持注解类型
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2019年9月27日 上午9:18
 */
@Data
public class AnnotationType {
    /**
     * USER 当前登陆者id
     */
    public static final String USER = "@user";
    /**
     * 当前登陆者部门id
     */
    public static final String DEPARTMENT = "@department";
    /**
     * 当前登陆者组织id
     */
    public static final String ORGANIZE = "@organize";
    /**
     * 当前登录者岗位id
     */
    public static final String POSTION = "@postion";

}
