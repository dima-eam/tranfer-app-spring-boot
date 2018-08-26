package org.test.transfer.bench.account;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.test.transfer.dao.account.AccountOperations;
import org.test.transfer.dao.account.impl.AccountStorage;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
public class AccountBenchmark {

    private AccountOperations opsWithLock = new AccountStorage();

    @Benchmark
    @Warmup(iterations = 10)
    @Measurement(iterations = 10)
    public void measureOpsWithLock() {
//        opsWithLock.
    }

}
