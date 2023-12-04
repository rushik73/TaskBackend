package com.example.Task.Task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    private static final Logger log = LoggerFactory.getLogger(TaskController.class);
    private final TaskRepository taskRepository;

    @Autowired
    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @GetMapping()
    public String showTaskList(Model model) {
        log.info("Showing task list");
        model.addAttribute("tasks", taskRepository.findAll());
        return "To-DoList";
    }

    @GetMapping("/new")
    public String showNewTaskForm(Model model) {
        log.info("Showing new task form");
        model.addAttribute("task", new Task());
        return "taskentry";
    }

    @GetMapping("/edit/{id}")
    public String showEditTaskForm(@PathVariable Long id, Model model) {
        log.info("Editing task with ID: {}", id);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid task Id:" + id));
        model.addAttribute("task", task);
        return "taskentry";
    }

    @PostMapping("/new")
    public String createTask(@Valid @ModelAttribute Task task,BindingResult result) {
        if (result.hasErrors()) {
            result.getAllErrors().forEach(error -> log.error(error.toString()));
            return "taskentry";
        }
        taskRepository.save(task);
        log.info("Created new task with ID: {}", task.getId());
        return "redirect:/tasks";
    }

    @PostMapping("/edit/{id}")
    public String updateTask(@PathVariable Long id, @Valid @ModelAttribute Task task, BindingResult result) {
        if (result.hasErrors()) {
            result.getAllErrors().forEach(error -> log.error(error.toString()));
            return "taskentry";
        }
        task.setId(id);
        taskRepository.save(task);
        log.info("Updated task with ID: {}", task.getId());
        return "redirect:/tasks";
    }

    @PostMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskRepository.deleteById(id);
        log.info("Deleted task with ID: {}", id);
        return "redirect:/tasks";
    }
}
