package uk.gov.hmcts.reform.dev.models;

import java.time.LocalDateTime;

public class TaskDetails {

    private int id;
    private String caseNumber;
    private String title;
    private String description;
    private String status;
    private LocalDateTime dueDate;
    
    public TaskDetails() {
    	
    }
    
	public TaskDetails(int id, String caseNumber, String title, String description, String status,
			LocalDateTime createdDate) {
		super();
		this.id = id;
		this.caseNumber = caseNumber;
		this.title = title;
		this.description = description;
		this.status = status;
		this.dueDate = createdDate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCaseNumber() {
		return caseNumber;
	}

	public void setCaseNumber(String caseNumber) {
		this.caseNumber = caseNumber;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDateTime getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDateTime createdDate) {
		this.dueDate = createdDate;
	}
    
    
}
