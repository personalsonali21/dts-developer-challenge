package uk.gov.hmcts.reform.dev.controllers;

import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import uk.gov.hmcts.reform.dev.exception.BadRequestException;
import uk.gov.hmcts.reform.dev.exception.TaskNotFoundException;
import uk.gov.hmcts.reform.dev.models.TaskDetails;
import uk.gov.hmcts.reform.dev.service.CaseService;

import java.util.List;

@RestController
@RequestMapping("tasks")
public class CaseController {
	
	private static final Logger logger = LoggerFactory.getLogger(CaseController.class);

	@Autowired
	CaseService service;
	
	/**
     * Retrieves a task by its ID.
     *
     * @param id the task identifier
     * @return the task details
     * @throws TaskNotFoundException if task does not exist
	 * @throws BadRequestException 
     */
	@GetMapping("/{id}")
	public TaskDetails getTaskById(@PathVariable("id") Long id) throws TaskNotFoundException, BadRequestException {
		return service.fetchTaskById(id);
	}

	@GetMapping
	public List<TaskDetails> getTasks() throws TaskNotFoundException {
		return service.fetchAllTasks();
	}
	
	@PostMapping
	public ResponseEntity<String>  createTasks(@RequestParam("title") String title, @RequestParam("description") String description, 
			@RequestParam("status") String status, @RequestParam("dueDate") String dueDate) throws TaskNotFoundException {
		service.create(title, description, status, dueDate);
		return ResponseEntity.ok("Task created successfully");
	}

	@PutMapping("/{id}")
	public ResponseEntity<String> updateTaskStatus(@PathVariable("id") Long id, @RequestParam("status") String status) {

		String sanitizedStatus = StringEscapeUtils.escapeJava(status);
		
		service.updateStatus(id, sanitizedStatus);
		return ResponseEntity.ok("Status updated successfully");
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteTask(@PathVariable("id") Long id) {
		service.deleteTaskById(id);
		return ResponseEntity.ok("Task deleted successfully");
	}
}
