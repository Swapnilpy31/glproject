package com.gl.apigateway.service;

import com.gl.apigateway.util.ThreadSafeUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.net.URI;
import java.util.Enumeration;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoutingService {

    private final DiscoveryClient discoveryClient;
    private final LoadBalancerStrategy loadBalancerStrategy;
    private final ThreadSafeUtility threadSafeUtility;
    private final RestTemplate restTemplate = new RestTemplate();

    public ResponseEntity<byte[]> routeRequest(String serviceName, String path, HttpServletRequest request) {
        log.info("Routing request for service: {} with path: {}", serviceName, path);

        // 1. Get available instances
        List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);
        if (instances.isEmpty()) {
            throw new RuntimeException("Service unavailable: " + serviceName);
        }

        // 2. Load Balance
        ServiceInstance instance = loadBalancerStrategy.choose(instances);
        if (instance == null) {
            throw new RuntimeException("No available instance for service: " + serviceName);
        }

        // 3. Track Stats (Thread Safe)
        threadSafeUtility.incrementRequestCount(serviceName);

        // 4. Construct Target URL
        String targetUrl = instance.getUri().toString() + path;
        String queryString = request.getQueryString();
        if (queryString != null) {
            targetUrl += "?" + queryString;
        }

        log.debug("Forwarding to: {}", targetUrl);

        try {
            // 5. Forward Request
            return forward(targetUrl, request);
        } catch (RestClientResponseException e) {
            return ResponseEntity.status(e.getStatusCode())
                    .headers(e.getResponseHeaders())
                    .body(e.getResponseBodyAsByteArray());
        } catch (Exception e) {
            log.error("Error routing request", e);
            return ResponseEntity.internalServerError().body(("Gateway Error: " + e.getMessage()).getBytes());
        }
    }

    private ResponseEntity<byte[]> forward(String url, HttpServletRequest request) throws java.io.IOException {
        HttpMethod method = HttpMethod.valueOf(request.getMethod());
        HttpHeaders headers = new HttpHeaders();

        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            // Skip content-length to let RestTemplate calculate it
            if (!headerName.equalsIgnoreCase("Content-Length")) {
                headers.add(headerName, request.getHeader(headerName));
            }
        }

        byte[] body = request.getInputStream().readAllBytes();
        HttpEntity<byte[]> httpEntity = new HttpEntity<>(body, headers);

        return restTemplate.exchange(url, method, httpEntity, byte[].class);
    }
}
