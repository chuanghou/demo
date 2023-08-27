package com.stellariver.milky.demo;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AntDesignController implements ErrorController {

    public String getErrorPath(){
        return "/error";
    }

    @RequestMapping(value = "/error")
    public String getIndex(){
        return "/index.html"; //返回index页面
    }

}