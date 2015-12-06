package com.egopulse.test;

import com.egopulse.junit.AsyncRule;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class AsyncTestForAllMethods {

    @Rule
    public AsyncRule asyncRule = AsyncRule.forAllMethods();

    @Test
    public void nonAsyncTest() {
        // This will fail miserably
        Assert.assertTrue(1 == 1);
    }

    @Test
    public void asyncTest() throws InterruptedException {
        // Still fail
        asyncRule.finished();
    }

    @Test
    public void asyncTest2() throws InterruptedException {
        asyncRule.finished();
    }

}
