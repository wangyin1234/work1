package com.wison.purchase.packages.interceptor;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.wison.purchase.packages.comm.domain.HeadInfoHolder;
import com.wison.purchase.packages.comm.domain.UserInfoHolder;
import com.wison.purchase.packages.comm.utils.StringUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestInterceptor implements AsyncHandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(RequestInterceptor.class);

    @Autowired
    private StringRedisTemplate redisTemplate;

    /*
     * 在请求处理之前进行调用(Controller方法调用之前)
     * 若返回true请求将会继续执行后面的操作
     * */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader("Authorization");
        String zoneCode = request.getHeader("zone-code");
        String projectCode = request.getHeader("selected-project-code");
        // 如果不是映射到方法不拦截 直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        //验证token
        if (ObjectUtil.isNull(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        String jsonStr = redisTemplate.opsForValue().get(token);
        if (StringUtils.isEmpty(jsonStr)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        jsonStr = StringEscapeUtils.unescapeJava(jsonStr.substring(1, jsonStr.length() - 1));
        UserInfoHolder.setUserInfo(JSONUtil.parse(jsonStr));
        //验证zoneCode
        if (ObjectUtil.isNull(zoneCode)) {
            throw new RuntimeException("zoneCode校验错误");
        }
        //projectCode
        if (ObjectUtil.isNull(projectCode)) {
            throw new RuntimeException("projectCode校验错误");
        }
        HeadInfoHolder.save(token, zoneCode, projectCode);
        return true;
    }


    /***
     * 整个请求结束之后被调用，也就是在DispatchServlet渲染了对应的视图之后执行（主要用于进行资源清理工作）
     */
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}

