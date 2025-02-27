<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.User" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Change Password</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
    <div class="container mt-5">
        <h1 class="text-center">Change Password</h1>
        
        <div class="card p-4">
            <form action="ChangePasswordServlet" method="post">
                <div class="form-group">
                    <label for="currentPassword">Current Password:</label>
                    <input type="password" class="form-control" name="currentPassword" required />
                </div>
                
                <div class="form-group">
                    <label for="newPassword">New Password:</label>
                    <input type="password" class="form-control" name="newPassword" required />
                </div>
                
                <button type="submit" class="btn btn-warning btn-block">Change Password</button>
            </form>
        </div>
        
        <div class="text-center mt-3">
            <a href="profile.jsp" class="btn btn-secondary">Back to Profile</a>
        </div>
    </div>
</body>
</html>
