package org.test.transfer.dao.account.impl;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

import static org.testng.Assert.assertEquals;

public class AccountStorageTest {

    private AccountStorage accountStorage;
    private final Random random = new Random();
    private final Map<Long, Long> checkSum = new ConcurrentHashMap<>();

    @Test(invocationCount = 10, threadPoolSize = 10, dataProvider = "accountPairs")
    public void shouldSuccessTransferMultiThreaded(long from, long to) {
        accountStorage.transferAtomically(from, to, BigDecimal.ONE);
    }

    @BeforeClass
    private void createAccounts() {
        accountStorage = new AccountStorage();
        accountStorage.store("test1", BigDecimal.valueOf(100L));
        accountStorage.store("test2", BigDecimal.valueOf(100L));

        checkSum.put(1L, 0L);
        checkSum.put(2L, 0L);
    }

    @SuppressWarnings("ConstantConditions")
    @AfterClass
    private void checkSums() {
        assertEquals(accountStorage.getAccount(1L).get().getBalance().longValue(), diff(1L, 2L));
        assertEquals(accountStorage.getAccount(2L).get().getBalance().longValue(), diff(2L, 1L));
    }

    private long diff(long from, long to) {
        return 100L - checkSum.get(from) + checkSum.get(to);
    }

    @DataProvider
    private Object[][] accountPairs() {
        return IntStream.range(0, 10)
                .mapToObj(i -> {
                    long from = 1 + random.nextInt(2);
                    long to = from == 1 ? 2 : 1;
                    checkSum.compute(from, (key, value) -> value + 1);
                    return new Object[]{from, to};
                }).toArray(Object[][]::new);
    }

}
