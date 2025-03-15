<%@ page import="java.util.List" %>
<%@ page import="model.Product" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    // Nhận danh sách kết quả tìm kiếm từ request do searchController gửi tới
    List<Product> searchResults = (List<Product>) request.getAttribute("searchResults");
%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Kết quả tìm kiếm</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <style>
            body {
                background-color: #f8f9fa;
                font-family: Arial, sans-serif;
            }
            .container {
                padding: 3rem 0;
            }
            .product-list {
                transition: transform 0.2s ease-in-out;
                border: none;
                overflow: hidden;
                border-radius: 10px;
                background-color: #fff;
                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            }
            .product-list:hover {
                transform: scale(1.05);
                box-shadow: 0 8px 16px rgba(0, 0, 0, 0.15);
            }
            .product-img img {
                width: 100%;
                height: auto;
                object-fit: cover;
                border-top-left-radius: 10px;
                border-top-right-radius: 10px;
            }
            .product-info {
                padding: 1rem;
                text-align: center;
            }
            .product-info h5 {
                font-size: 1.1rem;
                color: #333;
                font-weight: bold;
                margin: 0.5rem 0;
            }
            .product-info .description {
                font-size: 0.9rem;
                color: #777;
                margin-bottom: 0.75rem;
            }
            .product-price .current-price {
                font-size: 1.25rem;
                color: #e74c3c;
                font-weight: bold;
            }
            .product-new {
                position: absolute;
                top: 10px;
                left: 10px;
                font-size: 0.8rem;
                padding: 0.3rem 0.6rem;
                background-color: #ff4757;
                color: #fff;
                border-radius: 4px;
                font-weight: bold;
            }
            .logo{
                width: 200px; /* Chiều rộng hình ảnh */
                height: auto; /* Chiều cao tự động để giữ tỉ lệ hình ảnh */
            }
            .mustbuy-container{
                text-align: center;
            }
            .title-line{
                padding-top: 50px;
                padding-bottom: 20px;
            }

            .mustbuy-img{
                width: 100%;
            }

            .mustbuy-img img{
                max-width: 100%;
            }

            .mustbuy__image img{
                width: 100%;  /* Hình ảnh sẽ chiếm 100% chiều rộng của container */
                height: auto; /* Giữ tỷ lệ chiều cao theo chiều rộng */
                max-width: 150px; /* Giới hạn kích thước tối đa cho hình ảnh */
            }

            .row img{
                max-width: 100%;
            }

            .featured-products{
                padding-top: 3%;
            }

            .footer{
                margin-top: 3%;
                text-align: center;
            }

            .footer {
                background-color: #333;
                color: #fff;
                padding: 40px 0;
                margin-top: 40px;
            }

            .footer-container {
                display: flex;
                justify-content: space-between;
                flex-wrap: wrap;
                max-width: 1200px;
                margin: 0 auto;
                padding: 0 20px;
            }

            .footer-section {
                flex-basis: 25%;
                margin-bottom: 20px;
            }

            .footer-section h3 {
                color: #f4f4f9;
                margin-bottom: 15px;
                font-size: 18px;
                text-transform: uppercase;
            }

            .footer-section p, .footer-section ul, .footer-section a {
                font-size: 14px;
                color: #dcdcdc;
            }

            .footer-section ul {
                list-style-type: none;
                padding: 0;
            }

            .footer-section ul li {
                margin-bottom: 10px;
            }

            .footer-section ul li a {
                color: #dcdcdc;
                text-decoration: none;
                transition: color 0.3s ease;
            }

            .footer-section ul li a:hover {
                color: #ffa500;
            }

            .footer-section.contact p {
                margin-bottom: 10px;
            }

            .footer-section.social a {
                margin-right: 10px;
            }

            .footer-section.social a img {
                width: 24px;
                height: 24px;
                transition: transform 0.3s ease;
            }

            .footer-section.social a:hover img {
                transform: scale(1.1);
            }

            .footer-bottom {
                text-align: center;
                padding: 20px 0;
                background-color: #222;
                color: #999;
                font-size: 14px;
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
        <nav class="navbar navbar-expand-lg bg-body-tertiary">
            <div class="container-fluid ">
                <a class="navbar-brand" href="home.jsp" style="font-weight: bold;">Home</a>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNavDropdown" aria-controls="navbarNavDropdown" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarNavDropdown">
                    <ul class="navbar-nav w-100 d-flex justify-content-cente" >
                        <li class="nav-item mx-3" >
                            <a class="nav-link active" aria-current="page" href="all.jsp" style="font-weight: bold; color: red">All</a>
                        </li>
                        <li class="nav-item mx-3">
                            <a class="nav-link" href="TF_shoes.jsp" style="font-weight: bold;">TF Shoes</a>
                        </li>
                        <li class="nav-item mx-3">
                            <a class="nav-link" href="IC_shoes.jsp" style="font-weight: bold;">IC Shoes</a>
                        </li>
                        <li class="nav-item dropdown mx-3">
                            <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false" style="font-weight: bold;">
                                Brand
                            </a>
                            <ul class="dropdown-menu">
                                <li><a class="dropdown-item" href="nike.jsp">Nike</a></li>
                                <li><a class="dropdown-item" href="adidas.jsp">Adidas</a></li>
                                <li><a class="dropdown-item" href="#">Puma</a></li>
                                <li><a class="dropdown-item" href="mizuno.jsp">Mizuno</a></li>
                                <li><a class="dropdown-item" href="#">Joma</a></li>
                                <li><a class="dropdown-item" href="#">Kamito</a></li>
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
        <div class="container">
            <h3 class="mb-4">Result Search: </h3>
            <div class="row g-4">
                <% if (searchResults != null && !searchResults.isEmpty()) { %>
                <% for (Product product : searchResults) {%>
                <div class="col-md-3 col-sm-6 col-12">
                    <div class="card product-list position-relative">
                        <a href="DetailController?productId=<%=product.getProduct_ID()%>" class="text-decoration-none text-dark">
                            <div class="product-new">NEW ITEM</div>
                            <div class="product-img">
                                <img src="<%=product.getImage()%>" alt="<%=product.getProduct_Name()%>" />
                            </div>
                            <div class="product-info">
                                <h5><%=product.getProduct_Name()%></h5>
                                <p class="description"><%=product.getDescription()%></p>
                                <div class="product-price">
                                    <span class="current-price"><%=product.getPrice()%>$</span>
                                </div>
                            </div>
                        </a>
                    </div>
                </div>
                <% } %>
                <% } else { %>
                <p class="text-center">There are no products matching your search!!</p>
                <% }%>
            </div>
        </div>
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
    </body>
</html>
