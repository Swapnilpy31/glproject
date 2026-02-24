package com.gl.apigateway.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "route_logs")
@Data
@NoArgsConstructor
public class RouteLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String serviceName;
    private String path;
    private String requestingUser;
    private LocalDateTime timestamp;
    private int statusCode;
}
