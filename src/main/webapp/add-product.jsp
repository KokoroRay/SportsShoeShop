<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Add Product</title>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    </head>
    <body>
        <div class="container mt-5">
            <h2>Add New Product</h2><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

            <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

            <!-- Hiển thị thông báo lỗi -->
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <strong>Lỗi!</strong> ${errorMessage}
                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
            </c:if>

            <!-- Form thêm sản phẩm -->
            <form action="AddProductServlet" method="POST" enctype="multipart/form-data">
                <!-- ... (các trường input) -->
                <div class="form-group">
                    <label for="productName">Product Name:</label>
                    <input type="text" class="form-control" name="productName" required />
                </div>

                <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

                <!-- Phần chọn hãng -->
                <div class="form-group">
                    <label>Brand:</label>
                    <select name="brand" class="form-control" required>
                        <option value="">-- Select Brand --</option>
                        <c:forEach items="${brands}" var="brand">
                            <option value="${brand}">${brand}</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group">
                    <label for="price">Price:</label>
                    <input type="number" step="0.01" class="form-control" name="price" required />
                </div>

                <div class="form-group">
                    <label for="quantity">Quantity:</label>
                    <input type="number" class="form-control" name="quantity" required />
                </div>

                <div class="form-group">
                    <label for="size">Size:</label>
                    <input type="text" class="form-control" name="size" />
                </div>

                <div class="form-group">
                    <label for="description">Description:</label>
                    <textarea name="description" class="form-control" rows="4"></textarea>
                </div>

                <div class="form-group">
                    <label for="image">Upload Image:</label>
                    <input type="file" class="form-control-file" name="image" accept="image/*" />
                </div>

                <div class="form-group">
                    <label for="rate">Rate:</label>
                    <input type="number" step="0.01" class="form-control" name="rate" />
                </div>

                <!-- New dropdown for selecting product type -->
                <div class="form-group">
                    <label for="type">Type:</label>
                    <select name="type" class="form-control" required>
                        <option value="TF">TF</option>
                        <option value="IC">IC</option>
                    </select>
                </div>

                <button type="submit" class="btn btn-primary">Add Product</button>
            </form>

            <p class="mt-3"><a href="manage-products.jsp" class="btn btn-secondary">Back to Product Management</a></p>
        </div>

        <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.6.0/dist/umd/popper.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    </body>
</html>
