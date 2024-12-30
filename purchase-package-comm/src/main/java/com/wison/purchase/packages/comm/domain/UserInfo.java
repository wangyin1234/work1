package com.wison.purchase.packages.comm.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String deptName;

    private int companyId;

    private String phone;

    private String workCode;

    private String companyName;

    private String loginName;

    private int deptId;

    private String userName;

    private int userId;

    private String email;

    private String roleCodes;
}
