package com.future.module.system.fallback;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import com.future.module.system.SystemApi;
import com.future.module.system.entity.SystemEntity;
import com.future.module.system.model.base.SystemApiByIdsModel;
import com.future.module.system.model.base.SystemApiListModel;
import com.future.module.system.model.base.SystemApiModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date ：2022/8/1 15:12
 */
@Component
public class SystemApiFallback implements SystemApi {
//    @Override
//    public SystemEntity getMainSystem() {
//        return new SystemEntity();
//    }

    @Override
    public List<SystemEntity> getList(@RequestBody SystemApiListModel model) {
        return new ArrayList<>();
    }

    @Override
    public List<SystemEntity> getListByIds(@RequestBody SystemApiByIdsModel model) {
        return new ArrayList<>();
    }

    @Override
    public SystemEntity getInfoById(String systemId) {
        return new SystemEntity();
    }

    @Override
    public SystemEntity getInfoByEnCode(String enCode) {
        return new SystemEntity();
    }

//    @Override
//    public List<SystemEntity> getMainSys(List<String> systemIds) {
//        return new ArrayList<>();
//    }
//
//    @Override
//    public List<String> getCurrentUserSystem(UserInfo userInfo) {
//        return null;
//    }

    @Override
    public List<SystemEntity> findSystemAdmin(@RequestBody SystemApiModel model) {
        return new ArrayList<>();
    }

}
