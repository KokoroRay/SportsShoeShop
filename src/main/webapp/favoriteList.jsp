<%@page import="dao.FavoriteDAO, model.Product, model.User, java.util.List"%>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    FavoriteDAO favoriteDAO = new FavoriteDAO();
    List<Product> favorites = favoriteDAO.getFavoriteProducts(user.getUserId());
%>

<!DOCTYPE html>
<html>
    <head>
        <title>Wishlist</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <script>
            function toggleSelectAll(source) {
                const checkboxes = document.querySelectorAll('.product-checkbox');
                checkboxes.forEach(checkbox => {
                    checkbox.checked = source.checked;
                });
            }
            
            function validateSelection() {
                const checkboxes = document.querySelectorAll('.product-checkbox:checked');
                if (checkboxes.length === 0) {
                    alert("Please select at least one product!");
                    return false;
                }
                return true;
            }
        </script>
    </head>
    <body>
        <div class="container mt-4">
            <%-- Hi?n th? thông báo l?i --%>
            <% if ("no_selection".equals(request.getParameter("error"))) { %>
            <div class="alert alert-warning alert-dismissible fade show" role="alert">
                Please select at least one product before adding to cart!
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
            <% } %>
            
            <% if (favorites == null || favorites.isEmpty()) { %>
            <!-- Empty wishlist state -->
            <div class="text-center py-5">
                <div class="card shadow-sm mx-auto" style="max-width: 400px;">
                    <div class="card-body py-5">
                        <i class="far fa-heart fa-4x text-muted mb-4"></i>
                        <h3 class="mb-3">Your wishlist is empty</h3>
                        <p class="text-muted mb-4">Start adding items you love!</p>
                        <a href="all.jsp" class="btn btn-primary px-5">
                            <i class="fas fa-shopping-bag"></i> Continue Shopping
                        </a> <!-- ?ã thêm th? ?óng </a> -->
                    </div>
                </div>
            </div>
            <% } else { %>
            <!-- Wishlist with items -->
            <form action="AddToCartServlet" method="post" onsubmit="return validateSelection()">
                <div class="d-flex justify-content-between align-items-center mb-4">
                    <div class="form-check">
                        <input type="checkbox" 
                               class="form-check-input" 
                               id="selectAll"
                               onclick="toggleSelectAll(this)">
                        <label class="form-check-label" for="selectAll">Select All</label>
                    </div>
                    <div>
                        <button type="submit" class="btn btn-success me-2">
                            <i class="fas fa-cart-plus"></i> Add Selected to Cart
                        </button>
                        <button type="submit" formaction="RemoveFavoriteServlet" class="btn btn-danger">
                            <i class="fas fa-trash"></i> Remove Selected
                        </button>
                    </div>
                </div>

                <div class="row row-cols-1 row-cols-md-3 g-4">
                    <% for (Product p : favorites) { %>
                    <div class="col">
                        <div class="card h-100">
                            <div class="card-body">
                                <div class="form-check">
                                    <input type="checkbox" 
                                           class="form-check-input product-checkbox"
                                           name="productIds" 
                                           value="<%= p.getProduct_ID() %>">
                                </div>
                                <img src="<%= p.getImage() %>" 
                                     class="card-img-top mb-3" 
                                     alt="<%= p.getProduct_Name() %>">
                                <h5 class="card-title"><%= p.getProduct_Name() %></h5>
                                <p class="text-muted"><%= p.getBrand() %></p>
                                <p class="h5 text-primary">$<%= p.getPrice() %></p>
                            </div>
                        </div>
                    </div>
                    <% } %>
                </div>
            </form>
            <% } %>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
        <style>
            .product-checkbox {
                transform: scale(1.2);
                margin-bottom: 1rem;
            }
            .card {
                transition: transform 0.2s;
            }
            .card:hover {
                transform: translateY(-5px);
            }
            .fa-heart {
                color: #dc3545;
            }
        </style>
    </body>
</html>