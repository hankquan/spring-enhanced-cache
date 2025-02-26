package cn.howaric.cache.enhancer.delay;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DelayTrigger<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DelayTrigger.class);
    private final DelayQueue<DelayElement<T>> queue;
    private final DelayHandler<T> delayHandler;
    private final ExecutorService threadPool;
    private final long delayTime;

    public DelayTrigger(DelayHandler<T> delayHandler, long delayTime) {
        this.delayHandler = delayHandler;
        this.delayTime = delayTime;
        this.queue = new DelayQueue<>();
        threadPool = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.SECONDS,
                new SynchronousQueue<>(),//only one task for now
                new CustomizableThreadFactory("cache-delay-handler"),
                new ThreadPoolExecutor.DiscardPolicy());
        threadPool.execute(this::process);
    }

    public void put(final T t) {
        queue.add(new DelayElement<>(t, delayTime));
    }

    public void shutdown() {
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(5, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            threadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }


    private void process() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                final DelayElement<T> delayElement = queue.poll(3, TimeUnit.SECONDS);
                if (delayElement != null) {
                    delayHandler.handle(delayElement.getElement());
                }
            } catch (InterruptedException e) {
                LOGGER.warn("Error when kick-off delayed cache evict operation", e);
            }
        }
    }

}
