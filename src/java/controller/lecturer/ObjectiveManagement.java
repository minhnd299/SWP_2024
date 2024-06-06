/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.lecturer;

import dal.LecturerDAO;
import dal.ObjectiveDAO;
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
@WebServlet(name = "ObjectiveManagement", urlPatterns = {"/lecturer/objective"})
public class ObjectiveManagement extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account a = (Account) session.getAttribute("account");
        ObjectiveDAO ob = new ObjectiveDAO();

        if (a != null && a.getRoleAccount().getRole_id() == 3) {

            int cid = Integer.parseInt(request.getParameter("cid"));
            List<Objective> o = ob.getAllObjectiveByClass(cid);
            System.out.println(o);
            request.setAttribute("o", o);

            request.getRequestDispatcher("objective_manage.jsp").forward(request, response);
        } else {
            response.sendRedirect("../login");
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account a = (Account) session.getAttribute("account");
        ObjectiveDAO ob = new ObjectiveDAO();
        LecturerDAO lecturerDAO = new LecturerDAO();
        String action = request.getParameter("action");
        String cid = request.getParameter("cid");
        if (a != null && a.getRoleAccount().getRole_id() == 3) {
            if (action.equals("add")) {
                Lecturer s = lecturerDAO.getByAccountId(a.getId());
                String description = request.getParameter("description");
                try {
                    ob.addObjective(description, s.getLecturer_id(), Integer.parseInt(cid));
                    session.setAttribute("notification", "Objective add successfully!");
                    response.sendRedirect("objective?cid=" + cid);
                } catch (Exception e) {
                    session.setAttribute("notificationErr", e.getMessage());
                    response.sendRedirect("objective?cid=" + cid);
                }
            }
            if (action.equals("edit")) {

                String description = request.getParameter("description");
                int id = Integer.parseInt(request.getParameter("id"));

                try {
                    ob.updateObjective(description, id);
                    session.setAttribute("notification", "Objective update successfully!");
                    response.sendRedirect("objective?cid=" + cid);
                } catch (Exception e) {
                    session.setAttribute("notificationErr", e.getMessage());
                    response.sendRedirect("objective?cid=" + cid);
                }
            }
            if (action.equals("delete")) {
                int id = Integer.parseInt(request.getParameter("id"));
                try {
                    ob.deleteObjective(id);
                    session.setAttribute("notification", "Objective deleted!");
                } catch (Exception e) {
                    session.setAttribute("notificationErr", e.getMessage());
                } finally {
                    response.sendRedirect("objective?cid=" + cid);
                }
            }

        } else {
            response.sendRedirect("../login");
        }
    }

}
