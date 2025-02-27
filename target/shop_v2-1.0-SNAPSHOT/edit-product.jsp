<%@page import="model.User"%>
<%@page import="model.Product"%>
<%@page import="dao.ProductDAO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    User loggedInUser = (User) session.getAttribute("user");
    if (loggedInUser == null || !"admin".equals(loggedInUser.getRole())) {
        response.sendRedirect("login.jsp");
        return;
    }

    int productId = Integer.parseInt(request.getParameter("productId"));
    ProductDAO productDAO = new ProductDAO();
    Product product = productDAO.getProductById(String.valueOf(productId));

    if (product == null) {
        response.sendRedirect("not-found.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Product</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
    <div class="container mt-5">
        <h2>Edit Product</h2>
        <form action="EditProductServlet" method="post" enctype="multipart/form-data">
            <input type="hidden" name="productId" value="<%= product.getProduct_ID() %>" />

            <div class="form-group">
                <label for="productName">Product Name:</label>
                <input type="text" class="form-control" name="productName" value="<%= product.getProduct_Name() %>" required />
            </div>

            <div class="form-group">
                <label for="brand">Brand:</label>
                <input type="text" class="form-control" name="brand" value="<%= product.getBrand() %>" />
            </div>

            <div class="form-group">
                <label for="price">Price:</label>
                <input type="number" step="0.01" class="form-control" name="price" value="<%= product.getPrice() %>" required />
            </div>

            <div class="form-group">
                <label for="quantity">Quantity:</label>
                <input type="number" class="form-control" name="quantity" value="<%= product.getQuantity() %>" required />
            </div>

            <div class="form-group">
                <label for="size">Size:</label>
                <input type="text" class="form-control" name="size" value="<%= product.getSize() %>" />
            </div>

            <div class="form-group">
                <label for="description">Description:</label>
                <textarea name="description" class="form-control" rows="4"><%= product.getDescription() %></textarea>
            </div>

            <!-- File input for image selection -->
            <div class="form-group">
                <label for="image">Upload New Image:</label>
                <input type="file" class="form-control-file" name="image" accept="image/*" />
                <small>Current Image: <%= product.getImage() %></small>
            </div>

            <div class="form-group">
                <label for="rate">Rate:</label>
                <input type="number" step="0.01" class="form-control" name="rate" value="<%= product.getRate() %>" />
            </div>

            <div class="form-group">
                <label for="type">Type:</label>
                <select name="type" class="form-control" required>
                    <option value="TF" <%= "TF".equals(product.getType()) ? "selected" : "" %>>TF</option>
                    <option value="IC" <%= "IC".equals(product.getType()) ? "selected" : "" %>>IC</option>
                </select>
            </div>

            <button type="submit" class="btn btn-primary">Update Product</button>
        </form>

        <% if (request.getAttribute("errorMessage") != null) { %>
            <div class="alert alert-danger mt-3"><%= request.getAttribute("errorMessage") %></div>
        <% } %>

        <p class="mt-3"><a href="manage-products.jsp" class="btn btn-secondary">Back to Product Management</a></p>
    </div>

    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.6.0/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
