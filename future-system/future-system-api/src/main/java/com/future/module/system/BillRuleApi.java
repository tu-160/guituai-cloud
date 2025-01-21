package com.future.module.system;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.future.common.base.ActionResult;
import com.future.common.exception.DataException;
import com.future.feign.utils.FeignName;
import com.future.module.system.fallback.BillRuleApiFallback;

/**
 * 获取单据规则Api
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-03-24
 */
@FeignClient(name = FeignName.SYSTEM_SERVER_NAME , fallback = BillRuleApiFallback.class, path = "/BillRule")
public interface BillRuleApi {

    /**
     * 获取单据缓存
     * @param enCode
     * @return
     */
    @GetMapping("/useBillNumber/{enCode}")
    ActionResult useBillNumber(@PathVariable("enCode") String enCode);


    /**
     * 删除单据缓存
     * @param enCode
     * @return
     */
    @GetMapping("/getBillNumber/{enCode}")
    ActionResult<String> getBillNumber(@PathVariable("enCode") String enCode) throws DataException;

}
