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
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Account;
import model.Lecturer;

/**
 *
 * @author ADMIN
 */
public class LecturerDAO extends DBContext {

    public Lecturer getById(int lecturer_id) {
        String sql = "SELECT * FROM Lecturer WHERE lecturer_id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, lecturer_id);
            ResultSet rs = statement.executeQuery();
            Dao d = new Dao();
            if (rs.next()) {
                Lecturer l = new Lecturer();
                l.setLecturer_id(rs.getInt("lecturer_id"));
                Account acc = d.getAccountByID(rs.getInt("account_id"));
                l.setAccountLecturer(acc);
                l.setFullName(rs.getString("fullName"));
                l.setEmployeeNumber(rs.getString("employeeNumber"));
                l.setDepartment(rs.getString("department"));
                l.setDepartment(rs.getString("specialization"));
                return l;

            }
        } catch (SQLException ex) {
            Logger.getLogger(LecturerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null; // Subject not found
    }

    public Lecturer getByAccountId(int accountId) {
        String sql = "SELECT * FROM Lecturer WHERE account_id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, accountId);
            ResultSet rs = statement.executeQuery();
            Dao d = new Dao();
            if (rs.next()) {
                Lecturer l = new Lecturer();
                l.setLecturer_id(rs.getInt("lecturer_id"));
                Account acc = d.getAccountByID(rs.getInt("account_id"));
                l.setAccountLecturer(acc);
                l.setFullName(rs.getString("fullName"));
                l.setEmployeeNumber(rs.getString("employeeNumber"));
                l.setDepartment(rs.getString("department"));
                l.setDepartment(rs.getString("specialization"));
                return l;

            }
        } catch (SQLException ex) {
            Logger.getLogger(LecturerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null; // Subject not found
    }

    public List<Lecturer> getAllLecturer() {
        List<Lecturer> lecturers = new ArrayList<>();
        String sql = "SELECT * FROM Lecturer";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            Dao dao = new Dao(); // Assuming Dao class exists and has method getAccountByID()
            while (rs.next()) {
                Lecturer lecturer = new Lecturer();
                lecturer.setLecturer_id(rs.getInt("lecturer_id"));
                Account acc = dao.getAccountByID(rs.getInt("account_id"));
                lecturer.setAccountLecturer(acc);
                lecturer.setFullName(rs.getString("fullName"));
                lecturer.setEmployeeNumber(rs.getString("employeeNumber"));
                lecturer.setDepartment(rs.getString("department"));
                lecturer.setSpecialization(rs.getString("specialization"));
                lecturers.add(lecturer);
            }
        } catch (SQLException ex) {
            Logger.getLogger(LecturerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lecturers;
    }

    public static void main(String[] args) {
        LecturerDAO l = new LecturerDAO();
        System.out.println(l.getById(1));
    }
}
