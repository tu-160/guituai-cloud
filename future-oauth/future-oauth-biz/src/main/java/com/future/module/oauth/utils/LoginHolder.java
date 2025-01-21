package com.future.module.oauth.utils;

import com.future.permission.entity.UserEntity;


/**
 *
 * @author Future Platform Group
 * @copyright 直方信息科技有限公司
 */
public class LoginHolder {


    private static final ThreadLocal<UserEntity> USER_CACHE = new ThreadLocal<>();

    public static UserEntity getUserEntity(){
        return USER_CACHE.get();
    }

    public static void setUserEntity(UserEntity userEntity){
        USER_CACHE.set(userEntity);
    }

    public static void clearUserEntity(){
        USER_CACHE.remove();
    }
}
