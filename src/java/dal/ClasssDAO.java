/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import model.Class;
import model.Lecturer;

public class ClasssDAO extends DBContext {

    public List<Class> getAllClassByLecturer(Integer lecturer_id) {
        List<Class> classes = new ArrayList<>();

        try {
            String query = """
                         select c.*,(select count(*) from userClass where class_id = c.class_id) as student_number from Class c
                         where lecturer_id = ?  AND status = 'ACTIVE' ORDER BY class_id DESC
                         """;

            PreparedStatement preparedStatement;
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, lecturer_id);
            LecturerDAO lecturerDAO = new LecturerDAO();
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    Class cl = new Class();
                    cl.setClass_id(rs.getInt("class_id"));
                    cl.setClass_name(rs.getString("class_name"));
                    cl.setStatus(rs.getString("status"));
                    cl.setStudentNumber(rs.getInt("student_number"));
                    Lecturer l = lecturerDAO.getById(rs.getInt("lecturer_id"));
                    cl.setLecturer(l);
                    classes.add(cl);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return classes;
    }

    public List<Class> getAllClass() {
        List<Class> classes = new ArrayList<>();

        try {
            String query = """
                         select c.*,(select count(*) from userClass where class_id = c.class_id) as student_number from Class c
                          ORDER BY class_id DESC
                         """;

            PreparedStatement preparedStatement;
            preparedStatement = connection.prepareStatement(query);
            LecturerDAO lecturerDAO = new LecturerDAO();
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    Class cl = new Class();
                    cl.setClass_id(rs.getInt("class_id"));
                    cl.setClass_name(rs.getString("class_name"));
                    cl.setStatus(rs.getString("status"));
                    cl.setStudentNumber(rs.getInt("student_number"));
                    Lecturer l = lecturerDAO.getById(rs.getInt("lecturer_id"));
                    cl.setLecturer(l);
                    classes.add(cl);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return classes;
    }

    public Integer getLatestClassIdByStudent(Integer student_id) {
        Integer classId = null;
        try {
            String query = """
                           SELECT TOP 1 c.class_id FROM Class c 
                           JOIN userClass ul ON c.class_id = ul.class_id
                           WHERE student_id = ? 
                           ORDER BY c.class_id DESC
                           """;
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, student_id);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    classId = rs.getInt("class_id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return classId;
    }

    public boolean addClass(Class newClass) {
        try {
            String query = "INSERT INTO Class (class_name, status, lecturer_id) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, newClass.getClass_name());
            preparedStatement.setString(2, newClass.getStatus());
            preparedStatement.setInt(3, newClass.getLecturer().getLecturer_id());
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public model.Class getById(Integer class_id) {
        model.Class cl = null;
        try {
            String query = """
                       SELECT c.*, (SELECT COUNT(*) FROM userClass WHERE class_id = c.class_id) AS student_number 
                       FROM Class c 
                       WHERE c.class_id = ?
                       """;
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, class_id);
            LecturerDAO lecturerDAO = new LecturerDAO();
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    cl = new model.Class();
                    cl.setClass_id(rs.getInt("class_id"));
                    cl.setClass_name(rs.getString("class_name"));
                    cl.setStatus(rs.getString("status"));
                    cl.setStudentNumber(rs.getInt("student_number"));
                    Lecturer l = lecturerDAO.getById(rs.getInt("lecturer_id"));
                    cl.setLecturer(l);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cl;
    }

    public static void main(String[] args) {
        ClasssDAO cldao = new ClasssDAO();
        System.out.println(cldao.getLatestClassIdByStudent(2));
    }
}
