package com.future.module.system.model.datainterface;

import lombok.Data;

import java.io.Serializable;

/**
 * 数据接口调用返回模型
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-12-24
 */
@Data
public class DataInterfaceActionVo implements Serializable {

    private Object data;

}
