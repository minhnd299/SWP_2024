/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller.password;

import dal.Dao;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Account;
import send_mail.Mailer;

/**
 *
 * @author Admin
 */
@WebServlet(name="ForgetPassword", urlPatterns={"/forgetPassword"})
public class ForgetPassword extends HttpServlet {
  
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        request.getRequestDispatcher("forgot-password.jsp").forward(request, response);
    } 

   
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String email = request.getParameter("email");
        String a[] = email.split("@");
        String err = null;
        String suc = null;
        int l = a.length;
        if(l<2) {
            err = "Enter email, plz !!!";
        }else if(!a[1].equalsIgnoreCase("fpt.edu.vn")){
            err = "Enter email of FBT, plz!!!";
        }else {
            Dao d = new Dao();
            Account acc = d.getAccountByEmail(email);
            if(acc == null) {
                err = "You are not in the lab this semester!!!";
            }else {
                Mailer m = new Mailer();
                String newPassword = m.forgetPassword(email);
                d.setNewPassword(acc.getUsername(), newPassword);
                suc = "We send new password to email!!!";
            } 
        }
        request.setAttribute("err", err);
        request.setAttribute("suc", suc);
        request.getRequestDispatcher("forgot-password.jsp").forward(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
