package com.sbm.config;

import com.sbm.entity.Role;
import com.sbm.entity.User;
import com.sbm.repository.RoleRepository;
import com.sbm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Seed roles if they don't exist
        if (roleRepository.count() == 0) {
            roleRepository.save(Role.builder().name(Role.OWNER).build());
            roleRepository.save(Role.builder().name(Role.EMPLOYEE).build());
            roleRepository.save(Role.builder().name(Role.PLATFORM_ADMIN).build());
            log.info("Seeded roles: OWNER, EMPLOYEE, PLATFORM_ADMIN");
        }

        // Seed platform admin if no users exist
        if (userRepository.count() == 0) {
            Role adminRole = roleRepository.findByName(Role.PLATFORM_ADMIN)
                    .orElseThrow(() -> new RuntimeException("PLATFORM_ADMIN role not found"));
            User admin = User.builder()
                    .fullName("Platform Admin")
                    .email("admin@sbm.com")
                    .password(passwordEncoder.encode("Admin@123"))
                    .phone("0000000000")
                    .isActive(true)
                    .role(adminRole)
                    .build();
            userRepository.save(admin);
            log.info("Seeded platform admin user: admin@sbm.com / Admin@123");
        }
    }
}
