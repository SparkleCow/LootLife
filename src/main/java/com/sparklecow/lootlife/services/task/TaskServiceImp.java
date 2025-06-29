package com.sparklecow.lootlife.services.task;

import com.sparklecow.lootlife.entities.Task;
import com.sparklecow.lootlife.entities.User;
import com.sparklecow.lootlife.models.task.TaskDifficulty;
import com.sparklecow.lootlife.models.task.TaskRequestDto;
import com.sparklecow.lootlife.models.task.TaskResponseDto;
import com.sparklecow.lootlife.models.task.TaskUpdateDto;
import com.sparklecow.lootlife.repositories.TaskRepository;
import com.sparklecow.lootlife.services.mappers.TaskMapper;
import com.sparklecow.lootlife.services.stats.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImp implements TaskService {

    private final StatsService statsService;
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Override
    @Transactional
    public TaskResponseDto createTask(User user, TaskRequestDto taskRequestDto) {
        Task task = taskMapper.toEntity(taskRequestDto, user);
        user.getTasks().add(task);
        taskRepository.save(task);
        return taskMapper.toResponseDto(task);
    }

    @Override
    public List<TaskResponseDto> findTaskByUser(User user) {
        return taskRepository.findByUser(user).stream().map(taskMapper::toResponseDto).toList();
    }

    @Override
    public List<TaskResponseDto> findTaskByDifficulty(User user, TaskDifficulty taskDifficulty) {
        return taskRepository.findByTaskDifficultyAndUser(taskDifficulty, user).stream().map(taskMapper::toResponseDto).toList();
    }

    @Override
    @Transactional
    public TaskResponseDto updateTask(User user, Long id, TaskUpdateDto taskUpdateDto) {
        Task taskOpt = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("This task does not exist"));

        if (!taskOpt.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("This user is not the owner of this task");
        }

        Task task = taskMapper.updateEntity(taskOpt, taskUpdateDto);
        return taskMapper.toResponseDto(taskRepository.save(task));
    }

    //TODO Implementar stats y subida de lvl
    @Override
    @Transactional
    public void completeTask(User user, Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("This task does not exist"));

        if (!task.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("This user is not the owner of this task");
        }

        task.setIsActive(false);
        task.setIsExpired(false);

        if (task.getProgressRequired() != null) {
            task.setCurrentProgress(task.getProgressRequired());
        }

        taskRepository.save(task);
    }

    @Override
    @Transactional
    public void deleteTask(User user, Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("This task does not exist"));

        if (!task.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("This user is not the owner of this task");
        }

        taskRepository.delete(task);
    }
}
