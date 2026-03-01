package com.university.facility_booking.service;

import com.university.facility_booking.model.Booking;
import com.university.facility_booking.repository.BookingRepository;
import com.university.facility_booking.repository.FacilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final FacilityRepository facilityRepository;

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    public Booking createBooking(Booking booking) {
        // Check for conflicts
        List<Booking> conflicts = bookingRepository.findConflictingBookings(
            booking.getFacility().getId(),
            booking.getDate(),
            booking.getStartTime(),
            booking.getEndTime()
        );

        if (!conflicts.isEmpty()) {
            throw new RuntimeException("Booking conflict: the facility is already booked for this time slot.");
        }

        booking.setStatus("CONFIRMED");
        return bookingRepository.save(booking);
    }

    public Booking updateBooking(Long id, Booking updatedBooking) {
        Booking existing = bookingRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));

        // Check for conflicts excluding current booking
        List<Booking> conflicts = bookingRepository.findConflictingBookings(
            updatedBooking.getFacility().getId(),
            updatedBooking.getDate(),
            updatedBooking.getStartTime(),
            updatedBooking.getEndTime()
        ).stream().filter(b -> !b.getId().equals(id)).toList();

        if (!conflicts.isEmpty()) {
            throw new RuntimeException("Booking conflict: the facility is already booked for this time slot.");
        }

        existing.setFacility(updatedBooking.getFacility());
        existing.setUser(updatedBooking.getUser());
        existing.setDate(updatedBooking.getDate());
        existing.setStartTime(updatedBooking.getStartTime());
        existing.setEndTime(updatedBooking.getEndTime());
        existing.setStatus(updatedBooking.getStatus());

        return bookingRepository.save(existing);
    }

    public void cancelBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));
        booking.setStatus("CANCELLED");
        bookingRepository.save(booking);
    }

    public List<LocalTime[]> getAvailableSlots(Long facilityId, LocalDate date) {
        facilityRepository.findById(facilityId)
            .orElseThrow(() -> new RuntimeException("Facility not found with id: " + facilityId));

        List<Booking> bookings = bookingRepository.findByFacilityId(facilityId)
            .stream()
            .filter(b -> b.getDate().equals(date) && !b.getStatus().equals("CANCELLED"))
            .toList();

        // Generate 30-minute slots from 08:00 to 18:00
        List<LocalTime[]> availableSlots = new java.util.ArrayList<>();
        LocalTime slotStart = LocalTime.of(8, 0);
        LocalTime endOfDay = LocalTime.of(18, 0);

        while (slotStart.isBefore(endOfDay)) {
            LocalTime slotEnd = slotStart.plusMinutes(30);
            final LocalTime s = slotStart;
            final LocalTime e = slotEnd;

            boolean isBooked = bookings.stream().anyMatch(b ->
                b.getStartTime().isBefore(e) && b.getEndTime().isAfter(s)
            );

            if (!isBooked) {
                availableSlots.add(new LocalTime[]{s, e});
            }

            slotStart = slotEnd;
        }

        return availableSlots;
    }
}