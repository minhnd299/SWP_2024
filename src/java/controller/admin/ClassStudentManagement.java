/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.admin;

import dal.ClasssDAO;
import dal.LecturerDAO;
import dal.UserClassDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import model.*;

@WebServlet(name = "ClassStudentController", urlPatterns = {"/lecturer/class-student"})
public class ClassStudentManagement extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account a = (Account) session.getAttribute("account");
        int cid = Integer.parseInt(request.getParameter("cid"));
        UserClassDAO ucdao = new UserClassDAO();
        if (a != null && a.getRoleAccount().getRole_id() == 1) {
            List<UserClass> uc = ucdao.getAllUserClasses(cid);
            request.setAttribute("classList", uc);
            request.getRequestDispatcher("class-student.jsp").forward(request, response);
        } else {
            response.sendRedirect("../login");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }
}
