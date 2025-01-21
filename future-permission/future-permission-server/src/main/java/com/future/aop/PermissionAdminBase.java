package com.future.aop;

import org.aspectj.lang.ProceedingJoinPoint;

import com.future.common.base.ActionResult;
import com.future.common.base.UserInfo;
import com.future.common.constant.MsgCode;
import com.future.common.util.UserProvider;

public interface PermissionAdminBase{

    /**
     * 详细的权限判断
     * @param pjp AOP切点参数
     * @param operatorUserId 操作者对象
     */
    Boolean detailPermission(ProceedingJoinPoint pjp, String operatorUserId, String methodName);

    /**
     * 管理者权限判断
     *
     * @param userProvider 操作者对象
     */
    static Object permissionCommon(ProceedingJoinPoint pjp, UserProvider userProvider, PermissionAdminBase permissionAdminBase) throws Throwable {
        // 获取用户信息
        UserInfo operatorUser = userProvider.get();
        // 是否是管理员
        if(operatorUser.getIsAdministrator()){
            return pjp.proceed();
        }else {
            // 获取方法名
            String methodName = pjp.getSignature().getName();
            // 具体方法权限
            if(permissionAdminBase.detailPermission(pjp, operatorUser.getUserId(),methodName)){
                return pjp.proceed();
            }
        }
        return ActionResult.fail(MsgCode.FA021.get());
    }

}
