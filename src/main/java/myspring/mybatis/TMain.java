package myspring.mybatis;

import myspring.mybatis.daos.NameDao;
import myspring.mybatis.entity.Names;
import myspring.mybatis.proxy.SqlSessionManage;

import java.util.List;

/**
 * @Auther: jxy
 * @Date: 2019/3/21 21:22
 * @Description:
 */
public class TMain {
    public static void main(String[] args) {
        NameDao dao = SqlSessionManage.getDao(NameDao.class);
        /*  Names name = dao.getById(7l);*/
        List<Names> list = dao.getByName("nameTest");
        int row = dao.insert("瞎写，", "哪买的");
     /*   dao.delete(7l);
        Names name2 = dao.getById(1l);
        int row = dao.update("我改了7", "性别7", 7l);
        Names name32 = dao.getById(7l);*/
        System.out.println();
    }
}
