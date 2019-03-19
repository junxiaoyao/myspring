package myspring.ioc.service;

import myspring.ioc.annotion.MyAutowired;
import myspring.ioc.annotion.MyService;

@MyService
public class UserService {
    @MyAutowired
    private SayService sayService;

    public void say() {
        sayService.say();
        System.out.println("yes ! you are successful!");
    }
}
