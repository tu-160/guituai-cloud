package com.future.module.system.model.printlog;

import com.future.common.base.Pagination;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
public class PrintLogQuery  extends Pagination {

    private Long startTime;
    private Long endTime;
}
