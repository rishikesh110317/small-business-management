package com.sbm.controller;

import com.sbm.dto.SaleDto;
import com.sbm.security.SecurityUtils;
import com.sbm.service.SaleService;
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
@RequestMapping("/api/sales")
@RequiredArgsConstructor
public class SaleController {
    private final SaleService saleService;

    @GetMapping
    public ResponseEntity<Page<SaleDto>> getAll(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(saleService.getAllByBusiness(SecurityUtils.getCurrentBusinessId(), pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(saleService.getById(id, SecurityUtils.getCurrentBusinessId()));
    }

    @PostMapping
    public ResponseEntity<SaleDto> create(@Valid @RequestBody SaleDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(saleService.create(dto, SecurityUtils.getCurrentBusinessId(), SecurityUtils.getCurrentUserId()));
    }
}
