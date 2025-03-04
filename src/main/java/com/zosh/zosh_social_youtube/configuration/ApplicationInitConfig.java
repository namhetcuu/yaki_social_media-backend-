package com.zosh.zosh_social_youtube.configuration;

import com.zosh.zosh_social_youtube.entity.Role;
import com.zosh.zosh_social_youtube.enums.EnumRole;
import com.zosh.zosh_social_youtube.enums.Gender;
import com.zosh.zosh_social_youtube.entity.User;
import com.zosh.zosh_social_youtube.repository.UserRepository;
import com.zosh.zosh_social_youtube.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;
    UserRepository userRepository;

    @Bean
    @Transactional // Đảm bảo tất cả thay đổi DB thực hiện trong một transaction
    ApplicationRunner applicationRunner() {
        return args -> {
            // ✅ Kiểm tra và tạo Role ADMIN nếu chưa có
            Role adminRole = roleRepository.findById(EnumRole.ADMIN.name())
                    .orElseGet(() -> {
                        Role newRole = new Role(EnumRole.ADMIN.name(), "Administrator role", new HashSet<>(), new HashSet<>());
                        return roleRepository.save(newRole);
                    });

            // ✅ Kiểm tra và tạo user "admin" nếu chưa tồn tại
            Optional<User> existingAdmin = userRepository.findByUsername("admin");
            if (existingAdmin.isEmpty()) {
                User user = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .email("admin@example.com")
                        .firstName("Admin")
                        .lastName("User")
                        .dob(LocalDate.of(2000, 1, 1))
                        .gender(Gender.MALE) // Cập nhật lại kiểu dữ liệu gender
                        .roles(Set.of(adminRole)) // Gán role ngay khi tạo
                        .build();

                userRepository.save(user);
                log.warn("Admin user has been created with default password: admin, please change it");
            }
        };
    }
}
