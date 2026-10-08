package com.sbm.controller;

import com.sbm.entity.Inventory;
import com.sbm.repository.InventoryRepository;
import com.sbm.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryRepository inventoryRepository;

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAll() {
        Long businessId = SecurityUtils.getCurrentBusinessId();
        List<Map<String, Object>> result = inventoryRepository.findAllByBusinessIdWithProduct(businessId)
                .stream().map(this::toMap).collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/alerts")
    public ResponseEntity<List<Map<String, Object>>> getLowStock() {
        Long businessId = SecurityUtils.getCurrentBusinessId();
        List<Map<String, Object>> alerts = inventoryRepository.findLowStockByBusinessId(businessId)
                .stream().map(this::toMap).collect(Collectors.toList());
        return ResponseEntity.ok(alerts);
    }

    private Map<String, Object> toMap(Inventory inv) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", inv.getId());
        map.put("productId", inv.getProduct().getId());
        map.put("productName", inv.getProduct().getName());
        map.put("currentStock", inv.getCurrentStock());
        map.put("minStockLevel", inv.getMinStockLevel());
        map.put("isLowStock", inv.getCurrentStock() <= inv.getMinStockLevel());
        return map;
    }
}
