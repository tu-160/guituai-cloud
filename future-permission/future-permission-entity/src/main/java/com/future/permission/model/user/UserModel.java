package com.future.permission.model.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

import com.future.common.base.Pagination;

/**
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date ：2022/5/6 15:00
 */
@Data
public class UserModel implements Serializable {
    private Pagination pagination;
    private List<String> id;
}
