package myspring.services;

import myspring.annotations.MyAutowired;
import myspring.annotations.MyService;
import myspring.mybatis.daos.NameDao;
import myspring.mybatis.entity.Names;

/**
 * @Auther: jxy
 * @Date: 2019/3/19 15:25
 * @Description:
 */
@MyService
public class HomeService {
    @MyAutowired
    private NameDao dao;
    public void say(){
        Names names = dao.getById(6l);
        System.out.println("i am home services!");
    }
}
