package org.test.transfer.dao.account.impl;

import com.anarsoft.vmlens.concurrent.junit.ConcurrentTestRunner;
import com.anarsoft.vmlens.concurrent.junit.ThreadCount;
import org.junit.Assert;
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

    private AccountStorage accountStorage;
    private final Random random = new Random();

    @Before
    public void createAccounts() {
        accountStorage = new AccountStorage();
        accountStorage.store("test1", BigDecimal.TEN);
        accountStorage.store("test2", BigDecimal.TEN);
    }

    @Test
    @ThreadCount(8)
    public void testTransferAtomicallyForDeadlock() throws Exception { // With deadlock, test will never finish
        long from = 1 + random.nextInt(2); // 1 or 2
        long to = from == 1 ? 2 : 1;
        Thread.sleep(100);
        accountStorage.transferAtomically(from, to, BigDecimal.ONE);
    }

    @Test
    @ThreadCount(2)
    public void testTransferAtomicallyForInsufficientFunds() throws Exception {
        long from = 1;
        long to = 2;
        Thread.sleep(100);
        try {
            accountStorage.transferAtomically(from, to, BigDecimal.TEN);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals(e.getMessage(), "Insufficient funds");
        }
    }

    @Test
    public void testTransferAtomicallySameAccount() throws InterruptedException {
        long from = 1;
        Thread.sleep(100);
        try {
            accountStorage.transferAtomically(from, from, BigDecimal.TEN);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals(e.getMessage(), "Accounts should be different");
        }
    }

}