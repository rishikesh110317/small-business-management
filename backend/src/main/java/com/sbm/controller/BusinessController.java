package com.sbm.controller;

import com.sbm.dto.BusinessDto;
import com.sbm.entity.Business;
import com.sbm.exception.ResourceNotFoundException;
import com.sbm.repository.BusinessRepository;
import com.sbm.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/business")
@RequiredArgsConstructor
public class BusinessController {
    private final BusinessRepository businessRepository;

    @GetMapping
    public ResponseEntity<BusinessDto> getProfile() {
        Business b = businessRepository.findById(SecurityUtils.getCurrentBusinessId())
                .orElseThrow(() -> new ResourceNotFoundException("Business not found"));
        return ResponseEntity.ok(toDto(b));
    }

    @PutMapping
    public ResponseEntity<BusinessDto> updateProfile(@RequestBody BusinessDto dto) {
        Business b = businessRepository.findById(SecurityUtils.getCurrentBusinessId())
                .orElseThrow(() -> new ResourceNotFoundException("Business not found"));
        b.setName(dto.getName()); b.setOwnerName(dto.getOwnerName());
        b.setPhone(dto.getPhone()); b.setAddress(dto.getAddress());
        return ResponseEntity.ok(toDto(businessRepository.save(b)));
    }

    private BusinessDto toDto(Business b) {
        BusinessDto d = new BusinessDto(); d.setId(b.getId()); d.setName(b.getName());
        d.setOwnerName(b.getOwnerName()); d.setEmail(b.getEmail());
        d.setPhone(b.getPhone()); d.setAddress(b.getAddress()); d.setIsActive(b.getIsActive());
        return d;
    }
}
