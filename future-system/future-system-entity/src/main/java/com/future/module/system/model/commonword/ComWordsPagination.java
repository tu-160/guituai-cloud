package com.future.module.system.model.commonword;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.future.common.base.Pagination;
import com.future.module.system.entity.CommonWordsEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 类功能
 *
 * @author Future Platform Group
 * @version v4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2023-01-07
 */
@Data
public class ComWordsPagination extends Pagination {

    @Schema(description = "状态")
    private Integer enabledMark;

    public Page<CommonWordsEntity> getPage(){
        return new Page<>(getCurrentPage(), getPageSize(), getTotal());
    }

}
