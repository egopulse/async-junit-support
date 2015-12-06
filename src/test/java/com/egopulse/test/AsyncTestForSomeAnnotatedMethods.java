package com.egopulse.test;

import com.egopulse.junit.AsyncRule;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

public class AsyncTestForSomeAnnotatedMethods {

    @Rule
    public AsyncRule asyncRule = AsyncRule.forMethodsStartWith("async");

    @Test
    public void nonAsyncTest() {
        // This will fail miserably
        Assert.assertTrue(1 == 1);
    }

    @Test
    public void blahasyncTest() throws InterruptedException {
        // Still fail
        // Won't be affected by 5 seconds rule
        Thread.sleep(10000L);
        asyncRule.finished();
    }

    @Test
    public void asyncTest2() throws InterruptedException {
        Thread.sleep(9L);
        asyncRule.finished();
    }

}
