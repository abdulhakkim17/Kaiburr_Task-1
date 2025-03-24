package com.example.task_manager.service;

import com.example.task_manager.models.Task;
import com.example.task_manager.models.TaskExecution;
import com.example.task_manager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    private static final String[] UNSAFE_COMMANDS = {
        "rm -rf", "shutdown", "reboot", "mkfs", "wget http", "curl http", 
        "dd if=", ">:*", ":(){ :|:& };:", "poweroff"
    };    

    private boolean isSafeCommand(String command) {
        for (String unsafe : UNSAFE_COMMANDS) {
            if (command.toLowerCase().contains(unsafe)) {
                return false;
            }
        }
        return true;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Optional<Task> getTaskById(String id) {
        return taskRepository.findById(id);
    }

    public List<Task> getTasksByName(String name) {
        return taskRepository.findAll().stream()
                .filter(task -> task.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    public Task createTask(Task task) throws IllegalArgumentException {
        if (!isSafeCommand(task.getCommand())) {
            throw new IllegalArgumentException("Unsafe command detected!");
        }
        return taskRepository.save(task);
    }

    public Task updateTask(String id, Task task) throws IllegalArgumentException {
        if (!isSafeCommand(task.getCommand())) {
            throw new IllegalArgumentException("Unsafe command detected!");
        }
        if (taskRepository.existsById(id)) {
            task.setId(id);
            return taskRepository.save(task);
        }
        return null;
    }

    public void deleteTask(String id) {
        taskRepository.deleteById(id);
    }

    public TaskExecution executeTask(String taskId) {
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        if (optionalTask.isEmpty()) {
            throw new RuntimeException("Task not found");
        }

        Task task = optionalTask.get();
        if (!isSafeCommand(task.getCommand())) {
            throw new IllegalArgumentException("Unsafe command detected!");
        }

        TaskExecution execution = new TaskExecution();
        execution.setStartTime(Instant.now()); 

        try {
            ProcessBuilder builder = new ProcessBuilder();
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                builder.command("cmd.exe", "/c", task.getCommand());
            } else {
                builder.command("sh", "-c", task.getCommand());
            }
            builder.redirectErrorStream(true);
            
            Process process = builder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
                System.out.println(line);  // Print output in VS Code terminal
            }

            process.waitFor(); 
            execution.setOutput(output.toString().trim());
        } catch (Exception e) {
            execution.setOutput("Error: " + e.getMessage());
            System.out.println("Error: " + e.getMessage());  // Print error in terminal
        }

        execution.setEndTime(Instant.now()); 

        task.getTaskExecutions().add(execution);
        taskRepository.save(task);

        return execution;
    }
}
