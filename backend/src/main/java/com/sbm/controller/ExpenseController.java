package com.sbm.controller;

import com.sbm.dto.ExpenseDto;
import com.sbm.security.SecurityUtils;
import com.sbm.service.ExpenseService;
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
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {
    private final ExpenseService expenseService;

    @GetMapping
    public ResponseEntity<Page<ExpenseDto>> getAll(
            @PageableDefault(sort = "expenseDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(expenseService.getAllByBusiness(SecurityUtils.getCurrentBusinessId(), pageable));
    }

    @PostMapping
    public ResponseEntity<ExpenseDto> create(@Valid @RequestBody ExpenseDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(expenseService.create(dto, SecurityUtils.getCurrentBusinessId(), SecurityUtils.getCurrentUserId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        expenseService.delete(id, SecurityUtils.getCurrentBusinessId());
        return ResponseEntity.noContent().build();
    }
}
