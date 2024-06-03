/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.lecture;

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
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Admin
 */
@WebServlet(name = "Attendance", urlPatterns = {"/attendance"})
public class Attendance extends HttpServlet {

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
            out.println("<title>Servlet Attendance</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Attendance at " + request.getContextPath() + "</h1>");
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
        String role = null;
        String userId = null;
        HttpSession session = request.getSession();
        if (session.getAttribute("acc") != null) {
            role = session.getAttribute("acc").toString().split("\\|")[2];
            userId = session.getAttribute("acc").toString().split("\\|")[0];
        } else {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("acc")) {
                        role = cookie.getValue().split("\\|")[2];
                        userId = cookie.getValue().split("\\|")[0];
                        break;
                    }
                }
            }
        }
        if (role == null || "".equals(role)) {
            response.sendRedirect("login");
            return;
        }
        if (!"3".equals(role)) {
            response.sendRedirect("index.html");
            return;
        }

        String classID = request.getParameter("classID");
        String today = request.getParameter("today");
        Dao d = new Dao();
        request.setAttribute("list", d.getStudentInClass(userId, classID, today));
        if ("0".equals(today)) {
            request.setAttribute("run", "add");
        } else {
            request.setAttribute("run", "up");
        }
        session.setAttribute("classId", classID);
        request.getRequestDispatcher("attendance.jsp").forward(request, response);
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
        String[] list = request.getParameterValues("att");
        Dao d = new Dao();
        HttpSession session = request.getSession();
        String s = String.valueOf(session.getAttribute("classId"));
        d.addAttendance(list, s);
        response.sendRedirect("viewClass");
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
