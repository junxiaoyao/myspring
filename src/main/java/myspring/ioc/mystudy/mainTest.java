package myspring.ioc.mystudy;

import myspring.ioc.entity.User;
import myspring.ioc.xmlUtil.ClassXmlPathApplication;

/**
 * @Auther: jxy
 * @Date: 2019/3/18 11:04
 * @Description:
 */
public class mainTest {
    public static void main(String[] args) throws Exception {
        ClassXmlPathApplication classXmlPathApplication = new ClassXmlPathApplication("myspring.xml");
        User user =(User) classXmlPathApplication.getBean("user");
        user.say();
    }
}
