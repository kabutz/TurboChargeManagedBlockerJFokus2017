/*
 * Copyright 2017, Heinz Kabutz - All Rights Reserved
 */
package eu.javaspecialists.performance.managedblocker;

import java.math.*;
import java.util.concurrent.*;

public class Fibonacci {
    /*
    demo1: test100_000_000() time = 45935
    demo2: test100_000_000() time = 24572
     */
    public BigInteger f(int n) {
        if (n == 0) return BigInteger.ZERO;
        if (n == 1) return BigInteger.ONE;

        int half = (n + 1) / 2;

        ForkJoinTask<BigInteger> f0_task = new RecursiveTask<BigInteger>() {
            protected BigInteger compute() {
                return f(half - 1);
            }
        };
        f0_task.fork();
        BigInteger f1 = f(half);
        BigInteger f0 = f0_task.join();

        long time = n > 1000 ? System.currentTimeMillis() : 0;
        try {
            if (n % 2 == 1) {
                return f0.multiply(f0).add(f1.multiply(f1));
            } else {
                return f0.shiftLeft(1).add(f1).multiply(f1);
            }
        } finally {
            time = n > 1000 ? System.currentTimeMillis() - time : 0;
            if (time > 50) {
                System.out.println("fib(" + n + ") took " + time);
            }
        }
    }
}
