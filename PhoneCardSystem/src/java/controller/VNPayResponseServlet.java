package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import dal.UserDao;
import model.User;

public class VNPayResponseServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        String vnp_ResponseCode = request.getParameter("vnp_ResponseCode");
        String amountStr = request.getParameter("vnp_Amount");
        String vnp_TxnRef = request.getParameter("vnp_TxnRef");
        
        String storedTxnRef = (String) session.getAttribute("deposit_txn_ref");
        Double storedAmount = (Double) session.getAttribute("deposit_amount");
        
        if (storedTxnRef == null || !storedTxnRef.equals(vnp_TxnRef)) {
            request.setAttribute("error", "Giao dịch không hợp lệ");
            request.getRequestDispatcher("/user/deposit-result.jsp").forward(request, response);
            return;
        }
        
        if ("00".equals(vnp_ResponseCode)) {
            double amount = storedAmount != null ? storedAmount : (Double.parseDouble(amountStr) / 100);
            
            UserDao userDao = new UserDao();
            boolean success = userDao.deposit(user.getId(), amount);
            
            if (success) {
                User updatedUser = userDao.getUserById(user.getId());
                if (updatedUser != null) {
                    session.setAttribute("user", updatedUser);
                }
                
                request.setAttribute("success", true);
                request.setAttribute("message", "Nạp tiền thành công");
                request.setAttribute("amount", amount);
            } else {
                request.setAttribute("error", "Cập nhật số dư thất bại");
            }
        } else {
            request.setAttribute("error", "Thanh toán thất bại hoặc bị hủy");
        }
        
        session.removeAttribute("deposit_txn_ref");
        session.removeAttribute("deposit_amount");
        
        request.getRequestDispatcher("/user/deposit-result.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "VNPay Response Servlet";
    }
}

