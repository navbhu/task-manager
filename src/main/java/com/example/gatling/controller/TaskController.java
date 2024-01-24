package com.example.gatling.controller;

import com.example.gatling.dto.CreateTask;
import com.example.gatling.dto.TaskDTO;
import com.example.gatling.dto.UpdateTask;
import com.example.gatling.service.TaskService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/task")
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<String> createTask(@Valid @RequestBody CreateTask createTask) {
        taskService.createTask(createTask);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{taskId}")
    public ResponseEntity<Void> updateTask(@PathVariable UUID taskId, @Valid @RequestBody UpdateTask updateTask) {
        taskService.updateTask(taskId, updateTask);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<TaskDTO>> getTasks() {
        return ResponseEntity.ok(taskService.getTasks());
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteTasks(@NotEmpty @RequestBody List<UUID> taskIds) {
        taskService.deleteTask(taskIds);
        return ResponseEntity.ok().build();
    }
}
