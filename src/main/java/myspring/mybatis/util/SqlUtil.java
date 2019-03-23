package myspring.mybatis.util;

import myspring.mybatis.annotations.MyParam;

import java.lang.reflect.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Auther: jxy
 * @Date: 2019/3/22 10:39
 * @Description:
 */
public class SqlUtil {

    //返回sql，并将参数位置装到map
    public static String getParameter(String sql, Map<String, Integer> map) {
        int count = 1;
        String sqlC = sql;
        int firstC = sql.indexOf("#{");
        int lastC = sql.indexOf("}");
        while (firstC > 0 && lastC > 0) {
            String parameter = sql.substring(firstC + 2, lastC);
            map.put(parameter, count++);
            sqlC = sqlModify(sqlC, parameter);
            sql = sql.substring(lastC + 1);
            firstC = sql.indexOf("#{");
            lastC = sql.indexOf("}");
        }
        return sqlC;
    }

    //替换参数为问号
    public static String sqlModify(String sql, String parameter) {
        String REGEX = "#\\{" + parameter + "}";
        final String REPLACE = "?";
        Pattern p = Pattern.compile(REGEX);
        Matcher m = p.matcher(sql);
        sql = m.replaceAll(REPLACE);
        return sql;
    }

    //参数序号工具
    public static Map<Object, Integer> paramzz(Method method, Object[] args, Map<String, Integer> map) {
        Map<Object, Integer> parms = new HashMap<>();
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < map.size(); i++) {
            String key = parameters[i].getAnnotation(MyParam.class).value();
            int value = map.get(key);
            parms.put(args[i], value);
        }
        return parms;
    }

    //执行新增操作
    public static int insertAndUpateUtil(Connection connection, String sql, Map<Object, Integer> parms) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement(sql);
        for (Map.Entry<Object, Integer> entry : parms.entrySet()) {
            pstmt.setObject(entry.getValue(), entry.getKey());
        }
        int rows = pstmt.executeUpdate();
        pstmt.close();
        return rows;
    }

    //执行查询操作
    public static ResultSet getObjectUtil(Connection connection, String sql, Map<Object, Integer> parms) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement(sql);
        for (Map.Entry<Object, Integer> entry : parms.entrySet()) {
            pstmt.setObject(entry.getValue(), entry.getKey());
        }
        ResultSet rs = pstmt.executeQuery();
        return rs;
    }

    //执行删除操作
    public static void deleteObjectUtil(Connection connection, String sql, Map<Object, Integer> parms) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement(sql);
        for (Map.Entry<Object, Integer> entry : parms.entrySet()) {
            pstmt.setObject(entry.getValue(), entry.getKey());
        }
        pstmt.execute();
        //ResultSet rs = pstmt.executeQuery();

    }

    //获取结果集长度
    public static int getResultLength(ResultSet rs) throws SQLException {
        int count = 0;
        while (rs.next()) {
            count++;
        }
        return count;
    }

    //装载查询数据
    public static Object loadData(ResultSet rs, Method method) throws Exception {
        Class returnType = method.getReturnType();
        //返回类型为list
        if (returnType == List.class || returnType == ArrayList.class) {
            return resultToList(rs, method);
        } else {
            return resultToObject(rs, returnType);
        }
    }

    //给属性赋值
    public static void fieldsSetValue(Object o, ResultSet rs) throws Exception {
        Field[] fields = o.getClass().getDeclaredFields();
        for (Field field : fields) {
            Object value = rs.getObject(field.getName());
            field.setAccessible(true);
            field.set(o, value);
        }
    }

    //将结果装载为对象
    public static Object resultToObject(ResultSet rs, Class returnType) throws Exception {
        int count = 0;
        Object o = null;
        while (rs.next()) {
            if (count < 1) {
                o = returnType.newInstance();
                fieldsSetValue(o, rs);
            }
            count++;
        }
        if (count > 1) {
            throw new SQLException("Expected 1 found " + count);
        }
        return o;
    }

    //将结果装载为List
    public static List resultToList(ResultSet rs, Method method) throws Exception {
        List list = null;
        int count = 0;
        while (rs.next()) {
            if (count < 1) {
                list = new ArrayList();
            }
            Object o = getTypeClass(method.getGenericReturnType());
            fieldsSetValue(o, rs);
            list.add(o);
            count++;
        }
        return list;
    }

    //返回泛型实例化对象
    public static Object getTypeClass(Type returnType) throws Exception {
        if (returnType instanceof ParameterizedType) {
            Type[] typeArguments = ((ParameterizedType) returnType).getActualTypeArguments();
            if (typeArguments.length > 0) {
                return Class.forName(typeArguments[0].getTypeName()).newInstance();
            }
        }
        return null;
    }

    //打印SQL
    public static void printOutSqlAndParams(String sql, Map<Object, Integer> parms) {
        System.out.println("########################################################################################");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SQL:" + sql);
        for (Map.Entry<Object, Integer> entry : parms.entrySet()) {
            stringBuilder.append(" position:" + entry.getValue()).append(" value: " + entry.getKey());
        }
        System.out.println(stringBuilder.toString());
        System.out.println("########################################################################################");
    }
}
