package com.university.facility_booking.config;

import com.university.facility_booking.model.User;
import com.university.facility_booking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByEmail("admin@ug.edu.gh").isEmpty()) {
            User admin = new User();
            admin.setName("Admin");
            admin.setEmail("admin@ug.edu.gh");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole("ADMIN");
            userRepository.save(admin);
            System.out.println("Default admin created: admin@ug.edu.gh / admin123");
        }
    }
}