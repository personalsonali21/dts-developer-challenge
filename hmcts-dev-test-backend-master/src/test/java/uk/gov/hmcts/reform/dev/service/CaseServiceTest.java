package uk.gov.hmcts.reform.dev.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import uk.gov.hmcts.reform.dev.dao.TaskDetailsDao;
import uk.gov.hmcts.reform.dev.exception.BadRequestException;
import uk.gov.hmcts.reform.dev.exception.TaskNotFoundException;
import uk.gov.hmcts.reform.dev.models.TaskDetails;

class CaseServiceTest {

    @InjectMocks
    private CaseService tasksService;

    @Mock
    private TaskDetailsDao taskDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void fetchTaskById_returnsTask_whenFound() throws TaskNotFoundException, BadRequestException {
        TaskDetails mockTask = new TaskDetails();
        mockTask.setId(1);
        mockTask.setTitle("Test Task");
        mockTask.setStatus("PENDING");
        mockTask.setDueDate(LocalDateTime.now());

        when(taskDao.fetchTaskDetailById(1L)).thenReturn(mockTask);

        TaskDetails task = tasksService.fetchTaskById(1L);

        assertNotNull(task);
        assertEquals(1L, task.getId());
        assertEquals("Test Task", task.getTitle());
        verify(taskDao, times(1)).fetchTaskDetailById(1L);
    }

    @Test
    void fetchTaskById_throwsException_whenNotFound() {
        when(taskDao.fetchTaskDetailById(999L)).thenReturn(null);

        TaskNotFoundException ex = assertThrows(TaskNotFoundException.class, () -> {
            tasksService.fetchTaskById(999L);
        });

        assertEquals("Task with id 999 not found", ex.getMessage());
        verify(taskDao, times(1)).fetchTaskDetailById(999L);
    }

    @Test
    void fetchAllTasks_returnsList_whenFound() throws TaskNotFoundException {
        List<TaskDetails> mockList = new ArrayList<>();
        mockList.add(new TaskDetails());

        when(taskDao.fetchAllTaskDetails()).thenReturn(mockList);

        List<TaskDetails> result = tasksService.fetchAllTasks();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(taskDao, times(1)).fetchAllTaskDetails();
    }

    @Test
    void fetchAllTasks_throwsException_whenNull() {
        when(taskDao.fetchAllTaskDetails()).thenReturn(null);

        TaskNotFoundException ex = assertThrows(TaskNotFoundException.class, () -> {
            tasksService.fetchAllTasks();
        });

        assertEquals("Tasks not found", ex.getMessage());
        verify(taskDao, times(1)).fetchAllTaskDetails();
    }

    @Test
    void updateStatus_callsDao() {
        doNothing().when(taskDao).updateTaskStatus(1L, "DONE");

        tasksService.updateStatus(1L, "DONE");

        verify(taskDao, times(1)).updateTaskStatus(1L, "DONE");
    }

}
