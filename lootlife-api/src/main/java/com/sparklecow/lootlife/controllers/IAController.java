package com.sparklecow.lootlife.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparklecow.lootlife.entities.Mission;
import com.sparklecow.lootlife.entities.User;
import com.sparklecow.lootlife.services.ia.IAService;
import com.sparklecow.lootlife.services.mission.MissionService;
import com.sparklecow.lootlife.services.mission.MissionServiceImp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ia")
@Slf4j
public class IAController {

    private final IAService iaService;
    private final MissionServiceImp missionService;

    @PostMapping
    public ResponseEntity<String> test(){
        return ResponseEntity.ok(iaService.sendRequest("String prompt = \"\"\"\n" +
                "Eres un NPC maestro de misiones en un videojuego de gamificación de la vida real. " +
                "Tu trabajo es asignar misiones a los jugadores con un tono épico y motivador. Las misiones deben ser logrables." +
                "Devuélveme únicamente un JSON válido, sin texto adicional ni explicación.\n\n" +
                "\n" +
                "Genera una misión para el siguiente jugador:\n" +
                "\n" +
                "Dificultad: facil\n" +
                "Categorías estadísticas en las que el jugador es débil: fuerza, sabiduria, carisma\n" +
                "Nivel del jugador:3\n" +
                "La misión debe:\n" +
                "\n" +
                "Poder completarse en menos de 24 horas.\n" +
                "Tener una descripción inmersiva y clara, como si fuera parte de una historia.\n" +
                "Incluir una meta cuantificable (targetQuantity) como número entero >= 1.\n" +
                "Devuelve ÚNICAMENTE un JSON con los siguientes campos, sin explicación adicional:\n" +
                "{\n" +
                "\"title\": \"...\",\n" +
                "\"description\": \"...\",\n" +
                "\"targetQuantity\": número entero >= 1,\n" +
                "\"generationReason\": \"...\"\n" +
                "}\n" +
                "\"\"\""));
    }

    @GetMapping
    public ResponseEntity<Mission> test2(Authentication authentication) throws JsonProcessingException {
        return ResponseEntity.ok(missionService.createMission((User) authentication.getPrincipal()));
    }
}
