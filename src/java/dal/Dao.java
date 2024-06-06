/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Account;
import model.Role;

/**
 *
 * @author ADMIN
 */
public class Dao extends DBContext {

    public Account getAccountByEmail(String email) {
        String sql = """
                     select [username], [Password]
                     from Account a, Student s 
                     where Email = ? and [Name] = [User_name]""";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, email);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Account a = new Account();
                a.setUsername(rs.getString("User_name"));
                a.setPassword(rs.getString("Password"));
                return a;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }

    public Account getAccountByID(int id) {
        String sql = """
                     select * from Account where id = ?""";
        try {
            RoleDAO r = new RoleDAO();
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Account a = new Account();
                a.setId(rs.getInt("id"));
                a.setUsername(rs.getString("username"));
                a.setPassword(rs.getString("password"));
                a.setEmail(rs.getString("email"));
                a.setAddress(rs.getString("address"));
                a.setPhone(rs.getString("phone"));
                a.setStatus(rs.getString("status"));
                Role role = r.getById(rs.getInt("roleId"));
                a.setRoleAccount(role);
                return a;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }

    public Account getByUsernameAndPass(String username, String password) {
        String sql = """
                     select * 
                     from Account a, Student s 
                     where [username] = ? and [password] = ?""";
        RoleDAO r = new RoleDAO();
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, username);
            st.setString(2, password);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Account a = new Account();
                a.setId(rs.getInt("id"));
                a.setUsername(rs.getString("username"));
                a.setPassword(rs.getString("password"));
                a.setEmail(rs.getString("email"));
                a.setAddress(rs.getString("address"));
                a.setPhone(rs.getString("phone"));
                a.setStatus(rs.getString("status"));
                Role role = r.getById(rs.getInt("roleId"));
                a.setRoleAccount(role);
                return a;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }

    public boolean checkAccount(String userName, String password) {
        String sql = "select * from Account where [username] = ? and [password] = ?";
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
        String sql = """
                     UPDATE [dbo].[Account]
                        SET [password] = ?
                      WHERE [username] = ?""";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, password);
            st.setString(2, userName);
            st.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) {
        Dao d = new Dao();
        Account a = d.getByUsernameAndPass("lecturer_user", "password123");
        System.out.println(a);
    }
}
