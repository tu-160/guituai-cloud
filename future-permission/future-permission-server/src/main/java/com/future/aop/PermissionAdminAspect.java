package com.future.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.future.aop.constant.PermissionConstant;
import com.future.aop.util.PermissionAspectUtil;
import com.future.common.util.UserProvider;
import com.future.permission.service.OrganizeRelationService;

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
public class PermissionAdminAspect implements PermissionAdminBase {

    @Autowired
    private UserProvider userProvider;
    @Autowired
    private OrganizeRelationService organizeRelationService;

    /**
     * 分级管理切点
     */
    @Pointcut("@annotation(com.future.common.annotation.OrganizeAdminIsTrator)")
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
    public Boolean detailPermission(ProceedingJoinPoint pjp, String operatorUserId, String methodName){
        switch (methodName) {
            case PermissionConstant.METHOD_SAVE:
            case PermissionConstant.METHOD_SAVE_BATCH:
                return true;
            case PermissionConstant.METHOD_UPDATE:
                //判断是否有当前组织的修改权限
                String organizeId = String.valueOf(pjp.getArgs()[0]);
                return PermissionAspectUtil.containPermission(organizeId, operatorUserId, methodName);
            default:
                return false;
        }
    }

}
