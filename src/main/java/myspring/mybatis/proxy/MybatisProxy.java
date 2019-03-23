package myspring.mybatis.proxy;
import myspring.connect.ConnectManager;
import myspring.mybatis.annotations.MyDelete;
import myspring.mybatis.annotations.MyInsertInto;
import myspring.mybatis.annotations.MySelect;
import myspring.mybatis.annotations.MyUpdate;
import myspring.mybatis.util.SqlUtil;
import myspring.util.AnnotationUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: jxy
 * @Date: 2019/3/21 21:05
 * @Description:
 */
public class MybatisProxy implements InvocationHandler {
    private Object object;

    public MybatisProxy(Object object) {
        this.object = object;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (AnnotationUtil.testMethodHasAnnotion(method, MyInsertInto.class)) {
            return insert(method, args);
        }
        if (AnnotationUtil.testMethodHasAnnotion(method, MyUpdate.class)) {
            return update(method, args);
        }
        if (AnnotationUtil.testMethodHasAnnotion(method, MySelect.class)) {
            return select(method, args);
        }
        if (AnnotationUtil.testMethodHasAnnotion(method, MyDelete.class)) {
            delete(method, args);
        }
        return null;
    }

    /**
     * method.getParameters();获取参数
     * args 参数值
     */
    //新增操作
    private Object insert(Method method, Object[] args) throws Exception {
        MyInsertInto myInsertInto = AnnotationUtil.getMethodAnnotion(method, MyInsertInto.class);
        return insertAndUpdate(myInsertInto.value(), method, args);
    }

    //更新操作
    private Object update(Method method, Object[] args) throws Exception {
        MyUpdate myUpdate = AnnotationUtil.getMethodAnnotion(method, MyUpdate.class);
        return insertAndUpdate(myUpdate.value(), method, args);
    }

    //更新查询操作
    private Object insertAndUpdate(String sql, Method method, Object[] args) throws Exception {
        Map<String, Integer> mapSql = new HashMap<>();
        sql = sqlModify(sql, mapSql);
        Connection connection = getConnection();
        SqlUtil.printOutSqlAndParams(sql, SqlUtil.paramzz(method, args, mapSql));
        Object o = SqlUtil.insertAndUpateUtil(connection, sql, SqlUtil.paramzz(method, args, mapSql));
        releaseConnection(connection);
        return o;
    }

    //查询操作
    private Object select(Method method, Object[] args) throws Exception {
        MySelect mySelect = AnnotationUtil.getMethodAnnotion(method, MySelect.class);
        Map<String, Integer> mapSql = new HashMap<>();
        String sql = sqlModify(mySelect.value(), mapSql);
        Connection connection = getConnection();
        Map<Object, Integer> paramzz = SqlUtil.paramzz(method, args, mapSql);
        SqlUtil.printOutSqlAndParams(sql, paramzz);
        ResultSet rs = SqlUtil.getObjectUtil(connection, sql, paramzz);
        releaseConnection(connection);
        return SqlUtil.loadData(rs, method);
    }

    //删除操作
    private void delete(Method method, Object[] args) throws Exception {
        MyDelete myDelete = AnnotationUtil.getMethodAnnotion(method, MyDelete.class);
        Map<String, Integer> mapSql = new HashMap<>();
        String sql = sqlModify(myDelete.value(), mapSql);
        Connection connection = getConnection();
        Map<Object, Integer> paramzz = SqlUtil.paramzz(method, args, mapSql);
        SqlUtil.deleteObjectUtil(connection, sql, paramzz);
        SqlUtil.printOutSqlAndParams(sql, paramzz);
    }

    //调用处理sql装载参数顺序
    private String sqlModify(String sql, Map<String, Integer> mapSql) {
        return SqlUtil.getParameter(sql, mapSql);
    }

    //得到连接
    private Connection getConnection() {
        return ConnectManager.getConnection();
    }

    //释放连接
    private void releaseConnection(Connection connection) {
        ConnectManager.releaseConnection(connection);
    }
}
