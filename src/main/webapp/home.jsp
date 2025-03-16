<%-- 
    Document   : home
    Created on : Oct 20, 2024, 7:13:25 PM
    Author     : ADMIN
--%>

<%@page import="dao.FavoriteDAO"%>
<%@page import="java.util.List"%>
<%@page import="model.Product"%>
<%@page import="dao.ProductDAO"%>
<%@page import="model.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    // Retrieve user info from session
    User user = (User) session.getAttribute("user");

    // Fetch the list of products from the database
    ProductDAO productDAO = new ProductDAO();
    List<Product> productList = productDAO.getAllProducts();

    List<Product> bestsellerList = productDAO.getBestsellerProducts();
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Home Page</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" crossorigin="anonymous">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <link rel="stylesheet" href="style/style.css"/>
        <style>
            /* Bestseller slider styles */
            .bestseller-slider {
                overflow: hidden;
                position: relative;
                width: 100%;
                height: 400px; /* Tăng chiều cao slider */
                margin-bottom: 40px;
            }
            .slider-track {
                display: flex;
                width: calc(200%);
                animation: scroll 20s linear infinite;
            }
              .slider-track:hover {
                  animation-play-state: paused;
              }
            .bestseller-card {
                flex: 0 0 auto;
                width: 300px;  /* Tăng chiều rộng card */
                height: 400px; /* Tăng chiều cao card */
                margin-right: 15px;
                display: flex;
                flex-direction: column;
                justify-content: space-between;
            }
            /* Đặt hình ảnh chiếm 3/4 chiều cao card (400px * 3/4 = 300px) */
            .bestseller-card img {
                width: 100%;
                height: 300px;
                object-fit: cover;
            }
            /* Phần thông tin (card-body) sẽ tự động chiếm phần còn lại (100px) */
            @keyframes scroll {
                0% {
                    transform: translateX(0);
                }
                100% {
                    transform: translateX(-50%);
                }
            }

            /* Style cho badge yêu thích */
            .favorite-badge {
                position: absolute;
                top: 10px;
                left: 10px;
                background: rgba(255, 255, 255, 0.8);
                padding: 5px 10px;
                border-radius: 15px;
                z-index: 2;
                display: flex;
                align-items: center;
                gap: 5px;
                box-shadow: 0 2px 5px rgba(0,0,0,0.1);
            }

            .favorite-count {
                font-weight: 600;
                color: #dc3545;
            }

            /* Đảm bảo card có position relative */
            .bestseller-card {
                position: relative; /* Thêm dòng này */
                flex: 0 0 auto;
                width: 300px;
                height: 400px;
                margin-right: 15px;
                display: flex;
                flex-direction: column;
                justify-content: space-between;
            }
            /* Badge giảm giá */
            .discount-badge {
                position: absolute;
                top: 10px;
                right: 10px;
                background: #dc3545;
                color: white;
                padding: 5px 10px;
                border-radius: 5px;
                font-weight: bold;
                z-index: 2;
            }

            /* Thời gian còn lại (trang chủ) */
            .time-left-badge {
                position: absolute;
                bottom: 10px;
                left: 10px;
                background: #28a745;
                color: white;
                padding: 5px 10px;
                border-radius: 15px;
                font-size: 12px;
                z-index: 2;
            }

            /* Giá gốc */
            .original-price {
                text-decoration: line-through;
                color: #6c757d;
                margin-right: 8px;
            }

            /* Giá sau giảm */
            .discounted-price {
                color: #dc3545;
                font-weight: bold;
            }

            /* Thời gian còn lại (trang chi tiết) */
            .time-left {
                color: #28a745;
                font-size: 14px;
                margin: 10px 0;
            }

            .time-left .fa-clock {
                margin-right: 5px;
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
                        <li class="nav-item">
                            <a class="nav-link active" aria-current="page" href="orderStatus.jsp"> order status <i class=" "></i>
                            </a>
                        </li>

                        <% }%>
                    </ul>
                </div>
            </div>
        </nav>
        <nav class="navbar navbar-expand-lg bg-body-tertiary">
            <div class="container-fluid ">
                <a class="navbar-brand" href="home.jsp" style="font-weight: bold; color: red">Home</a>
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
                            <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false" style="font-weight: bold;">
                                Brand
                            </a>
                            <ul class="dropdown-menu">
                                <li><a class="dropdown-item" href="nike.jsp">Nike</a></li>
                                <li><a class="dropdown-item" href="adidas.jsp">Adidas</a></li>
                                <li><a class="dropdown-item" href="puma.jsp">Puma</a></li>
                                <li><a class="dropdown-item" href="mizuno.jsp">Mizuno</a></li>
                                <li><a class="dropdown-item" href="joma.jsp">Joma</a></li>
                                <li><a class="dropdown-item" href="kamito.jsp">Kamito</a></li>
                                <li><a class="dropdown-item" href="other.jsp">Other</a></li>
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
        <div id="carouselExampleAutoplaying" class="carousel slide" data-bs-ride="carousel">
            <div class="carousel-inner">
                <div class="carousel-item active">
                    <img src="asset/banner.png" class="d-block w-100" alt="banner">
                </div>
                <div class="carousel-item">
                    <img src="asset/banner_1.webp" class="d-block w-100" alt="...">
                </div>
                <div class="carousel-item">
                    <img src="asset/banner_2.webp" class="d-block w-100" alt="...">
                </div>
                <div class="carousel-item">
                    <img src="asset/banner_3.webp" class="d-block w-100" alt="...">
                </div>
                <div class="carousel-item">
                    <img src="asset/banner_4.webp" class="d-block w-100" alt="...">
                </div>
            </div>
            <button class="carousel-control-prev" type="button" data-bs-target="#carouselExampleAutoplaying" data-bs-slide="prev">
                <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                <span class="visually-hidden">Previous</span>   
            </button>
            <button class="carousel-control-next" type="button" data-bs-target="#carouselExampleAutoplaying" data-bs-slide="next">
                <span class="carousel-control-next-icon" aria-hidden="true"></span>
                <span class="visually-hidden">Next</span>
            </button>
        </div>
        <!-- Banner phụ -->
        <div>
            <img src="asset/home.jpg" class="d-block w-100" alt="banner">
        </div>

        <!-- Phần Bestseller List: 5 sản phẩm bán chạy nhất -->
        <div class="container mt-5">
            <h3 class="text-center mb-4" style="color: red; font-weight: bold; font-size: 2rem;">Bestseller</h3>
            <div class="bestseller-slider">
                <div class="slider-track">
                    <% if (bestsellerList != null && !bestsellerList.isEmpty()) {
                            for (Product p : bestsellerList) {
                                boolean isDiscountActive = productDAO.isDiscountActive(p);
                    %>
                    <a href="product_detail.jsp?productId=<%= p.getProduct_ID()%>" class="text-decoration-none">
                        <div class="card bestseller-card">
                            <!-- Badge giảm giá -->
                            <% if (isDiscountActive) {
                                    long remainingDays = p.getRemainingDiscountDays();
                                    if (remainingDays >= 0) {
                            %>
                            <div class="discount-badge bg-danger text-white p-1 rounded">
                                -<%= Math.round(p.getDiscountPercent())%>%
                            </div>
                            <div class="time-left-badge bg-warning text-dark p-1 rounded">
                                Còn <%= remainingDays%> ngày
                            </div>
                            <% }
                                }%>

                            <img src="<%= p.getImage()%>" class="card-img-top" alt="<%= p.getProduct_Name()%>">
                            <div class="card-body p-2">
                                <h6 class="card-title text-center mb-2"><%= p.getProduct_Name()%></h6>
                                <div class="price text-center">
                                    <% if (isDiscountActive && p.getOriginalPrice() > 0) {%>
                                    <span class="original-price text-muted text-decoration-line-through me-2">
                                        $<%= String.format("%.2f", p.getOriginalPrice())%>
                                    </span>
                                    <span class="discounted-price text-danger fw-bold">
                                        $<%= String.format("%.2f", productDAO.getDiscountedPrice(p))%>
                                    </span>
                                    <% } else {%>
                                    <span class="current-price fw-bold">
                                        $<%= String.format("%.2f", p.getPrice())%>
                                    </span>
                                    <% } %>
                                </div>
                            </div>
                        </div>
                    </a>
                    <% }
                    } else { %>
                    <div class="col-12">
                        <div class="alert alert-info text-center">No bestseller products found.</div>
                    </div>
                    <% } %>
                </div>
            </div>
        </div>
        <!-- Phần Most Favorited Products -->
        <div class="container mt-5">
            <h3 class="text-center mb-4" style="color: red; font-weight: bold; font-size: 2rem;">Most Loved</h3>
            <div class="bestseller-slider">
                <div class="slider-track">
                    <%
                        List<Product> mostFavorited = productDAO.getMostFavoritedProducts();
                        if (mostFavorited != null && !mostFavorited.isEmpty()) {
                            for (Product p : mostFavorited) {
                    %>
                    <a href="product_detail.jsp?productId=<%= p.getProduct_ID()%>" class="text-decoration-none">
                        <div class="card bestseller-card">
                            <div class="favorite-badge">
                                <i class="fa fa-heart text-danger"></i>
                                <span class="favorite-count"><%= p.getFavoriteCount()%></span>
                            </div>
                            <img src="<%= p.getImage()%>" class="card-img-top" alt="<%= p.getProduct_Name()%>">
                            <div class="card-body p-2">
                                <h6 class="card-title text-center"><%= p.getProduct_Name()%></h6>
                                <p class="card-text text-center" style="color: red; font-weight: bold;">
                                    $<%= p.getPrice()%>
                                </p>
                            </div>
                        </div>
                    </a>
                    <%
                        }
                    } else {
                    %>
                    <div class="col-12">
                        <div class="alert alert-info text-center">No products found.</div>
                    </div>
                    <% }%>
                </div>
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
        <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
        <script>
            $(window).on('load', function () {
                var $sliderTrack = $('.slider-track');
                var trackWidth = $sliderTrack.width(); // sau khi hình ảnh đã load
                var currentPos = 0;
                var step = 1;
                var intervalTime = 20;

                setInterval(function () {
                    currentPos -= step;
                    if (Math.abs(currentPos) >= trackWidth / 2) {
                        currentPos = 0;
                    }
                    $sliderTrack.css('transform', 'translateX(' + currentPos + 'px)');
                }, intervalTime);
            });
        </script>

    </body>
</html>
