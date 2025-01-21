package com.future.module.system;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.future.database.model.entity.DbLinkEntity;
import com.future.feign.utils.FeignName;
import com.future.module.system.fallback.DataSourceApiFallback;

/**
 * 数据连接Api
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-03-24
 */
@FeignClient(name = FeignName.SYSTEM_SERVER_NAME , fallback = DataSourceApiFallback.class, path = "/DataSource")
public interface DataSourceApi {

    /**
     * 数据连接
     * @param id 数据连接Id
     * @return 数据连接对象
     */
    @GetMapping("/{id}/info")
    DbLinkEntity getInfo(@PathVariable("id") String id);

    /**
     * 数据连接
     * @param id 数据连接Id
     * @return 数据连接对象
     */
    @GetMapping("/info/{id}/{tenantId}")
    Object getInfo(@PathVariable("id") String id, @PathVariable("tenantId") String tenantId);

    /**
     * 数据连接
     * @param fullName 数据连接名
     * @return 数据连接对象
     */
    @GetMapping("/infoByFullName")
    DbLinkEntity getInfoByFullName(@RequestParam("fullName") String fullName);

    /**
     * 数据连接
     * @param dbLinkId
     * @return 数据连接对象
     */
    @GetMapping("/getResource")
    DbLinkEntity getResource(@RequestParam("dbLinkId") String dbLinkId, @RequestParam(name = "tenantId", required = false) String tenantId) throws Exception;

}
