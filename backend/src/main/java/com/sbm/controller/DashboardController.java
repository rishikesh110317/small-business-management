package com.sbm.controller;

import com.sbm.dto.DashboardDto;
import com.sbm.security.SecurityUtils;
import com.sbm.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<DashboardDto> getDashboard() {
        return ResponseEntity.ok(dashboardService.getDashboard(SecurityUtils.getCurrentBusinessId()));
    }
}
