package com.gl.apigateway.service;

import com.gl.apigateway.model.RouteLog;
import com.gl.apigateway.repository.RouteLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditService {

    private final RouteLogRepository routeLogRepository;

    @Transactional(rollbackFor = Exception.class)
    public void logRouteAccess(String serviceName, String path, String user, int statusCode) {
        log.debug("Logging route access for service: {}", serviceName);

        try {
            RouteLog audit = new RouteLog();
            audit.setServiceName(serviceName);
            audit.setPath(path);
            audit.setRequestingUser(user != null ? user : "Anonymous");
            audit.setTimestamp(LocalDateTime.now());
            audit.setStatusCode(statusCode);

            routeLogRepository.save(audit);

        } catch (Exception e) {
            log.error("Failed to log route access", e);
            throw e; // Ensures standard rollback
        }
    }
}
