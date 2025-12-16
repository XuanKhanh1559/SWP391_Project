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

public class EditProviderServlet extends HttpServlet {

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
        
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/providers");
            return;
        }
        
        int id;
        try {
            id = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/providers");
            return;
        }
        
        ProviderDao providerDao = new ProviderDao();
        Provider provider = providerDao.getProviderById(id);
        
        if (provider == null) {
            response.sendRedirect(request.getContextPath() + "/admin/providers");
            return;
        }
        
        request.setAttribute("provider", provider);
        request.getRequestDispatcher("/admin/edit-provider.jsp").forward(request, response);
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
        
        String idParam = request.getParameter("id");
        String name = request.getParameter("name");
        String code = request.getParameter("code");
        String description = request.getParameter("description");
        String statusParam = request.getParameter("status");
        
        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/providers");
            return;
        }
        
        int id;
        try {
            id = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/providers");
            return;
        }
        
        if (name == null || name.trim().isEmpty() || code == null || code.trim().isEmpty()) {
            request.setAttribute("error", "Tên và mã nhà cung cấp không được để trống");
            response.sendRedirect(request.getContextPath() + "/admin/edit-provider?id=" + id);
            return;
        }
        
        ProviderDao providerDao = new ProviderDao();
        Provider provider = providerDao.getProviderById(id);
        
        if (provider == null) {
            response.sendRedirect(request.getContextPath() + "/admin/providers");
            return;
        }
        
        Provider existingProvider = providerDao.getProviderByCode(code.trim());
        if (existingProvider != null && existingProvider.getId() != id) {
            request.setAttribute("error", "Mã nhà cung cấp đã tồn tại");
            request.setAttribute("provider", provider);
            request.getRequestDispatcher("/admin/edit-provider.jsp").forward(request, response);
            return;
        }
        
        provider.setName(name.trim());
        provider.setCode(code.trim());
        provider.setDescription(description != null ? description.trim() : "");
        provider.setStatus(statusParam != null && "1".equals(statusParam) ? 1 : 0);
        
        if (providerDao.updateProvider(provider)) {
            session.setAttribute("successMessage", "Cập nhật nhà cung cấp thành công!");
            response.sendRedirect(request.getContextPath() + "/admin/providers");
        } else {
            request.setAttribute("error", "Có lỗi xảy ra khi cập nhật nhà cung cấp");
            request.setAttribute("provider", provider);
            request.getRequestDispatcher("/admin/edit-provider.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Edit Provider Servlet";
    }
}

