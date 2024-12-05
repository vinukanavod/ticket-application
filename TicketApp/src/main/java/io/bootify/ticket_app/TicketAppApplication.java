package io.bootify.ticket_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class TicketAppApplication {

    public static void main(final String[] args) {
        SpringApplication.run(TicketAppApplication.class, args);
        System.out.println("hello");
    }

}
