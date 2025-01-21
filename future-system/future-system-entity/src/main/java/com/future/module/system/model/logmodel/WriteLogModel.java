package com.future.module.system.model.logmodel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import com.future.common.base.UserInfo;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WriteLogModel implements Serializable {
    private String userId;
    private String userName;
    private String abstracts;
    private UserInfo userInfo;
    private int loginMark;
    private Integer loginType;
    private long requestDuration;


}
