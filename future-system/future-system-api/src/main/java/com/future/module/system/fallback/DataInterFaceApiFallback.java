package com.future.module.system.fallback;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.future.common.base.ActionResult;
import com.future.common.base.Pagination;
import com.future.module.system.DataInterFaceApi;
import com.future.module.system.entity.DataInterfaceEntity;
import com.future.module.system.model.datainterface.DataInterfaceInvokeModel;
import com.future.module.system.model.datainterface.DataInterfacePage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 调用数据接口Api降级处理
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-03-24
 */
@Component
public class DataInterFaceApiFallback implements DataInterFaceApi {

    @Override
    public DataInterfaceEntity getDataInterfaceInfo(String id, String tenantId) {
        return null;
    }

    @Override
    public ActionResult infoToIdById(String id, Map<String, String> parameterMap) {
        return ActionResult.fail("调用失败");
    }

    @Override
    public ActionResult infoToId(String id) {
        return ActionResult.fail("调用失败");
    }

    @Override
    public ActionResult invokeById(DataInterfaceInvokeModel dataInterfaceInvokeModel) {
        return ActionResult.fail("调用失败");
    }

    @Override
    public ActionResult infoToIdPageList(String id, DataInterfacePage page) {
        return null;
    }

    @Override
    public ActionResult<List<Map<String, Object>>> infoByIds(String id, DataInterfacePage page) {
        return null;
    }

    @Override
    public List<DataInterfaceEntity> getInterfaceList(List<String> id) {
        return new ArrayList<>();
    }

    @Override
    public DataInterfaceEntity getEntity(String id)  { return null; }
}
