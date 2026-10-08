package com.sbm.controller;

import com.sbm.dto.EmployeeDto;
import com.sbm.security.SecurityUtils;
import com.sbm.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_OWNER')")
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<Page<EmployeeDto>> getAll(@PageableDefault(sort = "fullName") Pageable pageable) {
        return ResponseEntity.ok(employeeService.getAllByBusiness(SecurityUtils.getCurrentBusinessId(), pageable));
    }

    @PostMapping
    public ResponseEntity<EmployeeDto> create(@Valid @RequestBody EmployeeDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.create(dto, SecurityUtils.getCurrentBusinessId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDto> update(@PathVariable Long id, @Valid @RequestBody EmployeeDto dto) {
        return ResponseEntity.ok(employeeService.update(id, dto, SecurityUtils.getCurrentBusinessId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        employeeService.delete(id, SecurityUtils.getCurrentBusinessId());
        return ResponseEntity.noContent().build();
    }
}
