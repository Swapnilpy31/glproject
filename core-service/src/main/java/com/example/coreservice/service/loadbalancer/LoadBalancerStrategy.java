package com.example.coreservice.service.loadbalancer;

import java.util.List;

public interface LoadBalancerStrategy {
    String selectServer(List<String> servers);
}
