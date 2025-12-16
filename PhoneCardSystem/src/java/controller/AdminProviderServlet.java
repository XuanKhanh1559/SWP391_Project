package controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import dal.ProviderDao;
import model.Provider;
import model.User;

public class AdminProviderServlet extends HttpServlet {

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
        if (!"admin".equalsIgnoreCase(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/products");
            return;
        }
        
        String statusParam = request.getParameter("status");
        String deletedParam = request.getParameter("deleted");
        String searchParam = request.getParameter("search");
        String pageParam = request.getParameter("page");
        
        Integer status = null;
        if (statusParam != null && !statusParam.trim().isEmpty()) {
            try {
                status = Integer.parseInt(statusParam);
            } catch (NumberFormatException e) {
            }
        }
        
        Integer deleted = null;
        if (deletedParam != null && !deletedParam.trim().isEmpty()) {
            try {
                deleted = Integer.parseInt(deletedParam);
            } catch (NumberFormatException e) {
            }
        }
        
        int page = 1;
        if (pageParam != null && !pageParam.trim().isEmpty()) {
            try {
                page = Integer.parseInt(pageParam);
                if (page < 1) {
                    page = 1;
                }
            } catch (NumberFormatException e) {
                page = 1;
            }
        }
        
        int pageSize = 15;
        
        ProviderDao providerDao = new ProviderDao();
        List<Provider> providers = providerDao.getAllProviders(searchParam, status, deleted, page, pageSize);
        int totalProviders = providerDao.countProviders(searchParam, status, deleted);
        int totalPages = (int) Math.ceil((double) totalProviders / pageSize);
        
        request.setAttribute("providers", providers);
        request.setAttribute("selectedStatus", statusParam);
        request.setAttribute("selectedDeleted", deletedParam);
        request.setAttribute("searchTerm", searchParam);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalProviders", totalProviders);
        
        request.getRequestDispatcher("/admin/providers-management.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Admin Provider Management Servlet";
    }
}

