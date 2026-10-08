package com.sbm.controller;

import com.sbm.dto.PurchaseDto;
import com.sbm.security.SecurityUtils;
import com.sbm.service.PurchaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/purchases")
@RequiredArgsConstructor
public class PurchaseController {
    private final PurchaseService purchaseService;

    @GetMapping
    public ResponseEntity<Page<PurchaseDto>> getAll(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(purchaseService.getAllByBusiness(SecurityUtils.getCurrentBusinessId(), pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(purchaseService.getById(id, SecurityUtils.getCurrentBusinessId()));
    }

    @PostMapping
    public ResponseEntity<PurchaseDto> create(@Valid @RequestBody PurchaseDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(purchaseService.create(dto, SecurityUtils.getCurrentBusinessId(), SecurityUtils.getCurrentUserId()));
    }
}
