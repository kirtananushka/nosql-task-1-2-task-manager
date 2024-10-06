package com.tananushka.taskmanager.repository;

import com.tananushka.taskmanager.model.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TaskRepository extends MongoRepository<Task, String> {

   List<Task> findByDeadlineBefore(LocalDate today);

   List<Task> findByCategory(String category);

   Optional<Task> findByTaskId(int taskId);

   void deleteByTaskId(int taskId);

   @Query("{ $text: { $search: ?0 } }")
   List<Task> searchByDescription(String text);

   @Query("{ 'subtasks.name': { $regex: ?0, $options: 'i' } }")
   List<Task> searchBySubtaskName(String text);
}
