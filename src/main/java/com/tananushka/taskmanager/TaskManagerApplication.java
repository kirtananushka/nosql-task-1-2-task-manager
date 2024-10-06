package com.tananushka.taskmanager;

import com.tananushka.taskmanager.model.Subtask;
import com.tananushka.taskmanager.model.Task;
import com.tananushka.taskmanager.service.TaskService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@SpringBootApplication
public class TaskManagerApplication implements CommandLineRunner {

   private final TaskService taskService;
   private final Scanner scanner = new Scanner(System.in);

   public TaskManagerApplication(TaskService taskService) {
      this.taskService = taskService;
   }

   public static void main(String[] args) {
      SpringApplication.run(TaskManagerApplication.class, args);
   }

   @Override
   public void run(String... args) {
      boolean running = true;
      while (running) {
         System.out.println("\nTask Manager:");
         System.out.println("1. Display all tasks");
         System.out.println("2. Display overdue tasks");
         System.out.println("3. Display tasks by category");
         System.out.println("4. Display subtasks by category");
         System.out.println("5. Add a new task");
         System.out.println("6. Update a task");
         System.out.println("7. Delete a task");
         System.out.println("8. Delete all subtasks of a task");
         System.out.println("9. Search tasks by description");
         System.out.println("10. Search tasks by subtask name");
         System.out.println("0. Exit");

         int choice = scanner.nextInt();
         scanner.nextLine();

         switch (choice) {
            case 1 -> displayAllTasks();
            case 2 -> displayOverdueTasks();
            case 3 -> displayTasksByCategory();
            case 4 -> displaySubtasksByCategory();
            case 5 -> addNewTask();
            case 6 -> updateTask();
            case 7 -> deleteTask();
            case 8 -> deleteAllSubtasks();
            case 9 -> searchTasksByDescription();
            case 10 -> searchTasksBySubtaskName();
            case 0 -> running = false;
            default -> System.out.println("Invalid choice. Try again.");
         }
      }
      scanner.close();
   }

   private void displayAllTasks() {
      taskService.getAllTasks().forEach(System.out::println);
   }

   private void displayOverdueTasks() {
      taskService.getOverdueTasks().forEach(System.out::println);
   }

   private void displayTasksByCategory() {
      System.out.print("Enter category: ");
      String category = scanner.nextLine();
      taskService.getTasksByCategory(category).forEach(System.out::println);
   }

   private void displaySubtasksByCategory() {
      System.out.print("Enter category: ");
      String category = scanner.nextLine();
      taskService.getSubtasksByCategory(category).forEach(System.out::println);
   }

   private void addNewTask() {
      System.out.print("Enter task name: ");
      String name = scanner.nextLine();

      System.out.print("Enter description: ");
      String description = scanner.nextLine();

      System.out.print("Enter deadline (yyyy-MM-dd) (leave blank for tomorrow): ");
      String deadlineInput = scanner.nextLine();
      LocalDate deadline = deadlineInput.isEmpty() ? LocalDate.now().plusDays(1) : LocalDate.parse(deadlineInput);

      System.out.print("Enter category: ");
      String category = scanner.nextLine();

      Task newTask = new Task();
      newTask.setName(name);
      newTask.setDescription(description);
      newTask.setDeadline(deadline);
      newTask.setCategory(category);

      List<Subtask> subtasks = new ArrayList<>();
      System.out.print("Do you want to add subtasks? (y/n): ");
      String addSubtask = scanner.nextLine();
      while (addSubtask.equalsIgnoreCase("y")) {
         System.out.print("Enter subtask name: ");
         String subtaskName = scanner.nextLine();
         System.out.print("Enter subtask description: ");
         String subtaskDescription = scanner.nextLine();

         Subtask subtask = new Subtask();
         subtask.setName(subtaskName);
         subtask.setDescription(subtaskDescription);
         subtasks.add(subtask);

         System.out.print("Do you want to add another subtask? (y/n): ");
         addSubtask = scanner.nextLine();
      }
      newTask.setSubtasks(subtasks);

      taskService.insertTask(newTask).ifPresentOrElse(
            task -> System.out.println("Task added: " + task),
            () -> System.out.println("Failed to add task.")
      );
   }

   private void updateTask() {
      System.out.print("Enter Task ID to update: ");
      int taskId = scanner.nextInt();
      scanner.nextLine();

      Optional<Task> optionalTask = taskService.getTaskByTaskId(taskId);
      if (optionalTask.isPresent()) {
         Task existingTask = optionalTask.get();

         System.out.print("Enter new task name (leave blank to keep [" + existingTask.getName() + "]): ");
         String name = scanner.nextLine();
         name = name.isEmpty() ? existingTask.getName() : name;

         System.out.print("Enter new description (leave blank to keep [" + existingTask.getDescription() + "]): ");
         String description = scanner.nextLine();
         description = description.isEmpty() ? existingTask.getDescription() : description;

         System.out.print("Enter new deadline (yyyy-MM-dd) (leave blank to keep [" + existingTask.getDeadline() + "]): ");
         String deadlineInput = scanner.nextLine();
         LocalDate deadline = deadlineInput.isEmpty() ? existingTask.getDeadline() : LocalDate.parse(deadlineInput);

         System.out.print("Enter new category (leave blank to keep [" + existingTask.getCategory() + "]): ");
         String category = scanner.nextLine();
         category = category.isEmpty() ? existingTask.getCategory() : category;

         Task updatedTask = new Task();
         updatedTask.setTaskId(taskId);
         updatedTask.setName(name);
         updatedTask.setDescription(description);
         updatedTask.setDeadline(deadline);
         updatedTask.setCategory(category);
         updatedTask.setCreationDate(existingTask.getCreationDate());
         updatedTask.setSubtasks(existingTask.getSubtasks());

         taskService.updateTask(taskId, updatedTask).ifPresentOrElse(
               task -> System.out.println("Task updated: " + task),
               () -> System.out.println("Task not found.")
         );
      } else {
         System.out.println("Task with ID " + taskId + " not found.");
      }
   }

   private void deleteTask() {
      System.out.print("Enter Task ID to delete: ");
      int taskId = scanner.nextInt();
      scanner.nextLine();
      taskService.deleteTask(taskId);
      System.out.println("Task deleted.");
   }

   private void deleteAllSubtasks() {
      System.out.print("Enter Task ID to delete all subtasks: ");
      int taskId = scanner.nextInt();
      scanner.nextLine();
      taskService.deleteAllSubtasks(taskId);
      System.out.println("All subtasks deleted for task with Task ID: " + taskId);
   }

   private void searchTasksByDescription() {
      System.out.print("Enter keyword to search in task descriptions: ");
      String keyword = scanner.nextLine();
      taskService.searchTasksByDescription(keyword).forEach(System.out::println);
   }

   private void searchTasksBySubtaskName() {
      System.out.print("Enter keyword to search in subtask names: ");
      String subtaskKeyword = scanner.nextLine();
      taskService.searchTasksBySubtaskName(subtaskKeyword).forEach(System.out::println);
   }
}
