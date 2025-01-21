package com.future.module.system.service;

import java.util.List;

import com.future.base.service.SuperService;
import com.future.module.system.entity.PrintLogEntity;
import com.future.module.system.model.printlog.PrintLogQuery;


public interface PrintLogService extends SuperService<PrintLogEntity> {

    List<PrintLogEntity> getListId(String printId, PrintLogQuery page);
}