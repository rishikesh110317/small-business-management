package com.sbm.service;

import com.sbm.dto.ProductDto;
import com.sbm.entity.*;
import com.sbm.exception.*;
import com.sbm.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BusinessRepository businessRepository;
    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public List<ProductDto> getAllByBusiness(Long businessId, String search) {
        List<Product> products;
        if (search != null && !search.isBlank()) {
            products = productRepository.searchByBusinessIdWithCategory(businessId, search);
        } else {
            products = productRepository.findAllByBusinessIdWithCategory(businessId);
        }
        return products.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductDto getById(Long id, Long businessId) {
        Product product = productRepository.findByIdAndBusinessId(id, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        return toDto(product);
    }

    @Transactional
    public ProductDto create(ProductDto dto, Long businessId) {
        if (dto.getSku() != null && !dto.getSku().isBlank() && productRepository.existsBySkuAndBusinessId(dto.getSku(), businessId)) {
            throw new DuplicateResourceException("Product with this SKU already exists");
        }
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Business not found"));

        Product product = Product.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .sku(dto.getSku())
                .costPrice(dto.getCostPrice())
                .sellingPrice(dto.getSellingPrice())
                .business(business)
                .isActive(true)
                .build();

        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findByIdAndBusinessId(dto.getCategoryId(), businessId)
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            product.setCategory(category);
        }

        product = productRepository.save(product);

        // Create inventory record
        Inventory inventory = Inventory.builder()
                .product(product)
                .currentStock(dto.getCurrentStock() != null ? dto.getCurrentStock() : 0)
                .minStockLevel(dto.getMinStockLevel() != null ? dto.getMinStockLevel() : 10)
                .business(business)
                .build();
        inventoryRepository.save(inventory);

        return toDto(product);
    }

    @Transactional
    public ProductDto update(Long id, ProductDto dto, Long businessId) {
        Product product = productRepository.findByIdAndBusinessId(id, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setSku(dto.getSku());
        product.setCostPrice(dto.getCostPrice());
        product.setSellingPrice(dto.getSellingPrice());
        if (dto.getIsActive() != null) product.setIsActive(dto.getIsActive());

        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findByIdAndBusinessId(dto.getCategoryId(), businessId)
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            product.setCategory(category);
        } else {
            product.setCategory(null);
        }

        product = productRepository.save(product);

        // Update min stock level if provided
        if (dto.getMinStockLevel() != null) {
            Inventory inventory = inventoryRepository.findByProductId(id).orElse(null);
            if (inventory != null) {
                inventory.setMinStockLevel(dto.getMinStockLevel());
                inventoryRepository.save(inventory);
            }
        }

        return toDto(product);
    }

    @Transactional
    public void delete(Long id, Long businessId) {
        Product product = productRepository.findByIdAndBusinessId(id, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        productRepository.delete(product);
    }

    private ProductDto toDto(Product entity) {
        ProductDto dto = new ProductDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setSku(entity.getSku());
        dto.setCostPrice(entity.getCostPrice());
        dto.setSellingPrice(entity.getSellingPrice());
        dto.setIsActive(entity.getIsActive());
        if (entity.getCategory() != null) {
            dto.setCategoryId(entity.getCategory().getId());
            dto.setCategoryName(entity.getCategory().getName());
        }
        // Fetch inventory
        inventoryRepository.findByProductId(entity.getId()).ifPresent(inv -> {
            dto.setCurrentStock(inv.getCurrentStock());
            dto.setMinStockLevel(inv.getMinStockLevel());
        });
        return dto;
    }
}
