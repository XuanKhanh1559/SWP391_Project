package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import dal.UserDao;
import model.User;
import config.UserStatus;
import util.PasswordUtil;

public class RegisterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.getRequestDispatcher("/guest/register.jsp").forward(request, response);
    } 

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        
        if (username == null || email == null || password == null || 
            username.trim().isEmpty() || email.trim().isEmpty() || password.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập đầy đủ thông tin bắt buộc");
            request.setAttribute("username", username);
            request.setAttribute("email", email);
            request.setAttribute("phone", phone);
            request.getRequestDispatcher("/guest/register.jsp").forward(request, response);
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Mật khẩu xác nhận không khớp");
            request.setAttribute("username", username);
            request.setAttribute("email", email);
            request.setAttribute("phone", phone);
            request.getRequestDispatcher("/guest/register.jsp").forward(request, response);
            return;
        }
        
        UserDao userDao = new UserDao();
        
        if (userDao.checkEmailExists(email)) {
            request.setAttribute("error", "Email đã được sử dụng");
            request.setAttribute("username", username);
            request.setAttribute("phone", phone);
            request.getRequestDispatcher("/guest/register.jsp").forward(request, response);
            return;
        }
        
        if (userDao.checkUsernameExists(username)) {
            request.setAttribute("error", "Username đã được sử dụng");
            request.setAttribute("email", email);
            request.setAttribute("phone", phone);
            request.getRequestDispatcher("/guest/register.jsp").forward(request, response);
            return;
        }
        
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPhone(phone != null ? phone : "");
        newUser.setPassword(PasswordUtil.hashPassword(password));
        newUser.setBalance(0.0);
        newUser.setStatus(UserStatus.ACTIVE.getValue());
        newUser.setRole("user");
        
        int userId = userDao.signup(newUser);
        
        if (userId > 0) {
            newUser.setId(userId);
            HttpSession session = request.getSession();
            session.setAttribute("user", newUser);
            session.setAttribute("userId", newUser.getId());
            session.setAttribute("username", newUser.getUsername());
            session.setAttribute("email", newUser.getEmail());
            session.setAttribute("balance", newUser.getBalance());
            session.setAttribute("role", newUser.getRole());
            
            response.sendRedirect(request.getContextPath() + "/user/dashboard");
        } else {
            String error = userDao.getLastError();
            if (error == null || error.isEmpty()) {
                error = "Đăng ký thất bại. Vui lòng thử lại";
            }
            request.setAttribute("error", error);
            request.setAttribute("username", username);
            request.setAttribute("email", email);
            request.setAttribute("phone", phone);
            request.getRequestDispatcher("/guest/register.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Register Servlet";
    }
}

