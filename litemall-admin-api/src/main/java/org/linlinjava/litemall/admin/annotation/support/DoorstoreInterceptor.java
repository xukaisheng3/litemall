package org.linlinjava.litemall.admin.annotation.support;


import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.linlinjava.litemall.admin.annotation.Doorstore;
import org.linlinjava.litemall.db.domain.LitemallAdmin;
import org.linlinjava.litemall.db.domain.LitemallDoorstore;
import org.linlinjava.litemall.db.service.LitemallDoorstoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * @author: xukaisheng
 * @date: 2019/4/28
 * @time: 10:32
 * @description:登录拦截器，默认拦截所有的，通过继承HandlerInterceptorAdapter重写preHandle
 * 实现方法级别的拦截，拦截@login注解的controller方法，当前只实现了json数据格式的传输解析，暂未实现
 * form-data的解析，但实现类似，注意读取输入流的时候要实现复制输入流的读取，切勿直接读取，因为流是不可重复
 * 读的，在拦截器读取了，在controller层在映射到实体类上就是空的，
 *
 */
@Component
public class DoorstoreInterceptor extends HandlerInterceptorAdapter {
    public static final String DOORSTORE_OBJECT = "doorstore_object";

    @Autowired
    private LitemallDoorstoreService doorstoreService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Doorstore annotation;
        if(handler instanceof HandlerMethod) {
            annotation = ((HandlerMethod) handler).getMethodAnnotation(Doorstore.class);
        }else{
            return true;
        }
        if(annotation == null){
            return true;
        }
        Subject currentUser = SecurityUtils.getSubject();
        LitemallAdmin admin = (LitemallAdmin) currentUser.getPrincipal();
        //管理员权限可以看所有的
        Integer[] doorstoreIds=null;
        if(admin.getRoleIds()!=null && Arrays.asList(admin.getRoleIds()).contains(1)){
            List<LitemallDoorstore> roleList = doorstoreService.queryAll();
            doorstoreIds=(Integer[])roleList.stream().mapToLong(LitemallDoorstore::getId).boxed().mapToInt(a -> Integer.parseInt(a.toString())).boxed().toArray(Integer[]::new);
        }else {
            doorstoreIds = admin.getDoorstoreIds();
        }

        //设置userId到request里，后续根据userId，获取用户信息
        request.setAttribute(DOORSTORE_OBJECT, doorstoreIds);
        return true;
    }
}
