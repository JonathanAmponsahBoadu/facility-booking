package com.university.facility_booking.controller;

import com.university.facility_booking.service.BookingService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/availability")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "bearerAuth")
public class AvailabilityController {

    private final BookingService bookingService;

    @GetMapping
    public ResponseEntity<?> getAvailability(
        @RequestParam Long facilityId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        try {
            List<LocalTime[]> slots = bookingService.getAvailableSlots(facilityId, date);
            return ResponseEntity.ok(slots);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}