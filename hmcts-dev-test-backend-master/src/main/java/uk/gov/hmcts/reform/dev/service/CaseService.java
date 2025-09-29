package uk.gov.hmcts.reform.dev.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import uk.gov.hmcts.reform.dev.dao.TaskDetailsDao;
import uk.gov.hmcts.reform.dev.exception.BadRequestException;
import uk.gov.hmcts.reform.dev.exception.TaskNotFoundException;
import uk.gov.hmcts.reform.dev.models.TaskDetails;

@Service
public class CaseService {
	private static final Logger logger = LoggerFactory.getLogger(CaseService.class);

	@Autowired
	TaskDetailsDao taskDao;

	/**
	 * Finds a task by ID.
	 * Throws an exception if the task does not exist.
	 *
	 * @param id the task ID
	 * @return the task
	 * @throws BadRequestException, TaskNotFoundException 
	 */
	public TaskDetails fetchTaskById(Long id) throws TaskNotFoundException, BadRequestException, TaskNotFoundException {
		logger.info("fetchTaskById {}",id);
		if(id <= 0) {
			throw new BadRequestException("Invalid Id");
		}
		TaskDetails task = taskDao.fetchTaskDetailById(id);
		if(task != null) {
			return task;
		} else {
			throw new TaskNotFoundException("Task with id " + id + " not found");
		}	
	}

	public List<TaskDetails> fetchAllTasks() throws TaskNotFoundException {
		List<TaskDetails> cases = new ArrayList<>();

		cases = taskDao.fetchAllTaskDetails();
		
		if(cases != null) {
			return cases;
		} else {
			throw new TaskNotFoundException("Tasks not found");
		}
	}
	public void updateStatus(Long id, String status) {
		taskDao.updateTaskStatus(id, status);
	}
	public void deleteTaskById(Long id) {
		taskDao.deleteTaskById(id);
	}

	public int create(String title, String description, String status, String dueDate) {
		return taskDao.createTaskDetails(title, description, status, dueDate);
	}
}
