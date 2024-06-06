/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.login;

import dal.Dao;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Account;

/**
 *
 * @author Admin
 */
@WebServlet(name = "Login", urlPatterns = {"/login"})
public class Login extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String user = request.getParameter("user");
        String password = request.getParameter("password");
        String remember = null;
        if (request.getParameter("remember") != null) {
            remember = request.getParameter("remember");
        }
        String err = null;
        String suc = null;
        Dao d = new Dao();
        Account a = d.getByUsernameAndPass(user.trim(), password);
        if (a == null) {
            err = "UserName or Password incorrect!!!";
            System.out.println(err);
        } else {
            if (remember == null) {
                HttpSession session = request.getSession();
                session.setAttribute("acc", user + "|" + password);
                session.setAttribute("account", a);
                switch (a.getRoleAccount().getRole_id()) {
                    case 1 ->
                        response.sendRedirect("admin/dashboard");
                    case 2 ->
                        response.sendRedirect("student/objective");
                    case 3 ->
                        response.sendRedirect("lecturer/class-list");
                    default -> {
                        break;
                    }
                }

            } else {
                Cookie cookie = new Cookie("acc", user + "|" + password);
                HttpSession session = request.getSession();
                session.setAttribute("account", a);
                cookie.setMaxAge(12 * 30 * 24 * 60 * 60);
                cookie.setPath("/");
                response.addCookie(cookie);
                switch (a.getRoleAccount().getRole_id()) {
                    case 1 ->
                        response.sendRedirect("admin/dashboard");
                    case 2 ->
                        response.sendRedirect("student/objective");
                    case 3 ->
                        response.sendRedirect("lecturer/class-list");
                    default -> {
                        break;
                    }
                }
            }
        }
        request.setAttribute("user", user);
        request.setAttribute("err", err);
        request.setAttribute("suc", suc);
    }
}
