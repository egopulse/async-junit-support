package com.egopulse.junit;

import com.jayway.awaitility.Awaitility;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class AsyncRule implements TestRule {

    private ExecutorService singleThread = Executors.newSingleThreadExecutor();
    private final boolean toAllMethods;
    private final String startsWith;
    private AtomicBoolean hasFinished = new AtomicBoolean(false);

    protected AsyncRule(boolean toAllMethods, String startsWith) {
        this.toAllMethods = toAllMethods;
        this.startsWith = startsWith;
    }

    public static AsyncRule byAnnotations() {
        return new AsyncRule(false, null);
    }

    public static AsyncRule forAllMethods() {
        return new AsyncRule(true, null);
    }

    public static AsyncRule forMethodsStartWith(String startsWith) {
        if (startsWith == null || startsWith.isEmpty()) throw new IllegalArgumentException();
        return new AsyncRule(false, startsWith);
    }

    @Override
    public Statement apply(final Statement base, final Description description) {
        Async async = description.getAnnotation(Async.class);

        if (!shouldIContinue(description, async)) {
            return base;
        }

        final Long timeout = getTimeout(async);
        final TimeUnit unit = getTimeUnit(async);

        return getStatement(base, description, timeout, unit);
    }

    protected Statement getStatement(final Statement base, final Description description, final Long timeout, final TimeUnit unit) {
        return new Statement() {

            @Override
            public void evaluate() throws Throwable {
                final Thread currentThread = Thread.currentThread();
                singleThread.execute(getCommand(currentThread, description, timeout, unit));
                base.evaluate();
            }

        };
    }

    protected Runnable getCommand(final Thread currentThread, final Description description, final Long timeout, final TimeUnit unit) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    Awaitility.await(description.getDisplayName())
                            .atMost(timeout, unit)
                            .untilTrue(hasFinished);
                } catch (Exception e) {
                    currentThread.interrupt();
                }
            }
        };
    }

    protected Long getTimeout(Async async) {
        return (async == null) ? Async.DEFAULT_TIMEOUT : async.value();
    }

    protected TimeUnit getTimeUnit(Async async) {
        return (async == null) ? Async.DEFAULT_TIME_UNIT : async.unit();
    }

    protected boolean shouldIContinue(Description description, Async async) {
        if (this.toAllMethods) {
            return true;
        } else if (this.startsWith != null) {
            return description.getMethodName().startsWith(this.startsWith);
        } else if (async != null) {
            return true;
        }

        return false;
    }

    public void finished() {
        hasFinished.compareAndSet(false, true);
    }

}
