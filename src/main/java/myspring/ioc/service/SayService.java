package myspring.ioc.service;

import myspring.annotations.MyService;

@MyService
public class SayService {
    public void say() {
        System.out.println("i am sayService");
    }
}
