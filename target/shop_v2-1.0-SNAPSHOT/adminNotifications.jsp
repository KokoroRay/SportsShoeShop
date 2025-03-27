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
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Admin Notifications</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <!-- Bootstrap & FontAwesome -->
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">

        <style>
            body {
                font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
                background-color: #f4f6f9;
            }

            /* Sidebar */
            .sidebar {
                position: fixed;
                top: 0;
                left: 0;
                width: 240px;
                height: 100%;
                background-color: #343a40;
                color: #adb5bd;
                padding-top: 20px;
            }
            .sidebar a {
                display: block;
                padding: 12px 20px;
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

            /* Main Content */
            .main-content {
                margin-left: 240px;
                padding: 20px;
            }

            /* Table */
            .table th {
                background-color: #0d6efd;
                color: white;
            }

            /* Status Badges */
            .badge-pending {
                background-color: #ffc107;
                color: black;
            }
            .badge-completed {
                background-color: #28a745;
            }
            .badge-canceled {
                background-color: #dc3545;
            }
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
            .sidebar .sidebar-header {
                text-align: center;
                font-size: 1.2rem;
                color: #fff;
                margin-bottom: 1rem;
            }

        </style>
    </head>
    <body>

        <!-- Navbar -->
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

        <!-- Sidebar (Đồng bộ với Dashboard) -->
        <<div class="sidebar">
            <div class="sidebar-header">
                <i class="fas fa-user-shield"></i> Admin
            </div>
            <a href="dashboard.jsp"><i class="fas fa-tachometer-alt me-2"></i> Dashboard</a>
            <a href="manage-orders.jsp"><i class="fas fa-shopping-cart me-2"></i> Manage Orders</a>
            <a href="manage-products.jsp"><i class="fas fa-box-open me-2"></i> Manage Products</a>
            <a href="manage-users.jsp"><i class="fas fa-users me-2"></i> Manage Users</a>
            <a href="manage-discount.jsp"><i class="fas fa-tag me-2"></i> Manage Discounts</a>
            <a href="manage-trademark.jsp"><i class="fas fa-copyright me-2"></i> Manage Trademark</a>
            <a href="adminNotifications.jsp" class="active"><i class="fas fa-bell me-2"></i> Notification Orders</a>
        </div>

        <!-- Main Content -->
        <div class="main-content">
            <h3 class="mb-4"><i class="fas fa-bell"></i> Pending Notifications</h3>

            <div class="table-responsive">
                <table class="table table-bordered table-hover">
                    <thead>
                        <tr>
                            <th>Order ID</th>
                            <th>User ID</th>
                            <th>Message</th>
                            <th>Created At</th>
                            <th>Status</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Notification notification : notifications) {%>
                        <tr>
                            <td>#<%= notification.getOrderId()%></td>
                            <td><%= notification.getUserId()%></td>
                            <td><%= notification.getMessage()%></td>
                            <td><%= notification.getCreatedAt()%></td>
                            <td>
                                <span class="badge
                                      <%= notification.getStatus().equals("pending") ? "badge-pending"
                                              : notification.getStatus().equals("completed") ? "badge-completed"
                                              : "badge-canceled"%>">
                                    <%= notification.getStatus()%>
                                </span>
                            </td>
                            <td>
                                <a href="ConfirmOrderServlet?notificationId=<%= notification.getNotificationId()%>&status=completed" 
                                   class="btn btn-sm btn-success">
                                    <i class="fas fa-check"></i> Confirm
                                </a>
                                <a href="ConfirmOrderServlet?notificationId=<%= notification.getNotificationId()%>&status=canceled" 
                                   class="btn btn-sm btn-danger">
                                    <i class="fas fa-times"></i> Cancel
                                </a>
                            </td>
                        </tr>
                        <% }%>
                    </tbody>
                </table>
            </div>
        </div>

        <!-- Bootstrap JS -->
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

    </body>
</html>
