<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*" %>
<%@page import="model.User" %>
<%@page import="dao.OrderDAO" %>
<%@page import="dao.ProductDAO" %>
<%@page import="dao.UserDAO" %>
<%@page import="model.Order" %>
<%@page import="model.Product" %>

<%
    // Kiểm tra quyền admin
    User loggedInUser = (User) session.getAttribute("user");
    if (loggedInUser == null || !"admin".equals(loggedInUser.getRole())) {
        response.sendRedirect("login.jsp");
        return;
    }

    // Khởi tạo DAO
    OrderDAO orderDAO = new OrderDAO();
    ProductDAO productDAO = new ProductDAO();
    UserDAO userDAO = new UserDAO();

    // Lấy danh sách
    List<Order> orderList = orderDAO.getAllOrdersWithDetails();
    List<Product> productList = productDAO.getAllProducts();
    List<User> userList = userDAO.getAllUsers();

    // Tính toán tóm tắt
    int totalOrders = (orderList != null) ? orderList.size() : 0;
    double totalRevenue = 0;
    if (orderList != null) {
        for (Order o : orderList) {
            totalRevenue += o.getTotal_Price();
        }
    }
    int totalProducts = (productList != null) ? productList.size() : 0;
    int totalUsers = (userList != null) ? userList.size() : 0;
%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Admin Dashboard Summary</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <!-- Bootstrap CSS -->
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
        <!-- Font Awesome (icon) -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.1/css/all.min.css">

        <style>
            body {
                font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
                background-color: #f4f6f9;
                margin: 0;
                padding: 0;
            }
            /* Top Navbar */
            .navbar {
                background-color: #0d6efd; /* Xanh dương */
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
                top: 56px; /* chiều cao navbar */
                left: 0;
                bottom: 0;
                width: 220px;
                background-color: #343a40;
                color: #adb5bd;
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
            .sidebar a:hover {
                background-color: #495057;
                color: #fff;
            }
            .sidebar a.active {
                background-color: #0d6efd;
                color: #fff;
            }

            /* Main content */
            .main-content {
                margin-left: 220px; /* tránh đè lên sidebar */
                margin-top: 56px;   /* tránh đè lên navbar */
                padding: 20px;
            }

            /* Cards tóm tắt */
            .info-card {
                background-color: #fff;
                border-radius: 8px;
                box-shadow: 0 2px 4px rgba(0,0,0,0.1);
                padding: 20px;
                text-align: center;
                margin-bottom: 20px;
                transition: transform 0.2s;
            }
            .info-card:hover {
                transform: translateY(-5px);
            }
            .info-card i {
                font-size: 2rem;
                margin-bottom: 10px;
            }
            .info-card h5 {
                font-weight: 600;
                margin-bottom: 10px;
            }
            .info-card p {
                margin: 0;
                font-size: 1.2rem;
            }
        </style>
    </head>
    <body>
        <!-- Top Navbar -->
        <nav class="navbar navbar-expand-lg navbar-dark">
            <a class="navbar-brand" href="#">
                <i class="fas fa-rocket"></i> Group 7
            </a>
            <div class="collapse navbar-collapse" id="topNav">
                <ul class="navbar-nav ml-auto">
                    <li class="nav-item">
                        <!-- Tên Admin, avatar, logout -->
                        <a class="nav-link" href="profile.jsp"><i class="fas fa-user-circle"></i> <%= loggedInUser.getUserName()%></a>
                    </li>
                </ul>
            </div>
        </nav>

        <!-- Sidebar -->
        <div class="sidebar">
            <div class="sidebar-header">
                <i class="fas fa-user-shield"></i> Admin
            </div>
            <a href="dashboard.jsp"class="active"><i class="fas fa-tachometer-alt mr-2"></i> Dashboard</a>
            <a href="manage-orders.jsp"><i class="fas fa-shopping-cart mr-2"></i> Manage Orders</a>
            <a href="manage-products.jsp"><i class="fas fa-box-open mr-2"></i> Manage Products</a>
            <a href="manage-users.jsp"><i class="fas fa-users mr-2"></i> Manage Users</a>
            <a href="manage-discount.jsp"><i class="fas fa-tag mr-2"></i> Manage Discounts</a>
            <a href="manage-trademark.jsp"><i class="fas fa-copyright mr-2"></i> Manage Trademark</a>
            <a href="adminNotifications.jsp?page=users"><i class="fas fa-bell mr-2"></i> Notification Order</a>

        </div>

        <!-- Main Content -->
        <div class="main-content">
            <h3>Dashboard Summary</h3>
            <hr>

            <!-- Hàng 1: Orders + Revenue -->
            <div class="row">
                <div class="col-md-6">
                    <div class="info-card">
                        <i class="fas fa-shopping-cart text-primary"></i>
                        <h5>Total Orders</h5>
                        <p><%= totalOrders%></p>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="info-card">
                        <i class="fas fa-dollar-sign text-success"></i>
                        <h5>Total Revenue</h5>
                        <p>$<%= String.format("%.2f", totalRevenue)%></p>
                    </div>
                </div>
            </div>

            <!-- Hàng 2: Products + Users -->
            <div class="row">
                <div class="col-md-6">
                    <div class="info-card">
                        <i class="fas fa-box-open text-info"></i>
                        <h5>Total Products</h5>
                        <p><%= totalProducts%></p>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="info-card">
                        <i class="fas fa-users text-warning"></i>
                        <h5>Total Users</h5>
                        <p><%= totalUsers%></p>
                    </div>
                </div>
            </div>

            <!-- Bạn có thể thêm biểu đồ, list, hay card khác tùy ý -->
        </div>

        <!-- Bootstrap JS, Popper.js -->
        <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
