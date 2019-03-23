package myspring.mybatis.entity;

/**
 * @Auther: jxy
 * @Date: 2019/3/22 20:09
 * @Description:
 */
public class Names {
    private long id;
    private String name;
    private String sex;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
