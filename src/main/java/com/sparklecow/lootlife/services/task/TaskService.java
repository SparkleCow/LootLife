package com.sparklecow.lootlife.services.task;

import com.sparklecow.lootlife.entities.Task;
import com.sparklecow.lootlife.entities.User;
import com.sparklecow.lootlife.models.task.TaskDifficulty;
import com.sparklecow.lootlife.models.task.TaskRequestDto;
import com.sparklecow.lootlife.models.task.TaskResponseDto;
import com.sparklecow.lootlife.models.task.TaskUpdateDto;

import java.util.List;

public interface TaskService {
    TaskResponseDto createTask(User user, TaskRequestDto taskRequestDto);
    List<TaskResponseDto> findTaskByUser(User user);
    List<TaskResponseDto> findTaskByDifficulty(User user, TaskDifficulty taskDifficulty);
    TaskResponseDto updateTask(User user, Long id, TaskUpdateDto taskUpdateDto);
    void completeTask(User user, Long id);
    void deleteTask(User user, Long id);
}
