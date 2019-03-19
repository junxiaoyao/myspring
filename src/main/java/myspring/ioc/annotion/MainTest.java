package myspring.ioc.annotion;

import myspring.ioc.service.UserService;

public class MainTest {

    public static void main(String[] args) throws Exception {
        AnnotionPathContext annotionPathContext = new AnnotionPathContext("myspring.ioc.service");
        UserService o=(UserService)annotionPathContext.getBean("userService");
        o.say();
    }
}
