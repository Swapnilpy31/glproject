package com.gl.apigateway.controller;

import com.gl.apigateway.model.RouteLog;
import com.gl.apigateway.repository.RouteLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/audit")
@RequiredArgsConstructor
public class AuditController {

    private final RouteLogRepository routeLogRepository;

    @GetMapping
    public ResponseEntity<List<RouteLog>> getRouteLogs() {
        return ResponseEntity.ok(routeLogRepository.findAll(Sort.by(Sort.Direction.DESC, "timestamp")));
    }
}
