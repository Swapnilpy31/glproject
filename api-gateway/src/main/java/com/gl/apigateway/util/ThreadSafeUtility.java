package com.gl.apigateway.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Component;

/**
 * Utility class for managing thread-safe operations.
 */
@Component
public class ThreadSafeUtility {

    private final ConcurrentHashMap<String, AtomicInteger> requestCounters = new ConcurrentHashMap<>();

    /**
     * Increments the request count for a specific service safely.
     *
     * @param serviceName the name of the service
     * @return the new count
     */
    public int incrementRequestCount(String serviceName) {
        return requestCounters.computeIfAbsent(serviceName, k -> new AtomicInteger(0))
                .incrementAndGet();
    }

    /**
     * Gets the current request count for a service.
     *
     * @param serviceName the name of the service
     * @return the current count
     */
    public int getRequestCount(String serviceName) {
        return requestCounters.getOrDefault(serviceName, new AtomicInteger(0)).get();
    }
}
