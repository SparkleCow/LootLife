package com.sparklecow.lootlife.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparklecow.lootlife.entities.Mission;
import com.sparklecow.lootlife.entities.User;
import com.sparklecow.lootlife.services.mission.MissionServiceImp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mission")
@Slf4j
public class MissionController {

    private final MissionServiceImp missionService;

    @PostMapping
    public ResponseEntity<Mission> generateNewMission(Authentication authentication) throws JsonProcessingException {
        return ResponseEntity.ok(missionService.createMission((User) authentication.getPrincipal()));
    }

    @GetMapping
    public ResponseEntity<Mission> getCurrentMission(Authentication authentication) throws JsonProcessingException {
        return ResponseEntity.ok(missionService.createMission((User) authentication.getPrincipal()));
    }

    @DeleteMapping
    public ResponseEntity<Void> cancelAllMissions(Authentication authentication){
        missionService.cancelAllUserMissions((User) authentication.getPrincipal(), "Prueba");
        return ResponseEntity.ok().build();
    }
}
