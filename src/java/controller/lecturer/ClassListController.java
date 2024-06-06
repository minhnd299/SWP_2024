/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.lecturer;

import dal.ClasssDAO;
import dal.LecturerDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import model.*;

@WebServlet(name = "ClassListController", urlPatterns = {"/lecturer/class-list"})
public class ClassListController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account a = (Account) session.getAttribute("account");
        ClasssDAO classsDAO = new ClasssDAO();
        LecturerDAO ldao = new LecturerDAO();
        if (a != null && a.getRoleAccount().getRole_id() == 3) {
            Lecturer l = ldao.getByAccountId(a.getId());
            List<model.Class> cl = classsDAO.getAllClassByLecturer(l.getLecturer_id());
            request.setAttribute("classList", cl);
            request.getRequestDispatcher("classes.jsp").forward(request, response);
        } else {
            response.sendRedirect("../login");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }
}
