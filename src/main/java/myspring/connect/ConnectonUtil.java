/*
package myspring.connect;

import myspring.annotations.MyService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

*/
/**
 * @Auther: jxy
 * @Date: 2019/3/20 20:28
 * @Description:
 *//*

@MyService
public class ConnectonUtil {
    //驱动名称
    private String driverClassName = "com.mysql.jdbc.Driver";
    private String url = "jdbc:mysql://localhost:3306/myspring?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=UTC";
    private String username = "root";
    private String password = "123456";
    //初始值
    private int initialSize = 5;
    //最大空闲数
    private int maxFree = 10;
    //最大活动连接数
    private int maxActive = 20;
    //最大连接时间
    private int maxWait = 1000 * 60 * 10;
    //重复连接时间
    private int timeBetweenEvictionRunsMillis = 1000 * 3;

    //创建连接
    public Connection createConnection() {
        Connection connection = null;
        try {
            Class.forName(driverClassName);
            connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            System.out.println("driver loading fail！");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("connect database fail！");
            System.exit(0);

        }
        return connection;
    }

    public int getInitialSize() {
        return initialSize;
    }

    public int getMaxFree() {
        return maxFree;
    }

    public int getMaxActive() {
        return maxActive;
    }

    public int getMaxWait() {
        return maxWait;
    }

    public int getTimeBetweenEvictionRunsMillis() {
        return timeBetweenEvictionRunsMillis;
    }

    public static void main(String[] args)throws Exception {
        ConnectonUtil connectonUtil=new ConnectonUtil();
        Connection connection=connectonUtil.createConnection();
        System.out.println(connection.isClosed());
    }
}
*/
