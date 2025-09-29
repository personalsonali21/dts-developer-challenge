package uk.gov.hmcts.reform.dev.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.stereotype.Repository;

import uk.gov.hmcts.reform.dev.models.TaskDetails;

@Repository
public class TaskDetailsDaoImpl implements TaskDetailsDao{

	private static final Logger logger = LoggerFactory.getLogger(TaskDetailsDaoImpl.class);
	
    private static NamedParameterJdbcTemplate template;

    // Initialize embedded DB and NamedParameterJdbcTemplate
    static {
    	logger.info("Loaded Database");
        try {
            EmbeddedDatabase db = new EmbeddedDatabaseBuilder()
                    .setType(EmbeddedDatabaseType.H2)
                    .addScript("schema.sql")
                    .addScript("data.sql")
                    .build();

            template = new NamedParameterJdbcTemplate(db);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // RowMapper for TaskDetails
    private static final RowMapper<TaskDetails> TASK_DETAILS_ROW_MAPPER = new RowMapper<>() {
        @Override
        public TaskDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
            TaskDetails task = new TaskDetails();
            task.setId(rs.getInt("id"));
            task.setTitle(rs.getString("title"));
            task.setDescription(rs.getString("description"));
            task.setStatus(rs.getString("status"));
            task.setDueDate(rs.getTimestamp("due_date") != null ? rs.getTimestamp("due_date").toLocalDateTime() : null);
            return task;
        }
    };

    // Fetch all tasks
    public List<TaskDetails> fetchAllTaskDetails() {
    	logger.info("fetchAllTaskDetails {}",template);
        String sql = "SELECT * FROM tasks";
        return template.query(sql, TASK_DETAILS_ROW_MAPPER);
    }

    // Fetch task by ID
    public TaskDetails fetchTaskDetailById(Long id) {
    	logger.info("fetchTaskDetailById {}",template);
        String sql = "SELECT * FROM tasks WHERE id = :id";
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        logger.info("params {}",params);
        List<TaskDetails> results = template.query(sql, params, TASK_DETAILS_ROW_MAPPER);
        logger.info("results {}",results);
        return results.isEmpty() ? null : results.get(0);
    }
    
    public int deleteTaskById(Long id) {
        String sql = "DELETE FROM tasks WHERE id = :id";
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        return template.update(sql, params);
    }

    public void updateTaskStatus(Long id, String status) {
        String sql = "UPDATE tasks SET status = :status WHERE id = :id";
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("status", status);
        template.update(sql, params);
    }

	@Override
	public int createTaskDetails(String title, String description, String status, String dueDate) {
		String sql = "INSERT INTO tasks (title, description, status, due_date) " +
                "VALUES (:title, :description, :status, :due_date)";
        Map<String, Object> params = new HashMap<>();
        params.put("title", title);
        params.put("status", status);
        params.put("description", description);
        params.put("due_date", dueDate);
        return template.update(sql, params);
	}
}
