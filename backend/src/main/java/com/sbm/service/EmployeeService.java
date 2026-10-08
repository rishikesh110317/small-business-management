package com.sbm.service;

import com.sbm.dto.*;
import com.sbm.entity.*;
import com.sbm.exception.*;
import com.sbm.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final UserRepository userRepository;
    private final BusinessRepository businessRepository;
    private final RoleRepository roleRepository;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    public Page<EmployeeDto> getAllByBusiness(Long businessId, Pageable pageable) {
        return userRepository.findByBusinessId(businessId, pageable).map(this::toDto);
    }

    @Transactional
    public EmployeeDto create(EmployeeDto dto, Long businessId) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("Email already registered");
        }
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Business not found"));
        Role role = roleRepository.findByName(Role.EMPLOYEE)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        User user = User.builder()
                .fullName(dto.getFullName()).email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .phone(dto.getPhone()).isActive(true).business(business).role(role).build();
        return toDto(userRepository.save(user));
    }

    @Transactional
    public EmployeeDto update(Long id, EmployeeDto dto, Long businessId) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        if (!user.getBusiness().getId().equals(businessId)) throw new UnauthorizedException("Access denied");
        user.setFullName(dto.getFullName()); user.setPhone(dto.getPhone());
        if (dto.getIsActive() != null) user.setIsActive(dto.getIsActive());
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        return toDto(userRepository.save(user));
    }

    @Transactional
    public void delete(Long id, Long businessId) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        if (!user.getBusiness().getId().equals(businessId)) throw new UnauthorizedException("Access denied");
        userRepository.delete(user);
    }

    private EmployeeDto toDto(User u) {
        EmployeeDto d = new EmployeeDto(); d.setId(u.getId()); d.setFullName(u.getFullName());
        d.setEmail(u.getEmail()); d.setPhone(u.getPhone()); d.setIsActive(u.getIsActive());
        d.setRoleName(u.getRole().getName());
        return d;
    }
}
