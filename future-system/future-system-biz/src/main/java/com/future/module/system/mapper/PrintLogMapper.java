package com.future.module.system.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.future.base.mapper.SuperMapper;
import com.future.module.system.entity.PrintLogEntity;

import java.util.List;

@Mapper
@Repository
public interface PrintLogMapper extends SuperMapper<PrintLogEntity> {

    List<String> getListId(@Param("printId") String printId, @Param("keyword") String keyword);

}