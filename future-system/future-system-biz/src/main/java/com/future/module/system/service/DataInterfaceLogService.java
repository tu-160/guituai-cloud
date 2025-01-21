package com.future.module.system.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.future.base.service.SuperService;
import com.future.common.base.Pagination;
import com.future.module.system.entity.DataInterfaceLogEntity;
import com.future.module.system.model.InterfaceOauth.PaginationIntrfaceLog;

import java.util.List;

/**
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021/3/12 15:31
 */
public interface DataInterfaceLogService extends SuperService<DataInterfaceLogEntity> {

    /**
     * 添加日志
     *
     * @param dateInterfaceId 接口Id
     * @param invokWasteTime  执行时间
     */
    void create(String dateInterfaceId, Integer invokWasteTime);

    /**
     * 获取调用日志列表
     *
     * @param invokId 接口id
     * @return
     */
    List<DataInterfaceLogEntity> getList(String invokId, Pagination pagination);

    /**
     * 通过权限判断添加日志
     *
     * @param dateInterfaceId 接口Id
     * @param invokWasteTime  执行时间
     */
    void create(String dateInterfaceId, Integer invokWasteTime,String appId,String invokType);

    /**
     * 获取调用日志列表(多id)
     *
     * @param invokIds    接口ids
     * @param pagination 分页参数
     * @return ignore
     */
    List<DataInterfaceLogEntity> getListByIds(String appId,List<String> invokIds, PaginationIntrfaceLog pagination);

}
