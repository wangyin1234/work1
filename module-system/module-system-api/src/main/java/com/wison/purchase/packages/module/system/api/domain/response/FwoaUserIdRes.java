package com.wison.purchase.packages.module.system.api.domain.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FwoaUserIdRes implements Serializable {

    private static final long serialVersionUID = 1L;

    private String loginName;

    private String userId;

    private String userName;

    private boolean isError;
}
