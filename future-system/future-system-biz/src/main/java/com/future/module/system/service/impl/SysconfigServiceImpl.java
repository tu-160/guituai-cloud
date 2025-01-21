package com.future.module.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.future.base.service.SuperServiceImpl;
import com.future.common.model.BaseSystemInfo;
import com.future.common.util.CacheKeyUtil;
import com.future.common.util.JsonUtil;
import com.future.module.system.entity.SysConfigEntity;
import com.future.module.system.mapper.SysconfigMapper;
import com.future.module.system.service.SysconfigService;
import com.future.reids.util.RedisUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 系统配置
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2019年9月27日 上午9:18
 */
@Service
public class SysconfigServiceImpl extends SuperServiceImpl<SysconfigMapper, SysConfigEntity> implements SysconfigService {

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private CacheKeyUtil cacheKeyUtil;

    @Override
    public List<SysConfigEntity> getList(String type) {
        List<SysConfigEntity> list =new ArrayList<>();
        if("WeChat".equals(type)){
            String cacheKey = cacheKeyUtil.getWechatConfig();
            if (redisUtil.exists(cacheKey)) {
                list = JsonUtil.getJsonToList(String.valueOf(redisUtil.getString(cacheKey)), SysConfigEntity.class);
            } else {
                QueryWrapper<SysConfigEntity> queryWrapper = new QueryWrapper<>();
                list = this.list(queryWrapper).stream().filter(t->"QYHConfig".equals(t.getCategory())||"MPConfig".equals(t.getCategory())).collect(Collectors.toList());
                redisUtil.insert(cacheKey, JsonUtil.getObjectToString(list));
            }
        }
        if("SysConfig".equals(type)){
            String cacheKey = cacheKeyUtil.getSystemInfo();
            if (redisUtil.exists(cacheKey)) {
                list = JsonUtil.getJsonToList(String.valueOf(redisUtil.getString(cacheKey)), SysConfigEntity.class);
            } else {
                QueryWrapper<SysConfigEntity> queryWrapper = new QueryWrapper<>();
                list = this.list(queryWrapper).stream().filter(t->!"QYHConfig".equals(t.getCategory())&&!"MPConfig".equals(t.getCategory())).collect(Collectors.toList());
                redisUtil.insert(cacheKey, JsonUtil.getObjectToString(list));
            }
        }
        return list;
    }

    @Override
    public BaseSystemInfo getWeChatInfo() {
        Map<String, String> objModel = new HashMap<>();
        List<SysConfigEntity> list = this.getList("WeChat");
        for (SysConfigEntity entity : list) {
            objModel.put(entity.getFkey(), entity.getValue());
        }
        BaseSystemInfo baseSystemInfo = JsonUtil.getJsonToBean(objModel, BaseSystemInfo.class);
        return baseSystemInfo;
    }

    @Override
    public String getValueByKey(String keyStr) {
        QueryWrapper<SysConfigEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysConfigEntity::getFkey,keyStr);
        SysConfigEntity sysConfigEntity = getOne(queryWrapper);
        return sysConfigEntity.getValue();
    }

    @Override
    public BaseSystemInfo getSysInfo() {
        Map<String, String> objModel = new HashMap<>();
        List<SysConfigEntity> list = this.getList("SysConfig");
        for (SysConfigEntity entity : list) {
            objModel.put(entity.getFkey(), entity.getValue());
        }
        BaseSystemInfo baseSystemInfo = JsonUtil.getJsonToBean(objModel, BaseSystemInfo.class);
        return baseSystemInfo;
    }

    @Override
    @Transactional
    public void save(List<SysConfigEntity> entitys) {
        String cacheKey = cacheKeyUtil.getSystemInfo();
        redisUtil.remove(cacheKey);
        this.baseMapper.deleteFig();
        for (SysConfigEntity entity: entitys) {
            entity.setCategory("SysConfig");
            this.baseMapper.insert(entity);
        }
    }
    @Override
    @Transactional
    public boolean saveMp(List<SysConfigEntity> entitys){
        String cacheKey = cacheKeyUtil.getWechatConfig();
        int flag=0;
        redisUtil.remove(cacheKey);
        this.baseMapper.deleteMpFig();
        for (SysConfigEntity entity: entitys) {
            entity.setCategory("MPConfig");
           if(this.baseMapper.insert(entity)>0){
               flag++;
           }
        }
        if(entitys.size()==flag){
            return true;
        }
        return false;
    }
    @Override
    @Transactional
    public void saveQyh(List<SysConfigEntity> entitys){
        String cacheKey = cacheKeyUtil.getWechatConfig();
        redisUtil.remove(cacheKey);
        this.baseMapper.deleteQyhFig();
        for (SysConfigEntity entity: entitys) {
            entity.setCategory("QYHConfig");
            this.baseMapper.insert(entity);
        }
    }


}
