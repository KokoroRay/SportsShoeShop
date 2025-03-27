<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Arrays"%>
<%@page import="dao.FavoriteDAO"%>
<%@page import="model.User"%>
<%@page import="java.util.List"%>
<%@page import="model.Product"%>
<%@page import="dao.ProductDAO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    // Xử lý parameters
    String[] selectedBrands = request.getParameterValues("brands");
    List<String> brandList = new ArrayList<>();
    if (selectedBrands != null && selectedBrands.length > 0) {
        brandList = new ArrayList<>(Arrays.asList(selectedBrands));
    }

    // Khởi tạo DAO
    ProductDAO productDAO = new ProductDAO();
    List<Product> filteredProducts = productDAO.getProductsByBrands(brandList);

    // Xử lý user session
    User user = (User) session.getAttribute("user");
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <link rel="stylesheet" href="style/style.css"/>
        <style>
            .sidebar {
                background-color: #f8f9fa;
                padding: 20px;
                border-right: 1px solid #dee2e6;
            }
            .filter-section {
                margin-bottom: 30px;
            }
            .product-table {
                width: 100%;
                border-collapse: collapse;
            }
            .product-table td, .product-table th {
                padding: 12px;
                border: 1px solid #dee2e6;
            }
            .price-highlight {
                color: #dc3545;
                font-weight: bold;
            }

            .sidebar {
                background-color: #f8f9fa;
                padding: 20px;
                border-radius: 8px;
                box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            }

            .sidebar h5 {
                color: #333;
                font-weight: 600;
                border-bottom: 2px solid #007bff;
                padding-bottom: 8px;
            }

            .list-unstyled li {
                padding: 8px 0;
                border-bottom: 1px solid #eee;
            }

            .list-unstyled li:last-child {
                border-bottom: none;
            }

            .card {
                height: 100%;
                overflow: hidden;
                transition: transform 0.3s; /* Hiệu ứng hover */
                margin-bottom: 20px; /* Khoảng cách giữa các card */
            }

            .card:hover {
                transform: translateY(-5px); /* Hiệu ứng nổi lên khi hover */
            }

            .card-img-top {
                background: #f8f9fa; /* Nền cho phần trống (nếu có) */
                border-radius: 8px 8px 0 0;
            }

            .card-body {
                padding: 1rem;
                flex-grow: 1; /* Căn đều nội dung */
            }
        </style>
    </head>
    <body>
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
        <nav class="navbar navbar-expand-lg bg-body-tertiary">
            <div class="container-fluid ">
                <a class="navbar-brand" href="home.jsp" style="font-weight: bold; ">Home</a>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNavDropdown" aria-controls="navbarNavDropdown" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarNavDropdown">
                    <ul class="navbar-nav w-100 d-flex justify-content-cente" >
                        <li class="nav-item mx-3" >
                            <a class="nav-link active" aria-current="page" href="all.jsp" style="font-weight: bold;">All</a>
                        </li>
                        <li class="nav-item mx-3">
                            <a class="nav-link" href="TF_shoes.jsp" style="font-weight: bold;">TF Shoes</a>
                        </li>
                        <li class="nav-item mx-3">
                            <a class="nav-link" href="IC_shoes.jsp" style="font-weight: bold;">IC Shoes</a>
                        </li>
                        <li class="nav-item dropdown mx-3">
                            <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false" style="font-weight: bold; color: red">
                                Brand
                            </a>
                            <ul class="dropdown-menu">
                                <li><a class="dropdown-item" href="nike.jsp">Nike</a></li>
                                <li><a class="dropdown-item" href="adidas.jsp">Adidas</a></li>
                                <li><a class="dropdown-item" href="puma.jsp">Puma</a></li>
                                <li><a class="dropdown-item" href="mizuno.jsp">Mizuno</a></li>
                                <li><a class="dropdown-item" href="joma.jsp">Joma</a></li>
                                <li><a class="dropdown-item" href="kamito.jsp">Kamito</a></li>
                                <li><a class="dropdown-item" href="other.jsp"style="color: red">Other</a></li>
                            </ul>
                        </li>
                        <li class="nav-item mx-3">
                            <a class="nav-link" href="contact.jsp" style="font-weight: bold;">Contact</a>
                        </li>
                        <li class="nav-item mx-3">
                            <a class="nav-link" href="rate.jsp" style="font-weight: bold;">Rate</a>
                        </li>
                    </ul>
                </div>
            </div>
        </nav>
    </div>          
    <div class="container-fluid mt-4">
        <div class="row">
            <!-- Sidebar Filters -->
            <div class="col-md-3 sidebar">
                <div class="d-flex align-items-center">
                    <h5 class="me-2">Thương hiệu</h5>
                </div>
                <form id="brandFilterForm" action="other.jsp" method="GET">
                    <ul class="list-unstyled">
                        <%
                            List<String> brands = productDAO.getAllBrands();
                            if (brands != null && !brands.isEmpty()) {
                                for (String brand : brands) {
                                    String brandId = brand.replaceAll("\\s+", "").toLowerCase();
                                    boolean isChecked = brandList.contains(brand);
                        %>
                        <li class="d-flex align-items-center">
                            <input type="checkbox" 
                                   id="<%= brandId%>" 
                                   class="me-2" 
                                   name="brands" 
                                   value="<%= brand%>"
                                   <%= isChecked ? "checked" : ""%> 
                                   onchange="submitFilterForm()">
                            <label for="<%= brandId%>"><%= brand%></label>
                        </li>
                        <%
                            }
                        } else {
                        %>
                        <li class="text-muted">No brand</li>
                            <% } %>
                    </ul>
                </form>
            </div>

            <!-- Product List Section -->
            <div class="col-md-9">
                <h2 class="text-center mb-4">
                    <% if (!brandList.isEmpty()) {%>
                    <%= String.join(", ", brandList)%> Shoes
                    <% } else { %>
                    All Shoes
                    <% } %>
                </h2>
                <div class="row">
                    <% if (filteredProducts != null && !filteredProducts.isEmpty()) { %>
                    <% for (Product product : filteredProducts) {%>
                    <!-- Trong phần hiển thị sản phẩm -->
                    <div class="col-md-4 col-12 mb-4"> <!-- Thay col-md-6 thành col-md-4 -->
                        <div class="card h-100 shadow-sm">
                            <div class="position-relative">
                                <img src="<%= product.getImage()%>" 
                                     class="card-img-top img-fluid" 
                                     alt="<%= product.getProduct_Name()%>"
                                     style="width: 100%; height: 400px; object-fit: contain;"> <!-- Giảm chiều cao hình ảnh -->
                                <% if (product.getDiscountPercent() > 0) {%>
                                <div class="badge bg-danger position-absolute top-0 start-0 m-2">
                                    -<%= product.getDiscountPercent()%>%
                                </div>
                                <% }%>
                            </div>

                            <div class="card-body d-flex flex-column">
                                <h5 class="card-title"><%= product.getProduct_Name()%></h5>
                                <p class="card-text flex-grow-1 text-muted">
                                    <%= product.getDescription()%>
                                </p>

                                <div class="mt-auto">
                                    <div class="d-flex justify-content-between align-items-center mb-3">
                                        <% if (product.getDiscountPercent() > 0) {%>
                                        <div>
                                            <span class="text-decoration-line-through text-muted">
                                                $<%= product.getOriginalPrice()%>
                                            </span>
                                            <span class="text-danger fw-bold fs-5 ms-2">
                                                $<%= product.getPrice()%>
                                            </span>
                                        </div>
                                        <% } else {%>
                                        <span class="text-danger fw-bold fs-5">
                                            $<%= product.getPrice()%>
                                        </span>
                                        <% }%>
                                    </div>

                                    <div class="d-grid gap-2">
                                        <a href="DetailController?productId=<%= product.getProduct_ID()%>" 
                                           class="btn btn-outline-primary">
                                            <i class="fa fa-info-circle me-2"></i>View 
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <% } %>
                    <% } else {%>
                    <div class="col-12">
                        <div class="alert alert-info text-center py-4">
                            <i class="fa fa-info-circle fa-2x mb-3"></i>
                            <h4>
                                <%= brandList.isEmpty()
                                        ? "No products available"
                                        : "No products found for selected brands"%>
                            </h4>
                        </div>
                    </div>
                    <% }%>
                </div>
            </div>
        </div>
    </div>



    <!-- footer -->
    <div class="footer">
        <footer>
            <div class="footer-container">
                <!-- Về chúng tôi -->
                <div class="footer-section about">
                    <h3>About us</h3>
                    <p>We are a shoe store that specializes in providing high quality products at reasonable prices. Customers are our top priority.</p>
                </div>

                <!-- Liên kết -->
                <div class="footer-section links">
                    <h3>Link</h3>
                    <ul>
                        <li><a href="aboutUs.jsp">About us</a></li>
                        <li><a href="home.jsp">Store</a></li>
                        <li><a href="contact.jsp">Contact</a></li>
                        <li><a href="policy.jsp">Privacy policy</a></li>    
                        <li><a href="faq.jsp">FAQ</a></li>
                    </ul>
                </div>

                <!-- Liên hệ -->
                <div class="footer-section contact">
                    <h3>Contact</h3>
                    <p>Address: SE1817, FPT University</p>
                    <p>Phone number: 123456789 </p>
                    <p>Email: kokororay356@gmail.com</p>
                </div>

                <!-- Theo dõi chúng tôi -->
                <div class="footer-section social">
                    <h3>Follow us</h3>
                    <a href="#"><img src="asset/logofacebook.png" alt="Facebook"></a>
                    <a href="#"><img src="asset/logoInsta.png" alt="Instagram"></a>
                </div>
            </div>

            <div class="footer-bottom">
                <p>&copy; Group 7.</p>
            </div>
        </footer>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
                                       // Submit form tự động khi thay đổi checkbox
                                       function submitFilterForm() {
                                           document.getElementById("brandFilterForm").submit();
                                       }

                                       // Hàm giả lập thêm vào giỏ hàng
                                       function addToCart(productId) {
                                           console.log("Added product to cart:", productId);
                                           // Thêm logic xử lý giỏ hàng tại đây
                                       }
    </script>
</body>
</html>
