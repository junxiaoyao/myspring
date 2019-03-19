package myspring.services;

import myspring.annotations.MyService;

/**
 * @Auther: jxy
 * @Date: 2019/3/19 15:25
 * @Description:
 */
@MyService
public class HomeService {
    public void say(){
        System.out.println("i am home services!");
    }
}
