<%@page import="java.util.List"%>
<%@page import="model.User"%>
<%@page import="dao.ProductDAO"%>
<%@page import="model.Product"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    // Retrieve user info from session
    User user = (User) session.getAttribute("user");

    // Fetch the list of products from the database
    ProductDAO productDAO = new ProductDAO();
    List<Product> productList = productDAO.getAllProducts();
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Product Details</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
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
        <%
            Product product = (Product) request.getAttribute("product");
        %>

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
                        <li class="nav-item">
                            <a class="nav-link active" aria-current="page" href="login.jsp">    <i class="fa fa-user"></i>
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link active" aria-current="page" href="shopping.jsp"><i class="fa fa-cart-plus"></i>
                            </a>
                        </li>
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

        <!-- Product Details -->
        <div class="container mt-4">
            <div class="product-detail">
                <h1 class="text-center"><%= product.getProduct_Name() %></h1>
                <div class="row">
                    <div class="col-md-6">
                        <img src="<%= product.getImage() %>" alt="<%= product.getProduct_Name() %>" class="img-fluid">
                    </div>
                    <div class="col-md-6">
                        <h2>Price: <%= product.getPrice() %>$</h2>
                        <p><strong>Brand:</strong> <%= product.getBrand() %></p>
                        <p><strong>Description:</strong> <%= product.getDescription() %></p>
                        <p><strong>Rating:</strong> <%= product.getRate() %> ⭐</p>

                        <% if (user == null) { %>
                            <a href="login.jsp" class="btn btn-success">Log in to purchase</a>
                        <% } else { %>
                            <!-- Add to Cart Form -->
                            <form action="CartServlet" method="post">
                                <input type="hidden" name="productId" value="<%= product.getProduct_ID()%>"/>
                                <input type="hidden" name="productName" value="<%= product.getProduct_Name() %>"/>
                                <input type="hidden" name="price" value="<%= product.getPrice() %>"/>

                                <!-- Size Selection -->
                                <label for="size">Size:</label>
                                <select name="size" required>
                                    <option value="39">39</option>
                                    <option value="40">40</option>
                                    <option value="41">41</option>
                                </select>

                                <!-- Quantity Selection -->
                                <label for="quantity">Quantity:</label>
                                <input type="number" name="quantity" min="1" value="1" required/>

                                <!-- Add to Cart Button -->
                                <button type="submit" class="btn btn-success">Add to Cart</button>
                            </form>
                        <% } %>
                    </div>
                </div>
            </div>
        </div>

        <!-- Footer -->
        <div class="footer mt-4">
            <footer>
                <div class="footer-container">
                    <p>&copy; 2024 Shoe Shop. All rights reserved.</p>
                    <a href="#">Privacy Policy</a> | <a href="#">Terms of Use</a>
                </div>
                <div class="footer-bottom">
                    <p>&copy; 2024 Shoe Shop. All rights reserved.</p>
                </div>
            </footer>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
    </body>
</html>
