package com.sbm.service;

import com.sbm.dto.SupplierDto;
import com.sbm.entity.*;
import com.sbm.exception.*;
import com.sbm.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class SupplierService {
    private final SupplierRepository supplierRepository;
    private final BusinessRepository businessRepository;

    public Page<SupplierDto> getAllByBusiness(Long businessId, Pageable pageable) {
        return supplierRepository.findByBusinessId(businessId, pageable).map(this::toDto);
    }

    public SupplierDto getById(Long id, Long businessId) {
        return toDto(supplierRepository.findByIdAndBusinessId(id, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found")));
    }

    @Transactional
    public SupplierDto create(SupplierDto dto, Long businessId) {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Business not found"));
        Supplier supplier = Supplier.builder()
                .name(dto.getName()).contactPerson(dto.getContactPerson()).email(dto.getEmail())
                .phone(dto.getPhone()).address(dto.getAddress())
                .outstandingBalance(BigDecimal.ZERO).business(business).build();
        return toDto(supplierRepository.save(supplier));
    }

    @Transactional
    public SupplierDto update(Long id, SupplierDto dto, Long businessId) {
        Supplier supplier = supplierRepository.findByIdAndBusinessId(id, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found"));
        supplier.setName(dto.getName()); supplier.setContactPerson(dto.getContactPerson());
        supplier.setEmail(dto.getEmail()); supplier.setPhone(dto.getPhone());
        supplier.setAddress(dto.getAddress());
        return toDto(supplierRepository.save(supplier));
    }

    @Transactional
    public void delete(Long id, Long businessId) {
        Supplier supplier = supplierRepository.findByIdAndBusinessId(id, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found"));
        supplierRepository.delete(supplier);
    }

    private SupplierDto toDto(Supplier e) {
        SupplierDto d = new SupplierDto(); d.setId(e.getId()); d.setName(e.getName());
        d.setContactPerson(e.getContactPerson()); d.setEmail(e.getEmail());
        d.setPhone(e.getPhone()); d.setAddress(e.getAddress());
        d.setOutstandingBalance(e.getOutstandingBalance());
        return d;
    }
}
