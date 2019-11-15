package com.stackroute.datapopulator.zuulapi.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class TestController {
        @RequestMapping("/secured")
        public String secured() {
            System.out.println("Inside secured()");
            return "Hello Ritesh Tiwari !!! : " + new Date();
        }
}
