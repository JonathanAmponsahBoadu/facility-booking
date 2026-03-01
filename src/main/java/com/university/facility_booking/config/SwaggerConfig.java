package com.university.facility_booking.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Campus Facility Booking API",
        version = "1.0",
        description = "API for booking campus facilities at the University of Ghana"
    ),
    servers = {
        @io.swagger.v3.oas.annotations.servers.Server(
            url = "http://localhost:8082",
            description = "Local Server"
        ),
        @io.swagger.v3.oas.annotations.servers.Server(
            url = "https://facility-booking-jpyp.onrender.com",
            description = "Production Server"
        )
    }
)
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT"
)
public class SwaggerConfig {
}