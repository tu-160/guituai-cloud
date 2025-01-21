package com.future.permission.model.usergroup;

import com.future.common.util.treeutil.SumTree;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 转树模型
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date ：2022/3/11 11:18
 */
@Data
public class GroupTreeModel extends SumTree {
    private String fullName;
    private String type;
    private Long num;

    private Integer enabledMark;
    private String icon;
}
