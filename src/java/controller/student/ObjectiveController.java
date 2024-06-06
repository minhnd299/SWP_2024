/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.student;

import dal.ClasssDAO;
import dal.ObjectiveDAO;
import dal.StudentDAO;
import java.io.IOException;
import model.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;

/**
 *
 * @author ADMIN
 */
@WebServlet(name = "ObjectiveController", urlPatterns = {"/student/objective"})
public class ObjectiveController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account a = (Account) session.getAttribute("account");
        ObjectiveDAO ob = new ObjectiveDAO();
        ClasssDAO classsDAO = new ClasssDAO();
        StudentDAO studentDAO = new StudentDAO();

        if (a != null && a.getRoleAccount().getRole_id() == 2) {
            Student student = studentDAO.getByAccountId(a.getId());
            int cid = classsDAO.getLatestClassIdByStudent(student.getStudent_id());
            List<Objective> o = ob.getAllObjectiveByClass(cid);

            request.setAttribute("o", o);

            request.getRequestDispatcher("objective_manage.jsp").forward(request, response);
        } else {
            response.sendRedirect("../login");
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }
}
