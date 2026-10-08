package com.sbm.service;

import com.sbm.dto.*;
import com.sbm.entity.*;
import com.sbm.exception.*;
import com.sbm.repository.*;
import com.sbm.security.CustomUserPrincipal;
import com.sbm.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BusinessRepository businessRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email is already registered");
        }

        // Create business
        Business business = Business.builder()
                .name(request.getBusinessName())
                .ownerName(request.getOwnerName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .isActive(true)
                .build();
        business = businessRepository.save(business);

        // Get owner role
        Role ownerRole = roleRepository.findByName(Role.OWNER)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        // Create owner user
        User user = User.builder()
                .fullName(request.getOwnerName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .isActive(true)
                .business(business)
                .role(ownerRole)
                .build();
        user = userRepository.save(user);

        // Generate token
        CustomUserPrincipal principal = new CustomUserPrincipal(user);
        String token = tokenProvider.generateToken(principal);

        return AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(ownerRole.getName())
                .businessId(business.getId())
                .businessName(business.getName())
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
        String token = tokenProvider.generateToken(principal);

        String businessName = null;
        if (principal.getBusinessId() != null) {
            businessName = businessRepository.findById(principal.getBusinessId())
                    .map(Business::getName)
                    .orElse(null);
        }

        return AuthResponse.builder()
                .token(token)
                .email(principal.getEmail())
                .fullName(principal.getFullName())
                .role(principal.getRoleName())
                .businessId(principal.getBusinessId())
                .businessName(businessName)
                .build();
    }
}
