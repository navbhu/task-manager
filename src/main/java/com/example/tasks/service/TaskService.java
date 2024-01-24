package com.example.tasks.service;

import com.example.tasks.dto.CreateTask;
import com.example.tasks.dto.TaskDTO;
import com.example.tasks.dto.TaskStatus;
import com.example.tasks.dto.UpdateTask;
import com.example.tasks.model.Task;
import com.example.tasks.model.User;
import com.example.tasks.repository.TaskRepository;
import com.example.tasks.repository.UserRepository;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public void createTask(CreateTask createTask){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName;
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
           currentUserName = authentication.getName();
        }
        else {
            throw new UsernameNotFoundException("user doesn't exist");
        }
        User user = userRepository.findByEmail(currentUserName).orElseThrow();
        Task task = new Task(UUID.randomUUID(), user, createTask.name(), TaskStatus.CREATED, Instant.now());
        taskRepository.save(task);
    }

    public void updateTask(UUID taskId, UpdateTask updateTask) {
        Task task = taskRepository.findById(taskId).orElseThrow();
        task.setName(updateTask.name());
        task.setStatus(updateTask.status());
        taskRepository.save(task);
    }

    public void deleteTask(List<UUID> taskIds) {
        taskRepository.deleteAllById(taskIds);
    }

    public List<TaskDTO> getTasks() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName;
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            currentUserName = authentication.getName();
        }
        else {
            throw new UsernameNotFoundException("user doesn't exist");
        }
        User user = userRepository.findByEmail(currentUserName).orElseThrow();
        List<Task> taskList = taskRepository.findAllByUser(user);
        return taskList.stream().map(task -> new TaskDTO(task.getId(), task.getName(), task.getStatus(), task.getCreated())).toList();
    }
}
