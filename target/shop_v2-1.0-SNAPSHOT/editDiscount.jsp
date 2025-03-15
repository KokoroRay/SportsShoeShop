<%-- 
    Document   : editDiscount
    Created on : Mar 12, 2025, 9:17:31 PM
    Author     : Nguyen Minh Tam - CE181522
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.Product, dao.ProductDAO, java.text.SimpleDateFormat, model.User" %>
<%
    // Kiểm tra quyền admin
    User user = (User) session.getAttribute("user");
    if (user == null || !user.getRole().equals("admin")) {
        response.sendRedirect("login.jsp");
        return;
    }

    ProductDAO productDAO = new ProductDAO();
    Product product = null;

    // Lấy thông tin sản phẩm từ parameter
    String productId = request.getParameter("productId");
    if (productId != null && !productId.isEmpty()) {
        product = productDAO.getProductById(productId);
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Quản lý khuyến mãi</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <style>
            .container {
                max-width: 600px;
                margin-top: 50px;
            }
            .card {
                box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            }
            .product-image {
                max-width: 200px;
                margin: 0 auto;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <div class="card p-4">
                <h3 class="text-center mb-4">Cập nhật thông tin khuyến mãi</h3>

                <% if (product != null) {%>
                <div class="text-center mb-4">
                    <img src="<%= product.getImage()%>" 
                         alt="<%= product.getProduct_Name()%>" 
                         class="product-image img-fluid">
                    <h4 class="mt-3"><%= product.getProduct_Name()%></h4>
                </div>

                <form action="UpdateDiscountServlet" method="POST">
                    <input type="hidden" name="productId" value="<%= product.getProduct_ID()%>">

                    <!-- Giá gốc -->
                    <div class="mb-3">
                        <label class="form-label">Giá gốc ($)</label>
                        <input type="number" 
                               class="form-control"
                               name="originalPrice"
                               step="0.01"
                               value="<%= product.getOriginalPrice()%>"
                               required>
                    </div>

                    <!-- % Giảm giá -->
                    <div class="mb-3">
                        <label class="form-label">% Giảm giá</label>
                        <input type="number" 
                               class="form-control"
                               name="discountPercent"
                               min="0"
                               max="100"
                               step="0.1"
                               value="<%= product.getDiscountPercent()%>"
                               required>
                    </div>

                    <!-- Ngày bắt đầu -->
                    <div class="mb-3">
                        <label class="form-label">Ngày bắt đầu</label>
                        <input type="date" 
                               class="form-control"
                               name="discountStartDate" 
                               value="<%= (product.getDiscountStartDate() != null)
                       ? new SimpleDateFormat("yyyy-MM-dd").format(product.getDiscountStartDate())
                       : ""%>"
                               required> <!-- Thêm required -->
                    </div>

                    <!-- Ngày kết thúc -->
                    <div class="mb-3">
                        <label class="form-label">Ngày kết thúc</label>
                        <input type="date" 
                               class="form-control"
                               name="discountEndDate"
                               value="<%= (product.getDiscountEndDate() != null)
                       ? new SimpleDateFormat("yyyy-MM-dd").format(product.getDiscountEndDate())
                       : ""%>"
                               required> <!-- Thêm required -->
                    </div>

                    <div class="d-grid gap-2">
                        <button type="submit" class="btn btn-primary">Lưu thay đổi</button>
                        <a href="manage-products.jsp" class="btn btn-secondary">Quay lại</a>
                    </div>
                </form>
                <% } else { %>
                <div class="alert alert-danger">
                    Không tìm thấy sản phẩm!
                </div>
                <% }%>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>