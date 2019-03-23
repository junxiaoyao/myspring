package myspring.connect;

import myspring.annotations.MyService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Auther: jxy
 * @Date: 2019/3/20 20:27
 * @Description:
 */
@MyService
public class ConnectionPool {
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


    //空闲连接
    private BlockingQueue<Connection> freeConnections = new LinkedBlockingDeque<>();
    //运行连接
    private BlockingQueue<Connection> activeConnections = new LinkedBlockingDeque<>();
    //计数器
    private AtomicInteger countConnections = new AtomicInteger();

    public ConnectionPool() {
        //  System.out.println(" ConnectionPool ");
        this.initMain();
    }

    private void initMain() {
        //  System.out.println(" initMain ");
        freeInit();
    }

    private void freeInit() {
        for (int i = 0; i < initialSize; i++) {
            Connection connection = this.createConnection();
            freeConnections.offer(connection);
        }
    }

    //创建连接
    private Connection createConnection() {
        //  System.out.println("创建连接");
        Connection connection = createConnectionUtil();
        //失败则递归
        if (isAlife(connection)) {
            //  System.out.println("创建连接成功");
            return connection;
        } else {
            // System.out.println("创建连接失败");
            return this.createConnection();
        }
    }

    //释放连接
    public synchronized void releaseConnection(Connection connection) {
        try {
            if (freeConnections.size() < maxFree) {
                if (isAlife(connection)) {
                    freeConnections.add(connection);
                }
            } else {
                connection.close();
            }
            activeConnections.remove(connection);
            countConnections.decrementAndGet();
            notifyAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //获取连接
    public synchronized Connection getConnection() {
        try {
            Connection connection = null;
            if (countConnections.get() < maxActive) {
                if (freeConnections.size() > 0) {
                    connection = freeConnections.poll();
                } else {
                    connection = createConnection();
                }
                if (isAlife(connection)) {
                    activeConnections.offer(connection);
                    countConnections.incrementAndGet();
                } else {
                    connection = getConnection();
                }
            } else {
                wait(timeBetweenEvictionRunsMillis);
                connection = getConnection();
            }
            return connection;
        } catch (Exception e) {
            return null;
        }

    }

    //判断连接是否可用
    private boolean isAlife(Connection connection) {
        try {
            if (connection == null || connection.isClosed()) {
                return false;
            }
        } catch (SQLException s) {
            s.printStackTrace();
        }
        return true;
    }

    //创建连接
    public Connection createConnectionUtil() {
        Connection connection = null;
        try {
            Class d = Class.forName(driverClassName);
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
}
