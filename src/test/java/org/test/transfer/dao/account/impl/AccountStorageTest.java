package org.test.transfer.dao.account.impl;

import com.anarsoft.vmlens.concurrent.junit.ConcurrentTestRunner;
import com.anarsoft.vmlens.concurrent.junit.ThreadCount;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.util.Random;

/**
 * Simple check for deadlocks.
 */
@RunWith(ConcurrentTestRunner.class)
public class AccountStorageTest {

    private final AccountStorage accountStorage = new AccountStorage();
    private final Random random = new Random();

    @Before
    public void createAccounts() {
        accountStorage.store("test1", BigDecimal.TEN);
        accountStorage.store("test2", BigDecimal.TEN);
    }

    @Test
    @ThreadCount(8)
    public void testTransferAtomically() throws Exception { // With deadlock, test will never finish
        long from = 1 + random.nextInt(2); // 1 or 2
        long to = from == 1 ? 2 : 1;
        accountStorage.transferAtomically(from, to, BigDecimal.ONE);
    }
}