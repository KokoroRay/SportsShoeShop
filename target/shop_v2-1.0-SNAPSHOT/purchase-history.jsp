<%@page import="model.OrderItem"%>
<%@page import="model.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.Order"%>
<%@page import="dao.OrderDAO"%>
<%@page import="java.util.List"%>

<%
    // Kiểm tra nếu người dùng đã đăng nhập
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    // Lấy danh sách đơn hàng của người dùng từ cơ sở dữ liệu
    OrderDAO orderDAO = new OrderDAO();
    List<Order> orderList = orderDAO.getOrdersByUserId(user.getUserId());
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Order History</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f9f9f9;
            color: #333;
            margin: 0;
            padding: 0;
        }

        .container {
            width: 80%;
            margin: 50px auto;
            background-color: #fff;
            padding: 20px;
            box-shadow: 0px 0px 15px rgba(0, 0, 0, 0.1);
            border-radius: 8px;
        }

        h2 {
            text-align: center;
            color: #4CAF50;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        table, th, td {
            border: 1px solid #ddd;
        }

        th, td {
            padding: 12px;
            text-align: center;
        }

        th {
            background-color: #4CAF50;
            color: white;
        }

        tr:nth-child(even) {
            background-color: #f2f2f2;
        }

        tr:hover {
            background-color: #ddd;
        }

        .btn {
            text-decoration: none;
            color: #fff;
            background-color: #4CAF50;
            padding: 8px 12px;
            border-radius: 4px;
            transition: background-color 0.3s;
        }

        .btn:hover {
            background-color: #45a049;
        }

        .no-orders {
            text-align: center;
            color: #888;
            font-style: italic;
            padding: 20px;
        }

        .back-home {
            display: block;
            text-align: center;
            margin-top: 20px;
            color: #4CAF50;
            text-decoration: none;
            font-weight: bold;
        }

        .back-home:hover {
            color: #45a049;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2>Order History for <%= user.getUserName() %></h2>

        <table>
            <tr>
                <th>Order ID</th>
                <th>Order Date</th>
                <th>Total Price</th>
                <th>Status</th>
                <th>Product Name</th>
                <th>Quantity</th>
                <th>Unit Price</th>
                <th>Size</th>
                
            </tr>

            <% if (orderList != null && !orderList.isEmpty()) { 
                for (Order order : orderList) { 
                    for (OrderItem item : order.getItems()) { %>
                        <tr>
                            <td><%= order.getOrder_ID() %></td>
                            <td><%= order.getOrder_Date() %></td>
                            <td>$<%= order.getTotal_Price() %></td>
                            <td><%= order.getStatus() %></td>
                            <td><%= item.getProductName() %></td>
                            <td><%= item.getQuantity() %></td>
                            <td>$<%= item.getUnitPrice() %></td>
                            <td><%= item.getSize() %></td>
                         
                        </tr>
            <%      }
                }
            } else { %>
                <tr>
                    <td colspan="9" class="no-orders">No orders found.</td>
                </tr>
            <% } %>
        </table>

        <a href="home.jsp" class="back-home">Back to Home</a>
    </div>
</body>
</html>
