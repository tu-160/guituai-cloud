package com.future.module.system.model.dblink;

import com.future.common.base.Pagination;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PaginationDbLink extends Pagination {

    private String dbType;

}
