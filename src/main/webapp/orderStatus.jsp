<%@page import="model.User"%>
<%@page import="java.util.List"%>
<%@page import="model.Notification"%>
<%@page import="dao.NotificationDAO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    User user = (User) session.getAttribute("user");
    NotificationDAO notificationDAO = new NotificationDAO();
    List<Notification> notifications = notificationDAO.getNotificationsByUserId(user.getUserId());
%>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Order Status</title>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    </head>
    <body>
        <style>
            .badge-completed {
                background-color: #007bff;
                color: white;
            }
        </style>
        <div class="container mt-5">
            <h2>Your Order Status</h2>
            <table class="table table-bordered">
                <thead>
                    <tr>
                        <th>Order Code</th>
                        <th>Notification Date</th>
                        <th>Message</th>
                        <th>Status</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <% for (Notification notification : notifications) {%>
                    <tr>
                        <td><%= notification.getOrderId()%></td>
                        <td><%= notification.getCreatedAt()%></td>
                        <td><%= notification.getMessage()%></td>
                        <td>
                            <span class="badge badge-<%= notification.getStatus().equalsIgnoreCase("completed") ? "success"
                                    : notification.getStatus().equalsIgnoreCase("canceled") ? "danger"
                                    : "warning"%>">
                                <%= notification.getStatus()%>
                            </span>
                        </td>
                        <td>
                            <% if ("pending".equalsIgnoreCase(notification.getStatus())) {%>
                            <a href="editOrder.jsp?orderId=<%= notification.getOrderId()%>" class="btn btn-warning btn-sm">
                                Edit
                            </a>
                            <form action="CancelOrderServlet" method="post" style="display:inline;">
                                <input type="hidden" name="orderId" value="<%= notification.getOrderId()%>">
                                <button type="submit" class="btn btn-danger btn-sm"
                                        onclick="return confirm('Are you sure you want to cancel this order?');">
                                    Cancel
                                </button>
                            </form>
                            <% } else { %>
                            <span class="text-muted">Not Editable</span>
                            <% } %>
                        </td>
                    </tr>
                    <% }%>
                </tbody>
            </table>
        </div>
    </body>
</html>
