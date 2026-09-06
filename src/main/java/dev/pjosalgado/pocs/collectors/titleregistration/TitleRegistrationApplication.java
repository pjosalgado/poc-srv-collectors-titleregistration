package dev.pjosalgado.pocs.collectors.titleregistration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class TitleRegistrationApplication {

    public static void main(String[] args) {
        SpringApplication.run(TitleRegistrationApplication.class, args);
    }

}
