package com.example.coreservice.service.loadbalancer.impl;

import com.example.coreservice.service.loadbalancer.LoadBalancerStrategy;
import com.example.coreservice.util.ThreadSafetyUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RoundRobinStrategy implements LoadBalancerStrategy {
    private final ThreadSafetyUtil threadSafetyUtil;

    @Override
    public String selectServer(List<String> servers) {
        if (servers == null || servers.isEmpty()) {
            return null;
        }
        int index = threadSafetyUtil.incrementAndGet(servers.size());
        return servers.get(index);
    }
}
