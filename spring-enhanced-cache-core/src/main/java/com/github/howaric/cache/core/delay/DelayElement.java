package com.github.howaric.cache.core.delay;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class DelayElement<T> implements Delayed {

    private final T element;
    private final long targetTime;

    public DelayElement(T element, long delayTime) {
        this.element = element;
        this.targetTime = System.currentTimeMillis() + delayTime;
    }

    public T getElement() {
        return element;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return targetTime - System.currentTimeMillis();
    }

    @Override
    public int compareTo(Delayed o) {
        return this.getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS) >= 0 ? 1 : -1;
    }

}
