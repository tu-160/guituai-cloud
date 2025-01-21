package com.future.common.database.util;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 日志分类
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021年3月13日 上午9:18
 */
@Data
public class LogWriteUtil {

    public static final String NOTWRITE = "/Log/writeLogRequest";

    public static final String NOTWRITETWO = "/SysConfig/getInfo";

    public static final String WRITELOG = "/Logout";

}
