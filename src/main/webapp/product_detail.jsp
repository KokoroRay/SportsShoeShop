<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="model.Product, dao.ProductDAO, model.User" %>
<%@ page import="java.util.List" %>
<%
    User user = (User) session.getAttribute("user");
    Product product = (Product) request.getAttribute("product");
    if (product == null) {
        String productIdStr = request.getParameter("productId");
        if (productIdStr != null) {
            try {
                ProductDAO productDAO = new ProductDAO();
                product = productDAO.getProductById(productIdStr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    if (product == null) {
%>
<h1 class="text-center text-danger">Product not found!</h1>
<%
        return;
    }
    ProductDAO productDAO = new ProductDAO();
    List<Product> bestSellingProducts = productDAO.getBestSellingProducts();
    List<Product> brandProducts = productDAO.getProductsByBrand(product.getBrand(), product.getProduct_ID());
    List<String> availableSizes = productDAO.getSizesByProductId(product.getProduct_ID());

%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Product Details</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    </head>
    <body>
        <div class="container mt-4">
            <div class="product-detail">
                <h1 class="text-center"><%= product.getProduct_Name()%></h1>
                <div class="row">
                    <div class="col-md-6">
                        <img src="<%= product.getImage()%>" alt="<%= product.getProduct_Name()%>" class="img-fluid">
                    </div>
                    <div class="col-md-6">
                        <h2>Price: $<%= product.getPrice()%></h2>
                        <p><strong>Brand:</strong> <%= product.getBrand()%></p>
                        <p><strong>Description:</strong> <%= product.getDescription()%></p>
                        <p><strong>Rating:</strong> <%= product.getRate()%> ⭐</p>
                        <% if (user == null) { %>
                        <a href="login.jsp" class="btn btn-success">Log in to purchase</a>
                        <% } else {%>
                        <form action="CartServlet" method="post">
                            <input type="hidden" name="productId" value="<%= product.getProduct_ID()%>">
                            <input type="hidden" name="productName" value="<%= product.getProduct_Name()%>">
                            <input type="hidden" name="price" value="<%= product.getPrice()%>">
                            <label for="size">Size:</label>
                            <select name="size" class="form-select" required>
                                <% if (availableSizes != null && !availableSizes.isEmpty()) {
                                        for (String size : availableSizes) {
                                            String cleanedSize = size.trim();%>
                                <option value="<%= cleanedSize%>"><%= cleanedSize%></option>
                                <% }
                                } else { %>
                                <option value="">No sizes available</option>
                                <% } %>
                            </select>
                            <label for="quantity">Quantity:</label>
                            <input type="number" name="quantity" min="1" value="1" required>
                            <button type="submit" class="btn btn-success">Add to Cart</button>
                        </form>
                        <% } %>
                    </div>
                </div>
            </div>

            <div class="container mt-5">
                <h3 class="text-center">Best Selling Products</h3>
                <div class="row">
                    <% for (Product p : bestSellingProducts) {%>
                    <div class="col-md-3">
                        <div class="card mb-4">
                            <img src="<%= p.getImage()%>" class="card-img-top" alt="<%= p.getProduct_Name()%>">
                            <div class="card-body">
                                <h5 class="card-title"><%= p.getProduct_Name()%></h5>
                                <p class="card-text">$<%= p.getPrice()%></p>
                                <a href="productDetail.jsp?productId=<%= p.getProduct_ID()%>" class="btn btn-primary">View Details</a>
                            </div>
                        </div>
                    </div>
                    <% }%>
                </div>
            </div>

            <div class="container mt-5">
                <h3 class="text-center">More from <%= product.getBrand()%></h3>
                <div class="row">
                    <% for (Product p : brandProducts) {%>
                    <div class="col-md-3">
                        <div class="card mb-4">
                            <img src="<%= p.getImage()%>" class="card-img-top" alt="<%= p.getProduct_Name()%>">
                            <div class="card-body">
                                <h5 class="card-title"><%= p.getProduct_Name()%></h5>
                                <p class="card-text">$<%= p.getPrice()%></p>
                                <a href="productDetail.jsp?productId=<%= p.getProduct_ID()%>" class="btn btn-primary">View Details</a>
                            </div>
                        </div>
                    </div>
                    <% }%>
                </div>
            </div>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
