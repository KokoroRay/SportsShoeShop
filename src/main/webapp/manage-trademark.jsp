<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="model.User, model.Trademark, dao.TrademarkDAO, java.util.List" %>
<%
    // Kiểm tra quyền admin
    User loggedInUser = (User) session.getAttribute("user");
    if (loggedInUser == null || !"admin".equals(loggedInUser.getRole())) {
        response.sendRedirect("login.jsp");
        return;
    }

    // Xử lý phân trang
    TrademarkDAO trademarkDAO = new TrademarkDAO();
    int pageSize = 10;
    int currentPage = 1;

    if (request.getParameter("page") != null) {
        try {
            currentPage = Integer.parseInt(request.getParameter("page"));
        } catch (NumberFormatException e) {
            currentPage = 1;
        }
    }

    int offset = (currentPage - 1) * pageSize;
    int totalTrademarks = trademarkDAO.getTrademarksCount();
    List<Trademark> trademarkList = trademarkDAO.getAllTrademarks(offset, pageSize);
    int totalPages = (int) Math.ceil((double) totalTrademarks / pageSize);
%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Manage Trademarks</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <!-- Bootstrap CSS -->
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
        <!-- Font Awesome for icons -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.1/css/all.min.css">

        <!-- Custom CSS cho giao diện chuyên nghiệp -->
        <style>
            body {
                font-family: "Segoe UI", sans-serif;
                background-color: #f4f6f9;
                margin: 0;
                padding: 0;
            }
            /* Top Navbar */
            .navbar {
                background-color: #0d6efd;
                box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            }
            .navbar-brand {
                color: #fff !important;
                font-weight: bold;
                margin-left: 1rem;
            }
            .navbar-nav .nav-link {
                color: #fff !important;
            }
            /* Sidebar */
            .sidebar {
                position: fixed;
                top: 56px;
                left: 0;
                bottom: 0;
                width: 220px;
                background-color: #343a40;
                padding-top: 20px;
                overflow-y: auto;
            }
            .sidebar .sidebar-header {
                text-align: center;
                font-size: 1.2rem;
                color: #fff;
                margin-bottom: 1rem;
            }
            .sidebar a {
                display: block;
                padding: 10px 20px;
                color: #adb5bd;
                text-decoration: none;
                transition: background 0.3s;
            }
            .sidebar a:hover,
            .sidebar a.active {
                background-color: #0d6efd;
                color: #fff;
            }
            /* Main Content */
            .main-content {
                margin-left: 220px;
                margin-top: 56px;
                padding: 20px;
            }
            .table-container {
                background: #fff;
                padding: 20px;
                border-radius: 8px;
                box-shadow: 0 2px 4px rgba(0,0,0,0.1);
                margin-bottom: 20px;
            }
            /* Phân trang */
            .pagination {
                justify-content: center;
            }
        </style>
    </head>
    <body>
        <!-- Top Navbar -->
        <nav class="navbar navbar-expand-lg navbar-dark">
            <a class="navbar-brand" href="#"><i class="fas fa-rocket"></i> Group 7</a>
            <div class="collapse navbar-collapse" id="topNav">
                <ul class="navbar-nav ml-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="profile.jsp"><i class="fas fa-user-circle"></i> <%= loggedInUser.getUserName()%></a>
                    </li>
                </ul>
            </div>
        </nav>

        <!-- Sidebar -->
        <div class="sidebar">
            <div class="sidebar-header">
                <i class="fas fa-user-shield"></i> Admin
            </div>
            <a href="dashboard.jsp"><i class="fas fa-tachometer-alt mr-2"></i> Dashboard</a>
            <a href="manage-orders.jsp"><i class="fas fa-shopping-cart mr-2"></i> Manage Orders</a>
            <a href="manage-products.jsp"><i class="fas fa-box-open mr-2"></i> Manage Products</a>
            <a href="manage-users.jsp"><i class="fas fa-users mr-2"></i> Manage Users</a>
            <a href="manage-discount.jsp"><i class="fas fa-tag mr-2"></i> Manage Discounts</a>
            <a href="manage-trademark.jsp" class="active"><i class="fas fa-copyright mr-2"></i> Manage Trademark</a>
        </div>

        <%-- Hiển thị thông báo --%>
        <%
            String status = request.getParameter("status");
            String message = "";
            String alertType = "danger";

            if ("success".equals(status)) {
                message = "Thao tác thành công!";
                alertType = "success";
            } else if ("error".equals(status)) {
                message = "Có lỗi xảy ra!";
            } else if ("delete_success".equals(status)) {
                message = "Xóa thành công!";
                alertType = "success";
            }
        %>

        <% if (status != null) {%>
        <div class="alert alert-<%= alertType%> alert-dismissible fade show">
            <%= message%>
            <button type="button" class="close" data-dismiss="alert">&times;</button>
        </div>
        <% } %>
        <%-- Hiển thị thông báo không được xóa --%>
        <% if ("cannot_delete_default".equals(request.getParameter("error"))) { %>
        <div class="alert alert-danger alert-dismissible fade show">
            Không thể xóa nhãn hàng mặc định!
            <button type="button" class="close" data-dismiss="alert">&times;</button>
        </div>
        <% } %>
        <!-- Main Content -->
        <div class="main-content">
            <h3 class="text-center text-primary mb-4">Manage Trademarks</h3>
            <div class="text-right mb-3">
                <button class="btn btn-success" data-toggle="modal" data-target="#addModal">
                    <i class="fas fa-plus-circle"></i> Add New Trademark
                </button>
                <a href="dashboard.jsp" class="btn btn-secondary ml-2">Back to Dashboard</a>
            </div>

            <div class="table-container">
                <% if (trademarkList != null && !trademarkList.isEmpty()) { %>
                <table class="table table-bordered table-hover">
                    <thead class="thead-dark">
                        <tr>
                            <th>ID</th>
                            <th>Trademark Name</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Trademark t : trademarkList) {%>
                        <tr>
                            <td><%= t.getTrademarkId()%></td>
                            <td><%= t.getTrademarkName()%></td>
                            <td>
                                <button class="btn btn-primary btn-sm edit-btn" 
                                        data-id="<%= t.getTrademarkId()%>"
                                        data-name="<%= t.getTrademarkName()%>">
                                    <i class="fas fa-edit"></i> Edit
                                </button>

                                <%-- Chỉ hiện nút xóa nếu ID không nằm trong 1-6 --%>
                                <% if (t.getTrademarkId() > 6) {%>
                                <a href="DeleteTrademarkServlet?id=<%= t.getTrademarkId()%>" 
                                   class="btn btn-danger btn-sm"
                                   onclick="return confirm('Are you sure to delete this trademark?')">
                                    <i class="fas fa-trash-alt"></i> Delete
                                </a>
                                <% } %>
                            </td>
                        </tr>
                        <% } %>
                    </tbody>
                </table>
                <% } else { %>
                <div class="alert alert-info">No trademarks found.</div>
                <% } %>
            </div>

            <!-- Thanh phân trang -->
            <nav aria-label="Page navigation">
                <ul class="pagination">
                    <% for (int i = 1; i <= totalPages; i++) {%>
                    <li class="page-item <%= (i == currentPage ? "active" : "")%>">
                        <a class="page-link" href="manage-trademark.jsp?page=<%= i%>"><%= i%></a>
                    </li>
                    <% }%>
                </ul>
            </nav>

            <!-- Add Trademark Modal -->
            <div class="modal fade" id="addModal" tabindex="-1">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <form action="${pageContext.request.contextPath}/TrademarkServlet" method="POST">
                            <input type="hidden" name="action" value="add">
                            <div class="modal-header">
                                <h5 class="modal-title">Add New Trademark</h5>
                                <button type="button" class="close" data-dismiss="modal">&times;</button>
                            </div>
                            <div class="modal-body">
                                <div class="form-group">
                                    <label>Trademark Name:</label>
                                    <input type="text" class="form-control" name="trademarkName" required>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <button type="submit" class="btn btn-primary">Add</button>
                                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>

            <!-- Edit Trademark Modal -->
            <div class="modal fade" id="editModal" tabindex="-1">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <form action="${pageContext.request.contextPath}/TrademarkServlet" method="POST">
                            <input type="hidden" name="action" value="update">
                            <input type="hidden" id="editId" name="trademarkId">
                            <div class="modal-header">
                                <h5 class="modal-title">Edit Trademark</h5>
                                <button type="button" class="close" data-dismiss="modal">&times;</button>
                            </div>
                            <div class="modal-body">
                                <div class="form-group">
                                    <label>Trademark Name:</label>
                                    <input type="text" class="form-control" id="editName" name="trademarkName" required>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <button type="submit" class="btn btn-primary">Save Changes</button>
                                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>

        </div>

        <!-- Bootstrap JS, Popper.js -->
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
        <script>
                                       $(document).ready(function () {
                                           $('.edit-btn').click(function () {
                                               const id = $(this).data('id');
                                               const name = $(this).data('name');
                                               $('#editId').val(id);
                                               $('#editName').val(name);
                                               $('#editModal').modal('show');
                                           });
                                       });
        </script>
    </body>
</html>