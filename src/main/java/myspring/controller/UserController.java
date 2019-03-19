package myspring.controller;

import myspring.annotations.MyController;
import myspring.annotations.MyRequestMapping;

/**
 * @Auther: jxy
 * @Date: 2019/3/19 15:56
 * @Description:
 */
@MyController
@MyRequestMapping(url = "/user")
public class UserController {
    @MyRequestMapping(url = "/home")
    public String home(){
        return "user";
    }
}
