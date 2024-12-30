package com.wison.purchase.packages.comm.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MailInfo {

    /**
     * 通知者邮箱
     */
    @NotBlank(message = "通知者邮箱不能为空")
    private String toEmail;

    /**
     * 通知者名字
     */
    @NotBlank(message = "通知者名字不能为空")
    private String toName;

    /**
     * 邮件标题
     */
    private String title;

    /**
     * 邮件内容
     */
    private Map<String, Object> mail;

    /**
     * 邮件模板
     */
    private String template;
}
