/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wison.purchase.packages.config;

import com.wison.purchase.packages.interceptor.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author leyou
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final String[] excludePathPatterns = new String[]{
            "/api/**/flowAttachList",
            "/api/**/oa/response",
            "/api/tbe/allListForContract",
            "/api/tbeSupplier/uploadFile",
            "/v3/api-docs/**",
            "/v2/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/doc.html",
            "/swagger-resources/**"
    };

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册TestInterceptor拦截器
        registry.addInterceptor(requestInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(excludePathPatterns); //添加不拦截的请求路径
        //添加需要拦截的路径
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor();
    }

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**").allowCredentials(true).allowedOriginPatterns("*").allowedHeaders("*").allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS").exposedHeaders("*");
//    }

//    @Bean
//    public CorsFilter corsFilter() {
//        CorsConfiguration config = new CorsConfiguration();
//        config.addAllowedOrigin("*");
//        config.addAllowedMethod("*");
//        config.addAllowedHeader("*");
//        config.addExposedHeader("*");
//        UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
//        configurationSource.registerCorsConfiguration("/**", config);
//        return new CorsFilter(configurationSource);
//    }
}