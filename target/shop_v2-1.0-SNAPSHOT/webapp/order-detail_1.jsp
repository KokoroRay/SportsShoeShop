<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.User"%>
<%@page import="model.OrderItem"%>
<%@page import="model.Order"%>
<%@page import="java.util.List"%>

<%
    Order order = (Order) request.getAttribute("order");
    List<OrderItem> orderItems = (List<OrderItem>) request.getAttribute("orderItems");
    User user = (User) session.getAttribute("user");

    // Kiểm tra xem người dùng đã đăng nhập hay chưa
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html>
<head>
    <title>Order Details</title>
</head>
<body>
    <h2>Order Details</h2>
    <p><strong>Order ID:</strong> <%= order.getOrder_ID() %></p>
    <p><strong>User ID:</strong> <%= order.getUser_ID() %></p>
    <p><strong>Order Date:</strong> <%= order.getOrder_Date() %></p>
    <p><strong>Total Price:</strong> $<%= String.format("%.2f", order.getTotal_Price()) %></p>
    <p><strong>Status:</strong> <%= order.getStatus() %></p>

    
    <%
        // Kiểm tra vai trò của người dùng để xác định liên kết quay lại
        String backLink = user.getRole().equals("admin") ? "manage-orders.jsp" : "purchase-history.jsp";
    %>
    <p><a href="<%= backLink %>">Back to <%= user.getRole().equals("admin") ? "Manage Orders" : "Purchase History" %></a></p>
</body>
</html>
