package com.tananushka.taskmanager.service;

import com.tananushka.taskmanager.model.Subtask;
import com.tananushka.taskmanager.model.Task;
import com.tananushka.taskmanager.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {
   private final TaskRepository taskRepository;
   private final SequenceGeneratorService sequenceGeneratorService;

   public List<Task> getAllTasks() {
      return taskRepository.findAll();
   }

   public Optional<Task> getTaskByTaskId(int taskId) {
      return taskRepository.findByTaskId(taskId);
   }

   public List<Task> getOverdueTasks() {
      return taskRepository.findByDeadlineBefore(LocalDate.now());
   }

   public List<Task> getTasksByCategory(String category) {
      return taskRepository.findByCategory(category);
   }

   public List<Subtask> getSubtasksByCategory(String category) {
      return getTasksByCategory(category)
            .stream()
            .flatMap(task -> task.getSubtasks().stream())
            .toList();
   }

   public Optional<Task> insertTask(Task task) {
      task.setTaskId(sequenceGeneratorService.generateSequence("task_sequence"));
      task.setCreationDate(LocalDate.now());
      return Optional.of(taskRepository.save(task));
   }

   public Optional<Task> updateTask(int taskId, Task updatedTask) {
      return taskRepository.findByTaskId(taskId).map(existingTask -> {
         updatedTask.setId(existingTask.getId());
         updatedTask.setTaskId(taskId);
         updatedTask.setCreationDate(existingTask.getCreationDate());
         return taskRepository.save(updatedTask);
      });
   }

   public void deleteTask(int taskId) {
      taskRepository.deleteByTaskId(taskId);
   }

   public void deleteAllSubtasks(int taskId) {
      taskRepository.findByTaskId(taskId).ifPresent(task -> {
         task.setSubtasks(null);
         taskRepository.save(task);
      });
   }

   public List<Task> searchTasksByDescription(String keyword) {
      return taskRepository.searchByDescription(keyword);
   }

   public List<Task> searchTasksBySubtaskName(String subtaskName) {
      return taskRepository.searchBySubtaskName(subtaskName);
   }
}
