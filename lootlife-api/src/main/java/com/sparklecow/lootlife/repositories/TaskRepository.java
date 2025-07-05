package com.sparklecow.lootlife.repositories;

import com.sparklecow.lootlife.entities.Task;
import com.sparklecow.lootlife.entities.User;
import com.sparklecow.lootlife.models.task.TaskDifficulty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByTaskDifficultyAndUser(TaskDifficulty taskDifficulty, User user);
    List<Task> findByUser(User user);
}
