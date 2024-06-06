/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Account;
import model.Student;

/**
 *
 * @author ADMIN
 */
public class StudentDAO extends DBContext {

    public Student getById(int student_id) {
        String sql = "SELECT * FROM student WHERE student_id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, student_id);
            ResultSet rs = statement.executeQuery();
            Dao d = new Dao();
            if (rs.next()) {
                Student s = new Student();
                s.setStudent_id(rs.getInt("student_id"));
                Account acc = d.getAccountByID(rs.getInt("account_id"));
                s.setAccount(acc);
                s.setFullName(rs.getString("fullName"));
                s.setRollNumber(rs.getString("rollNumber"));
                s.setBirthDate(rs.getDate("birthDate"));
                s.setSchoolYear(rs.getInt("schoolYear"));
                return s;

            }
        } catch (SQLException ex) {
            Logger.getLogger(LecturerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null; // Subject not found
    }

    public Student getByAccountId(int accountId) {
        String sql = "SELECT * FROM Student WHERE account_id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, accountId);
            ResultSet rs = statement.executeQuery();
            Dao d = new Dao();
            if (rs.next()) {
                Student s = new Student();
                s.setStudent_id(rs.getInt("student_id"));
                Account acc = d.getAccountByID(rs.getInt("account_id"));
                s.setAccount(acc);
                s.setFullName(rs.getString("fullName"));
                s.setRollNumber(rs.getString("rollNumber"));
                s.setBirthDate(rs.getDate("birthDate"));
                s.setSchoolYear(rs.getInt("schoolYear"));
                return s;

            }
        } catch (SQLException ex) {
            Logger.getLogger(LecturerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null; // Subject not found
    }

    public boolean create(Student student) {
        String insertStudentSQL = "INSERT INTO Student (account_id, fullName, rollNumber) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(insertStudentSQL)) {
            ps.setInt(1, student.getAccount().getId());
            ps.setString(2, student.getFullName());
            ps.setString(3, student.getRollNumber());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating student failed, no rows affected.");
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Student getLastestStudent() {
        
        Dao d = new Dao();
        String query = "SELECT top 1 * FROM Student ORDER BY student_id DESC ";
        try (PreparedStatement ps = connection.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                Student s = new Student();
                s.setStudent_id(rs.getInt("student_id"));
                Account acc = d.getAccountByID(rs.getInt("account_id"));
                s.setAccount(acc);
                s.setFullName(rs.getString("fullName"));
                s.setRollNumber(rs.getString("rollNumber"));
                s.setBirthDate(rs.getDate("birthDate"));
                s.setSchoolYear(rs.getInt("schoolYear"));
                return s;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        StudentDAO l = new StudentDAO();
        System.out.println(l.getLastestStudent( ));
    }
}
