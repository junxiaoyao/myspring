package myspring.ioc.entity;

/**
 * @Auther: jxy
 * @Date: 2019/3/18 10:43
 * @Description:
 */
public class User {
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public void say(){
        System.out.println("我说话了");
    }
}
