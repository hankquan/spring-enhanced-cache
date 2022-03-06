package com.github.howaric.cache.core.delay;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class DelayTrigger<T> {

    private static final AtomicInteger cnt = new AtomicInteger();
    private final DelayQueue<DelayElement<T>> queue;
    private final DelayHandler<T> delayHandler;
    private final ExecutorService consumer;
    private final long delayTime;
    private volatile boolean running = true;

    public DelayTrigger(DelayHandler<T> delayHandler, long delayTime) {
        this.delayHandler = delayHandler;
        this.delayTime = delayTime;
        this.queue = new DelayQueue<>();
        CacheOperationHandler cacheOperationHandler = new CacheOperationHandler();
        cacheOperationHandler.setName("delay-trigger-" + cnt.incrementAndGet());
        cacheOperationHandler.setDaemon(true);
        cacheOperationHandler.start();
        consumer = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.SECONDS,
                new SynchronousQueue<>(),
//                new CustomizableThreadFactory("cache-delay-handler-"),
                new ThreadPoolExecutor.DiscardPolicy());
    }

    public void put(final T t) {
        queue.add(new DelayElement<>(t, delayTime));
    }

    public void shutdown() {
        this.running = false;
        this.consumer.shutdownNow();
    }

    private void process() {
        try {
            final DelayElement<T> delayElement = queue.poll(3, TimeUnit.SECONDS);
            if (delayElement != null) {
                consumer.execute(() -> delayHandler.handle(delayElement.getElement()));
            }
        } catch (InterruptedException e) {
            // ignore interrupt
            e.printStackTrace();
        }
    }

    private class CacheOperationHandler extends Thread {

        @Override
        public void run() {
            while (running && !isInterrupted()) {
                process();
            }
            System.out.println("quit");
        }

    }

}
