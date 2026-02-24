package com.gl.apigateway.service.impl;

import com.gl.apigateway.service.LoadBalancerStrategy;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Round Robin implementation of LoadBalancerStrategy.
 * Demonstrates thread safety and strategy pattern.
 */
@Service
@Primary
public class RoundRobinLoadBalancer implements LoadBalancerStrategy {

    private final ConcurrentHashMap<String, AtomicInteger> positionMap = new ConcurrentHashMap<>();

    @Override
    public ServiceInstance choose(List<ServiceInstance> instances) {
        if (instances == null || instances.isEmpty()) {
            return null;
        }

        String serviceId = instances.get(0).getServiceId();
        AtomicInteger position = positionMap.computeIfAbsent(serviceId, k -> new AtomicInteger(0));

        // Atomically increment and get the current index, wrapping around using modulo
        int index = Math.abs(position.getAndIncrement() % instances.size());

        return instances.get(index);
    }
}
