/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.admin;

import dal.AccountDAO;
import dal.ClasssDAO;
import dal.LecturerDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import model.Account;
import model.Lecturer;

/**
 *
 * @author ADMIN
 */
@WebServlet(name = "UserManagement", urlPatterns = {"/admin/user-managament"})
public class UserManagement extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account a = (Account) session.getAttribute("account");
        if (a != null && a.getRoleAccount().getRole_id() == 1) {
            AccountDAO accountDAO = new AccountDAO();
            List<Account> listA = accountDAO.getAllUser();
           
            request.setAttribute("account", listA);
            request.setAttribute("accounts", a);
            request.getRequestDispatcher("user-list.jsp").forward(request, response);
        } else {
            response.sendRedirect("../login");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

}
