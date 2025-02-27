<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.User" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    String successMessage = (String) session.getAttribute("successMessage");
    String errorMessage = (String) session.getAttribute("errorMessage");

    // Clear the messages after displaying
    session.removeAttribute("successMessage");
    session.removeAttribute("errorMessage");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Profile</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
    <div class="container mt-5">
        <h1 class="text-center">Profile</h1>

        <!-- Display success or error message if exists -->
        <% if (successMessage != null) { %>
            <div class="alert alert-success" role="alert">
                <%= successMessage %>
            </div>
        <% } %>
        <% if (errorMessage != null) { %>
            <div class="alert alert-danger" role="alert">
                <%= errorMessage %>
            </div>
        <% } %>

        <div class="card p-4 mb-4">
            <form action="UpdateProfileServlet" method="post">
                <div class="form-group">
                    <label>Username:</label>
                    <input type="text" class="form-control" name="username" value="${user.userName}" required />
                </div>
                
                <div class="form-group">
                    <label>Email:</label>
                    <input type="email" class="form-control" name="email" value="${user.email}" readonly />
                </div>
                
                <div class="form-group">
                    <label>Phone:</label>
                    <input type="text" class="form-control" name="phone" value="${user.phone}" required />
                </div>
                
                <div class="form-group">
                    <label>Address:</label>
                    <input type="text" class="form-control" name="address" value="${user.address}" />
                </div>
                
                <div class="form-group">
                    <label>Date of Birth:</label>
                    <input type="date" class="form-control" name="dob" value="${user.dob}" />
                </div>
                
                <button type="submit" class="btn btn-primary btn-block">Update Profile</button>
            </form>
        </div>

        <div class="text-center">
            <a href="ChangePassword.jsp" class="btn btn-warning">Change Password</a>
            <a href="PurchaseHistoryServlet" class="btn btn-info">Order History</a>
            <% if (session.getAttribute("user") != null) { %>
                <a href="LogoutServlet" class="btn btn-danger">Logout</a>
            <% } %>
        </div>
    </div>
</body>
</html>
