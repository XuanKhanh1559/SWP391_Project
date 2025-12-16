package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import dal.ProviderDao;
import model.Provider;
import model.User;

public class CreateProviderServlet extends HttpServlet {

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
        
        request.getRequestDispatcher("/admin/create-provider.jsp").forward(request, response);
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
        
        User user = (User) session.getAttribute("user");
        if (!"admin".equalsIgnoreCase(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/products");
            return;
        }
        
        String name = request.getParameter("name");
        String code = request.getParameter("code");
        String description = request.getParameter("description");
        String statusParam = request.getParameter("status");
        
        if (name == null || name.trim().isEmpty() || code == null || code.trim().isEmpty()) {
            request.setAttribute("error", "Tên và mã nhà cung cấp không được để trống");
            request.getRequestDispatcher("/admin/create-provider.jsp").forward(request, response);
            return;
        }
        
        ProviderDao providerDao = new ProviderDao();
        
        Provider existingProvider = providerDao.getProviderByCode(code.trim());
        if (existingProvider != null) {
            request.setAttribute("error", "Mã nhà cung cấp đã tồn tại");
            request.setAttribute("name", name);
            request.setAttribute("code", code);
            request.setAttribute("description", description);
            request.setAttribute("status", statusParam);
            request.getRequestDispatcher("/admin/create-provider.jsp").forward(request, response);
            return;
        }
        
        Provider provider = new Provider();
        provider.setName(name.trim());
        provider.setCode(code.trim());
        provider.setDescription(description != null ? description.trim() : "");
        
        int status = 1;
        if (statusParam != null && "0".equals(statusParam)) {
            status = 0;
        }
        provider.setStatus(status);
        
        if (providerDao.createProvider(provider)) {
            session.setAttribute("successMessage", "Thêm nhà cung cấp thành công!");
            response.sendRedirect(request.getContextPath() + "/admin/providers");
        } else {
            request.setAttribute("error", "Có lỗi xảy ra khi thêm nhà cung cấp");
            request.setAttribute("name", name);
            request.setAttribute("code", code);
            request.setAttribute("description", description);
            request.setAttribute("status", statusParam);
            request.getRequestDispatcher("/admin/create-provider.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Create Provider Servlet";
    }
}

