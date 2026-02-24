package com.example.coreservice.util;

import org.springframework.stereotype.Component;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ThreadSafetyUtil {
    private final AtomicInteger counter = new AtomicInteger(0);

    /**
     * Increments the counter and returns the value modulo the limit.
     * This is used for thread-safe round-robin server selection.
     */
    public int incrementAndGet(int limit) {
        if (limit <= 0)
            return 0;
        return counter.getAndAccumulate(1, (current, next) -> (current + next) % limit);
    }
}
