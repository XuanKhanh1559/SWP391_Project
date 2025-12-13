<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sản phẩm - Hệ Thống Bán Thẻ Điện Thoại</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
    <div id="header-placeholder"></div>

    <main class="main-content">
        <div class="page-header">
            <h2>Sản phẩm</h2>
        </div>
        <form method="GET" action="${pageContext.request.contextPath}/products" id="filterForm">
            <div class="filter-bar">
                <select name="provider" id="filterProvider" class="filter-select">
                    <option value="">Tất cả nhà mạng</option>
                    <option value="1" ${selectedProvider == 1 ? 'selected' : ''}>Viettel</option>
                    <option value="2" ${selectedProvider == 2 ? 'selected' : ''}>Vinaphone</option>
                    <option value="3" ${selectedProvider == 3 ? 'selected' : ''}>Mobifone</option>
                </select>
                <select name="type" id="filterType" class="filter-select">
                    <option value="">Tất cả loại</option>
                    <option value="topup" ${selectedType == 'topup' ? 'selected' : ''}>Nạp tiền</option>
                    <option value="data_package" ${selectedType == 'data_package' ? 'selected' : ''}>Gói data</option>
                </select>
                <input type="text" name="search" id="searchProduct" class="search-input" placeholder="Tìm kiếm sản phẩm..." value="${searchTerm != null ? searchTerm : ''}">
                <button type="submit" class="btn btn-primary">Tìm kiếm</button>
            </div>
        </form>
        <div class="products-grid" id="productsGrid">
            <c:choose>
                <c:when test="${products != null && !products.isEmpty()}">
                    <c:forEach var="product" items="${products}">
                        <div class="product-card">
                            <div class="product-image">
                                <i class="fas fa-mobile-alt"></i>
                            </div>
                            <div class="product-info">
                                <h3>${product.name}</h3>
                                <p>${product.description != null ? product.description : ''}</p>
                                <div class="product-price">
                                    <fmt:formatNumber value="${product.price}" type="currency" currencyCode="VND" pattern="#,##0 đ"/>
                                </div>
                                <div class="product-actions">
                                    <a href="${pageContext.request.contextPath}/product-detail?id=${product.id}" class="btn btn-outline">Xem chi tiết</a>
                                    <c:if test="${sessionScope.user != null}">
                                        <button class="btn btn-primary btn-add-cart" onclick="addToCart(${product.id})">
                                            <i class="fas fa-cart-plus"></i> Thêm vào giỏ
                                        </button>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <div class="empty-state">
                        <p>Không tìm thấy sản phẩm nào</p>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
        
        <c:if test="${totalPages > 1}">
            <div class="pagination">
                <c:set var="baseUrl" value="${pageContext.request.contextPath}/products"/>
                <c:set var="queryParams" value=""/>
                <c:if test="${selectedProvider != null}">
                    <c:set var="queryParams" value="${queryParams}&provider=${selectedProvider}"/>
                </c:if>
                <c:if test="${selectedType != null && selectedType != ''}">
                    <c:set var="queryParams" value="${queryParams}&type=${selectedType}"/>
                </c:if>
                <c:if test="${searchTerm != null && searchTerm != ''}">
                    <c:set var="queryParams" value="${queryParams}&search=${searchTerm}"/>
                </c:if>
                
                <c:if test="${currentPage > 1}">
                    <a href="${baseUrl}?page=${currentPage - 1}${queryParams}" class="pagination-link">
                        <i class="fas fa-chevron-left"></i> Trước
                    </a>
                </c:if>
                
                <c:forEach var="i" begin="1" end="${totalPages}">
                    <c:choose>
                        <c:when test="${i == currentPage}">
                            <span class="pagination-link active">${i}</span>
                        </c:when>
                        <c:when test="${i == 1 || i == totalPages || (i >= currentPage - 2 && i <= currentPage + 2)}">
                            <a href="${baseUrl}?page=${i}${queryParams}" class="pagination-link">${i}</a>
                        </c:when>
                        <c:when test="${i == currentPage - 3 || i == currentPage + 3}">
                            <span class="pagination-ellipsis">...</span>
                        </c:when>
                    </c:choose>
                </c:forEach>
                
                <c:if test="${currentPage < totalPages}">
                    <a href="${baseUrl}?page=${currentPage + 1}${queryParams}" class="pagination-link">
                        Sau <i class="fas fa-chevron-right"></i>
                    </a>
                </c:if>
            </div>
        </c:if>
        
        <c:if test="${totalProducts > 0}">
            <div class="pagination-info">
                <span>Hiển thị ${(currentPage - 1) * 15 + 1} - ${currentPage * 15 > totalProducts ? totalProducts : currentPage * 15} trong tổng số ${totalProducts} sản phẩm</span>
            </div>
        </c:if>
    </main>

    <div id="footer-placeholder"></div>

    <script>
        window.productsData = [
            <c:forEach var="product" items="${products}" varStatus="loop">
            {
                id: ${product.id},
                provider_id: ${product.provider_id},
                name: "<c:out value="${product.name}" escapeXml="true"/>",
                type: ${product.type},
                denomination: ${product.denomination},
                price: ${product.price},
                description: "<c:out value="${product.description != null ? product.description : ''}" escapeXml="true"/>",
                status: ${product.status}
            }<c:if test="${!loop.last}">,</c:if>
            </c:forEach>
        ];
    </script>
    <script src="${pageContext.request.contextPath}/js/layout.js"></script>
    <script src="${pageContext.request.contextPath}/js/app.js"></script>
    <script src="${pageContext.request.contextPath}/js/products.js"></script>
</body>
</html>

