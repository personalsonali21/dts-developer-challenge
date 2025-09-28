package uk.gov.hmcts.reform.dev.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import uk.gov.hmcts.reform.dev.models.TaskDetails;

@JdbcTest  
@Import(TaskDetailsDaoImpl.class) 
class TaskDetailsDaoImplTest {

    @Autowired
    private TaskDetailsDao taskDetailsDao;

    @Test
    void fetchTaskDetailById_existingId_shouldReturnTask() {
        TaskDetails task = taskDetailsDao.fetchTaskDetailById(3L);
        assertThat(task).isNotNull();
        assertThat(task.getId()).isEqualTo(3);
        assertThat(task.getTitle()).isNotBlank();
    }

    
    @Test
    void fetchAllTaskDetails_shouldReturnTasks() {
        List<TaskDetails> tasks = taskDetailsDao.fetchAllTaskDetails();
        assertThat(tasks).isNotEmpty();
        assertThat(tasks.get(0).getTitle()).isNotBlank();
    }

    @Test
    void fetchTaskDetailById_nonExistingId_shouldReturnNull() {
        TaskDetails task = taskDetailsDao.fetchTaskDetailById(999L);
        assertThat(task).isNull();
    }

    @Test
    void deleteTaskById_existingId_shouldDeleteAndReturnCount() {
        int deletedCount = taskDetailsDao.deleteTaskById(1L);
        assertThat(deletedCount).isEqualTo(1);
        // Confirm deletion
        TaskDetails task = taskDetailsDao.fetchTaskDetailById(1L);
        assertThat(task).isNull();
    }

    @Test
    void updateTaskStatus_existingId_shouldUpdate() {
        taskDetailsDao.updateTaskStatus(2L, "COMPLETED");
        TaskDetails updatedTask = taskDetailsDao.fetchTaskDetailById(2L);
        assertThat(updatedTask.getStatus()).isEqualTo("COMPLETED");
    }

}
