<%-- 
    Document   : adminNotifications.jsp
    Created on : Mar 14, 2025, 6:35:05 PM
    Author     : lam gia bao ce180780
--%>
<%@page import="model.User"%>
<%@page import="model.Notification"%>
<%@page import="java.util.List"%>
<%@page import="dao.NotificationDAO"%>
<%
    // Kiểm tra session Admin
    User loggedInUser = (User) session.getAttribute("user");
    if (loggedInUser == null || !"admin".equals(loggedInUser.getRole())) {
        response.sendRedirect("login.jsp");
        return;
    }

    NotificationDAO notificationDAO = new NotificationDAO();
    List<Notification> notifications = notificationDAO.getNotificationsForAdmin();
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Admin Notifications</title>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    </head>
    <body>
        <div class="container mt-5">
            <h2>Pending Notifications</h2>
            <table class="table table-bordered">
                <thead>
                    <tr>
                        <th>Order ID</th>
                        <th>User ID</th>
                        <th>Message</th>
                        <th>Created At</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <% for (Notification notification : notifications) {%>
                    <tr>
                        <td><%= notification.getOrderId()%></td>
                        <td><%= notification.getUserId()%></td>
                        <td><%= notification.getMessage()%></td>
                        <td><%= notification.getCreatedAt()%></td>
                        <td>
                            <a href="ConfirmOrderServlet?notificationId=<%= notification.getNotificationId()%>&status=completed" 
                               class="btn btn-success">Confirm</a>

                            <a href="ConfirmOrderServlet?notificationId=<%= notification.getNotificationId()%>&status=canceled" 
                               class="btn btn-danger">Cancel</a>
                        </td>
                    </tr>
                    <% }%>
                </tbody>
            </table>
        </div>
    </body>
</html>

