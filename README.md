# Simplify Async JUnit testing

JUnit doesn't support non-blocking testing out of the box. It will be pretty hassle to setup everything on your own, your test will just fail as fast as it reaches the last line, even you have bunch of asynch operations behind the screen.

`async-junit-support` comes into play by providing another dedicated thread monitoring timing of test running thread and interupt the main thread on timeout.

# Sample

```java
// Declare Rule for Async
@Rule public AsyncRule async = AsyncRule.byAnnotations();

// This test will fail as it's decorated with @Async annotation and it doesn't define the end state
// Async annotation comes with async-junit-support
@Test
@Async
public void simpleTest() {
    Thread.sleep(9000);
}

// Statement of async.finished define operation has finished
@Test
@Async
public void anotherSimpleTest() {
    async.finished();
}

// Custom timeout (default timeunit is second). Test will be passed even though the default timeout is 5 seconds
@Test
@Async(6)
public void anotherNotSoSimpleTest() {
    Thread.sleep(5000);
    async.finished();
}
```
