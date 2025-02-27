<%@page import="model.OrderItem"%>
<%@page import="model.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.Order"%>
<%@page import="dao.OrderDAO"%>
<%@page import="java.util.List"%>

<%
    // Check if the user is an admin
    User user = (User) session.getAttribute("user");
    if (user == null || !"admin".equals(user.getRole())) {
        response.sendRedirect("login.jsp");
        return;
    }

    // Fetch all orders with details from the database
    OrderDAO orderDAO = new OrderDAO();
    List<Order> orderList = orderDAO.getAllOrdersWithDetails();
%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Manage Orders</title>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    </head>
    <body>
        <div class="container mt-5">
            <div class="d-flex justify-content-between mb-4">
                <h2 class="text-primary">Manage Orders</h2>
                <a href="dashboard.jsp" class="btn btn-secondary">Back to Dashboard</a>
            </div>
            
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
                        <% if (orderList != null && !orderList.isEmpty()) {
                                for (Order order : orderList) {
                                    List<OrderItem> items = order.getItems();
                                    if (items != null && !items.isEmpty()) {
                                        for (OrderItem item : items) {%>
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
                                <!-- Order status update buttons -->
                                <form action="UpdateOrderStatusServlet" method="post" class="d-inline">
                                    <input type="hidden" name="orderId" value="<%= order.getOrder_ID()%>">
                                    <input type="hidden" name="status" value="pending">
                                    <button type="submit" class="btn btn-warning btn-sm mb-1">Mark as Pending</button>
                                </form>

                                <form action="UpdateOrderStatusServlet" method="post" class="d-inline">
                                    <input type="hidden" name="orderId" value="<%= order.getOrder_ID()%>">
                                    <input type="hidden" name="status" value="completed">
                                    <button type="submit" class="btn btn-success btn-sm mb-1">Mark as Completed</button>
                                </form>

                                <form action="UpdateOrderStatusServlet" method="post" class="d-inline">
                                    <input type="hidden" name="orderId" value="<%= order.getOrder_ID()%>">
                                    <input type="hidden" name="status" value="canceled">
                                    <button type="submit" class="btn btn-danger btn-sm mb-1">Cancel Order</button>
                                </form>
                            </td>
                        </tr>
                        <%              }
                                }
                            }
                        } else { %>
                        <tr>
                            <td colspan="10" class="text-center text-muted">No orders found.</td>
                        </tr>
                        <% }%>
                    </tbody>
                </table>
            </div>

            <div class="text-center mt-3">
                <a href="dashboard.jsp" class="btn btn-secondary">Back to Dashboard</a>
            </div>
        </div>

        <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.6.0/dist/umd/popper.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    </body>
</html>
