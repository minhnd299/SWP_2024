/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import model.Account;
import model.Student;

/**
 *
 * @author ADMIN
 */
public class Dao extends DBContext {

    public Student getInfoStudentByUserName(String userName) {
        String sql = "select [Name], [Address], BirthDate, [Phone], [Email]\n"
                + "from Student where [Name] = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, userName);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Student s = new Student();
                s.setName(rs.getString("Name"));
                s.setAddress(rs.getString("Address"));
                s.setDob(rs.getString("BirthDate"));
                s.setPhone(rs.getString("Phone"));
                s.setEmail(rs.getString("Email"));
                return s;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }

    public Account getAccountByEmail(String email) {
        String sql = "select [User_name], [Password]\n"
                + "from Account a, Student s \n"
                + "where Email = ? and [Name] = [User_name]";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, email);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Account a = new Account();
                a.setUserName(rs.getString("User_name"));
                a.setPassword(rs.getString("Password"));
                return a;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }

    public boolean checkAccount(String userName, String password) {
        String sql = "select * from Account where [User_name] = ? and [Password] = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, userName);
            st.setString(2, password);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return false;
    }

    public void setNewPassword(String userName, String password) {
        String sql = "UPDATE [dbo].[Account]\n"
                + "   SET [Password] = ?\n"
                + " WHERE [User_name] = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, password);
            st.setString(2, userName);
            st.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

}
