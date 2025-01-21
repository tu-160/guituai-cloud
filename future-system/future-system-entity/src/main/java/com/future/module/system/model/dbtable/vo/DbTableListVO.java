package com.future.module.system.model.dbtable.vo;

import lombok.AllArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

import com.future.common.base.vo.PaginationVO;

/**
 * 表列表返回对象
 *
 * @author Future Platform Group
 * @version v4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2023-03-16
 */
@Data
@AllArgsConstructor
public class DbTableListVO<T> {

    /**
     * 数据集合
     */
    private List<T> list;

    /**
     * 分页信息
     */
    PaginationVO pagination;

}
