package com.sparklecow.lootlife.services.task;

import com.sparklecow.lootlife.entities.Stats;
import com.sparklecow.lootlife.entities.Task;
import com.sparklecow.lootlife.entities.User;
import com.sparklecow.lootlife.models.stats.StatType;
import com.sparklecow.lootlife.models.task.TaskDifficulty;
import com.sparklecow.lootlife.models.task.TaskRequestDto;
import com.sparklecow.lootlife.models.task.TaskResponseDto;
import com.sparklecow.lootlife.models.task.TaskUpdateDto;
import com.sparklecow.lootlife.repositories.StatsRepository;
import com.sparklecow.lootlife.repositories.TaskRepository;
import com.sparklecow.lootlife.repositories.UserRepository;
import com.sparklecow.lootlife.services.mappers.TaskMapper;
import com.sparklecow.lootlife.services.stats.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskServiceImp implements TaskService {

    private final StatsService statsService;
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final UserRepository userRepository;
    private final StatsRepository statsRepository;

    @Override
    @Transactional
    public TaskResponseDto createTask(User user, TaskRequestDto taskRequestDto) {
        User persistentUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Task task = taskMapper.toEntity(taskRequestDto, persistentUser);
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
    public TaskResponseDto findTaskByUserAndId(User user, Long id) {
        User persistentUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Optional<Task> task = persistentUser.getTasks().stream().filter(x -> x.getId().equals(id)).findFirst();
        return task.map(taskMapper::toResponseDto).orElseThrow(() -> new RuntimeException("Task not found"));
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

    @Override
    @Transactional
    public void completeTask(User user, Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("This task does not exist"));

        // Load a fresh user from the DB
        User userOpt = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User with id: " + user.getId() + " not found"));

        if (!task.getUser().getId().equals(userOpt.getId())) {
            throw new RuntimeException("This user is not the owner of this task");
        }

        Stats userStats = userOpt.getStats();

        task.setIsActive(false);
        task.setIsExpired(false);

        if (task.getProgressRequired() != null) {
            task.setCurrentProgress(task.getProgressRequired());
        }

        for (StatType statType : task.getStatsCategories()) {
            statsService.addExperienceToSingleStat(userStats, statType, task.getXpReward());
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
