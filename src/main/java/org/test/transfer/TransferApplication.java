package org.test.transfer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Main class. Starts the application with spring boot runner.
 */
@SpringBootApplication
public class TransferApplication {

    public static void main(String[] args) {
        SpringApplication.run(TransferApplication.class, args);
    }

    /**
     * Convenient way to pass args or fill the app with data
     * via wiring beans needed (e.g. DAO)
     */
    @Bean
    public CommandLineRunner runner() {
        return args -> {
        };
    }

}
