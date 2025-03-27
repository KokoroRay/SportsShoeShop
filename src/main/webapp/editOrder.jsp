<%@page import="dao.NotificationDAO"%>
<%@page import="model.Notification"%>
<%@page import="model.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    int orderId = -1;
    try {
        orderId = Integer.parseInt(request.getParameter("orderId"));
    } catch (NumberFormatException e) {
        response.sendRedirect("orderStatus.jsp?error=Invalid order ID");
        return;
    }

    NotificationDAO notificationDAO = new NotificationDAO();
    Notification notification = notificationDAO.getNotificationByOrderId(orderId);

    if (notification == null) {
        response.sendRedirect("orderStatus.jsp?error=Order not found or not editable");
        return;
    }
%>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Edit Order</title>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    </head>
    <body>

        <div class="container mt-5">
            <h2>Edit Order #<%= orderId%></h2>
            <% if (notification == null) { %>
            <div class="alert alert-danger">
                Order not found or cannot be edited.
            </div>
            <% } else { %>
            <form action="UpdateOrderServlet" method="post">
                <input type="hidden" name="orderId" value="<%= orderId%>">

                <div class="form-group">
                    <label>Full Name:</label>
                    <input type="text" name="fullName" class="form-control" value="<%= notification.getFullName() != null ? notification.getFullName() : ""%>" required>
                </div>

                <div class="form-group">
                    <label>Phone Number:</label>
                    <input type="text" name="phoneNumber" class="form-control" value="<%= notification.getPhoneNumber() != null ? notification.getPhoneNumber() : ""%>" required>
                </div>

                <div class="form-group">
                    <label>Address:</label>
                    <textarea name="address" class="form-control" required><%= notification.getAddress() != null ? notification.getAddress() : ""%></textarea>
                </div>

                <div class="form-group">
                    <label>Payment Method:</label>
                    <select name="paymentMethod" class="form-control">
                        <option value="Cash" <%= "Cash".equals(notification.getPaymentMethod()) ? "selected" : "" %>>Cash</option>
                        <option value="Credit Card" <%= "Credit Card".equals(notification.getPaymentMethod()) ? "selected" : "" %>>Credit Card</option>
                        <option value="COD" <%= "COD".equals(notification.getPaymentMethod()) ? "selected" : "" %>>Cash on Delivery (COD)</option>
                    </select>
                </div>

                <button type="submit" class="btn btn-success">Save Changes</button>
                <a href="orderStatus.jsp" class="btn btn-secondary">Cancel</a>
            </form>
            <% } %>
        </div>
    </body>
</html>
