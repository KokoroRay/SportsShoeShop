<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="model.Product, dao.ProductDAO, model.User" %>
<%@ page import="java.util.List" %>
<%@ page import="dao.FavoriteDAO" %> 
<%@page import="java.util.List"%>
<%@page import="model.User"%>
<%@page import="dao.ProductDAO"%>
<%@page import="model.Product"%>
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
    List<Product> productList = productDAO.getAllProducts();
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Product Details</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <style>
            /* General Style */
            body {
                background-color: #f4f7fa;
                font-family: 'Arial', sans-serif;
            }

            /* Navbar */
            .navbar-brand img {
                width: 120px;
            }
            .navbar-nav .nav-link {
                color: #555;
                font-weight: 500;
                transition: color 0.3s;
            }
            .navbar-nav .nav-link:hover {
                color: #e63946;
            }
            .navbar-nav .nav-item {
                margin: 0 15px; /* Adjust spacing as needed */
            }
            .navbar-nav .nav-link {
                padding: 10px 0; /* Adjust vertical padding */
                font-weight: bold;
            }
            .navbar-nav {
                gap: 10px; /* Adds even spacing between all items */
            }

            /* Product Detail Style */
            .product-detail {
                background-color: #ffffff;
                border-radius: 10px;
                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
                padding: 2rem;
                margin-top: 2rem;
            }
            .product-detail h1 {
                font-size: 2.5rem;
                color: #333;
                font-weight: bold;
                text-align: center;
                margin-bottom: 1rem;
            }
            .product-detail img {
                border-radius: 10px;
                width: 100%;
            }
            .product-detail h2 {
                color: #e63946;
                font-weight: bold;
            }
            .product-detail p {
                font-size: 1.1rem;
                color: #555;
            }
            .btn-success {
                background-color: #e63946;
                border: none;
                font-size: 1.2rem;
                padding: 0.75rem 1.5rem;
                transition: background-color 0.3s;
            }
            .btn-success:hover {
                background-color: #d62828;
            }

            /* Footer */
            .footer {
                background-color: #333;
                color: #fff;
                padding: 2rem 0;
                text-align: center;
                margin-top: 2rem;
            }
            .footer a {
                color: #f4a261;
                text-decoration: none;
                transition: color 0.3s;
            }
            .footer a:hover {
                color: #e76f51;
            }
            .footer-bottom {
                font-size: 0.9rem;
                margin-top: 1rem;
            }

        </style>
    </head>
    <body>

        <!-- Navbar 1 -->
        <nav class="navbar navbar-expand-lg bg-body-tertiary">
            <div class="container-fluid">
                <div class="collapse navbar-collapse d-flex justify-content-between" id="navbarSupportedContent">
                    <a class="navbar-brand" href="home.jsp">
                        <img class="logo" src="asset/logoShop_transparent.png" alt="alt"/></a>

                    <form action="searchController" method="POST" class="d-flex w-100" role="search">
                        <input class="form-control me-2 flex-grow-1" type="search" name="productName" placeholder="Search" aria-label="Search">
                        <button class="btn btn-outline-success" type="submit">Search</button>   
                    </form>

                    <ul class="navbar-nav mb-2 mb-lg-0">
                        <% if (user == null) { %>
                        <li class="nav-item"> 
                            <a class="nav-link active" aria-current="page" href="login.jsp">    <i class="fa fa-user"></i>
                            </a>
                        </li>
                        <% } else { %>
                        <li class="nav-item">
                            <a class="nav-link active" aria-current="page" href="profile.jsp">    <i class="fa fa-user"></i>
                            </a>
                        </li>
                        <%
                            FavoriteDAO favoriteDAO = new FavoriteDAO();
                            List<Product> favorites = favoriteDAO.getFavoriteProducts(user.getUserId());
                        %>
                        <li class="nav-item mx-3">
                            <a class="nav-link" href="favoriteList.jsp">
                                <i class="fa fa-heart text-danger"></i>
                                <% if (!favorites.isEmpty()) {%>
                                <span class="badge bg-danger rounded-pill"><%= favorites.size()%></span>
                                <% } %>
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link active" aria-current="page" href="viewcart.jsp"><i class="fa fa-cart-plus"></i>
                            </a>
                        </li>

                        <% }%>
                    </ul>
                </div>
            </div>
        </nav>

        <!-- Navbar 2 -->
        <nav class="navbar navbar-expand-lg bg-light">
            <div class="container-fluid">
                <a class="navbar-brand" href="home.jsp" style="font-weight: bold;">Home</a>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNavDropdown" aria-controls="navbarNavDropdown" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarNavDropdown">
                    <ul class="navbar-nav w-100 d-flex justify-content-center">
                        <li class="nav-item mx-3"><a class="nav-link active" href="all.jsp" style="font-weight: bold; color: red">All</a></li>
                        <li class="nav-item mx-3"><a class="nav-link" href="TF_shoes.jsp" style="font-weight: bold;">TF Shoes</a></li>
                        <li class="nav-item mx-3"><a class="nav-link" href="IC_shoes.jsp" style="font-weight: bold;">IC Shoes</a></li>
                        <li class="nav-item dropdown mx-3">
                            <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" style="font-weight: bold;">Brand</a>
                            <ul class="dropdown-menu">
                                <li><a class="dropdown-item" href="nike.jsp">Nike</a></li>
                                <li><a class="dropdown-item" href="adidas.jsp">Adidas</a></li>
                                <li><a class="dropdown-item" href="#">Puma</a></li>
                                <li><a class="dropdown-item" href="#">Mizuno</a></li>
                                <li><a class="dropdown-item" href="#">Joma</a></li>
                                <li><a class="dropdown-item" href="#">Kamito</a></li>
                            </ul>
                        </li>
                        <li class="nav-item mx-3"><a class="nav-link" href="contact.jsp" style="font-weight: bold;">Contact</a></li>
                        <li class="nav-item mx-3"><a class="nav-link" href="rate.jsp" style="font-weight: bold;">Rate</a></li>
                    </ul>
                </div>
            </div>
        </nav>


        <div class="container mt-4">
            <div class="product-detail">
                <h1 class="text-center"><%= product.getProduct_Name()%></h1>
                <div class="row">
                    <div class="col-md-6">
                        <img src="<%= product.getImage()%>" alt="<%= product.getProduct_Name()%>" class="img-fluid">
                    </div>
                    <div class="col-md-6">
                        <h2>Price: $<%= product.getPrice()%></h2>
                        <div class="mb-3">
                            <%
                                if (user != null) {
                                    FavoriteDAO favoriteDAO = new FavoriteDAO();
                                    boolean isFavorite = favoriteDAO.isFavorite(user.getUserId(), product.getProduct_ID());
                            %>
                            <form action="favorite" method="post" class="d-inline">
                                <input type="hidden" name="productId" value="<%= product.getProduct_ID()%>">
                                <input type="hidden" name="action" value="<%= isFavorite ? "remove" : "add"%>">
                                <button type="submit" class="btn btn-link p-0 border-0 bg-transparent">
                                    <i class="fa fa-heart <%= isFavorite ? "text-danger" : "text-secondary"%> fa-2x"></i>
                                </button>
                            </form>
                            <span class="text-muted"><%= isFavorite ? "Liked" : "Add to favorites"%></span>
                            <% } else { %>
                            <a href="login.jsp" class="text-decoration-none">
                                <i class="fa fa-heart text-secondary fa-2x"></i>
                                <span class="text-muted">Log in to like</span>
                            </a>
                            <% }%>
                        </div>
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

        <div class="footer mt-4">
            <footer>
                <div class="footer-container">
                    <p>&copy; 2025 Shoe Shop. All rights reserved.</p>
                    <a href="#">Privacy Policy</a> | <a href="#">Terms of Use</a>
                </div>
                <div class="footer-bottom">
                    <p>&copy; 2025 Shoe Shop. All rights reserved.</p>
                </div>
            </footer>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
