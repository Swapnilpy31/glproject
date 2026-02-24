package com.gl.apigateway.controller;

import com.gl.apigateway.service.AuditService;
import com.gl.apigateway.service.RoutingService;
import com.gl.apigateway.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class GatewayController {

    private final RoutingService routingService;
    private final AuditService auditService;
    private final UserService userService;

    @RequestMapping(value = "/{serviceName}/**", method = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
            RequestMethod.DELETE })
    public ResponseEntity<byte[]> proxyRequest(
            @PathVariable String serviceName,
            HttpServletRequest request) {

        log.info("Received request for service: {}", serviceName);

        String path = request.getRequestURI().substring(("/api/" + serviceName).length());
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // 1. Validate User (N+1 scenario handled internally if user service loads
        // roles)
        // If validation logic is needed here besides standard JWT
        try {
            userService.validateUser(username); // Can throw exception if invalid
        } catch (Exception e) {
            throw new RuntimeException("User validation failed: " + e.getMessage());
        }

        ResponseEntity<byte[]> response = null;
        try {
            // 2. Perform Routing
            response = routingService.routeRequest(serviceName, path, request);

            // 3. Log Success
            auditService.logRouteAccess(serviceName, path, username, response.getStatusCode().value());

            return response;

        } catch (Exception e) {
            log.error("Request failed", e);
            // 3. Log Failure
            auditService.logRouteAccess(serviceName, path, username, 500);
            throw e;
        }
    }
}
