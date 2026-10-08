package com.sbm.service;

import com.sbm.dto.CategoryDto;
import com.sbm.entity.Business;
import com.sbm.entity.Category;
import com.sbm.exception.*;
import com.sbm.repository.BusinessRepository;
import com.sbm.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final BusinessRepository businessRepository;

    public Page<CategoryDto> getAllByBusiness(Long businessId, Pageable pageable) {
        return categoryRepository.findByBusinessId(businessId, pageable).map(this::toDto);
    }

    public List<CategoryDto> listAllByBusiness(Long businessId) {
        return categoryRepository.findAllByBusinessId(businessId).stream().map(this::toDto).collect(Collectors.toList());
    }

    public CategoryDto getById(Long id, Long businessId) {
        Category category = categoryRepository.findByIdAndBusinessId(id, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        return toDto(category);
    }

    @Transactional
    public CategoryDto create(CategoryDto dto, Long businessId) {
        if (categoryRepository.existsByNameAndBusinessId(dto.getName(), businessId)) {
            throw new DuplicateResourceException("Category with this name already exists");
        }
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Business not found"));
        Category category = Category.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .business(business)
                .build();
        return toDto(categoryRepository.save(category));
    }

    @Transactional
    public CategoryDto update(Long id, CategoryDto dto, Long businessId) {
        Category category = categoryRepository.findByIdAndBusinessId(id, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        return toDto(categoryRepository.save(category));
    }

    @Transactional
    public void delete(Long id, Long businessId) {
        Category category = categoryRepository.findByIdAndBusinessId(id, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        categoryRepository.delete(category);
    }

    private CategoryDto toDto(Category entity) {
        CategoryDto dto = new CategoryDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        return dto;
    }
}
