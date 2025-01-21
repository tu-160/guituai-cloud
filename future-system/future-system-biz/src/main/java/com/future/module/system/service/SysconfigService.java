package com.future.module.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.future.base.service.SuperService;
import com.future.common.model.BaseSystemInfo;
import com.future.module.system.entity.SysConfigEntity;

import java.util.List;

/**
 * 系统配置
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2019年9月27日 上午9:18
 */
public interface SysconfigService extends SuperService<SysConfigEntity> {

    /**
     * 列表
     *
     * @param type
     * @return
     */
    List<SysConfigEntity> getList(String type);

    /**
     * 信息
     *
     * @return
     */
    BaseSystemInfo getWeChatInfo();

    /**
     * 根据key获取value
     * @param keyStr
     * @return
     */
    String getValueByKey(String keyStr);
    /**
     * 获取系统配置
     * @return
     */
    BaseSystemInfo getSysInfo();
    /**
     * 保存系统配置
     *
     * @param entitys 实体对象
     * @return
     */
    void save(List<SysConfigEntity> entitys);
    /**
     * 保存公众号配置
     *
     * @param entitys 实体对象
     * @return
     */
    boolean saveMp(List<SysConfigEntity> entitys);
    /**
     * 保存企业号配置
     *
     * @param entitys 实体对象
     */
    void saveQyh(List<SysConfigEntity> entitys);
}
