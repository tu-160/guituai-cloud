package com.future.module.system.model.printdev;

import com.future.common.util.treeutil.SumTree;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 类功能
 *
 * @author Future Platform Group
 * @version v4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2022-07-22
 */
@Data
public class PrintTableTreeModel extends SumTree<PrintTableTreeModel> {

    private String fullName;

}
