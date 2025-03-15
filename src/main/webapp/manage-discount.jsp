<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="model.User, model.Product, dao.ProductDAO, java.util.List, java.text.SimpleDateFormat" %>
<%
    User loggedInUser = (User) session.getAttribute("user");
    if (loggedInUser == null || !"admin".equals(loggedInUser.getRole())) {
        response.sendRedirect("login.jsp");
        return;
    }

    ProductDAO productDAO = new ProductDAO();
    int pageSize = 10;
    int currentPage = request.getParameter("page") != null ? Integer.parseInt(request.getParameter("page")) : 1;
    int offset = (currentPage - 1) * pageSize;
    int totalProducts = productDAO.getProductsCount();
    List<Product> productList = productDAO.getAllProducts();
    int totalPages = (int) Math.ceil((double) totalProducts / pageSize);
%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Manage Discounts</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.1/css/all.min.css">
        <style>
            body {
                font-family: "Segoe UI", sans-serif;
                background-color: #f4f6f9;
                margin: 0;
                padding: 0;
            }
            /* Top Navbar */
            .navbar {
                background-color: #0d6efd;
                box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            }
            .navbar-brand {
                color: #fff !important;
                font-weight: bold;
                margin-left: 1rem;
            }
            .navbar-nav .nav-link {
                color: #fff !important;
            }
            /* Sidebar */
            .sidebar {
                position: fixed;
                top: 56px;
                left: 0;
                bottom: 0;
                width: 220px;
                background-color: #343a40;
                padding-top: 20px;
                overflow-y: auto;
            }
            .sidebar .sidebar-header {
                text-align: center;
                font-size: 1.2rem;
                color: #fff;
                margin-bottom: 1rem;
            }
            .sidebar a {
                display: block;
                padding: 10px 20px;
                color: #adb5bd;
                text-decoration: none;
                transition: background 0.3s;
            }
            .sidebar a:hover,
            .sidebar a.active {
                background-color: #0d6efd;
                color: #fff;
            }
            .main-content {
                margin-left: 220px;
                margin-top: 56px;
                padding: 20px;
            }
            .table-container {
                background: #fff;
                padding: 20px;
                border-radius: 8px;
                box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            }
            .img-thumbnail {
                max-width: 80px;
                height: auto;
            }
        </style>
    </head>
    <body>
        <!-- Top Navbar -->
        <nav class="navbar navbar-expand-lg navbar-dark">
            <a class="navbar-brand" href="#"><i class="fas fa-rocket"></i> Group 7</a>
            <div class="collapse navbar-collapse" id="topNav">
                <ul class="navbar-nav ml-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="profile.jsp"><i class="fas fa-user-circle"></i> <%= loggedInUser.getUserName()%></a>
                    </li>
                </ul>
            </div>
        </nav>

        <!-- Sidebar (Giống hệt trang quản lý sản phẩm) -->
        <div class="sidebar">
            <div class="sidebar-header">
                <i class="fas fa-user-shield"></i> Admin
            </div>
            <a href="dashboard.jsp"><i class="fas fa-tachometer-alt mr-2"></i> Dashboard</a>
            <a href="manage-orders.jsp"><i class="fas fa-shopping-cart mr-2"></i> Manage Orders</a>
            <a href="manage-products.jsp"><i class="fas fa-box-open mr-2"></i> Manage Products</a>
            <a href="manage-users.jsp"><i class="fas fa-users mr-2"></i> Manage Users</a>
            <a href="manage-discount.jsp" class="active"><i class="fas fa-tag mr-2"></i> Manage Discounts</a>
            <a href="manage-trademark.jsp"><i class="fas fa-copyright mr-2"></i> Manage Trademark</a>
        </div>

        <!-- Main Content -->
        <div class="main-content">
            <!-- Thông báo -->
            <% if (request.getParameter("status") != null) {%>
            <div class="alert alert-<%= request.getParameter("status").equals("success") ? "success" : "danger"%> alert-dismissible fade show">
                <%= request.getParameter("status").equals("success") ? "Operation successful!" : "An error occurred!"%>
                <button type="button" class="close" data-dismiss="alert">&times;</button>
            </div>
            <% } %>

            <!-- Bảng danh sách -->
            <div class="table-container">
                <h3 class="text-primary mb-4">Discount Management</h3>

                <div class="table-responsive">
                    <table class="table table-bordered table-hover">
                        <thead class="thead-dark">
                            <tr>
                                <th>Image</th>
                                <th>Product Name</th>
                                <th>Current Price</th>
                                <th>Discount (%)</th>
                                <th>Status</th>
                                <th>Start Date</th>
                                <th>End Date</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% if (productList.isEmpty()) { %>
                            <tr>
                                <td colspan="8" class="text-center">No data available</td>
                            </tr>
                            <% } else {
                                SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy");
                                for (Product product : productList) {
                                    boolean isActive = productDAO.isDiscountActive(product);
                                    String startDate = product.getDiscountStartDate() != null ? displayFormat.format(product.getDiscountStartDate()) : "N/A";
                                    String endDate = product.getDiscountEndDate() != null ? displayFormat.format(product.getDiscountEndDate()) : "N/A";
                            %>
                            <tr>
                                <td><img src="<%= product.getImage()%>" class="img-thumbnail"></td>
                                <td><%= product.getProduct_Name()%></td>
                                <td>$<%= String.format("%.2f", product.getPrice())%></td>
                                <td><%= product.getDiscountPercent()%>%</td>
                                <td>
                                    <span class="badge <%= isActive ? "badge-success" : "badge-danger"%>">
                                        <%= isActive ? "Active" : "Inactive"%>
                                    </span>
                                </td>
                                <td><%= startDate%></td>
                                <td><%= endDate%></td>
                                <td>
                                    <button class="btn btn-sm <%= product.getDiscountPercent() > 0 ? "btn-warning" : "btn-success" %>" 
                                            data-bs-toggle="modal" 
                                            data-bs-target="#discountModal"
                                            data-product-id="<%= product.getProduct_ID()%>"
                                            data-discount-percent="<%= product.getDiscountPercent()%>"
                                            data-start-date="<%= product.getDiscountStartDate() != null ? new SimpleDateFormat("yyyy-MM-dd").format(product.getDiscountStartDate()) : "" %>"
                                            data-end-date="<%= product.getDiscountEndDate() != null ? new SimpleDateFormat("yyyy-MM-dd").format(product.getDiscountEndDate()) : "" %>">
                                        <%= product.getDiscountPercent() > 0 ? "Edit" : "Add Discount" %>
                                    </button>
                                </td>
                            </tr>
                            <% }
                                } %>
                        </tbody>
                    </table>
                </div>

                <!-- Phân trang -->
                <nav aria-label="Page navigation">
                    <ul class="pagination justify-content-center">
                        <% for (int i = 1; i <= totalPages; i++) {%>
                        <li class="page-item <%= (i == currentPage) ? "active" : ""%>">
                            <a class="page-link" href="manage-discount.jsp?page=<%= i%>"><%= i%></a>
                        </li>
                        <% }%>
                    </ul>
                </nav>
            </div>
        </div>

        <!-- Modal -->
        <div class="modal fade" id="discountModal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="modalTitle"></h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <form action="UpdateDiscountServlet" method="post">
                        <div class="modal-body">
                            <input type="hidden" name="productId" id="modalProductId">

                            <div class="mb-3">
                                <label>Discount Percentage (%)</label>
                                <input type="number" class="form-control" name="discountPercent" 
                                       min="0" max="100" step="0.1" required>
                            </div>

                            <div class="mb-3">
                                <label>Start Date</label>
                                <input type="date" class="form-control" name="discountStartDate" required>
                            </div>

                            <div class="mb-3">
                                <label>End Date</label>
                                <input type="date" class="form-control" name="discountEndDate" required>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                            <button type="submit" class="btn btn-primary">Save Changes</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <script>
            // Modal Handling
            const discountModal = document.getElementById('discountModal');
            discountModal.addEventListener('show.bs.modal', event => {
                const button = event.relatedTarget;

                // Extract data from button
                const productId = button.getAttribute('data-product-id');
                const discountPercent = button.getAttribute('data-discount-percent') || 0;
                const startDate = button.getAttribute('data-start-date');
                const endDate = button.getAttribute('data-end-date');

                // Update modal title
                const modalTitle = discountModal.querySelector('.modal-title');
                modalTitle.textContent = discountPercent > 0 ? 'Edit Discount' : 'Add New Discount';

                // Update form fields
                discountModal.querySelector('#modalProductId').value = productId;
                discountModal.querySelector('[name="discountPercent"]').value = discountPercent;
                discountModal.querySelector('[name="discountStartDate"]').value = startDate;
                discountModal.querySelector('[name="discountEndDate"]').value = endDate;
            });
        </script>
    </body>
</html>