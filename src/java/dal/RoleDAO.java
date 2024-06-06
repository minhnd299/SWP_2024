/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;
import java.sql.*;
import model.Role;

/**
 *
 * @author ADMIN
 */
public class RoleDAO extends DBContext {
    public Role getById(int id) {
        String sql = """
                     select * 
                     from Role
                     where role_id = ?""";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Role r = new Role();
                r.setRole_id(rs.getInt("role_id"));
                r.setRole_name(rs.getString("role_name"));
                return r;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }
    public static void main(String[] args) {
        RoleDAO r = new  RoleDAO();
        System.out.println(r.getById(1));
    }
}
