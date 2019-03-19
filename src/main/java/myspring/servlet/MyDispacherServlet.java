package myspring.servlet;

import myspring.annotations.MyController;
import myspring.annotations.MyRequestMapping;
import myspring.annotations.MyAutowired;
import myspring.annotations.MyService;
import myspring.util.AnnotationUtil;
import myspring.util.ClassUtil;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Auther: jxy
 * @Date: 2019/3/19 11:05
 * @Description:
 */
public class MyDispacherServlet extends HttpServlet {
    //key为类名小写
    private ConcurrentHashMap<String, Object> beans = new ConcurrentHashMap<>();
    //key为url controllers
    private ConcurrentHashMap<String, Object> controllers = new ConcurrentHashMap<>();
    //key为url value 为方法名
    private ConcurrentHashMap<String, String> methods = new ConcurrentHashMap<>();
    //services
    private ConcurrentHashMap<String, Object> services = new ConcurrentHashMap<>();

    @Override
    public void init() throws ServletException {
        System.out.println("init method has bean called!");
        try {
            initMain();
        } catch (Exception e) {
            System.out.println("something wrong with your spring application");
            e.printStackTrace();
        }
    }

    //初始化方法
    public void initMain() throws Exception {
        getObjects();
        autowiredFields();
        handlerMappingObject();
        handlerMappingMethods();
    }

    //controllers装配
    public void handlerMappingObject() {
        for (Object o : beans.values()) {
            if (AnnotationUtil.testClassHasAnnotion(o.getClass(), MyRequestMapping.class)) {
                MyRequestMapping myRequestMapping = o.getClass().getAnnotation(MyRequestMapping.class);
                controllers.put(myRequestMapping.url(), o);
            }
        }
    }

    //遍历含MyRequestMapping注解的方法
    public void handlerMappingMethods() {
        for (Object o : controllers.values()) {
            handlerMappingMethods(o);
        }
    }

    //遍历含MyRequestMapping注解的方法
    public void handlerMappingMethods(Object o) {
        Method[] ms = o.getClass().getDeclaredMethods();
        for (Method m : ms) {
            if (AnnotationUtil.testMethodHasAnnotion(m, MyRequestMapping.class)) {
                String controllerUrl = o.getClass().getAnnotation(MyRequestMapping.class).url();
                String methodUrl = m.getAnnotation(MyRequestMapping.class).url();
                methods.put(controllerUrl + methodUrl, m.getName());
            }
        }
    }

    //根据id获取对象
    public Object getBean(Map map, String id) throws Exception {
        if (map.isEmpty()) {
            throw new Exception("该包下不含带注解的类");
        }
        Object o = map.get(id);
        if (o == null) {
            throw new Exception("查找不到指定类");
        }
        return o;
    }

    //装配对象的属性主方法
    public void autowiredFields() throws Exception {
        for (Object o : beans.values()) {
            autowiredField(o);
        }
    }

    //装配对象的属性
    public void autowiredField(Object o) throws Exception {
        Field[] fields = o.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (AnnotationUtil.testFieldHasAnnotion(field, MyAutowired.class)) {
                String id = toLowerCaseFirstOne(field.getType().getSimpleName());
                field.set(o, getBean(services, id));
            }
        }
    }

    //实例化所有带MyController注解的类
    public void getObjects() throws InstantiationException, IllegalAccessException {
        List<Class<?>> classes = ClassUtil.getClasses("myspring");
        for (Class classNow : classes) {
            if (AnnotationUtil.testClassHasAnnotion(classNow, MyController.class)) {
                beans.put(toLowerCaseFirstOne(classNow.getSimpleName()), newInstance(classNow));
            }
            if (AnnotationUtil.testClassHasAnnotion(classNow, MyService.class)) {
                services.put(toLowerCaseFirstOne(classNow.getSimpleName()), newInstance(classNow));
            }
        }
    }

    //实例化
    public Object newInstance(Class c) throws InstantiationException, IllegalAccessException {
        return c.newInstance();
    }

    // 首字母转小写
    public static String toLowerCaseFirstOne(String s) {
        if (Character.isLowerCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
    }

    private void extResourceViewResolver(String pageName, HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        // 根路径
        String prefix = "/";
        String suffix = ".jsp";
        req.getRequestDispatcher(prefix + pageName + suffix).forward(req, res);
    }

    //执行方法
    private Object methodInvoke(Object object, String methodName) {
        try {
            Class<? extends Object> classInfo = object.getClass();
            Method method = classInfo.getMethod(methodName);
            Object result = method.invoke(object);
            return result;
        } catch (Exception e) {
            return null;
        }

    }

    //根据url查找key
    public String getUrl(Map<String, ?> map, String url) {
        for (String key : map.keySet()) {
            if (url.contains(key) || url.equals(key)) {
                return key;
            }
        }
        return "";
    }

    //根据url结束的查找key
    public String getUrl2(Map<String, ?> map, String url) {
        for (String key : map.keySet()) {
            if (url.endsWith(key)) {
                return key;
            }
        }
        return "";
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //转发都post方法
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestURI = req.getRequestURI();
        if (StringUtils.isEmpty(requestURI)) {
            return;
        }
        //获取controller
        String beanKey = getUrl(controllers, requestURI);
        Object object = controllers.get(beanKey);
        if (object == null) {
            notFoundPage(req, resp);
            return;
        }
        // 使用url地址获取方法
        String methodKey = getUrl2(methods, requestURI);
        String methodName = methods.get(methodKey);
        if (StringUtils.isEmpty(methodName)) {
            notFoundPage(req, resp);
            return;
        }
        //执行方法调用，获取page
        String resultPage = (String) methodInvoke(object, methodName);
        // 调用视图转换器渲染给页面展示
        extResourceViewResolver(resultPage, req, resp);
    }

    public void notFoundPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        extResourceViewResolver("404", request, response);
    }
}
