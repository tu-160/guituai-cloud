package com.future.module.system.service;

import java.util.List;

import com.future.base.service.SuperService;
import com.future.common.base.ActionResult;
import com.future.common.base.Pagination;
import com.future.common.exception.DataException;
import com.future.module.system.entity.BillRuleEntity;
import com.future.module.system.model.billrule.BillRulePagination;

/**
 * 单据规则
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2019年9月27日 上午9:18
 */
public interface BillRuleService extends SuperService<BillRuleEntity> {

    /**
     * 列表
     *
     * @param pagination 条件
     * @return
     */
    List<BillRuleEntity> getList(BillRulePagination pagination);

    /**
     * 列表
     *
     * @return
     */
    List<BillRuleEntity> getList();

    /**
     * 信息
     *
     * @param id 主键值
     * @return
     */
    BillRuleEntity getInfo(String id);

    /**
     * 验证名称
     *
     * @param fullName 名称
     * @param id       主键值
     * @return
     */
    boolean isExistByFullName(String fullName, String id);

    /**
     * 验证编码
     *
     * @param enCode 编码
     * @param id     主键值
     * @return
     */
    boolean isExistByEnCode(String enCode, String id);

    /**
     * 获取流水号
     *
     * @param enCode 流水编码
     * @return
     */
    String getNumber(String enCode) throws DataException;

    /**
     * 创建
     *
     * @param entity 实体对象
     */
    void create(BillRuleEntity entity);

    /**
     * 更新
     * @param id 主键值
     * @param entity 实体对象
     * @return
     */
    boolean update(String id, BillRuleEntity entity);

    /**
     * 删除
     *
     * @param entity 实体对象
     */
    void delete(BillRuleEntity entity);

    /**
     * 上移
     * @param id 主键值
     * @return
     */
    boolean first(String id);

    /**
     * 下移
     * @param id
     * @return
     */
    boolean next(String id);

    /**
     * 获取单据流水号
     * @param enCode 流水编码
     * @param isCache 是否缓存：每个用户会自动占用一个流水号，这个刷新页面也不会跳号
     * @return
     * @throws DataException
     */
    String getBillNumber(String enCode, boolean isCache) throws DataException;

    /**
     * 使用单据流水号（注意：必须是缓存的单据才可以调用这个方法，否则无效）
     *
     * @param enCode 流水编码
     */
    void useBillNumber(String enCode);

    /**
     * 单据规则导入
     *
     * @param entity      实体对象
     * @return
     * @throws DataException
     */
    ActionResult ImportData(BillRuleEntity entity, Integer type) throws DataException;


    /**
     *
     *
     * @param pagination 根据业务条件
     * @return  单据规则列表
     */
    List<BillRuleEntity> getListByCategory(String id,Pagination pagination);

}
