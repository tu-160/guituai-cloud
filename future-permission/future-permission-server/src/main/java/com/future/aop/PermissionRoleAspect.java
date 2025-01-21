package com.future.aop;

import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.future.aop.constant.PermissionConstant;
import com.future.aop.util.PermissionAspectUtil;
import com.future.common.util.UserProvider;
import com.future.permission.entity.OrganizeRelationEntity;
import com.future.permission.entity.RoleEntity;
import com.future.permission.model.role.RoleCrForm;
import com.future.permission.model.role.RoleUpForm;
import com.future.permission.service.OrganizeRelationService;
import com.future.permission.service.RoleService;

import lombok.extern.slf4j.Slf4j;

/**
 * 角色操作权限
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2022/2/10
 */
@Slf4j
@Aspect
@Component
public class PermissionRoleAspect implements PermissionAdminBase {

    @Autowired
    private UserProvider userProvider;
    @Autowired
    private RoleService roleService;
    @Autowired
    private OrganizeRelationService organizeRelationService;

    /**
     * 分级管理切点
     */
    @Pointcut("@annotation(com.future.common.annotation.RolePermission)")
    public void pointcut() {
    }

    /**
     * 分级管理切点
     *
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        return PermissionAdminBase.permissionCommon(pjp, userProvider, this);
    }

    @Override
    public Boolean detailPermission(ProceedingJoinPoint pjp, String operatorUserId, String methodName) {
        boolean flag = false;
        switch (methodName) {
            case PermissionConstant.METHOD_CREATE:
                RoleCrForm roleCrForm = (RoleCrForm) pjp.getArgs()[0];
                if (!checkAdminGlobal(roleCrForm.getGlobalMark(), userProvider)) {
                    return PermissionAspectUtil.getPermitByOrgId(
                            // 操作目标对象组织ID集合
                            getOrganize(roleCrForm.getOrganizeIdsTree()),
                            operatorUserId,
                            PermissionConstant.METHOD_CREATE);
                }
                return true;
            case PermissionConstant.METHOD_UPDATE:
                RoleUpForm roleUpForm = (RoleUpForm) pjp.getArgs()[0];
                // 非管理员情况下
                if (!checkAdminGlobal(roleUpForm.getGlobalMark(), userProvider)) {
                    // 得到以前的组织id
                    String roleId = (String) pjp.getArgs()[1];
                    List<String> relationListByRoleId = organizeRelationService.getRelationListByRoleId(roleId).stream().map(OrganizeRelationEntity::getOrganizeId).collect(Collectors.toList());
                    StringJoiner stringJoiners = new StringJoiner(",");
                    relationListByRoleId.forEach(t -> {
                        stringJoiners.add(t);
                    });
                    if (PermissionAspectUtil.getPermitByOrgId(
                            // 操作目标对象组织ID集合
                            stringJoiners.toString(),
                            operatorUserId,
                            PermissionConstant.METHOD_UPDATE)) {
                        return PermissionAspectUtil.getPermitByOrgId(
                                // 操作目标对象组织ID集合
                                getOrganize(roleUpForm.getOrganizeIdsTree()),
                                operatorUserId,
                                PermissionConstant.METHOD_UPDATE);
                    }
                    return false;
                }
                return true;
            case PermissionConstant.METHOD_DELETE:
                String roleId = pjp.getArgs()[0].toString();
                RoleEntity roleEntity = roleService.getInfo(roleId);
                // 获取角色关联的组织信息
                List<OrganizeRelationEntity> relationListByRoleId = organizeRelationService.getRelationListByRoleId(roleId);
                StringBuilder orgId = new StringBuilder();
                relationListByRoleId.stream().forEach(t->{
                    orgId.append(t.getOrganizeId() + ",");
                });
                if (roleEntity != null && !checkAdminGlobal(roleEntity.getGlobalMark(), userProvider)) {
                    return PermissionAspectUtil.getPermitByOrgId(
                            // 操作目标对象组织ID集合
                            orgId.toString(),
                            operatorUserId,
                            PermissionConstant.METHOD_DELETE);
                }
                return true;
            default:
                break;
        }
        return true;
    }

    /**
     * 转成组织id字符串
     * @param orgIdsTree
     * @return
     */
    private String getOrganize(List<List<String>> orgIdsTree) {
        StringBuilder orgIds = new StringBuilder();
        for (List<String> list : orgIdsTree) {
            if (list.size() > 0) {
                String orgId = list.get(list.size() - 1);
                orgIds.append(orgId + ",");
            }
        }
        return orgIds.toString();
    }

    /**
     * 全局角色只能超管来操作
     *
     * @param globalMark   全局标识 1：全局 0: 非全局
     * @param userProvider 操作者
     */
    private Boolean checkAdminGlobal(Integer globalMark, UserProvider userProvider) {
        if (globalMark != null && globalMark == 1) {
            return userProvider.get().getIsAdministrator();
        }
        return false;
    }

}
