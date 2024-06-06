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
import model.Lecturer;
import model.Objective;
import model.Student;

/**
 *
 * @author ADMIN
 */
public class ObjectiveDAO extends DBContext {

    public List<Objective> getAllObjectiveByClass(Integer class_id) {
        ClasssDAO dAO = new ClasssDAO();
        LecturerDAO lecturerDAO = new LecturerDAO();
        List<Objective> objectives = new ArrayList<>();
        try {
            String query = """
                         select o.* from Objective o JOIN Class c On c.class_id = o.class_id
                         WHERE c.class_id = ?
                         ORDER BY o.Objective_id DESC
                         """;
            PreparedStatement preparedStatement;
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, class_id);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    Objective o = new Objective();
                    o.setObjective_id(rs.getInt("objective_id"));
                    o.setDescription(rs.getString("description"));
                    o.setStatus(rs.getString("status"));
                    o.setCreateAt(rs.getDate("create_at"));
                    model.Class c = dAO.getById(rs.getInt("class_id"));
                    Lecturer l = lecturerDAO.getById(rs.getInt("createBy"));
                    o.setClasses(c);
                    o.setLecturer(l);
                    objectives.add(o);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return objectives;
    }

    public void changeObjectiveStatus(int objectiveId, String newStatus) {
        try {
            String query = "UPDATE Objective SET status = ? WHERE objective_id = ? ";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, newStatus);
            preparedStatement.setInt(2, objectiveId);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Objective status updated successfully.");
            } else {
                System.out.println("Failed to update objective status.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Objective getById(int objectiveId) {

        LecturerDAO ldao = new LecturerDAO();
        Objective objective = null;
        try {
            String query = "SELECT * FROM Objective WHERE objective_id = ? ";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, objectiveId);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    objective = new Objective();
                    objective.setObjective_id(rs.getInt("objective_id"));
                    objective.setDescription(rs.getString("description"));
                    objective.setStatus(rs.getString("status"));
                    objective.setCreateAt(rs.getDate("create_at"));
                    Lecturer l = ldao.getById(rs.getInt("createBy"));
                    objective.setLecturer(l);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return objective;
    }

    public void addObjective(String description, int createBy, int class_id) {
        try {
            String query = "INSERT INTO Objective (description, createBy, class_id) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, description);
            preparedStatement.setInt(2, createBy);
            preparedStatement.setInt(3, class_id);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Objective added successfully.");
            } else {
                System.out.println("Failed to add objective.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateObjective(String description, int objective_id) {
        try {
            String query = "UPDATE Objective SET description = ? where objective_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, description);
            preparedStatement.setInt(2, objective_id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public void deleteObjective(int objectiveId) throws Exception {
        try {
            // Check if there are any tasks associated with this objective
            String checkQuery = "SELECT COUNT(*) AS task_count FROM Task WHERE objective_id = ?";
            PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
            checkStatement.setInt(1, objectiveId);
            ResultSet checkResultSet = checkStatement.executeQuery();

            if (checkResultSet.next() && checkResultSet.getInt("task_count") > 0) {
                throw new Exception("Cannot delete objective. There are tasks associated with this objective.");
            } else {
                // Proceed with the deletion if no tasks are found
                String deleteQuery = "DELETE FROM Objective WHERE objective_id = ?";
                PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
                deleteStatement.setInt(1, objectiveId);
                deleteStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new Exception("Error occurred while deleting objective: " + e.getMessage(), e);
        }
    }

    public static void main(String[] args) {
        ObjectiveDAO o = new ObjectiveDAO();
        List<Objective> l = o.getAllObjectiveByClass(2);
        for (Objective ob : l) {
            System.out.println(ob);
        }
    }
}
