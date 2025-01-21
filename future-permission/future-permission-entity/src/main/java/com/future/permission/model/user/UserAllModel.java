package com.future.permission.model.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

import com.future.common.util.treeutil.SumTree;

@Data
public class UserAllModel extends SumTree implements Serializable {
    private String id;
    private String account;
    private String gender;
    private String realName;
    private String headIcon;
    private String department;
    private String departmentId;
    private String organizeId;
    private String organize;
    private String roleId;
    private String roleName;
    private String positionId;
    private String positionName;
    private String managerId;
    private String managerName;
    private String quickQuery;
    private String portalId;
    private Integer isAdministrator;
}
