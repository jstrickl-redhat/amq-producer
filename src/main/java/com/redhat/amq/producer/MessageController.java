package com.redhat.amq.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MessageController {

    @Autowired
    private Producer producer;

    @GetMapping("/")
    public String index() {
        return "redirect:/send";
    }

    @GetMapping("/send")
    public String send() {
        producer.sendMessage("MESSAGE !!!!! ");
        return "home";
    }
}
