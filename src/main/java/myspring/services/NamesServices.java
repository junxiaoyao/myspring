package myspring.services;

import myspring.annotations.MyAutowired;
import myspring.annotations.MyService;
import myspring.mybatis.daos.NameDao;
import myspring.mybatis.entity.Names;

/**
 * @Auther: jxy
 * @Date: 2019/3/24 11:09
 * @Description:
 */
@MyService
public class NamesServices {
    @MyAutowired
    private NameDao dao;

    public Names getById(long id) {
        return dao.getById(id);
    }
}
