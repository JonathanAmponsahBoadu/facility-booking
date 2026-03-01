package com.university.facility_booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private Long id;
    private String token;
    private String name;
    private String email;
    private String role;
}