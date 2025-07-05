package com.sparklecow.lootlife.controllers;

import com.sparklecow.lootlife.entities.User;
import com.sparklecow.lootlife.models.stats.StatsResponseDto;
import com.sparklecow.lootlife.services.stats.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stat")
@CrossOrigin("*")
@RequiredArgsConstructor
public class StatsController {
    private final StatsService statsService;

    @GetMapping
    public ResponseEntity<StatsResponseDto> findStatByUser(Authentication authentication){
        return ResponseEntity.ok(statsService.findStatsByUser((User) authentication.getPrincipal()));
    }
}
