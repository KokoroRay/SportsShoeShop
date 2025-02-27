<%@page import="model.User"%>
<%@page import="model.Product"%>
<%@page import="dao.ProductDAO"%>
<%@page import="java.util.List"%>

<%
    // Ensure only admin has access
    User loggedInUser = (User) session.getAttribute("user");
    if (loggedInUser == null || !"admin".equals(loggedInUser.getRole())) {
        response.sendRedirect("login.jsp");
        return;
    }

    // Fetch product list from the database
    ProductDAO productDAO = new ProductDAO();
    List<Product> productList = productDAO.getAllProducts();
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Manage Products</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
    <div class="container mt-5">
        <h2 class="text-center mb-4">Manage Products</h2>
        <div class="text-right mb-3">
            <a href="add-product.jsp" class="btn btn-success">Add New Product</a>
            <a href="dashboard.jsp" class="btn btn-secondary ml-2">Back to Dashboard</a>
        </div>

        <table class="table table-bordered table-hover">
            <thead class="thead-dark">
                <tr>
                    <th>Image</th>
                    <th>Product Name</th>
                    <th>Brand</th>
                    <th>Price</th>
                    <th>Quantity</th>
                    <th>Description</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <% for (Product product : productList) { %>
                    <tr>
                        <!-- Display product image -->
                        <td>
                            <img src="<%= product.getImage() %>" alt="<%= product.getProduct_Name() %>" width="100" height="100" class="img-thumbnail">
                        </td>
                        <td><%= product.getProduct_Name() %></td>
                        <td><%= product.getBrand() %></td>
                        <td>$<%= product.getPrice() %></td>
                        <td><%= product.getQuantity() %></td>
                        <td><%= product.getDescription() %></td>
                        <td>
                            <a href="edit-product.jsp?productId=<%= product.getProduct_ID() %>" class="btn btn-primary btn-sm">Edit</a>
                            <a href="DeleteProductServlet?productId=<%= product.getProduct_ID() %>" 
                               class="btn btn-danger btn-sm" 
                               onclick="return confirm('Are you sure you want to delete this product?');">
                               Delete
                            </a>
                        </td>
                    </tr>
                <% } %>
            </tbody>
        </table>
    </div>

    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.6.0/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
