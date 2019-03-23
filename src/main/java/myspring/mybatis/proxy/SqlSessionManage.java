package myspring.mybatis.proxy;

import myspring.annotations.MyService;

import java.lang.reflect.Proxy;

/**
 * @Auther: jxy
 * @Date: 2019/3/21 21:08
 * @Description:
 */
@MyService
public class SqlSessionManage {
    public static <T> T getDao(Class dao) {
        return (T) Proxy.newProxyInstance(dao.getClassLoader(), new Class[]{dao}, new MybatisProxy(dao));
    }
}
