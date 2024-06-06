/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.password;

import dal.Dao;
import init.InitAndCheck;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author Admin
 */
@WebServlet(name = "ChangePassword", urlPatterns = {"/changePassword"})
public class ChangePassword extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ChangePassword</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ChangePassword at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("change-password.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String old = request.getParameter("old");
        String newP = request.getParameter("newP");
        String conP = request.getParameter("conP");
        String user = null;
        String err = null;
        String suc = null;
        InitAndCheck ic = new InitAndCheck();
        if (!ic.checkPassword(old) || !ic.checkPassword(newP)) {
            err = "Password must be 8-20 characters have most less 1 number, 1 letter, 1 capital letter, 1 special character!!!";
        } else if (!newP.equalsIgnoreCase(conP)) {
            err = "New password and confirm password not match!!!";
        } else {
            HttpSession session = request.getSession();
            if (session.getAttribute("acc") != null) {
                user = session.getAttribute("acc").toString().split("\\|")[0];
            } else {
                Cookie[] cookies = request.getCookies();
                if (cookies != null) {
                    for (Cookie cookie : cookies) {
                        if (cookie.getName().equals("acc")) {
                            user = cookie.getValue().split("\\|")[0];
                            break;
                        }
                    }
                }
            }

            if (user == null) {
//                err = "Login plz!!!";
                response.sendRedirect("login");
            } else {
                Dao d = new Dao();
                if (!d.checkAccount(user, old)) {
                    err = "Old password incorrect!!!";
                } else {
                    d.setNewPassword(user, newP);
                    suc = "Change password successfully!!!";
                }
            }
        }

        request.setAttribute("err", err);
        request.setAttribute("suc", suc);
        request.getRequestDispatcher("change-password.jsp").forward(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
