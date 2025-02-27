<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.CartItem"%>
<%@page import="java.util.List"%>

<%
    List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
    if (cart == null) {
        cart = new ArrayList<>();
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Your Cart</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <script>
        function updateTotal(productIndex) {
            let price = parseFloat(document.getElementById("price_" + productIndex).innerText);
            let quantity = parseInt(document.getElementById("quantity_" + productIndex).value);
            let total = price * quantity;
            document.getElementById("total_" + productIndex).innerText = total.toFixed(2);
            updateGrandTotal();
        }

        function updateGrandTotal() {
            let totalCost = 0;
            let totals = document.getElementsByClassName("product-total");
            for (let i = 0; i < totals.length; i++) {
                totalCost += parseFloat(totals[i].innerText);
            }
            document.getElementById("grandTotal").innerText = totalCost.toFixed(2);
        }
    </script>
</head>
<body>
    <div class="container mt-5">
        <h2 class="text-center mb-4">Your Cart</h2>

        <table class="table table-bordered">
            <thead class="thead-light">
                <tr>
                    <th>Product Name</th>
                    <th>Size</th>
                    <th>Price</th>
                    <th>Quantity</th>
                    <th>Total</th>
                </tr>
            </thead>
            <tbody>
                <% double totalCost = 0; %>
                <% for (int i = 0; i < cart.size(); i++) {
                    CartItem item = cart.get(i);
                    double itemTotalPrice = item.getTotalPrice();
                    totalCost += itemTotalPrice;
                %>
                    <tr>
                        <td><%= item.getProduct_Name() %></td>
                        <td>
                            <select name="size_<%= i %>" class="form-control">
                                <option value="39" <%= item.getSize().equals("39") ? "selected" : "" %>>39</option>
                                <option value="40" <%= item.getSize().equals("40") ? "selected" : "" %>>40</option>
                                <option value="41" <%= item.getSize().equals("41") ? "selected" : "" %>>41</option>
                            </select>
                        </td>
                        <td id="price_<%= i %>"><%= item.getPrice() %></td>
                        <td>
                            <input type="number" id="quantity_<%= i %>" value="<%= item.getQuantity() %>" min="1" class="form-control" onchange="updateTotal(<%= i %>)">
                        </td>
                        <td class="product-total" id="total_<%= i %>"><%= String.format("%.2f", itemTotalPrice) %></td>
                    </tr>
                <% } %>
                <tr>
                    <td colspan="4" class="text-right font-weight-bold">Grand Total</td>
                    <td id="grandTotal"><%= String.format("%.2f", totalCost) %></td>
                </tr>
            </tbody>
        </table>

        <div class="text-center mt-3">
            <form action="CheckoutServlet" method="post" style="display:inline;">
                <button type="submit" class="btn btn-success">Checkout</button>
            </form>
            <a href="all.jsp" class="btn btn-primary ml-2">Continue Shopping</a>
        </div>
    </div>

    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.6.0/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
