package com.future.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.future.reids.config.ConfigValueUtil;
import com.future.reids.util.RedisUtil;

@Component
public class OauthListener implements ApplicationRunner {

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ConfigValueUtil configValueUtil;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if ("false".equals(configValueUtil.getTestVersion())) {
            redisUtil.removeAll();
        }
    }
}
