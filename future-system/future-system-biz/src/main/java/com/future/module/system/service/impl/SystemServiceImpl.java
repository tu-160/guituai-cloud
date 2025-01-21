package com.future.module.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.future.base.service.*;
import com.future.common.base.UserInfo;
import com.future.common.base.entity.*;
import com.future.common.constant.PlatformConst;
import com.future.common.constant.PermissionConst;
import com.future.common.util.JsonUtil;
import com.future.common.util.RandomUtil;
import com.future.common.util.StringUtil;
import com.future.common.util.UserProvider;
import com.future.module.system.entity.SystemEntity;
import com.future.module.system.mapper.SystemMapper;
import com.future.module.system.model.base.SystemBaeModel;
import com.future.module.system.service.ModuleService;
import com.future.module.system.service.SystemService;
import com.future.permission.AuthorizeApi;
import com.future.permission.OrganizeAdminTratorApi;
import com.future.permission.PermissionGroupApi;
import com.future.permission.entity.OrganizeAdministratorEntity;
import com.future.permission.entity.PermissionGroupEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 系统
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2019年9月27日 上午9:18
 */
@Service
public class SystemServiceImpl extends SuperServiceImpl<SystemMapper, SystemEntity> implements SystemService {

    @Autowired
    private UserProvider userProvider;
    @Autowired
    private ModuleService moduleService;
    @Autowired
    private AuthorizeApi authorizeApi;
    @Autowired
    private OrganizeAdminTratorApi organizeAdminTratorApi;
    @Autowired
    private PermissionGroupApi permissionGroupApi;

    @Override
    public List<SystemEntity> getList() {
        QueryWrapper<SystemEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().orderByAsc(SystemEntity::getSortCode)
                .orderByDesc(SystemEntity::getCreatorTime);
        return this.list(queryWrapper);
    }

    @Override
    public List<SystemEntity> getList(String keyword, Boolean filterEnableMark, boolean verifyAuth, Boolean filterMain, boolean isList, List<String> moduleAuthorize) {
        List<String> systemIds = new ArrayList<>();
        QueryWrapper<SystemEntity> queryWrapper = new QueryWrapper<>();
        // 是否为列表，特殊处理
        if (isList) {
            systemIds.addAll(authorizeApi.getAuthorizeByUser(true).getSystemList().stream().map(SystemBaeModel::getId).collect(Collectors.toList()));
        }
        if (StringUtil.isNotEmpty(keyword)) {
            queryWrapper.lambda().and(t ->
                    t.like(SystemEntity::getFullName, keyword).or().like(SystemEntity::getEnCode, keyword)
            );
        }
        if (filterEnableMark == null) {
            queryWrapper.lambda().eq(SystemEntity::getEnabledMark, 0);
        } else if (filterEnableMark) {
            queryWrapper.lambda().eq(SystemEntity::getEnabledMark, 1);
        }
        if (verifyAuth) {
            List<String> systemList = authorizeApi.getAuthorizeByUser(false).getSystemList()
                    .stream().map(SystemBaeModel::getId).collect(Collectors.toList());
            if (systemList.size() == 0) {
                return new ArrayList<>();
            }
            systemIds.addAll(systemList);
            queryWrapper.lambda().in(SystemEntity::getId, systemIds);
        }
        // 过滤掉开发平台
        if (filterMain != null && filterMain) {
            queryWrapper.lambda().ne(SystemEntity::getEnCode, PlatformConst.MAIN_SYSTEM_CODE);
        }
        if (moduleAuthorize.size() > 0) {
            queryWrapper.lambda().notIn(SystemEntity::getId, moduleAuthorize);
        }
        queryWrapper.lambda().orderByAsc(SystemEntity::getSortCode).orderByDesc(SystemEntity::getCreatorTime);
        return this.list(queryWrapper);
    }

    @Override
    public SystemEntity getInfo(String id) {
        QueryWrapper<SystemEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SystemEntity::getId, id);
        return this.getOne(queryWrapper);
    }

    @Override
    public Boolean isExistFullName(String id, String fullName) {
        QueryWrapper<SystemEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SystemEntity::getFullName, fullName);
        if (StringUtil.isNotEmpty(id)) {
            queryWrapper.lambda().ne(SystemEntity::getId, id);
        }
        return this.count(queryWrapper) > 0;
    }

    @Override
    public Boolean isExistEnCode(String id, String enCode) {
        QueryWrapper<SystemEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SystemEntity::getEnCode, enCode);
        if (StringUtil.isNotEmpty(id)) {
            queryWrapper.lambda().ne(SystemEntity::getId, id);
        }
        return this.count(queryWrapper) > 0;
    }

    @Override
    @Transactional
    public Boolean create(SystemEntity entity) {
        entity.setId(RandomUtil.uuId());
        entity.setIsMain(0);
        entity.setCreatorUserId(userProvider.get().getUserId());
        entity.setCreatorTime(new Date());
        boolean save = this.save(entity);
        if (!userProvider.get().getIsAdministrator() && save) {
            // 当前用户创建的组织要赋予权限
            OrganizeAdministratorEntity organizeAdministratorEntity = new OrganizeAdministratorEntity();
            organizeAdministratorEntity.setUserId(userProvider.get().getUserId());
            organizeAdministratorEntity.setOrganizeId(entity.getId());
            organizeAdministratorEntity.setOrganizeType(PermissionConst.SYSTEM);
            organizeAdminTratorApi.saveOrganizeAdminTrator(organizeAdministratorEntity);
        }
        return save;
    }

    @Override
    @Transactional
    public Boolean update(String id, SystemEntity entity) {
        entity.setId(id);
        if (entity.getIsMain() == null) {
            entity.setIsMain(0);
        }
        entity.setLastModifyUserId(userProvider.get().getUserId());
        entity.setLastModifyTime(new Date());
        return this.updateById(entity);
    }

    @Override
    @Transactional
    public Boolean delete(String id) {
        moduleService.deleteBySystemId(id);
        return this.removeById(id);
    }

    @Override
    public List<SystemEntity> getListByIds(List<String> list, List<String> moduleAuthorize) {
        List<SystemEntity> systemList = new ArrayList<>(16);
        if (list.size() > 0) {
            QueryWrapper<SystemEntity> queryWrapper = new QueryWrapper<>();
            if (moduleAuthorize != null && moduleAuthorize.size() > 0) {
                queryWrapper.lambda().notIn(SystemEntity::getId, moduleAuthorize);
            }
            queryWrapper.lambda().in(SystemEntity::getId, list);
            queryWrapper.lambda().eq(SystemEntity::getEnabledMark, 1);
            queryWrapper.lambda().orderByAsc(SystemEntity::getSortCode).orderByDesc(SystemEntity::getCreatorTime);
            return this.list(queryWrapper);
        }
        return systemList;
    }

    @Override
    public SystemEntity getInfoByEnCode(String enCode) {
        QueryWrapper<SystemEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SystemEntity::getEnCode, enCode);
        return this.getOne(queryWrapper);
    }

    @Override
    public List<SystemEntity> findSystemAdmin(int mark, String mainSystemCode, List<String> moduleAuthorize) {
        QueryWrapper<SystemEntity> queryWrapper = new QueryWrapper<>();
        if (mark == 1) {
            queryWrapper.lambda().eq(SystemEntity::getEnabledMark, mark)
                    .ne(SystemEntity::getEnCode, mainSystemCode);
        }
        if (moduleAuthorize != null && moduleAuthorize.size() > 0) {
            queryWrapper.lambda().notIn(SystemEntity::getId, moduleAuthorize);
        }
        queryWrapper.lambda().orderByAsc(SystemEntity::getSortCode).orderByDesc(SystemEntity::getCreatorTime);
        return this.list(queryWrapper);
    }

}
