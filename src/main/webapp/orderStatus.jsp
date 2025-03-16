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
        <title>Trạng thái đơn hàng</title>
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
            <h2>Your order status</h2>
            <table class="table table-bordered">
                <thead>
                    <tr>
                        <th>Order code</th>
                        <th>Notification Date</th>
                        <th>Message</th>
                        <th>Status</th>
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
                    </tr>
                    <% }%>
                </tbody>
            </table>
        </div>
    </body>
</html>
