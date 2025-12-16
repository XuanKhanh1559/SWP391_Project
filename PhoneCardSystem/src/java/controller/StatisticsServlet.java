package controller;

import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import dal.StatisticsDao;
import model.User;

public class StatisticsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        if (!"admin".equalsIgnoreCase(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }
        
        String action = request.getParameter("action");
        
        if ("getRevenue".equals(action) || "getUsers".equals(action)) {
            handleAjaxRequest(request, response);
        } else {
            request.getRequestDispatcher("/admin/statistics.jsp").forward(request, response);
        }
    }

    private void handleAjaxRequest(HttpServletRequest request, HttpServletResponse response) 
    throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        String period = request.getParameter("period");
        String year = request.getParameter("year");
        String month = request.getParameter("month");
        String action = request.getParameter("action");
        
        // Default values
        if (period == null || period.trim().isEmpty()) {
            period = "month";
        }
        if (year == null || year.trim().isEmpty()) {
            year = String.valueOf(LocalDate.now().getYear());
        }
        if (month == null || month.trim().isEmpty()) {
            month = String.valueOf(LocalDate.now().getMonthValue());
        }
        
        StatisticsDao statisticsDao = new StatisticsDao();
        JsonObject result;
        
        if ("getRevenue".equals(action)) {
            result = statisticsDao.getRevenueStatistics(period, year, month);
        } else {
            result = statisticsDao.getUserStatistics(period, year, month);
        }
        
        out.print(result.toString());
        out.flush();
    }

    @Override
    public String getServletInfo() {
        return "Statistics Servlet";
    }
}

