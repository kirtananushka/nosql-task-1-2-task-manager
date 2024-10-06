package com.tananushka.taskmanager.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Document(collection = "tasks")
@Getter
@Setter
public class Task {
   @Id
   private String id;

   private int taskId;
   private LocalDate creationDate;
   private LocalDate deadline;
   private String name;

   @TextIndexed
   private String description;
   private List<Subtask> subtasks;
   private String category;

   @Override
   public String toString() {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
      return "Task {" +
            "Task ID=" + taskId +
            ", ID='" + id + '\'' +
            ", Name='" + name + '\'' +
            ", Description='" + description + '\'' +
            ", Creation Date='" + (creationDate != null ? creationDate.format(formatter) : "N/A") + '\'' +
            ", Deadline='" + (deadline != null ? deadline.format(formatter) : "N/A") + '\'' +
            ", Category='" + category + '\'' +
            ", Subtasks=" + (subtasks != null ? subtasks.stream()
            .map(Subtask::toString)
            .collect(Collectors.joining(", ")) : "None") +
            '}';
   }
}