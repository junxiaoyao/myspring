package myspring.controller;

import myspring.annotations.MyAutowired;
import myspring.annotations.MyController;
import myspring.annotations.MyRequestMapping;
import myspring.connect.ConnectManager;
import myspring.mybatis.daos.NameDao;
import myspring.mybatis.entity.Names;
import myspring.mybatis.proxy.SqlSessionManage;
import myspring.services.HomeService;

import java.sql.Connection;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.Calendar;

/**
 * @Auther: jxy
 * @Date: 2019/3/19 11:12
 * @Description:
 */
@MyController
@MyRequestMapping(url = "/home")
public class HomeController {
    @MyAutowired
    private HomeService homeService;
    @MyRequestMapping(url = "/home")
    public String home() {
        homeService.say();
        System.out.println("跳转至home.jsp");
        return "home";
    }

    @MyRequestMapping(url = "/index")
    public String index() {
        System.out.println("跳转至index.jsp");
        return "index";
    }
}
