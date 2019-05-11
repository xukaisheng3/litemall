package org.linlinjava.litemall.admin.annotation.support;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author: xukaisheng
 * @date: 2019/4/28
 * @time: 17:30
 * @description:
 */
@Configuration
public class WebTokenConfig extends WebMvcConfigurerAdapter {
    @Bean
    public DoorstoreInterceptor getLoginInterceptor() {
        return new DoorstoreInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration addInterceptor = registry.addInterceptor(getLoginInterceptor());
        // 排除配置
        addInterceptor.excludePathPatterns("/error");
        addInterceptor.excludePathPatterns("/login**");
        // 拦截配置
        addInterceptor.addPathPatterns("/**");
    }
}

