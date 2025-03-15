<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="model.User" %>
<%@page import="dao.UserDAO" %>
<%@page import="java.util.List" %>
<%
    // Ensure only admin has access
    User loggedInUser = (User) session.getAttribute("user");
    if (loggedInUser == null || !"admin".equals(loggedInUser.getRole())) {
        response.sendRedirect("login.jsp");
        return;
    }

    // Retrieve all users from the database
    UserDAO userDAO = new UserDAO();
    List<User> userList = userDAO.getAllUsers();
%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Manage Users</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <!-- Bootstrap CSS -->
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
        <!-- Font Awesome for icons -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.1/css/all.min.css">

        <!-- Custom CSS cho giao diện Admin Dashboard -->
        <style>
            body {
                font-family: "Segoe UI", sans-serif;
                background-color: #f4f6f9;
                margin: 0;
                padding: 0;
            }
            /* Top Navbar */
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
            .sidebar a:hover,
            .sidebar a.active {
                background-color: #0d6efd;
                color: #fff;
            }
            /* Main Content */
            .main-content {
                margin-left: 220px; /* tránh đè lên sidebar */
                margin-top: 56px;   /* tránh đè lên navbar */
                padding: 20px;
            }
            /* Table container */
            .table-container {
                background: #fff;
                padding: 20px;
                border-radius: 8px;
                box-shadow: 0 2px 4px rgba(0,0,0,0.1);
                margin-bottom: 20px;
            }
        </style>
    </head>
    <body>
        <!-- Top Navbar -->
        <nav class="navbar navbar-expand-lg navbar-dark">
            <a class="navbar-brand" href="#"><i class="fas fa-rocket"></i> Group 7</a>
            <div class="collapse navbar-collapse" id="topNav">
                <ul class="navbar-nav ml-auto">
                    <li class="nav-item">
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
            <a href="dashboard.jsp"><i class="fas fa-tachometer-alt mr-2"></i> Dashboard</a>
            <a href="manage-orders.jsp"><i class="fas fa-shopping-cart mr-2"></i> Manage Orders</a>
            <a href="manage-products.jsp"><i class="fas fa-box-open mr-2"></i> Manage Products</a>
            <a href="manage-users.jsp" class="active"><i class="fas fa-users mr-2"></i> Manage Users</a>
            <a href="manage-discount.jsp"><i class="fas fa-tag mr-2"></i> Manage Discounts</a>
            <a href="manage-trademark.jsp"><i class="fas fa-copyright mr-2"></i> Manage Trademark</a>
        </div>

        <!-- Main Content -->
        <div class="main-content">
            <h3 class="text-center text-primary mb-4">User Management</h3>
            <div class="text-right mb-3">
                <a href="add-user-for-admin.jsp" class="btn btn-success">Add New User</a>
                <a href="dashboard.jsp" class="btn btn-secondary ml-2">Back to Dashboard</a>
            </div>
            <div class="table-container">
                <table class="table table-bordered table-hover">
                    <thead class="thead-dark">
                        <tr>
                            <th>Username</th>
                            <th>Email</th>
                            <th>Phone</th>
                            <th>Role</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (User u : userList) {%>
                        <tr>
                            <td><%= u.getUserName()%></td>
                            <td><%= u.getEmail()%></td>
                            <td><%= u.getPhone()%></td>
                            <td><%= u.getRole()%></td>
                            <td>
                                <a href="edit-user.jsp?userId=<%= u.getUserId()%>" class="btn btn-primary btn-sm">Edit</a>
                                <a href="DeleteUserServlet?userId=<%= u.getUserId()%>" class="btn btn-danger btn-sm" 
                                   onclick="return confirm('Are you sure you want to delete this user?');">
                                    Delete
                                </a>
                            </td>
                        </tr>
                        <% }%>
                    </tbody>
                </table>
            </div>
        </div>

        <!-- Bootstrap JS and dependencies -->
        <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
