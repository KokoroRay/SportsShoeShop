<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
    <div class="container mt-5">
        <h1 class="text-center mb-4">Admin Dashboard</h1>
        
        <div class="row">
            <!-- Manage Products -->
            <div class="col-md-4 mb-3">
                <div class="card">
                    <div class="card-body text-center">
                        <h5 class="card-title">Manage Products</h5>
                        <p class="card-text">View, add, and update products in the catalog.</p>
                        <a href="manage-products.jsp" class="btn btn-primary">Go to Products</a>
                    </div>
                </div>
            </div>
            
            <!-- Manage Orders -->
            <div class="col-md-4 mb-3">
                <div class="card">
                    <div class="card-body text-center">
                        <h5 class="card-title">Manage Orders</h5>
                        <p class="card-text">View and manage customer orders.</p>
                        <a href="manage-orders.jsp" class="btn btn-primary">Go to Orders</a>
                    </div>
                </div>
            </div>
            
            <!-- Manage Users -->
            <div class="col-md-4 mb-3">
                <div class="card">
                    <div class="card-body text-center">
                        <h5 class="card-title">Manage Users</h5>
                        <p class="card-text">Manage customer and admin user accounts.</p>
                        <a href="manage-users.jsp" class="btn btn-primary">Go to Users</a>
                    </div>
                </div>
            </div>
        </div>
        
        <!-- Logout Button -->
        <div class="text-center mt-4">
            <a href="LogoutServlet" class="btn btn-danger">Logout</a>
        </div>
    </div>

    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.6.0/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
