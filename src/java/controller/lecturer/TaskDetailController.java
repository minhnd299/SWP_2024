/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.lecturer;

import dal.CommentDAO;
import dal.LecturerDAO;
import dal.TaskDAO;
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
import model.Comment;
import model.Task;

@WebServlet(name = "TaskDetailController", urlPatterns = {"/lecturer/task-detail"})
public class TaskDetailController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account a = (Account) session.getAttribute("account");
        TaskDAO taskDAO = new TaskDAO();
        CommentDAO commentDAO = new CommentDAO();
        int id = Integer.parseInt(request.getParameter("id"));
        if (a != null && a.getRoleAccount().getRole_id() == 3) {
            Task task = taskDAO.getById(id);
            List<Comment> listComments = commentDAO.getAllCCommetOfTask(id);
            request.setAttribute("task", task);
            request.setAttribute("comments", listComments);
            request.getRequestDispatcher("task-detail.jsp").forward(request, response);
        } else {
            response.sendRedirect("../login");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account a = (Account) session.getAttribute("account");

        LecturerDAO lecturerDAO = new LecturerDAO();
        CommentDAO commentDAO = new CommentDAO();
        String action = request.getParameter("action");

        int tid = Integer.parseInt(request.getParameter("tid"));
        if (a != null && a.getRoleAccount().getRole_id() == 3) {
            int lid = lecturerDAO.getByAccountId(a.getId()).getLecturer_id();
            if (action.equals("add-comment")) {
                String comment = request.getParameter("comment");
                try {
                    commentDAO.addLecturerComment(tid, lid, comment);
                    session.setAttribute("notification", "Add comment success!");
                    response.sendRedirect("task-detail?id=" + tid);
                } catch (Exception e) {
                    session.setAttribute("notificationErr", e.getMessage());
                    response.sendRedirect("task-detail?id=" + tid);
                }

            }
            if (action.equals("edit-comment")) {
                try {
                    String comment = request.getParameter("comment");
                    int id = Integer.parseInt(request.getParameter("cid"));
                    commentDAO.updateComment(id, comment);
                    session.setAttribute("notification", "Edit comment success!");
                    response.sendRedirect("task-detail?id=" + tid);
                } catch (Exception e) {
                    session.setAttribute("notificationErr", e.getMessage());
                    response.sendRedirect("task-detail?id=" + tid);
                }

            }

        } else {
            response.sendRedirect("../login");
        }
    }
}
