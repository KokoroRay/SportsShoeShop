<%@page import="java.util.*" %>
<%@page import="model.OrderItem" %>
<%@page import="model.Order" %>
<%@page import="model.User" %>
<%@page import="dao.OrderDAO" %>
<%@page import="dao.ProductDAO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    // Kiểm tra quyền admin
    User user = (User) session.getAttribute("user");
    if (user == null || !"admin".equals(user.getRole())) {
        response.sendRedirect("login.jsp");
        return;
    }

    // Khởi tạo DAO
    OrderDAO orderDAO = new OrderDAO();
    ProductDAO productDAO = new ProductDAO();

    // Lấy danh sách đơn hàng (có đầy đủ thông tin)
    List<Order> orderList = orderDAO.getAllOrdersWithDetails();

    // Các map thống kê
    Map<Integer, Double> yearlyRevenue = new HashMap<>();
    Map<Integer, Double> monthlyRevenue = new HashMap<>();
    Map<String, Integer> weeklyBrandCount = new HashMap<>();
    Map<String, Integer> monthlyBrandCount = new HashMap<>();

    // Xác định ngày hiện tại
    Calendar now = Calendar.getInstance();
    int currentWeek = now.get(Calendar.WEEK_OF_YEAR);
    int currentMonth = now.get(Calendar.MONTH) + 1; // MONTH là 0-based
    int currentYear = now.get(Calendar.YEAR);

    // Tính toán doanh thu và số lượng hãng
    if (orderList != null) {
        for (Order order : orderList) {
            Date orderDate = order.getOrder_Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(orderDate);

            int orderYear = cal.get(Calendar.YEAR);
            int orderMonth = cal.get(Calendar.MONTH) + 1;
            int orderWeek = cal.get(Calendar.WEEK_OF_YEAR);

            double orderTotal = order.getTotal_Price();

            // Doanh thu theo năm
            yearlyRevenue.put(orderYear, yearlyRevenue.getOrDefault(orderYear, 0.0) + orderTotal);

            // Doanh thu theo tháng (không phân biệt năm)
            monthlyRevenue.put(orderMonth, monthlyRevenue.getOrDefault(orderMonth, 0.0) + orderTotal);

            // Nếu đơn hàng thuộc tuần hiện tại
            if (orderYear == currentYear && orderWeek == currentWeek) {
                List<OrderItem> items = order.getItems();
                if (items != null) {
                    for (OrderItem item : items) {
                        String productIdStr = String.valueOf(item.getProduct_Id());
                        String brand = "";
                        try {
                            model.Product product = productDAO.getProductById(productIdStr);
                            if (product != null) {
                                brand = product.getBrand();
                            }
                        } catch (Exception e) {
                        }
                        if (brand != null && !brand.isEmpty()) {
                            int quantity = item.getQuantity();
                            weeklyBrandCount.put(brand, weeklyBrandCount.getOrDefault(brand, 0) + quantity);
                        }
                    }
                }
            }

            // Nếu đơn hàng thuộc tháng hiện tại
            if (orderYear == currentYear && orderMonth == currentMonth) {
                List<OrderItem> items = order.getItems();
                if (items != null) {
                    for (OrderItem item : items) {
                        String productIdStr = String.valueOf(item.getProduct_Id());
                        String brand = "";
                        try {
                            model.Product product = productDAO.getProductById(productIdStr);
                            if (product != null) {
                                brand = product.getBrand();
                            }
                        } catch (Exception e) {
                        }
                        if (brand != null && !brand.isEmpty()) {
                            int quantity = item.getQuantity();
                            monthlyBrandCount.put(brand, monthlyBrandCount.getOrDefault(brand, 0) + quantity);
                        }
                    }
                }
            }
        }
    }

    // Chuyển dữ liệu thống kê sang dạng JSON (chuỗi) cho Chart.js
    // Doanh thu theo năm (Pie)
    List<Integer> sortedYears = new ArrayList<>(yearlyRevenue.keySet());
    Collections.sort(sortedYears);
    StringBuilder yearlyLabels = new StringBuilder("[");
    StringBuilder yearlyData = new StringBuilder("[");
    for (int i = 0; i < sortedYears.size(); i++) {
        int year = sortedYears.get(i);
        yearlyLabels.append("\"").append(year).append("\"");
        yearlyData.append(yearlyRevenue.get(year));
        if (i < sortedYears.size() - 1) {
            yearlyLabels.append(",");
            yearlyData.append(",");
        }
    }
    yearlyLabels.append("]");
    yearlyData.append("]");

    // Doanh thu theo tháng (Bar)
    List<Integer> sortedMonths = new ArrayList<>(monthlyRevenue.keySet());
    Collections.sort(sortedMonths);
    StringBuilder monthlyLabels = new StringBuilder("[");
    StringBuilder monthlyData = new StringBuilder("[");
    for (int i = 0; i < sortedMonths.size(); i++) {
        int month = sortedMonths.get(i);
        monthlyLabels.append("\"Tháng ").append(month).append("\"");
        monthlyData.append(monthlyRevenue.get(month));
        if (i < sortedMonths.size() - 1) {
            monthlyLabels.append(",");
            monthlyData.append(",");
        }
    }
    monthlyLabels.append("]");
    monthlyData.append("]");

    // Hãng giày (tuần) (Pie)
    List<String> sortedWeeklyBrands = new ArrayList<>(weeklyBrandCount.keySet());
    Collections.sort(sortedWeeklyBrands);
    StringBuilder weeklyBrandLabels = new StringBuilder("[");
    StringBuilder weeklyBrandData = new StringBuilder("[");
    for (int i = 0; i < sortedWeeklyBrands.size(); i++) {
        String brand = sortedWeeklyBrands.get(i);
        weeklyBrandLabels.append("\"").append(brand).append("\"");
        weeklyBrandData.append(weeklyBrandCount.get(brand));
        if (i < sortedWeeklyBrands.size() - 1) {
            weeklyBrandLabels.append(",");
            weeklyBrandData.append(",");
        }
    }
    weeklyBrandLabels.append("]");
    weeklyBrandData.append("]");

    // Hãng giày (tháng) (Pie)
    List<String> sortedMonthlyBrands = new ArrayList<>(monthlyBrandCount.keySet());
    Collections.sort(sortedMonthlyBrands);
    StringBuilder monthlyBrandLabels = new StringBuilder("[");
    StringBuilder monthlyBrandData = new StringBuilder("[");
    for (int i = 0; i < sortedMonthlyBrands.size(); i++) {
        String brand = sortedMonthlyBrands.get(i);
        monthlyBrandLabels.append("\"").append(brand).append("\"");
        monthlyBrandData.append(monthlyBrandCount.get(brand));
        if (i < sortedMonthlyBrands.size() - 1) {
            monthlyBrandLabels.append(",");
            monthlyBrandData.append(",");
        }
    }
    monthlyBrandLabels.append("]");
    monthlyBrandData.append("]");
%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Manage Orders</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <!-- Bootstrap CSS -->
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
        <!-- Font Awesome for icons -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.1/css/all.min.css">

        <!-- Custom CSS (phong cách Pluto) -->
        <style>
            body {
                background-color: #f4f6f9;
                margin: 0;
                padding: 0;
                font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
            }
            /* Navbar trên cùng */
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
            /* Card heading */
            .section-title {
                margin-top: 20px;
                margin-bottom: 20px;
                font-weight: 600;
            }
            /* Chart container styling */
            .chart-box {
                background: #fff;
                border-radius: 8px;
                padding: 20px;
                box-shadow: 0 2px 4px rgba(0,0,0,0.1);
                margin-bottom: 20px;
            }
            .chart-box h4 {
                margin-bottom: 15px;
            }
            /* Table styling */
            .orders-box {
                background: #fff;
                border-radius: 8px;
                padding: 20px;
                box-shadow: 0 2px 4px rgba(0,0,0,0.1);
                margin-bottom: 20px;
            }
            .orders-box h4 {
                margin-bottom: 15px;
            }
        </style>
    </head>
    <body>
        <!-- Thanh navbar trên cùng -->
        <nav class="navbar navbar-expand-lg navbar-dark">
            <a class="navbar-brand" href="#"><i class="fas fa-rocket"></i> Group 7</a>
            <div class="collapse navbar-collapse" id="topNav">
                <ul class="navbar-nav ml-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="profile.jsp"><i class="fas fa-user-circle"></i> <%= user.getUserName()%></a>
                    </li>
                </ul>
            </div>
        </nav>

        <!-- Sidebar -->
        <div class="sidebar">
            <div class="sidebar-header">
                <i class="fas fa-user-shield"></i> Admin
            </div>
            <a href="dashboard.jsp"><i class="fas fa-tachometer-alt mr-2"></i> Dashboard</a>
            <a href="manage-orders.jsp" class="active"><i class="fas fa-shopping-cart mr-2"></i> Manage Orders</a>
            <a href="manage-products.jsp"><i class="fas fa-box-open mr-2"></i> Manage Products</a>
            <a href="manage-users.jsp"><i class="fas fa-users mr-2"></i> Manage Users</a>
            <a href="manage-discount.jsp"><i class="fas fa-tag mr-2"></i> Manage Discounts</a>
            <a href="manage-trademark.jsp"><i class="fas fa-copyright mr-2"></i> Manage Trademark</a>
        </div>

        <!-- Main content -->
        <div class="main-content">
            <h3 class="section-title">Manage Orders - Statistics</h3>

            <!-- Khu vực thống kê -->
            <div class="row">
                <div class="col-md-6">
                    <div class="chart-box">
                        <h4>Yearly Revenue (Pie Chart)</h4>
                        <canvas id="yearlyRevenueChart"></canvas>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="chart-box">
                        <h4>Monthly Revenue (Bar Chart)</h4>
                        <canvas id="monthlyRevenueChart"></canvas>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-6">
                    <div class="chart-box">
                        <h4>Weekly Shoe Brand (Pie Chart)</h4>
                        <canvas id="weeklyBrandChart"></canvas>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="chart-box">
                        <h4>Monthly Shoe Brand (Pie Chart)</h4>
                        <canvas id="monthlyBrandChart"></canvas>
                    </div>
                </div>
            </div>

            <!-- Khu vực bảng đơn hàng -->
            <div class="orders-box">
                <h4>Manage Orders</h4>
                <div class="table-responsive">
                    <table class="table table-bordered table-hover">
                        <thead class="thead-dark">
                            <tr>
                                <th>Order ID</th>
                                <th>User ID</th>
                                <th>Order Date</th>
                                <th>Total Price</th>
                                <th>Status</th>
                                <th>Product Name</th>
                                <th>Quantity</th>
                                <th>Unit Price</th>
                                <th>Size</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                if (orderList != null && !orderList.isEmpty()) {
                                    for (Order order : orderList) {
                                        List<OrderItem> items = order.getItems();
                                        if (items != null && !items.isEmpty()) {
                                            for (OrderItem item : items) {
                            %>
                            <tr>
                                <td><%= order.getOrder_ID()%></td>
                                <td><%= order.getUser_ID()%></td>
                                <td><%= order.getOrder_Date()%></td>
                                <td>$<%= String.format("%.2f", order.getTotal_Price())%></td>
                                <td><%= order.getStatus()%></td>
                                <td><%= item.getProductName()%></td>
                                <td><%= item.getQuantity()%></td>
                                <td>$<%= String.format("%.2f", item.getUnitPrice())%></td>
                                <td><%= item.getSize()%></td>
                                <td>
                                    <form action="UpdateOrderStatusServlet" method="post" class="d-inline">
                                        <input type="hidden" name="orderId" value="<%= order.getOrder_ID()%>">
                                        <input type="hidden" name="status" value="pending">
                                        <button type="submit" class="btn btn-warning btn-sm mb-1">Pending</button>
                                    </form>
                                    <form action="UpdateOrderStatusServlet" method="post" class="d-inline">
                                        <input type="hidden" name="orderId" value="<%= order.getOrder_ID()%>">
                                        <input type="hidden" name="status" value="completed">
                                        <button type="submit" class="btn btn-success btn-sm mb-1">Completed</button>
                                    </form>
                                    <form action="UpdateOrderStatusServlet" method="post" class="d-inline">
                                        <input type="hidden" name="orderId" value="<%= order.getOrder_ID()%>">
                                        <input type="hidden" name="status" value="canceled">
                                        <button type="submit" class="btn btn-danger btn-sm mb-1">Cancel</button>
                                    </form>
                                </td>
                            </tr>
                            <%
                                        }
                                    }
                                }
                            } else {
                            %>
                            <tr>
                                <td colspan="10" class="text-center text-muted">No orders found.</td>
                            </tr>
                            <%
                                }
                            %>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <!-- Chart.js -->
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
        <!-- Vẽ các biểu đồ -->
        <script>
            // Dữ liệu JSON
            var yearlyLabels = <%= yearlyLabels.toString()%>;
            var yearlyData = <%= yearlyData.toString()%>;
            var monthlyLabels = <%= monthlyLabels.toString()%>;
            var monthlyData = <%= monthlyData.toString()%>;
            var weeklyBrandLabels = <%= weeklyBrandLabels.toString()%>;
            var weeklyBrandData = <%= weeklyBrandData.toString()%>;
            var monthlyBrandLabels = <%= monthlyBrandLabels.toString()%>;
            var monthlyBrandData = <%= monthlyBrandData.toString()%>;

            // Yearly Revenue (Pie)
            var ctxYearly = document.getElementById('yearlyRevenueChart').getContext('2d');
            var yearlyChart = new Chart(ctxYearly, {
                type: 'pie',
                data: {
                    labels: yearlyLabels,
                    datasets: [{
                            data: yearlyData,
                            backgroundColor: [
                                'rgba(255, 99, 132, 0.6)',
                                'rgba(54, 162, 235, 0.6)',
                                'rgba(255, 206, 86, 0.6)',
                                'rgba(75, 192, 192, 0.6)',
                                'rgba(153, 102, 255, 0.6)'
                            ],
                            borderColor: [
                                'rgba(255, 99, 132, 1)',
                                'rgba(54, 162, 235, 1)',
                                'rgba(255, 206, 86, 1)',
                                'rgba(75, 192, 192, 1)',
                                'rgba(153, 102, 255, 1)'
                            ],
                            borderWidth: 1
                        }]
                },
                options: {responsive: true}
            });

            // Monthly Revenue (Bar)
            var ctxMonthly = document.getElementById('monthlyRevenueChart').getContext('2d');
            var monthlyChart = new Chart(ctxMonthly, {
                type: 'bar',
                data: {
                    labels: monthlyLabels,
                    datasets: [{
                            label: 'Monthly Revenue',
                            data: monthlyData,
                            backgroundColor: 'rgba(153, 102, 255, 0.6)',
                            borderColor: 'rgba(153, 102, 255, 1)',
                            borderWidth: 1
                        }]
                },
                options: {
                    responsive: true,
                    scales: {
                        y: {beginAtZero: true}
                    }
                }
            });

            // Weekly Brand (Pie)
            var ctxWeeklyBrand = document.getElementById('weeklyBrandChart').getContext('2d');
            var weeklyBrandChart = new Chart(ctxWeeklyBrand, {
                type: 'pie',
                data: {
                    labels: weeklyBrandLabels,
                    datasets: [{
                            data: weeklyBrandData,
                            backgroundColor: [
                                'rgba(255, 159, 64, 0.6)',
                                'rgba(255, 99, 132, 0.6)',
                                'rgba(54, 162, 235, 0.6)',
                                'rgba(75, 192, 192, 0.6)'
                            ],
                            borderColor: [
                                'rgba(255, 159, 64, 1)',
                                'rgba(255, 99, 132, 1)',
                                'rgba(54, 162, 235, 1)',
                                'rgba(75, 192, 192, 1)'
                            ],
                            borderWidth: 1
                        }]
                },
                options: {responsive: true}
            });

            // Monthly Brand (Pie)
            var ctxMonthlyBrand = document.getElementById('monthlyBrandChart').getContext('2d');
            var monthlyBrandChart = new Chart(ctxMonthlyBrand, {
                type: 'pie',
                data: {
                    labels: monthlyBrandLabels,
                    datasets: [{
                            data: monthlyBrandData,
                            backgroundColor: [
                                'rgba(255, 205, 86, 0.6)',
                                'rgba(75, 192, 192, 0.6)',
                                'rgba(153, 102, 255, 0.6)',
                                'rgba(201, 203, 207, 0.6)'
                            ],
                            borderColor: [
                                'rgba(255, 205, 86, 1)',
                                'rgba(75, 192, 192, 1)',
                                'rgba(153, 102, 255, 1)',
                                'rgba(201, 203, 207, 1)'
                            ],
                            borderWidth: 1
                        }]
                },
                options: {responsive: true}
            });
        </script>

        <!-- Bootstrap JS -->
        <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.bundle.min.js"></script>
    </body>
    <div class="container-fluid">
        <%-- Phần thông báo --%>
        <%
            String status = request.getParameter("status");
            if (status != null) {
                String message = "";
                String alertType = "";

                switch (status) {
                    case "success":
                        message = "Cập nhật khuyến mãi thành công!";
                        alertType = "success";
                        break;
                    case "error":
                        message = "Lỗi khi cập nhật khuyến mãi!";
                        alertType = "danger";
                        break;
                    case "invalid_data":
                        message = "Dữ liệu nhập không hợp lệ!";
                        alertType = "warning";
                        break;
                }
        %>
        <div class="alert alert-<%= alertType%> alert-dismissible fade show m-4">
            <%= message%>
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <% }%>

        <div class="row">
            <!-- Phần sidebar và nội dung giữ nguyên -->
        </div>
    </div>
</html>
