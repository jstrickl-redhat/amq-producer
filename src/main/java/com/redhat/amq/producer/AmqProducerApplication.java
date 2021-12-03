package com.redhat.amq.producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
public class AmqProducerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AmqProducerApplication.class, args);
    }
}
