package com.example.coreservice.service.loadbalancer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoadBalancingService {
    private final LoadBalancerStrategy strategy;
    // Example list of servers for demonstration
    private final List<String> availableServers = List.of("Server-Port-1", "Server-Port-2", "Server-Port-3");

    public String getNextServer() {
        return strategy.selectServer(availableServers);
    }
}
