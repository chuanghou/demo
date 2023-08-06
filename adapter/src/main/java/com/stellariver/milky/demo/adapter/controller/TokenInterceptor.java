package com.stellariver.milky.demo.adapter.controller;

import com.stellariver.milky.common.base.ExceptionType;
import com.stellariver.milky.common.base.Result;
import com.stellariver.milky.common.tool.util.Json;
import com.stellariver.milky.demo.basic.ErrorEnums;
import com.stellariver.milky.demo.basic.TokenUtils;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {

        if(request.getMethod().equals("OPTIONS")){
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }

        response.setCharacterEncoding("utf-8");
        String token = request.getHeader("token");
        if (token != null && TokenUtils.verify(token)){
            return true;
        }
        response.setContentType("application/json; charset=utf-8");
        Result<Object> result = Result.error(ErrorEnums.PARAM_FORMAT_WRONG.message("账户密码错误!"), ExceptionType.BIZ);
        response.getWriter().append(Json.toJson(result));
        return false;
    }

}