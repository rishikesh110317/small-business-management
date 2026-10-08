package com.sbm.service;

import com.sbm.dto.ExpenseDto;
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
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final BusinessRepository businessRepository;

    public Page<ExpenseDto> getAllByBusiness(Long businessId, Pageable pageable) {
        return expenseRepository.findByBusinessId(businessId, pageable).map(this::toDto);
    }

    @Transactional
    public ExpenseDto create(ExpenseDto dto, Long businessId, Long userId) {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Business not found"));
        Expense expense = Expense.builder()
                .category(Expense.ExpenseCategory.valueOf(dto.getCategory()))
                .amount(dto.getAmount())
                .description(dto.getDescription())
                .expenseDate(dto.getExpenseDate())
                .business(business)
                .build();
        return toDto(expenseRepository.save(expense));
    }

    @Transactional
    public void delete(Long id, Long businessId) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));
        if (!expense.getBusiness().getId().equals(businessId)) throw new UnauthorizedException("Access denied");
        expenseRepository.delete(expense);
    }

    private ExpenseDto toDto(Expense e) {
        ExpenseDto d = new ExpenseDto(); d.setId(e.getId()); d.setCategory(e.getCategory().name());
        d.setAmount(e.getAmount()); d.setDescription(e.getDescription()); d.setExpenseDate(e.getExpenseDate());
        return d;
    }
}
