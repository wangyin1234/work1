package com.wison.purchase.packages.module.tbe.domain.request;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class SupplierOaInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String SupplierCode;
    private String SupplierName;
    private String contacts;
    private String email;
}
