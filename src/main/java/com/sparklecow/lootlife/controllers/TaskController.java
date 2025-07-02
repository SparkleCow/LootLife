package com.sparklecow.lootlife.controllers;

import com.sparklecow.lootlife.entities.User;
import com.sparklecow.lootlife.models.task.TaskDifficulty;
import com.sparklecow.lootlife.models.task.TaskRequestDto;
import com.sparklecow.lootlife.models.task.TaskResponseDto;
import com.sparklecow.lootlife.services.task.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/task")
@CrossOrigin("*")
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskResponseDto> createTask(Authentication authentication, @RequestBody @Valid TaskRequestDto taskRequestDto){
        return ResponseEntity.ok(taskService.createTask((User) authentication.getPrincipal(), taskRequestDto));
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDto>> findTaskByUser(Authentication authentication,
                                                                @RequestParam(value = "difficulty", required = false) TaskDifficulty taskDifficulty){
        if(taskDifficulty != null){
            return ResponseEntity.ok(taskService.findTaskByDifficulty((User) authentication.getPrincipal(), taskDifficulty));
        }
        return ResponseEntity.ok(taskService.findTaskByUser((User) authentication.getPrincipal()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> findTaskByUserAndId(Authentication authentication,
                                                                     @PathVariable Long id){
        return ResponseEntity.ok(taskService.findTaskByUserAndId((User) authentication.getPrincipal(), id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(Authentication authentication, @PathVariable Long id){
        taskService.deleteTask((User) authentication.getPrincipal(), id );
        return ResponseEntity.noContent().build();
    }
}
