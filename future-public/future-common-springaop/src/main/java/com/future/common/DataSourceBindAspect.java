package com.future.common;

/**
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-03-26
 */

import cn.dev33.satoken.context.SaHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.future.common.base.UserInfo;
import com.future.common.util.StringUtil;
import com.future.common.util.TenantHolder;
import com.future.common.util.UserProvider;
import com.future.database.util.NotTenantPluginHolder;
import com.future.database.util.TenantDataSourceUtil;
import com.future.reids.config.ConfigValueUtil;

/**
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021/3/15 17:12
 */
@Slf4j
@Aspect
@Component
@Order(1)
public class DataSourceBindAspect {
    @Autowired
    private ConfigValueUtil configValueUtil;

    @Pointcut("!execution(* com.future.*.LoginController.login(..)) && (execution(* com.xxl.job.admin.controller.*.*(..)) || execution(* com.future.controller.*.*(..)) || execution(* com.future.*.controller.*.*(..)))")
    public void bindDataSource() {

    }

    /**
     * NoDataSourceBind 不需要绑定数据库的注解
     *
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around("bindDataSource() && !@annotation(com.future.common.util.NoDataSourceBind)")
    public Object doAroundService(ProceedingJoinPoint pjp) throws Throwable {
        if (configValueUtil.isMultiTenancy()) {
            if(StringUtil.isEmpty(TenantHolder.getDatasourceId())){
                UserInfo userInfo = UserProvider.getUser();
                String url = null;
                try{
                    url = SaHolder.getRequest().getRequestPath();
                }catch (Exception ee){ }
                log.error("租户" + userInfo.getTenantId() + "数据库不存在, URL: {}, TOKEN: {}", url, userInfo.getToken());
                return null;
            }
            return pjp.proceed();
        }
        Object obj = pjp.proceed();
        return obj;
    }


    /**
     * NoDataSourceBind 不需要绑定数据库的注解 加入不切租户库标记
     *
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around("bindDataSource() && @annotation(com.future.common.util.NoDataSourceBind)")
    public Object doAroundService2(ProceedingJoinPoint pjp) throws Throwable {
        try{
            NotTenantPluginHolder.setNotSwitchAlwaysFlag();
            //Filter中提前设置租户信息, 不需要切库的方法进行清除切库
            TenantDataSourceUtil.clearLocalTenantInfo();
            return pjp.proceed();
        }finally {
            NotTenantPluginHolder.clearNotSwitchAlwaysFlag();
        }
    }
}

