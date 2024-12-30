package com.wison.purchase.packages.comm.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 消息的dto
 *
 * @author zendwang
 */
@Data
public class WebSocketMessageDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 需要推送到的session key 列表
     */
    private List<String> sessionKeys;

    /**
     * 需要发送的消息
     */
    private String message;
}
