# Kaiburr_Task-1
Java backend and REST API example

#The repository includes screenshots demonstrating API functionality.
Inside the folder: Task 1/Screenshots

#Source code: Task 1/task-manager/src/main/java/com/example/task_manager

#Tech Stack:
  -Java 21
  -Spring Boot
  -Maven
  -MangoDB
  -Postman (for Testing)

#REST API Endpoints:

--GET /tasks → Retrieve all tasks or a specific task by ID.

--POST or PUT /tasks → Create or update a task.

--DELETE /tasks/{id} → Delete a task by ID.

--GET /tasks/find?name=xyz → Search tasks by name.

--PUT /tasks/{id}/execute → Run the task's shell command in the terminal and store execution details.

#Task Execution Management:

Each task has an ID, name, owner, command, and execution history.

When a task runs, a new TaskExecution entry is recorded with start time, end time, and output.

#MongoDB Integration:

Tasks and their executions are stored in MongoDB.

Uses Spring Data MongoDB for database operations.

#Security and Validation:

Application validates the command provided in the request - if the command contain unsafe/malicious code, it will return { error: Unsafe Command Detected}

This backend is tested using Postman

