package com.study.sparkweb.sparkweb.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class HelloWeb {

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello(){
        return "hellospringboot";
    }

    @RequestMapping(value = "/firstDemo", method = RequestMethod.GET)
    public ModelAndView firstDemo() {
        return new ModelAndView("test");
    }

    @RequestMapping("/pieChart")
    public ModelAndView pieChart(){
        return new ModelAndView("Piechart");
    }
}
