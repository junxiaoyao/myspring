package myspring.controller;

import myspring.annotations.MyController;
import myspring.annotations.MyRequestMapping;

/**
 * @Auther: jxy
 * @Date: 2019/3/19 11:12
 * @Description:
 */
@MyController
@MyRequestMapping(url = "/home")
public class HomeController {
    @MyRequestMapping(url = "/index")
    public String home() {
        System.out.println("跳转至首页");
        return "home";
    }
}
