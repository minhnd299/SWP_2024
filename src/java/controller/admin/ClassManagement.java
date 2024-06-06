/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.admin;

import dal.AccountDAO;
import dal.ClasssDAO;
import dal.LecturerDAO;
import dal.RoleDAO;
import dal.StudentDAO;
import dal.UserClassDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import model.Account;
import model.Lecturer;
import model.Role;
import model.Student;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import send_mail.SendMail;
import utils.PasswordUtils;

/**
 *
 * @author ADMIN
 */
@MultipartConfig
@WebServlet(name = "ClassManagement", urlPatterns = {"/admin/class-list"})
public class ClassManagement extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account a = (Account) session.getAttribute("account");
        ClasssDAO classsDAO = new ClasssDAO();
        LecturerDAO lecturerDAO = new LecturerDAO();
        if (a != null && a.getRoleAccount().getRole_id() == 1) {
            List<model.Class> cl = classsDAO.getAllClass();
            List<Lecturer> lecturers = lecturerDAO.getAllLecturer();
            request.setAttribute("classList", cl);
            request.setAttribute("l", lecturers);
            request.getRequestDispatcher("classes.jsp").forward(request, response);
        } else {
            response.sendRedirect("../login");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account a = (Account) session.getAttribute("account");
        UserClassDAO userClassDAO = new UserClassDAO();
        StudentDAO studentDAO = new StudentDAO();
        String action = request.getParameter("action");
        ClasssDAO classsDAO = new ClasssDAO();
        LecturerDAO lecturerDAO = new LecturerDAO();
        List<String> existingStudents = new ArrayList<>();
        if (a != null && a.getRoleAccount().getRole_id() == 1) {
            if (action.equals("import")) {
                int cid = Integer.parseInt(request.getParameter("cid"));
                Part filePart = request.getPart("file");
                File file = new File(filePart.getSubmittedFileName());
                filePart.write(file.getAbsolutePath());
                RoleDAO roleDAO = new RoleDAO();
                SendMail sendMail = new SendMail();
                try (FileInputStream fis = new FileInputStream(file); Workbook workbook = new XSSFWorkbook(fis)) {
                    Sheet sheet = workbook.getSheetAt(0);
                    Row headerRow = sheet.getRow(0);
                    if (headerRow == null
                            || !headerRow.getCell(0).getStringCellValue().equalsIgnoreCase("Email")
                            || !headerRow.getCell(1).getStringCellValue().equalsIgnoreCase("FullName")
                            || !headerRow.getCell(2).getStringCellValue().equalsIgnoreCase("RollNumber")) {

                        session.setAttribute("notificationErr", "File format is incorrect. Please download the templete!");
                        response.sendRedirect("class-list");
                        return;
                    }
                    Iterator<Row> rowIterator = sheet.iterator();

                    AccountDAO accountDAO = new AccountDAO();
                    while (rowIterator.hasNext()) {
                        Row row = rowIterator.next();
                        if (row.getRowNum() == 0) {
                            continue; // skip header row
                        }
                        String email = row.getCell(0).getStringCellValue();
                        String fullName = row.getCell(1).getStringCellValue();
                        String rollNumber = row.getCell(2).getStringCellValue();

                        // Add other fields as needed
                        Account account = accountDAO.getByEmail(email);
                        if (account == null) {
                            Role r = roleDAO.getById(2);// Assuming 2 is the roleId for students
                            account = new Account();
                            account.setEmail(email);
                            String username = email.split("@")[0];
                            account.setUsername(username);
                            String password = PasswordUtils.generateRandomPassword();
                            account.setPassword(password);
                            account.setRoleAccount(r); // 
                            boolean isAdded = accountDAO.create(account);
                            if (isAdded) {

                                Student student = new Student();
                                student.setAccount(accountDAO.getLatestAccount());
                                student.setFullName(fullName);
                                student.setRollNumber(rollNumber);
                                boolean isAddStudent = studentDAO.create(student);
                                if (isAddStudent) {
                                    sendMail.sendMailErrol(email, username, password);
                                    userClassDAO.addStudentToClass(studentDAO.getLastestStudent().getStudent_id(), cid);
                                    session.setAttribute("notification", "Import successfully");

                                }
                            }
                        } else {
                            boolean isExistStudentInClass = userClassDAO.isStudentInClass(studentDAO.getByAccountId(account.getId()).getStudent_id());
                            if (isExistStudentInClass) {
                                existingStudents.add("Student with email " + email + " already in this class or other class \n");
                                continue;
                            }
                            userClassDAO.addStudentToClass(studentDAO.getByAccountId(account.getId()).getStudent_id(), cid);
                            session.setAttribute("notification", "Import successfully");
                        }
                    }
                } catch (Exception e) {
                    session.setAttribute("notificationErr", "Somthing went wrong. Please try again!");
                    response.sendRedirect("class-list");
                    return;
                }
                if (!existingStudents.isEmpty()) {
                    session.setAttribute("notificationErr", existingStudents);
                }
                response.sendRedirect("class-list");
            }
            if (action.equals("add")) {

                String name = request.getParameter("name");
                int lid = Integer.parseInt(request.getParameter("lid"));
                Lecturer l = lecturerDAO.getById(lid);
                model.Class newClass = new model.Class(0, name, l, "ACTIVE", 0);

                boolean isAdded = classsDAO.addClass(newClass);
                if (isAdded) {
                    session.setAttribute("notification", "Add class successfully");
                    response.sendRedirect("class-list");
                } else {
                    session.setAttribute("notificationErr", "Add failed! Please try again!");
                    response.sendRedirect("class-list");
                }

            }
        } else {
            response.sendRedirect("../login");
        }
    }

}
