package com.sbm.service;

import com.sbm.dto.CustomerDto;
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
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final BusinessRepository businessRepository;

    public Page<CustomerDto> getAllByBusiness(Long businessId, String search, Pageable pageable) {
        if (search != null && !search.isBlank()) {
            return customerRepository.searchByBusinessId(businessId, search, pageable).map(this::toDto);
        }
        return customerRepository.findByBusinessId(businessId, pageable).map(this::toDto);
    }

    public CustomerDto getById(Long id, Long businessId) {
        return toDto(customerRepository.findByIdAndBusinessId(id, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found")));
    }

    @Transactional
    public CustomerDto create(CustomerDto dto, Long businessId) {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Business not found"));
        Customer customer = Customer.builder()
                .name(dto.getName()).email(dto.getEmail()).phone(dto.getPhone())
                .address(dto.getAddress()).business(business).build();
        return toDto(customerRepository.save(customer));
    }

    @Transactional
    public CustomerDto update(Long id, CustomerDto dto, Long businessId) {
        Customer customer = customerRepository.findByIdAndBusinessId(id, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        customer.setName(dto.getName()); customer.setEmail(dto.getEmail());
        customer.setPhone(dto.getPhone()); customer.setAddress(dto.getAddress());
        return toDto(customerRepository.save(customer));
    }

    @Transactional
    public void delete(Long id, Long businessId) {
        Customer customer = customerRepository.findByIdAndBusinessId(id, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        customerRepository.delete(customer);
    }

    private CustomerDto toDto(Customer e) {
        CustomerDto d = new CustomerDto(); d.setId(e.getId()); d.setName(e.getName());
        d.setEmail(e.getEmail()); d.setPhone(e.getPhone()); d.setAddress(e.getAddress());
        return d;
    }
}
