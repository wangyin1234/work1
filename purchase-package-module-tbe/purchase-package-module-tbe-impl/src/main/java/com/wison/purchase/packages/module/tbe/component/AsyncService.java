package com.wison.purchase.packages.module.tbe.component;


import com.wison.base.core.exception.BusinessException;
import com.wison.purchase.packages.comm.utils.HttpUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Service接口
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AsyncService {

    @Value("${supplier.url.reply}")
    private String replyUrl;

    @Async("taskExecutor")
    public void pushTechnicalBid(String url, Object data) {
        push(url, data);
    }

    @Async("taskExecutor")
    public void replyToSupplier(Object data) {
        push(replyUrl, data);
    }

    private void push(String url, Object data) {
        log.info("通知供应商");
        Object result = HttpUtils.postJson(url, data);
        if (null == result) {
            throw new BusinessException("发送失败");
        }
        log.info("通知成功");
    }
}
