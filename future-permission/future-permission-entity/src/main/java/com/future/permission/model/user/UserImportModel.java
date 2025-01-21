package com.future.permission.model.user;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 导入模型
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-12-20
 */
@Data
public class UserImportModel implements Serializable {
    private String account;

    private String realName;

    private String organizeId;

    private String managerId;

    private String positionId;

    private String roleId;

    private String description;

    private String gender;

    private String nation;

    private String nativePlace;

    private String certificatesType;

    private String certificatesNumber;

    private String education;

    private Date birthday;

    private String telePhone;

    private String landline;

    private String mobilePhone;

    private String email;

    private String urgentContacts;

    private String urgentTelePhone;

    private String postalAddress;

    private Long sortCode;

    private Date entryDate;

    private Integer enabledMark;
    private String ranks;
}
