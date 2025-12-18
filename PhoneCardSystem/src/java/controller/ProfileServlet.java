package controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import dal.UserDao;
import dal.PaymentTransactionDao;
import model.User;
import model.PaymentTransaction;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import java.util.List;

public class ProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String action = request.getParameter("action");
        User currentUser = (User) session.getAttribute("user");
        
        if ("getTransactions".equals(action)) {
            handleGetTransactions(request, response, currentUser);
            return;
        }
        
        UserDao userDao = new UserDao();
        User user = userDao.getUserById(currentUser.getId());
        
        if (user != null) {
            request.setAttribute("user", user);
            session.setAttribute("user", user);
            session.setAttribute("balance", user.getBalance());
            session.setAttribute("role", user.getRole());
        }
        
        response.setContentType("text/html;charset=UTF-8");
        request.getRequestDispatcher("/user/profile.jsp").forward(request, response);
    }
    
    private void handleGetTransactions(HttpServletRequest request, HttpServletResponse response, User user)
    throws ServletException, IOException {
        // Set request encoding to UTF-8
        request.setCharacterEncoding("UTF-8");
        
        // Set response encoding and content type
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        
        PrintWriter out = response.getWriter();
        
        int page = 1;
        String pageParam = request.getParameter("page");
        if (pageParam != null && !pageParam.isEmpty()) {
            try {
                page = Integer.parseInt(pageParam);
                if (page < 1) page = 1;
            } catch (NumberFormatException e) {
                page = 1;
            }
        }
        
        int limit = 10;
        int offset = (page - 1) * limit;
        
        PaymentTransactionDao transactionDao = new PaymentTransactionDao();
        List<PaymentTransaction> transactions = transactionDao.getTransactionsByUserId(user.getId(), limit, offset);
        int totalTransactions = transactionDao.countTransactionsByUserId(user.getId());
        
        Gson gson = new Gson();
        JsonObject result = new JsonObject();
        result.addProperty("success", true);
        result.addProperty("currentPage", page);
        result.addProperty("totalPages", (int) Math.ceil((double) totalTransactions / limit));
        result.addProperty("totalTransactions", totalTransactions);
        
        JsonArray jsonArray = new JsonArray();
        for (PaymentTransaction transaction : transactions) {
            JsonObject jsonTransaction = new JsonObject();
            jsonTransaction.addProperty("id", transaction.getId());
            jsonTransaction.addProperty("transaction_code", transaction.getTransaction_code() != null ? transaction.getTransaction_code() : "");
            jsonTransaction.addProperty("type", transaction.getType());
            jsonTransaction.addProperty("amount", transaction.getAmount());
            jsonTransaction.addProperty("balance_before", transaction.getBalance_before());
            jsonTransaction.addProperty("balance_after", transaction.getBalance_after());
            jsonTransaction.addProperty("status", transaction.getStatus());
            // Ensure description is properly encoded
            String description = transaction.getDescription();
            if (description != null) {
                jsonTransaction.addProperty("description", description);
            } else {
                jsonTransaction.addProperty("description", "");
            }
            jsonTransaction.addProperty("created_at", transaction.getCreated_at() != null ? transaction.getCreated_at().getTime() : null);
            jsonArray.add(jsonTransaction);
        }
        result.add("transactions", jsonArray);
        
        // Write JSON with UTF-8 encoding
        out.print(gson.toJson(result));
        out.flush();
        out.close();
    } 

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        User currentUser = (User) session.getAttribute("user");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        
        if (email == null || email.trim().isEmpty()) {
            request.setAttribute("profileError", "Email không được để trống");
            request.getRequestDispatcher("/user/profile.jsp").forward(request, response);
            return;
        }
        
        // Validate phone number if provided
        if (phone != null && !phone.trim().isEmpty()) {
            String phoneTrimmed = phone.trim();
            if (!phoneTrimmed.startsWith("0")) {
                request.setAttribute("profileError", "Số điện thoại phải bắt đầu bằng 0");
                request.setAttribute("user", currentUser);
                request.getRequestDispatcher("/user/profile.jsp").forward(request, response);
                return;
            }
            if (!phoneTrimmed.matches("^0[0-9]{9,10}$")) {
                request.setAttribute("profileError", "Số điện thoại không hợp lệ. Phải bắt đầu bằng 0 và có 10-11 chữ số");
                request.setAttribute("user", currentUser);
                request.getRequestDispatcher("/user/profile.jsp").forward(request, response);
                return;
            }
        }
        
        UserDao userDao = new UserDao();
        
        User existingUser = userDao.getUserByEmail(email);
        if (existingUser != null && existingUser.getId() != currentUser.getId()) {
            request.setAttribute("profileError", "Email đã được sử dụng bởi tài khoản khác");
            request.setAttribute("user", currentUser);
            request.getRequestDispatcher("/user/profile.jsp").forward(request, response);
            return;
        }
        
        boolean updated = userDao.updateProfile(currentUser.getId(), email, phone != null ? phone : "");
        if (updated) {
            User updatedUser = userDao.getUserById(currentUser.getId());
            if (updatedUser != null) {
                session.setAttribute("user", updatedUser);
                session.setAttribute("email", updatedUser.getEmail());
                session.setAttribute("role", updatedUser.getRole());
                request.setAttribute("user", updatedUser);
                request.setAttribute("profileSuccess", "Cập nhật thông tin thành công");
            }
        } else {
            String err = userDao.getLastError();
            request.setAttribute("profileError", err != null ? err : "Cập nhật thông tin thất bại");
            request.setAttribute("user", currentUser);
        }
        
        request.getRequestDispatcher("/user/profile.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Profile Servlet";
    }
}

