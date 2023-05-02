package edu.nuist.codehelper.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
    @RequestMapping("/index")
    public String page(){
        return "pages/index";
    }
    @RequestMapping("/code")
    public String code(){
        return "pages/code";
    }
    @RequestMapping("/help")
    public String help(){
        return "pages/help";
    }
    @RequestMapping("/advice")
    public String advice(){
        return "pages/advice";
    }
    @RequestMapping("/show")
    public String show(){
        return "pages/show";
    }
    @RequestMapping("/forgetpwd")
    public String forgetpwd(){
        return "pages/forgetpwd";
    }
    @RequestMapping("/regist")
    public String regist(){
        return "pages/regist";
    }
}