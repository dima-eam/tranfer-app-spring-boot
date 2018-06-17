package org.test.transfer.dao.account.impl;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.Random;
import java.util.stream.IntStream;

public class AccountStorageTest {

    private AccountStorage accountStorage;
    private final Random random = new Random();

    @Test(invocationCount = 2, threadPoolSize = 2, dataProvider = "accountPairs")
    public void shouldSuccessTransferMultiThreaded(long from, long to) throws Exception {
        Thread.sleep(100);
        accountStorage.transferAtomically(from, to, BigDecimal.ONE);
    }

    @BeforeClass
    private void createAccounts() {
        accountStorage = new AccountStorage();
        accountStorage.store("test1", BigDecimal.valueOf(100L));
        accountStorage.store("test2", BigDecimal.valueOf(100L));
    }

    @DataProvider
    private Object[][] accountPairs() {
        return IntStream.range(0, 5)
                .mapToObj(i -> {
                    long from = 1 + random.nextInt(2);
                    long to = from == 1 ? 2 : 1;
                    return new Object[]{from, to};
                }).toArray(Object[][]::new);
    }

}
