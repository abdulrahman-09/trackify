package com.mujahid.trackify.domain.dto;

import com.mujahid.trackify.domain.dto.request.*;
import com.mujahid.trackify.domain.enums.TaskPriority;
import com.mujahid.trackify.domain.enums.TaskStatus;
import com.mujahid.trackify.security.dto.request.LoginRequest;
import com.mujahid.trackify.security.dto.request.RegisterRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class DtoValidationTests {

    @Autowired
    private Validator validator;

    // TaskRequest Tests
    @Test
    void taskRequest_shouldBeValidWithCorrectData() {
        TaskRequest request = new TaskRequest("Task Title", "Task Description", LocalDateTime.now().plusDays(1), TaskPriority.HIGH);
        Set<ConstraintViolation<TaskRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @Test
    void taskRequest_shouldFailValidationWithBlankTitle() {
        TaskRequest request = new TaskRequest("", "Task Description", LocalDateTime.now().plusDays(1), TaskPriority.HIGH);
        Set<ConstraintViolation<TaskRequest>> violations = validator.validate(request);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("title"));
    }

    @Test
    void taskRequest_shouldFailValidationWithNullTitle() {
        TaskRequest request = new TaskRequest(null, "Task Description", LocalDateTime.now().plusDays(1), TaskPriority.HIGH);
        Set<ConstraintViolation<TaskRequest>> violations = validator.validate(request);
        assertThat(violations).isNotEmpty();
    }

    @Test
    void taskRequest_shouldFailValidationWithNullDueDate() {
        TaskRequest request = new TaskRequest("Task Title", "Description", null, TaskPriority.HIGH);
        Set<ConstraintViolation<TaskRequest>> violations = validator.validate(request);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("dueDate"));
    }

    @Test
    void taskRequest_shouldFailValidationWithPastDueDate() {
        TaskRequest request = new TaskRequest("Task Title", "Description", LocalDateTime.now().minusDays(1), TaskPriority.HIGH);
        Set<ConstraintViolation<TaskRequest>> violations = validator.validate(request);
        assertThat(violations).isNotEmpty();
    }

    @Test
    void taskRequest_shouldFailValidationWithNullPriority() {
        TaskRequest request = new TaskRequest("Task Title", "Description", LocalDateTime.now().plusDays(1), null);
        Set<ConstraintViolation<TaskRequest>> violations = validator.validate(request);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("taskPriority"));
    }

    // TaskListRequest Tests
    @Test
    void taskListRequest_shouldBeValidWithCorrectData() {
        TaskListRequest request = new TaskListRequest("My Task List", "List Description");
        Set<ConstraintViolation<TaskListRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @Test
    void taskListRequest_shouldFailValidationWithBlankTitle() {
        TaskListRequest request = new TaskListRequest("", "Description");
        Set<ConstraintViolation<TaskListRequest>> violations = validator.validate(request);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("title"));
    }

    @Test
    void taskListRequest_shouldFailValidationWithNullTitle() {
        TaskListRequest request = new TaskListRequest(null, "Description");
        Set<ConstraintViolation<TaskListRequest>> violations = validator.validate(request);
        assertThat(violations).isNotEmpty();
    }

    // UserUpdateRequest Tests
    @Test
    void userUpdateRequest_shouldBeValidWithCorrectData() {
        UserUpdateRequest request = new UserUpdateRequest("John", "Doe");
        Set<ConstraintViolation<UserUpdateRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @Test
    void userUpdateRequest_shouldFailValidationWithBlankFirstName() {
        UserUpdateRequest request = new UserUpdateRequest("", "Doe");
        Set<ConstraintViolation<UserUpdateRequest>> violations = validator.validate(request);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("firstName"));
    }

    @Test
    void userUpdateRequest_shouldFailValidationWithBlankLastName() {
        UserUpdateRequest request = new UserUpdateRequest("John", "");
        Set<ConstraintViolation<UserUpdateRequest>> violations = validator.validate(request);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("lastName"));
    }

    @Test
    void userUpdateRequest_shouldFailValidationWithNullFirstName() {
        UserUpdateRequest request = new UserUpdateRequest(null, "Doe");
        Set<ConstraintViolation<UserUpdateRequest>> violations = validator.validate(request);
        assertThat(violations).isNotEmpty();
    }

    // TaskPriorityRequest Tests
    @Test
    void taskPriorityRequest_shouldBeValidWithCorrectData() {
        TaskPriorityRequest request = new TaskPriorityRequest(TaskPriority.HIGH);
        Set<ConstraintViolation<TaskPriorityRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @Test
    void taskPriorityRequest_shouldFailValidationWithNullPriority() {
        TaskPriorityRequest request = new TaskPriorityRequest(null);
        Set<ConstraintViolation<TaskPriorityRequest>> violations = validator.validate(request);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("priority"));
    }

    // TaskStatusRequest Tests
    @Test
    void taskStatusRequest_shouldBeValidWithCorrectData() {
        TaskStatusRequest request = new TaskStatusRequest(TaskStatus.IN_PROGRESS);
        Set<ConstraintViolation<TaskStatusRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @Test
    void taskStatusRequest_shouldFailValidationWithNullStatus() {
        TaskStatusRequest request = new TaskStatusRequest(null);
        Set<ConstraintViolation<TaskStatusRequest>> violations = validator.validate(request);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("status"));
    }

    // LoginRequest Tests
    @Test
    void loginRequest_shouldBeValidWithCorrectData() {
        LoginRequest request = new LoginRequest("user@example.com", "password123");
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @Test
    void loginRequest_shouldFailValidationWithInvalidEmail() {
        LoginRequest request = new LoginRequest("invalid-email", "password123");
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("email"));
    }

    @Test
    void loginRequest_shouldFailValidationWithBlankPassword() {
        LoginRequest request = new LoginRequest("user@example.com", "");
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("password"));
    }

    // RegisterRequest Tests
    @Test
    void registerRequest_shouldBeValidWithCorrectData() {
        RegisterRequest request = new RegisterRequest("John", "Doe", "user@example.com", "password123");
        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @Test
    void registerRequest_shouldFailValidationWithInvalidEmail() {
        RegisterRequest request = new RegisterRequest("John", "Doe", "invalid-email", "password123");
        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("email"));
    }

    @Test
    void registerRequest_shouldFailValidationWithShortPassword() {
        RegisterRequest request = new RegisterRequest("John", "Doe", "user@example.com", "123");
        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("password"));
    }

    @Test
    void registerRequest_shouldFailValidationWithBlankFirstName() {
        RegisterRequest request = new RegisterRequest("", "Doe", "user@example.com", "password123");
        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("firstName"));
    }

    @Test
    void registerRequest_shouldFailValidationWithBlankLastName() {
        RegisterRequest request = new RegisterRequest("John", "", "user@example.com", "password123");
        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("lastName"));
    }
}
