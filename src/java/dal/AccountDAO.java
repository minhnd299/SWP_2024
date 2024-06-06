/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.beans.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Account;
import model.Lecturer;
import model.Role;

/**
 *
 * @author ADMIN
 */
public class AccountDAO extends DBContext {

    public List<Account> getAllUser() {
        List<Account> accounts = new ArrayList<>();

        RoleDAO roleDAO = new RoleDAO();
        try {
            String query = """
                         Select * from Account where id != 1 ORDER BY id DESC
                         """;

            PreparedStatement preparedStatement;
            preparedStatement = connection.prepareStatement(query);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    Account a = new Account();
                    a.setId(rs.getInt("id"));
                    a.setUsername(rs.getString("username"));
                    a.setStatus(rs.getString("status"));
                    a.setAddress(rs.getString("address"));
                    a.setPhone(rs.getString("phone"));
                    a.setEmail(rs.getString("email"));
                    a.setRoleAccount(roleDAO.getById(rs.getInt("roleId")));

                    accounts.add(a);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }

    public Account getByEmail(String email) {
        Account account = null;
        RoleDAO roleDAO = new RoleDAO();
        try {
            String query = """
                           SELECT * FROM Account WHERE email = ?
                           """;

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    account = new Account();
                    account.setId(rs.getInt("id"));
                    account.setUsername(rs.getString("username"));
                    account.setStatus(rs.getString("status"));
                    account.setAddress(rs.getString("address"));
                    account.setPhone(rs.getString("phone"));
                    account.setEmail(rs.getString("email"));
                    account.setRoleAccount(roleDAO.getById(rs.getInt("roleId")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return account;
    }

    public boolean create(Account account) {
        String query = "INSERT INTO Account (username, password, email, roleId, status) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());
            ps.setString(3, account.getEmail());
            ps.setInt(4, 2); // student role
            ps.setString(5, "active");
            int row = ps.executeUpdate();
            return row > 0;
        } catch (SQLException e) {
            return false;
        }
    }
public Account getLatestAccount() {
        Account account = null;
        RoleDAO roleDAO = new RoleDAO();
        String query = "SELECT top 1 * FROM Account ORDER BY id DESC ";
        try (PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                account = new Account();
                account.setId(rs.getInt("id"));
                account.setUsername(rs.getString("username"));
                account.setStatus(rs.getString("status"));
                account.setAddress(rs.getString("address"));
                account.setPhone(rs.getString("phone"));
                account.setEmail(rs.getString("email"));
                account.setRoleAccount(roleDAO.getById(rs.getInt("roleId")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return account;
    }
    public static void main(String[] args) {
        AccountDAO accountDAO = new AccountDAO();
        Account a = accountDAO.getLatestAccount();
        System.out.println(a);
         
    }
}
