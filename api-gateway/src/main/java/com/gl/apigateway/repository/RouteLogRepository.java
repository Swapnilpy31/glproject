package com.gl.apigateway.repository;

import com.gl.apigateway.model.RouteLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteLogRepository extends JpaRepository<RouteLog, Long> {
}
