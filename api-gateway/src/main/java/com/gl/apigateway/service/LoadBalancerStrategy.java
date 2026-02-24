package com.gl.apigateway.service;

import org.springframework.cloud.client.ServiceInstance;
import java.util.List;

/**
 * Strategy interface for load balancing logic.
 */
public interface LoadBalancerStrategy {
    /**
     * Selects a service instance from the list of available instances.
     *
     * @param instances The list of available service instances
     * @return The selected service instance
     */
    ServiceInstance choose(List<ServiceInstance> instances);
}
