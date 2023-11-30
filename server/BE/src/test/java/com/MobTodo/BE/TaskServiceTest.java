package com.MobTodo.BE;

import com.MobTodo.BE.Reusable.Function;
import com.MobTodo.BE.models.Task;
import com.MobTodo.BE.service.TaskService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;

import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TaskServiceTest {

    @InjectMocks
    private TaskService taskService;

    @Mock
    private Function function; // Mock for the Function class

    @Test
    void testCreateTask() throws ExecutionException, InterruptedException {
        Task task = new Task();
        task.setStartTime("2023-01-01 10:00:00");
        task.setEndTime("2023-01-01 12:00:00");

        // Mocking the checkDateTimeFormat and distanceDateTime methods
        when(function.checkDateTimeFormat(anyString())).thenReturn(true);
        when(function.distanceDateTime(anyString(), anyString())).thenReturn(3600L);

        // Mocking the postData method
        when(function.postData(any(), anyString())).thenReturn(true);

        assertTrue(taskService.createTask(task));

        // Verify that checkDateTimeFormat and distanceDateTime methods were called
        verify(function, times(2)).checkDateTimeFormat(anyString());
        verify(function, times(1)).distanceDateTime(anyString(), anyString());

        // Verify that postData method was called
        verify(function, times(1)).postData(any(), anyString());
    }

}
