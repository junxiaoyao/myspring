package myspring.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @Auther: jxy
 * @Date: 2019/3/19 9:12
 * @Description:注解工具类
 */
public class AnnotationUtil {
    //判断class是否含指定注解
    public static boolean testClassHasAnnotion(Class classNow, Class annotion) {
        if (classNow.getAnnotation(annotion) != null) {
            return true;
        }
        return false;
    }
    //判断Field是否含指定注解
    public static boolean testFieldHasAnnotion(Field field, Class annotion) {
        if (field.getAnnotation(annotion) != null) {
            return true;
        }
        return false;
    }

    //判断Method是否含指定注解
    public static boolean testMethodHasAnnotion(Method method, Class annotion) {
        if (method.getAnnotation(annotion) != null) {
            return true;
        }
        return false;
    }
}
