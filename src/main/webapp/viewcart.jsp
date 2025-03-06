<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="model.CartItem"%>
<%@page import="model.User"%>
<%@page import="dao.ProductDAO"%>

<%
    User user = (User) session.getAttribute("user");
    List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
    ProductDAO productDAO = new ProductDAO();

    double totalCost = 0;
    double shippingCost = 0;
    String northernProvinces = "Hà Nội,Hải Phòng,Bắc Giang,Bắc Kạn,Bắc Ninh,Cao Bằng,Hà Giang,Hà Nam,Hưng Yên,Lạng Sơn,Lào Cai,Nam Định,Ninh Bình,Phú Thọ,Quảng Ninh,Thái Bình,Thái Nguyên,Thanh Hóa,Tuyên Quang,Vĩnh Phúc,Yên Bái,Điện Biên,Hòa Bình,Lai Châu,Sơn La,Hải Dương";

    if (cart != null && !cart.isEmpty()) {
        for (CartItem item : cart) {
            totalCost += item.getTotalPrice();
        }
    }
    double subtotal = totalCost;
    totalCost += shippingCost;
%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Checkout</title>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
        <script>
        var northernProvinces = "<%= northernProvinces%>".split(',');

        function updateShippingCost() {
            const province = document.getElementById('province').value;
            const isNorthern = northernProvinces.includes(province);
            const shippingCost = isNorthern ? 2.00 : 0.00;
            const subtotal = parseFloat(document.getElementById('subtotal').value);
            document.getElementById('shippingCost').textContent = shippingCost.toFixed(2);
            document.getElementById('grandTotal').textContent = (subtotal + shippingCost).toFixed(2);
        }

        window.onload = function () {
            if(document.getElementById('province')){
                updateShippingCost();
                document.getElementById('province').addEventListener('change', updateShippingCost);
            }
        };
    </script>
    </head>
    <body>
        <div class="container mt-5">
            <% if (cart == null || cart.isEmpty()) { %>
            <!-- Empty Cart Design -->
            <div class="text-center">
                <div class="card shadow-lg p-5 mb-5 bg-white rounded" style="max-width: 600px; margin: 0 auto;">
                    <i class="fas fa-shopping-cart fa-5x text-muted mb-4"></i>
                    <h2 class="text-secondary mb-4">Your cart is empty</h2>
                    <p class="text-muted mb-4">Looks like you haven't added any items to your cart yet.</p>
                    <a href="all.jsp" class="btn btn-primary btn-lg px-5">
                        <i class="fas fa-arrow-left mr-2"></i>Continue Shopping
                    </a>
                </div>
            </div>
            <% } else { %>
            <!-- Existing Checkout Form -->
            <h2 class="text-center mb-4">Checkout</h2>
            <!-- Rest of the checkout form remains the same -->
            <!-- ... (keep all existing checkout form code here) ... -->
            <% }%>
        </div>
    <body>
        <div class="container mt-5">
            <h2 class="text-center mb-4">Checkout</h2>

            <!-- Form chính chứa tất cả thông tin và nút Checkout -->
            <form id="checkoutForm" action="CheckoutServlet" method="post" class="needs-validation" novalidate>
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="fullName">Full Name</label>
                        <input type="text" class="form-control" id="fullName" name="fullName" required>
                        <div class="invalid-feedback">Please enter your full name.</div>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="phoneNumber">Phone Number</label>
                        <input type="text" class="form-control" id="phoneNumber" name="phoneNumber" required>
                        <div class="invalid-feedback">Please enter your phone number.</div>
                    </div>
                </div>

                <div class="mb-3">
                    <label for="email">Email</label>
                    <input type="email" class="form-control" id="email" name="email" value="<%= user != null ? user.getEmail() : ""%>" readonly>
                </div>

                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="province">Province/City</label>
                        <select class="form-control" id="province" name="province" required onchange="updateShippingCost()">
                            <option value="">Select Province/City</option>
                            <option value="An Giang">An Giang</option>
                            <option value="Bà Rịa - Vũng Tàu">Bà Rịa - Vũng Tàu</option>
                            <option value="Bắc Giang">Bắc Giang</option>
                            <option value="Bắc Kạn">Bắc Kạn</option>
                            <option value="Bạc Liêu">Bạc Liêu</option>
                            <option value="Bắc Ninh">Bắc Ninh</option>
                            <option value="Bến Tre">Bến Tre</option>
                            <option value="Bình Định">Bình Định</option>
                            <option value="Bình Dương">Bình Dương</option>
                            <option value="Bình Phước">Bình Phước</option>
                            <option value="Bình Thuận">Bình Thuận</option>
                            <option value="Cà Mau">Cà Mau</option>
                            <option value="Cao Bằng">Cao Bằng</option>
                            <option value="Đắk Lắk">Đắk Lắk</option>
                            <option value="Đắk Nông">Đắk Nông</option>
                            <option value="Điện Biên">Điện Biên</option>
                            <option value="Đồng Nai">Đồng Nai</option>
                            <option value="Đồng Tháp">Đồng Tháp</option>
                            <option value="Gia Lai">Gia Lai</option>
                            <option value="Hà Giang">Hà Giang</option>
                            <option value="Hà Nam">Hà Nam</option>
                            <option value="Hà Tĩnh">Hà Tĩnh</option>
                            <option value="Hải Dương">Hải Dương</option>
                            <option value="Hậu Giang">Hậu Giang</option>
                            <option value="Hòa Bình">Hòa Bình</option>
                            <option value="Hưng Yên">Hưng Yên</option>
                            <option value="Khánh Hòa">Khánh Hòa</option>
                            <option value="Kiên Giang">Kiên Giang</option>
                            <option value="Kon Tum">Kon Tum</option>
                            <option value="Lai Châu">Lai Châu</option>
                            <option value="Lâm Đồng">Lâm Đồng</option>
                            <option value="Lạng Sơn">Lạng Sơn</option>
                            <option value="Lào Cai">Lào Cai</option>
                            <option value="Long An">Long An</option>
                            <option value="Nam Định">Nam Định</option>
                            <option value="Nghệ An">Nghệ An</option>
                            <option value="Ninh Bình">Ninh Bình</option>
                            <option value="Ninh Thuận">Ninh Thuận</option>
                            <option value="Phú Thọ">Phú Thọ</option>
                            <option value="Phú Yên">Phú Yên</option>
                            <option value="Quảng Bình">Quảng Bình</option>
                            <option value="Quảng Nam">Quảng Nam</option>
                            <option value="Quảng Ngãi">Quảng Ngãi</option>
                            <option value="Quảng Ninh">Quảng Ninh</option>
                            <option value="Quảng Trị">Quảng Trị</option>
                            <option value="Sóc Trăng">Sóc Trăng</option>
                            <option value="Sơn La">Sơn La</option>
                            <option value="Tây Ninh">Tây Ninh</option>
                            <option value="Thái Bình">Thái Bình</option>
                            <option value="Thái Nguyên">Thái Nguyên</option>
                            <option value="Thanh Hóa">Thanh Hóa</option>
                            <option value="Thừa Thiên Huế">Thừa Thiên Huế</option>
                            <option value="Tiền Giang">Tiền Giang</option>
                            <option value="Trà Vinh">Trà Vinh</option>
                            <option value="Tuyên Quang">Tuyên Quang</option>
                            <option value="Vĩnh Long">Vĩnh Long</option>
                            <option value="Vĩnh Phúc">Vĩnh Phúc</option>
                            <option value="Yên Bái">Yên Bái</option>
                            <option value="Đà Nẵng">Đà Nẵng</option>
                            <option value="Hải Phòng">Hải Phòng</option>
                            <option value="Hà Nội">Hà Nội</option>
                            <option value="TP. Hồ Chí Minh">TP. Hồ Chí Minh</option>
                            <option value="Cần Thơ">Cần Thơ</option>
                        </select>
                        <div class="invalid-feedback">Please select a province.</div>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="district">District</label>
                        <input type="text" class="form-control" id="district" name="district" required>
                        <div class="invalid-feedback">Please enter your district.</div>
                    </div>
                </div>

                <div class="mb-3">
                    <label for="address">Address</label>
                    <textarea class="form-control" id="address" name="address" rows="3" required></textarea>
                    <div class="invalid-feedback">Please enter your address.</div>
                </div>

                <!-- Bảng giỏ hàng -->
                <table class="table table-bordered">
                    <thead>
                        <tr>
                            <th>Product</th>
                            <th>Size</th>
                            <th>Price</th>
                            <th>Quantity</th>
                            <th>Total</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (CartItem item : cart) {%>
                        <tr>
                            <td><%= item.getProduct_Name()%></td>
                            <td>
                                <select class="form-control" onchange="updateSize('<%= item.getProduct_Id()%>', this.value, <%= item.getQuantity()%>)">
                                    <%
                                        List<String> sizes = productDAO.getSizesByProductId(item.getProduct_Id());
                                        for (String size : sizes) {
                                    %>
                                    <option value="<%= size.trim()%>" <%= size.trim().equals(item.getSize()) ? "selected" : ""%>><%= size.trim()%></option>
                                    <% }%>
                                </select>
                            </td>
                            <td><%= item.getPrice()%></td>
                            <td>
                                <input type="number" class="form-control" value="<%= item.getQuantity()%>" min="1" onchange="updateQuantity('<%= item.getProduct_Id()%>', '<%= item.getSize()%>', this.value)">
                            </td>
                            <td><%= String.format("%.2f", item.getTotalPrice())%></td>
                            <td>
                                <button type="button" class="btn btn-danger" 
                                        onclick="removeFromCart('<%= item.getProduct_Id()%>', '<%= item.getSize()%>')">
                                    Remove
                                </button>
                            </td>

                        </tr>
                        <% }%>
                        <tr>
                            <td colspan="4" class="text-right font-weight-bold">Shipping Cost</td>
                            <td id="shippingCost"><%= String.format("%.2f", shippingCost)%></td>
                            <td></td>
                        </tr>
                        <tr>
                            <td colspan="4" class="text-right font-weight-bold">Grand Total</td>
                            <td id="grandTotal"><%= String.format("%.2f", totalCost)%></td>
                            <td></td>
                        </tr>
                    </tbody>
                </table>

                <!-- Nút Continue Shopping và Checkout -->
                <div class="d-flex justify-content-between">
                    <a href="all.jsp" class="btn btn-primary ml-2">Continue Shopping</a>
                    <button type="submit" class="btn btn-success">Checkout</button>
                </div>
            </form>

            <input type="hidden" id="subtotal" value="<%= subtotal%>">
        </div>

        <!-- Bootstrap Validation Script -->
        <script>
            (function () {
                'use strict';
                window.addEventListener('load', function () {
                    var forms = document.getElementsByClassName('needs-validation');
                    Array.prototype.filter.call(forms, function (form) {
                        form.addEventListener('submit', function (event) {
                            if (form.checkValidity() === false) {
                                event.preventDefault();
                                event.stopPropagation();
                            }
                            form.classList.add('was-validated');
                        }, false);
                    });
                }, false);
            })();

            function removeFromCart(productId) {
                if (confirm("Are you sure you want to remove this product from your cart?")) {
                    fetch('RemoveFromCartServlet', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        },
                        body: 'productId=' + encodeURIComponent(productId)
                    })
                            .then(response => {
                                if (response.ok) {
                                    location.reload();  // reload the page to refresh cart after deletion
                                } else {
                                    alert('Failed to remove the product.');
                                }
                            })
                            .catch(error => {
                                console.error('Error:', error);
                                alert('An error occurred while removing the product.');
                            });
                }
            }

        </script>
    </body>
</html>