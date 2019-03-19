package myspring.ioc.service;

import myspring.annotations.MyAutowired;
import myspring.annotations.MyService;

@MyService
public class UserService {
    @MyAutowired
    private SayService sayService;

    public void say() {
        sayService.say();
        System.out.println("yes ! you are successful!");
    }
}
