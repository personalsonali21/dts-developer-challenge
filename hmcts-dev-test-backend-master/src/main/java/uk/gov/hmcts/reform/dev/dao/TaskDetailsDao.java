package uk.gov.hmcts.reform.dev.dao;

import java.util.List;

import uk.gov.hmcts.reform.dev.models.TaskDetails;

public interface TaskDetailsDao {

    // Fetch all tasks
    public List<TaskDetails> fetchAllTaskDetails() ;
    
    // Fetch all tasks
    public int createTaskDetails(String title, String description, String status, String dueDate) ;
    
    // Fetch task by ID
    public TaskDetails fetchTaskDetailById(Long id) ;
    
    public int deleteTaskById(Long id);
    
    public void updateTaskStatus(Long id, String status);
}
