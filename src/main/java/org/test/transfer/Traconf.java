package org.test.transfer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.test.transfer.dao.account.AccountOperations;
import org.test.transfer.dao.account.impl.AccountStorage;

@Configuration
public class Traconf {

    @Bean
    AccountOperations accountOperations() {
        return new AccountStorage();
    }
}
