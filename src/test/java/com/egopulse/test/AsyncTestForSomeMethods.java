package com.egopulse.test;

import com.egopulse.junit.AsyncRule;
import com.egopulse.junit.Async;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class AsyncTestForSomeMethods {

    @Rule
    public AsyncRule async = AsyncRule.byAnnotations();

    @Test
    public void nonAsyncTest() {
        Assert.assertTrue(1 == 1);
    }

    @Test
    @Async(value = 7, unit = TimeUnit.SECONDS)
    public void asyncTest() throws InterruptedException {
        Thread.sleep(6000L);
        async.finished();
    }

    @Test
    @Async
    public void asyncTest2() throws InterruptedException {
        async.finished();
    }

}
