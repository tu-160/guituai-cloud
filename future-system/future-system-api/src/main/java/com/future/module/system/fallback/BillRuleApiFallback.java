package com.future.module.system.fallback;

import org.springframework.stereotype.Component;

import com.future.common.base.ActionResult;
import com.future.common.exception.DataException;
import com.future.module.system.BillRuleApi;

/**
 * 获取单据规则Api降级处理
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-03-24
 */
@Component
public class BillRuleApiFallback implements BillRuleApi {

    @Override
    public ActionResult useBillNumber(String enCode) {
        return null;
    }

    @Override
    public ActionResult<String> getBillNumber(String enCode) throws DataException {
        return null;
    }
}
