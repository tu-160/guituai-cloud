package com.future.module.system.model.commonword;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

import com.future.common.util.treeutil.SumTree;

/**
 * 类功能
 *
 * @author Future Platform Group
 * @version v4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2023-01-09
 */
@Data
public class ComWordsTreeModel extends SumTree {

    /**
     * 分类下模板数量
     */
    private Integer num;

    /**
     * 显示名
     */
    private String fullName;

    /**
     * 自然主键
     */
    private String id;

    /**
     * 应用id
     */
    private List<String> systemIds;

    /**
     * 应用名称
     */
    private String systemNames;

    /**
     * 常用语
     */
    private String commonWordsText;

    /**
     * 常用语类型(0:系统,1:个人)
     */
    private Integer commonWordsType;

    /**
     * 排序
     */
    private Long sortCode;

    /**
     * 有效标志
     */
    private Integer enabledMark;

}
