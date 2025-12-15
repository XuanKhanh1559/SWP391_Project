package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import dal.UserDao;
import model.User;

public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.getRequestDispatcher("/guest/login.jsp").forward(request, response);
    } 

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        String emailOrUsername = request.getParameter("emailOrUsername");
        String password = request.getParameter("password");
        
        if (emailOrUsername == null || password == null || emailOrUsername.trim().isEmpty() || password.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập đầy đủ thông tin");
            request.getRequestDispatcher("/guest/login.jsp").forward(request, response);
            return;
        }
        
        UserDao userDao = new UserDao();
        User user = null;
        
        if (emailOrUsername.contains("@")) {
            user = userDao.signin(emailOrUsername, password);
        } else {
            user = userDao.signinByUsername(emailOrUsername, password);
        }
        
        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            session.setAttribute("userId", user.getId());
            session.setAttribute("username", user.getUsername());
            session.setAttribute("email", user.getEmail());
            session.setAttribute("balance", user.getBalance());
            session.setAttribute("role", user.getRole());
            
            if ("admin".equalsIgnoreCase(user.getRole())) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            } else {
                response.sendRedirect(request.getContextPath() + "/user/dashboard");
            }
        } else {
            String error = userDao.getLastError();
            if (error == null || error.isEmpty()) {
                error = "Email/Username hoặc mật khẩu không đúng";
            }
            request.setAttribute("error", error);
            request.getRequestDispatcher("/guest/login.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Login Servlet";
    }
}
