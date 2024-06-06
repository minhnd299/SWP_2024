/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.*;

/**
 *
 * @author ADMIN
 */
public class CommentDAO extends DBContext {

    public List<Comment> getAllCCommetOfTask(Integer task_id) {
        List<Comment> comments = new ArrayList<>();
        TaskDAO taskDAO = new TaskDAO();
        LecturerDAO lecturerDAO = new LecturerDAO();
        StudentDAO studentDAO = new StudentDAO();
        try {
            String query = """
                         Select * from comment where task_id = ?  ORDER BY comment_id DESC
                         """;

            PreparedStatement preparedStatement;
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, task_id);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    Comment c = new Comment();
                    c.setComment_id(rs.getInt("comment_id"));
                    Task t = taskDAO.getById(rs.getInt("task_id"));
                    c.setTask(t);
                    c.setComment(rs.getString("comment"));
                    c.setTime(rs.getTimestamp("timestamp"));
                    Lecturer l = lecturerDAO.getById(rs.getInt("lecturer_id"));
                    c.setLecturer(l);
                    Student s = studentDAO.getById(rs.getInt("student_id"));
                    c.setStudent(s);
                    comments.add(c);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comments;
    }

    public void addLecturerComment(int task_id, int lecturer_id, String comment) {
        try {
            String query = """
                           INSERT INTO comment (task_id, lecturer_id, student_id, comment, timestamp) 
                           VALUES (?, ?, NULL, ?, CURRENT_TIMESTAMP)
                           """;

            PreparedStatement preparedStatement;
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, task_id);
            preparedStatement.setInt(2, lecturer_id);
            preparedStatement.setString(3, comment);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateComment(int commentId, String newComment) {
        try {
            String query = """
                           UPDATE comment
                           SET comment = ? , timestamp = CURRENT_TIMESTAMP
                           WHERE comment_id = ?
                           """;

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, newComment);
            preparedStatement.setInt(2, commentId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addStudentComment(int task_id, int student_id, String comment) {
        try {
            String query = """
                           INSERT INTO comment (task_id, lecturer_id, student_id, comment, timestamp) 
                           VALUES (?, NULL, ? , ?, CURRENT_TIMESTAMP)
                           """;

            PreparedStatement preparedStatement;
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, task_id);
            preparedStatement.setInt(2, student_id);
            preparedStatement.setString(3, comment);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        CommentDAO c = new CommentDAO();
        List<Comment> s = c.getAllCCommetOfTask(1);
        for (Comment ss : s) {
            System.out.println(ss);
        }
    }
}
