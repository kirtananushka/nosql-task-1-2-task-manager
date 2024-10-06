package com.tananushka.taskmanager.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.TextIndexed;

@Getter
@Setter
public class Subtask {
   private String name;

   @TextIndexed
   private String description;

   @Override
   public String toString() {
      return "{ Name='" + name + "', Description='" + description + "' }";
   }
}
