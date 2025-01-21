package com.future.module.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.future.base.service.SuperService;
import com.future.common.base.Pagination;
import com.future.module.system.entity.MessageTemplateEntity;

import java.util.List;

/**
 * 消息模板表
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021年12月8日17:40:37
 */
public interface MessageTemplateService extends SuperService<MessageTemplateEntity> {

    /**
     * 列表（无分页）
     *
     * @return
     */
    List<MessageTemplateEntity> getList();

    /**
     * 列表
     *
     * @param pagination 条件
     * @return 单据规则列表
     */
    List<MessageTemplateEntity> getList(Pagination pagination, Boolean filter);

    /**
     * 信息
     *
     * @param id 主键值
     * @return 单据规则
     */
    MessageTemplateEntity getInfo(String id);

    /**
     * 创建
     *
     * @param entity 实体
     */
    void create(MessageTemplateEntity entity);

    /**
     * 更新
     *
     * @param id     主键值
     * @param entity 实体对象
     * @return ignore
     */
    boolean update(String id, MessageTemplateEntity entity);

    /**
     * 删除
     *
     * @param entity 实体
     */
    void delete(MessageTemplateEntity entity);

    /**
     * 验证名称
     *
     * @param fullName 名称
     * @param id       主键值
     * @return ignore
     */
    boolean isExistByFullName(String fullName, String id);

    /**
     * 验证编码
     *
     * @param enCode 编码
     * @param id       主键值
     * @return ignore
     */
    boolean isExistByEnCode(String enCode, String id);
}
