package com.sparklecow.lootlife.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparklecow.lootlife.entities.Mission;
import com.sparklecow.lootlife.entities.User;
import com.sparklecow.lootlife.models.mission.MissionResponseDto;
import com.sparklecow.lootlife.services.mission.MissionServiceImp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mission")
@Slf4j
public class MissionController {

    private final MissionServiceImp missionService;

    @PostMapping
    public ResponseEntity<MissionResponseDto> generateNewMission(Authentication authentication) throws JsonProcessingException {
        MissionResponseDto responseDto =  missionService.createMission((User) authentication.getPrincipal());
        URI location = URI.create("/missions/" + responseDto.id());
        return ResponseEntity
                .created(location)
                .body(responseDto);
    }

    @PostMapping("/complete/{id}")
    public ResponseEntity<MissionResponseDto> completeMission(Authentication authentication, @PathVariable Long id){
        return ResponseEntity.ok(missionService.completeMission((User) authentication.getPrincipal(), id));
    }

    @PostMapping("/activate/{id}")
    public ResponseEntity<MissionResponseDto> activateMission(Authentication authentication, @PathVariable Long id){
        return ResponseEntity.ok(missionService.startMission(id));
    }

    @GetMapping
    public ResponseEntity<List<MissionResponseDto>> getCurrentMission(Authentication authentication) {
        return ResponseEntity.ok(missionService.getActiveMissions((User) authentication.getPrincipal()));
    }

    @GetMapping("/all")
    public ResponseEntity<List<MissionResponseDto>> getAllMission(Authentication authentication) {
        return ResponseEntity.ok(missionService.getUserMissions((User) authentication.getPrincipal()));
    }

    @DeleteMapping
    public ResponseEntity<Void> cancelAllMissions(Authentication authentication){
        missionService.cancelAllUserMissions((User) authentication.getPrincipal(), "Prueba");
        return ResponseEntity.ok().build();
    }
}
