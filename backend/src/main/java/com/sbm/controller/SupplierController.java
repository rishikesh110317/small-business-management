package com.sbm.controller;

import com.sbm.dto.SupplierDto;
import com.sbm.security.SecurityUtils;
import com.sbm.service.SupplierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
public class SupplierController {
    private final SupplierService supplierService;

    @GetMapping
    public ResponseEntity<Page<SupplierDto>> getAll(@PageableDefault(sort = "name") Pageable pageable) {
        return ResponseEntity.ok(supplierService.getAllByBusiness(SecurityUtils.getCurrentBusinessId(), pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(supplierService.getById(id, SecurityUtils.getCurrentBusinessId()));
    }

    @PostMapping
    public ResponseEntity<SupplierDto> create(@Valid @RequestBody SupplierDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(supplierService.create(dto, SecurityUtils.getCurrentBusinessId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplierDto> update(@PathVariable Long id, @Valid @RequestBody SupplierDto dto) {
        return ResponseEntity.ok(supplierService.update(id, dto, SecurityUtils.getCurrentBusinessId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        supplierService.delete(id, SecurityUtils.getCurrentBusinessId());
        return ResponseEntity.noContent().build();
    }
}
