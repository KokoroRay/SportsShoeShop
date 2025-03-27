<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.User"%>
<%@page import="dao.UserDAO"%>

<%
    // Check if admin is logged in
    User loggedInUser = (User) session.getAttribute("user");
    if (loggedInUser == null || !"admin".equals(loggedInUser.getRole())) {
        response.sendRedirect("login.jsp");
        return;
    }

    // Get userId from request
    int userId = Integer.parseInt(request.getParameter("userId"));

    // Get user information from database
    UserDAO userDAO = new UserDAO();
    User user = userDAO.getUserById(userId);
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit User</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <style>
        body {
            background-color: #f8f9fa;
        }
        .container {
            margin-top: 50px;
        }
        .form-container {
            padding: 30px;
            border-radius: 8px;
            background-color: #ffffff;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }
        .form-title {
            font-size: 24px;
            font-weight: bold;
            color: #007bff;
            margin-bottom: 20px;
        }
        .btn-primary {
            background-color: #007bff;
            border-color: #007bff;
        }
        .btn-primary:hover {
            background-color: #0056b3;
            border-color: #004085;
        }
        .back-link {
            color: #007bff;
            text-decoration: none;
        }
        .back-link:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>

<div class="container">
    <div class="row justify-content-center">
        <div class="col-md-6">
            <div class="form-container">
                <h2 class="text-center form-title">Edit User</h2>
                
                <!-- Form to edit user information -->
                <form action="${pageContext.request.contextPath}/EditUserServlet" method="post">
                    <input type="hidden" name="userId" value="<%= user.getUserId() %>">

                    <div class="form-group">
                        <label for="username">Username:</label>
                        <input type="text" class="form-control" name="username" value="<%= user.getUserName() %>" required>
                    </div>

                    <div class="form-group">
                        <label for="email">Email:</label>
                        <input type="email" class="form-control" name="email" value="<%= user.getEmail() %>" required>
                    </div>

                    <div class="form-group">
                        <label for="phone">Phone:</label>
                        <input type="text" class="form-control" name="phone" value="<%= user.getPhone() %>">
                    </div>

                    <div class="form-group">
                        <label for="role">Role:</label>
                        <select name="role" class="form-control">
                            <option value="admin" <%= user.getRole().equals("admin") ? "selected" : "" %>>Admin</option>
                            <option value="customer" <%= user.getRole().equals("customer") ? "selected" : "" %>>Customer</option>
                        </select>
                    </div>

                    <button type="submit" class="btn btn-primary btn-block">Update User</button>
                </form>

                <% if (request.getAttribute("errorMessage") != null) { %>
                    <div class="alert alert-danger mt-3">
                        <%= request.getAttribute("errorMessage") %>
                    </div>
                <% } %>

                <div class="text-center mt-3">
                    <a href="manage-users.jsp" class="back-link">Back to User Management</a>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.6.0/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
