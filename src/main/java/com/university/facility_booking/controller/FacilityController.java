package com.university.facility_booking.controller;

import com.university.facility_booking.model.Facility;
import com.university.facility_booking.service.FacilityService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/facilities")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "bearerAuth")
public class FacilityController {

    private final FacilityService facilityService;

    @GetMapping
    public List<Facility> getAllFacilities() {
        return facilityService.getAllFacilities();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Facility> getFacilityById(@PathVariable Long id) {
        return facilityService.getFacilityById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Facility> createFacility(@RequestBody Facility facility) {
        return ResponseEntity.ok(facilityService.saveFacility(facility));
    }

    @DeleteMapping("/{id}")
@PreAuthorize("hasAuthority('ADMIN')")
public ResponseEntity<?> deleteFacility(@PathVariable Long id) {
    try {
        facilityService.deleteFacility(id);
        return ResponseEntity.ok("Facility deleted successfully.");
    } catch (RuntimeException e) {
        return ResponseEntity.badRequest().body("Failed to delete facility.");
    }
}
}