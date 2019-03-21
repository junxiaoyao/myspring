package myspring.connect;

import myspring.annotations.MyAutowired;
import myspring.annotations.MyService;

import java.sql.Connection;

/**
 * @Auther: jxy
 * @Date: 2019/3/21 15:59
 * @Description:
 */
@MyService
public class ConnectManager {
    @MyAutowired
    private ConnectionPool pool;

    public void releaseConnection(Connection connection) {
        pool.releaseConnection(connection);
    }

    public Connection getConnection() {
        return pool.getConnection();
    }
}
