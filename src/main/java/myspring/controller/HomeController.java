package myspring.controller;

import myspring.annotations.MyAutowired;
import myspring.annotations.MyController;
import myspring.annotations.MyRequestMapping;
import myspring.connect.ConnectManager;
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
    @MyAutowired
    private ConnectManager connectManager;

    @MyRequestMapping(url = "/home")
    public String home() {
        String sql = "INSERT INTO connect (connect,time)VALUES (?,?);";
        Connection connection = connectManager.getConnection();
        try {
            System.out.println(" 尝试操作数据");
            PreparedStatement pstmt = (PreparedStatement) connection.prepareStatement(sql);
            pstmt.setString(1, connection.toString());
            java.util.Date date = Calendar.getInstance().getTime();
            pstmt.setDate(2, new Date(date.getYear(), date.getMonth(), date.getDay()));
            pstmt.executeUpdate();
            pstmt.close();
            connectManager.releaseConnection(connection);
        } catch (Exception e) {
            System.out.println(" something wrong ");
        } finally {
            System.out.println(" something wrong ");
            connectManager.releaseConnection(connection);
        }

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
