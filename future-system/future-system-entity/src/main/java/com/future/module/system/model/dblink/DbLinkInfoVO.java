package com.future.module.system.model.dblink;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Map;

import com.future.common.exception.DataException;
import com.future.common.util.JsonUtil;
import com.future.common.util.JsonUtilEx;
import com.future.common.util.StringUtil;
import com.future.common.util.XSSEscape;
import com.future.database.model.entity.DbLinkEntity;
import com.future.database.source.impl.DbOracle;

/**
 * 页面显示对象
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021/3/12 15:31
 */
@Data
public class DbLinkInfoVO extends DbLinkBaseForm {

    /**
     * 获取连接页面显示对象
     * @param entity 连接实体对象
     * @return 返回显示对象
     * @throws DataException ignore
     */
    public DbLinkInfoVO getDbLinkInfoVO(DbLinkEntity entity) throws DataException {
        DbLinkInfoVO vo = JsonUtilEx.getJsonToBeanEx(entity, DbLinkInfoVO.class);
        vo.setServiceName(XSSEscape.escape(entity.getDbName()));
        vo.setTableSpace(XSSEscape.escape(entity.getDbTableSpace()));
        vo.setOracleExtend(entity.getOracleExtend() != null && entity.getOracleExtend() == 1);
        if(StringUtil.isNotEmpty(entity.getOracleParam())){
            Map<String, Object> oracleParam = JsonUtil.stringToMap(entity.getOracleParam());
            if(oracleParam.size() > 0){
                vo.setOracleLinkType(oracleParam.get(DbOracle.ORACLE_LINK_TYPE).toString());
                vo.setOracleRole(oracleParam.get(DbOracle.ORACLE_ROLE).toString());
                vo.setOracleService(oracleParam.get(DbOracle.ORACLE_SERVICE).toString());
                vo.setOracleExtend(true);
            }
        }
        return vo;
    }

    @Schema(description ="主键")
    private String id;

}
