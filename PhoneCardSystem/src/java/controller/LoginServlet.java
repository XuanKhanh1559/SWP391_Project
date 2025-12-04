/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import dal.UserDao;
import model.User;

/**
 *
 * @author admin
 */
public class LoginServlet extends HttpServlet {
   
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        // If GET request, just show login page
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            request.getRequestDispatcher("/guest/login.jsp").forward(request, response);
            return;
        }
        
        // POST request - process login
        String emailOrUsername = request.getParameter("emailOrUsername");
        String password = request.getParameter("password");
        
        if (emailOrUsername == null || password == null || emailOrUsername.trim().isEmpty() || password.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập đầy đủ thông tin");
            request.getRequestDispatcher("/guest/login.jsp").forward(request, response);
            return;
        }
        
        UserDao userDao = new UserDao();
        User user = null;
        
        // Try login by email first
        if (emailOrUsername.contains("@")) {
            user = userDao.signin(emailOrUsername, password);
        } else {
            // Try login by username
            user = userDao.signinByUsername(emailOrUsername, password);
        }
        
        if (user != null) {
            // Login successful
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            session.setAttribute("userId", user.getId());
            session.setAttribute("username", user.getUsername());
            session.setAttribute("email", user.getEmail());
            session.setAttribute("balance", user.getBalance());
            
            // Check if admin (you can add role field later)
            // For now, check by email or username
            if ("admin".equalsIgnoreCase(user.getUsername()) || "admin@admin.com".equalsIgnoreCase(user.getEmail())) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard.jsp");
            } else {
                response.sendRedirect(request.getContextPath() + "/user/dashboard.jsp");
            }
        } else {
            // Login failed
            String error = userDao.getLastError();
            if (error == null || error.isEmpty()) {
                error = "Email/Username hoặc mật khẩu không đúng";
            }
            request.setAttribute("error", error);
            request.getRequestDispatcher("/guest/login.jsp").forward(request, response);
        }
    } 

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    } 

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Login Servlet";
    }
}
