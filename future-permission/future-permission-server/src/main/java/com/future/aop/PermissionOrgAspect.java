package com.future.aop;

import java.util.StringJoiner;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.future.aop.constant.PermissionConstant;
import com.future.aop.util.PermissionAspectUtil;
import com.future.common.util.UserProvider;
import com.future.permission.entity.OrganizeEntity;
import com.future.permission.model.organize.OrganizeCrForm;
import com.future.permission.model.organize.OrganizeDepartCrForm;
import com.future.permission.model.organize.OrganizeDepartUpForm;
import com.future.permission.model.organize.OrganizeUpForm;
import com.future.permission.service.OrganizeService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021/3/15 17:12
 */
@Slf4j
@Aspect
@Component
public class PermissionOrgAspect implements PermissionAdminBase {

    @Autowired
    private UserProvider userProvider;
    @Autowired
    private OrganizeService organizeService;

    /**
     * 分级管理切点
     */
    @Pointcut("@annotation(com.future.common.annotation.OrganizePermission)")
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
        switch (methodName) {
            case PermissionConstant.METHOD_CREATE:
                return PermissionAspectUtil.getPermitByOrgIds(
                        // 操作目标对象表单对象
                        ((OrganizeCrForm) pjp.getArgs()[0]).getParentId(),
                        operatorUserId,
                        PermissionConstant.METHOD_CREATE);
            case PermissionConstant.METHOD_CREATE_DEPARTMENT:
                return PermissionAspectUtil.getPermitByOrgIds(
                        // 操作目标对象表单对象
                        ((OrganizeDepartCrForm) pjp.getArgs()[0]).getParentId(),
                        operatorUserId,
                        PermissionConstant.METHOD_CREATE);
            case PermissionConstant.METHOD_UPDATE:
                // 当前组织id
                String orgId = (String) pjp.getArgs()[0];
                // 当前组织父级id
                OrganizeEntity info = organizeService.getInfo(orgId);
                // 修改后的id
                OrganizeUpForm organizeUpForm = (OrganizeUpForm) pjp.getArgs()[1];
                StringJoiner stringJoiner = new StringJoiner(",");
                stringJoiner.add(orgId);
                if (!organizeUpForm.getParentId().equals(info.getParentId()) && !"-1".equals(info.getParentId())) {
                    stringJoiner.add(info.getParentId());
                }
                if (!organizeUpForm.getParentId().equals(info.getParentId()) && !"-1".equals(organizeUpForm.getParentId())) {
                    stringJoiner.add(organizeUpForm.getParentId());
                }
                return PermissionAspectUtil.getPermitByOrgIds(
                        // 操作目标对象ID
                        stringJoiner.toString(),
                        operatorUserId,
                        PermissionConstant.METHOD_UPDATE);
            case PermissionConstant.METHOD_UPDATE_DEPARTMENT:
                // 当前组织id
                String orgIds = (String) pjp.getArgs()[0];
                // 当前组织父级id
                OrganizeEntity infos = organizeService.getInfo(orgIds);
                // 修改后的id
                OrganizeDepartUpForm organizeDepartUpForm = (OrganizeDepartUpForm) pjp.getArgs()[1];
                StringJoiner stringJoiners = new StringJoiner(",");
                stringJoiners.add(orgIds);
                if (!organizeDepartUpForm.getParentId().equals(infos.getParentId())) {
                    stringJoiners.add(infos.getParentId());
                    stringJoiners.add(organizeDepartUpForm.getParentId());
                }
                return PermissionAspectUtil.getPermitByOrgIds(
                        // 操作目标对象ID
                        stringJoiners.toString(),
                        operatorUserId,
                        PermissionConstant.METHOD_UPDATE);
            case PermissionConstant.METHOD_DELETE:
            case PermissionConstant.METHOD_DELETE_DEPARTMENT:
                return PermissionAspectUtil.getPermitByOrgIds(
                        // 操作目标对象ID
                        pjp.getArgs()[0].toString(),
                        operatorUserId,
                        PermissionConstant.METHOD_DELETE);
            default:
                return false;
        }
    }
}
