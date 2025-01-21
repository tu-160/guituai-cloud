package com.future.module.system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.future.common.base.Page;
import com.future.common.base.Pagination;
import com.future.common.base.UserInfo;
import com.future.common.consts.DeviceType;
import com.future.common.util.*;
import com.future.module.oauth.util.AuthUtil;
import com.future.module.system.model.UserOnlineModel;
import com.future.module.system.service.UserOnlineService;
import com.future.reids.util.RedisUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 在线用户
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2019年9月26日 上午9:18
 */
@Service
public class UserOnlineServiceImpl implements UserOnlineService {

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private UserProvider userProvider;
    @Autowired
    private CacheKeyUtil cacheKeyUtil;

    @Override
    public List<UserOnlineModel> getList(Pagination page) {
        List<UserOnlineModel> userOnlineList = new ArrayList<>();
        List<String> tokens = UserProvider.getLoginUserListToken();
        for (String token : tokens) {
            UserInfo userInfo = UserProvider.getUser(token);
            if(userInfo.getId() != null){
                if(UserProvider.isTempUser(userInfo)){
                    //临时用户不显示
                    continue;
                }
                UserOnlineModel userOnlineModel = new UserOnlineModel();
                userOnlineModel.setUserId(userInfo.getUserId());
                userOnlineModel.setUserName((userInfo.getUserName()) + "/" + userInfo.getUserAccount());
                userOnlineModel.setLoginIPAddress(userInfo.getLoginIpAddress());
                userOnlineModel.setLoginAddress(userInfo.getLoginIpAddressName());
                userOnlineModel.setOrganize(userInfo.getOrganize());
                userOnlineModel.setLoginTime(userInfo.getLoginTime());
                userOnlineModel.setTenantId(userInfo.getTenantId());
                userOnlineModel.setToken(token);
                userOnlineModel.setDevice(userInfo.getLoginDevice());
                userOnlineModel.setLoginBrowser(userInfo.getBrowser());
                userOnlineModel.setLoginSystem(userInfo.getLoginPlatForm());
                userOnlineList.add(userOnlineModel);
            }
        }
        String tenantId =userProvider.get().getTenantId();
        userOnlineList = userOnlineList.stream().filter(t -> String.valueOf(t.getTenantId()).equals(String.valueOf(tenantId))).collect(Collectors.toList());
        if(!StringUtil.isEmpty(page.getKeyword())){
            userOnlineList=userOnlineList.stream().filter(t->t.getUserName().contains(page.getKeyword())).collect(Collectors.toList());
        }
        userOnlineList.sort(Comparator.comparing(UserOnlineModel::getLoginTime).reversed());
        page.setTotal(userOnlineList.size());
        userOnlineList = PageUtil.getListPage((int) page.getCurrentPage(), (int) page.getPageSize(), userOnlineList);
        return userOnlineList;
    }

    @Override
    public void delete(String... token) {
        AuthUtil.kickoutByToken(token);
    }
}
