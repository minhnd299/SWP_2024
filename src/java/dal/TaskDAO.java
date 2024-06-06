/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import model.Objective;
import model.Task;

/**
 *
 * @author ADMIN
 */
public class TaskDAO extends DBContext {

    public List<Task> getAllTaskyObject(Integer objective_id, String status) {
        List<Task> tasks = new ArrayList<>();
        List<Object> list = new ArrayList<>();
        ObjectiveDAO objectiveDAO = new ObjectiveDAO();
        try {
            StringBuilder query = new StringBuilder();
            query.append("""
                         select * from task where objective_id = ? """);
            list.add(objective_id);
            if (status != null && !status.trim().isEmpty()) {
                query.append(" AND  status = ? ");
                list.add(status);
            }

            query.append(" ORDER BY task_id DESC");
            PreparedStatement preparedStatement;
            preparedStatement = connection.prepareStatement(query.toString());
            mapParams(preparedStatement, list);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    Task t = new Task();
                    t.setTask_id(rs.getInt("task_id"));
                    t.setTitle(rs.getString("title"));
                    t.setDescription(rs.getString("description"));
                    t.setStatus(rs.getString("status"));
                    t.setStart_date(rs.getDate("start_date"));
                    t.setEnd_date(rs.getDate("end_date"));
                    Objective o = objectiveDAO.getById(rs.getInt("objective_id"));
                    t.setObjecttive(o);
                    tasks.add(t);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tasks;
    }

    public void mapParams(PreparedStatement ps, List<Object> args) throws SQLException {
        int i = 1;
        for (Object arg : args) {
            if (arg instanceof Date) {
                ps.setTimestamp(i++, new Timestamp(((Date) arg).getTime()));
            } else if (arg instanceof Integer) {
                ps.setInt(i++, (Integer) arg);
            } else if (arg instanceof Long) {
                ps.setLong(i++, (Long) arg);
            } else if (arg instanceof Double) {
                ps.setDouble(i++, (Double) arg);
            } else if (arg instanceof Float) {
                ps.setFloat(i++, (Float) arg);
            } else {
                ps.setString(i++, (String) arg);
            }

        }
    }

    public void changeTaskStatus(int task_id, String newStatus) {
        try {
            String query = "UPDATE Task SET status = ? WHERE task_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, newStatus);
            preparedStatement.setInt(2, task_id);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Task status updated successfully.");
            } else {
                System.out.println("Failed to update task status.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Task getById(int taskId) {
        Task t = null;
        ObjectiveDAO objectiveDAO = new ObjectiveDAO();
        try {
            String query = "SELECT * FROM Task WHERE task_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, taskId);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    t = new Task();
                    t.setTask_id(rs.getInt("task_id"));
                    t.setTitle(rs.getString("title"));
                    t.setDescription(rs.getString("description"));
                    t.setStatus(rs.getString("status"));
                    Objective o = objectiveDAO.getById(rs.getInt("objective_id"));
                    t.setObjecttive(o);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return t;
    }

    public void addTask(String title, String description, String status, String start_date, String end_date, int objectiveId) {
        String query = "INSERT INTO Task (title, description, status,start_date, end_date ,objective_id) VALUES (?, ?,?,?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, description);
            preparedStatement.setString(3, status);
            preparedStatement.setString(4, start_date);
            preparedStatement.setString(5, end_date);
            preparedStatement.setInt(6, objectiveId);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Task added successfully.");
            } else {
                System.out.println("Failed to add task.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateTask(String title, String description,  String start_date, String end_date, int task_id) {
        try {
            String query = "UPDATE Task SET title = ?, description = ?,  start_date =? , end_date =? where task_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, description);
   
            preparedStatement.setString(3, start_date);
            preparedStatement.setString(4, end_date);
            preparedStatement.setInt(5, task_id);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Task updated successfully.");
            } else {
                System.out.println("Failed to update task.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteTask(int taskId) {
        try {
            String query = "DELETE FROM Task WHERE task_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, taskId);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Task deleted successfully.");
            } else {
                System.out.println("Failed to delete task.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isTaskInUse(int taskId) {
        try {
            // Check if the task is referenced in the Comment table
            String commentQuery = "SELECT COUNT(*) FROM Comment WHERE task_id = ?";
            try (PreparedStatement commentStatement = connection.prepareStatement(commentQuery)) {
                commentStatement.setInt(1, taskId);
                try (ResultSet commentResultSet = commentStatement.executeQuery()) {
                    if (commentResultSet.next() && commentResultSet.getInt(1) > 0) {
                        return true; // Task is referenced in the Comment table
                    }
                }
            }

            // Check if the task is referenced in the Report table
            String reportQuery = "SELECT COUNT(*) FROM Report WHERE task_id = ?";
            try (PreparedStatement reportStatement = connection.prepareStatement(reportQuery)) {
                reportStatement.setInt(1, taskId);
                try (ResultSet reportResultSet = reportStatement.executeQuery()) {
                    if (reportResultSet.next() && reportResultSet.getInt(1) > 0) {
                        return true; // Task is referenced in the Report table
                    }
                }
            }

            // Check if the task is referenced in the Code table
            String codeQuery = "SELECT COUNT(*) FROM Code WHERE task_id = ?";
            try (PreparedStatement codeStatement = connection.prepareStatement(codeQuery)) {
                codeStatement.setInt(1, taskId);
                try (ResultSet codeResultSet = codeStatement.executeQuery()) {
                    if (codeResultSet.next() && codeResultSet.getInt(1) > 0) {
                        return true; // Task is referenced in the Code table
                    }
                }
            }

            // If the task is not referenced in any of the tables, return false
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return true; // Consider task as in use if there's an error
        }
    }

    public void updateTaskStatuses() {
        try {
            LocalDate currentDate = LocalDate.now();

            // Update status to "progress" for tasks starting today
            String progressQuery = """
                UPDATE Task SET status = 'in progress'
                WHERE start_date = ? AND status != 'done'
            """;
            try (PreparedStatement progressStatement = connection.prepareStatement(progressQuery)) {
                progressStatement.setDate(1, Date.valueOf(currentDate));
                progressStatement.executeUpdate();
            }

            // Update status to "done" for tasks that have ended
            String doneQuery = """
                UPDATE Task SET status = 'done'
                WHERE end_date < ? AND status != 'done'
            """;
            try (PreparedStatement doneStatement = connection.prepareStatement(doneQuery)) {
                doneStatement.setDate(1, Date.valueOf(currentDate));
                doneStatement.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        TaskDAO aO = new TaskDAO();
        System.out.println(aO.isTaskInUse(3));
    }
}
