package dao;

import connect.DBContext;
import model.CartItem;
import model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Order;
import model.OrderItem;

public class OrderDAO {

    private final DBContext dbContext = new DBContext();

    public boolean saveOrder(User user, List<CartItem> cart, double totalPrice) {
        String orderQuery = "INSERT INTO Orders (User_ID, Total_Price, Status) VALUES (?, ?, 'pending')";
        String itemQuery = "INSERT INTO Order_Items (Order_ID, Product_ID, Quantity, Unit_Price, Size) VALUES (?, ?, ?, ?, ?)";

        try ( Connection conn = dbContext.getConnection()) {
            conn.setAutoCommit(false); // Bắt đầu giao dịch

            try ( PreparedStatement orderStmt = conn.prepareStatement(orderQuery, Statement.RETURN_GENERATED_KEYS)) {
                orderStmt.setInt(1, user.getUserId());
                orderStmt.setDouble(2, totalPrice);
                int affectedRows = orderStmt.executeUpdate();

                if (affectedRows == 0) {
                    conn.rollback();
                    return false;
                }

                ResultSet generatedKeys = orderStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int orderId = generatedKeys.getInt(1);

                    try ( PreparedStatement itemStmt = conn.prepareStatement(itemQuery)) {
                        for (CartItem item : cart) {
                            itemStmt.setInt(1, orderId);
                            itemStmt.setInt(2, item.getProduct_Id());
                            itemStmt.setInt(3, item.getQuantity());
                            itemStmt.setDouble(4, item.getPrice());
                            itemStmt.setString(5, item.getSize());
                            itemStmt.addBatch();
                        }
                        itemStmt.executeBatch();
                    }

                    conn.commit(); // Kết thúc giao dịch
                    return true;
                }
            } catch (SQLException e) {
                conn.rollback(); // Quay lại nếu có lỗi
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteOrder(int orderId, int userId) {
        String sqlDeleteOrderItems = "DELETE FROM Order_Items WHERE Order_ID = ?";
        String sqlDeleteOrders = "DELETE FROM Orders WHERE Order_ID = ? AND User_ID = ?";
        Connection conn = null; // Khai báo biến conn bên ngoài try-with-resources

        try {
            conn = new DBContext().getConnection();
            conn.setAutoCommit(false); // Bắt đầu transaction

            // Xóa Order_Items trước
            try ( PreparedStatement stmt1 = conn.prepareStatement(sqlDeleteOrderItems)) {
                stmt1.setInt(1, orderId);
                stmt1.executeUpdate();
            }

            // Sau đó xóa Orders
            try ( PreparedStatement stmt2 = conn.prepareStatement(sqlDeleteOrders)) {
                stmt2.setInt(1, orderId);
                stmt2.setInt(2, userId);
                int rowsDeleted = stmt2.executeUpdate();
                System.out.println("Rows deleted from Orders: " + rowsDeleted); // Debug

                if (rowsDeleted > 0) {
                    conn.commit(); // Nếu xóa thành công, commit transaction
                    return true;
                } else {
                    conn.rollback(); // Nếu xóa thất bại, rollback
                    return false;
                }
            }
        } catch (SQLException e) {
            System.err.println("[SQL Error] " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM Orders";

        try ( Connection conn = dbContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(query);  ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Order order = new Order();
                order.setOrder_ID(rs.getInt("Order_ID"));
                order.setUser_ID(rs.getInt("User_ID"));
                order.setOrder_Date(rs.getTimestamp("Order_Date"));
                order.setTotal_Price(rs.getDouble("Total_Price"));
                order.setStatus(rs.getString("Status"));

                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }

// Cập nhật method updateOrderStatus trong OrderDAO
    public boolean updateOrderStatus(int orderId, String status) {
        String sql = "UPDATE Notifications SET Status = ? WHERE Order_ID = ?;";
        try ( Connection conn = dbContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, orderId);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                return true;
            } else {
                System.err.println("No rows updated. Check if Order_ID exists: " + orderId);
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Database error occurred while updating order status: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<OrderItem> getOrderItemsByOrderId(int orderId) {
        List<OrderItem> orderItems = new ArrayList<>();
        String query = "SELECT * FROM Order_Items WHERE Order_ID = ?";

        try ( Connection conn = dbContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, orderId);
            try ( ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    OrderItem item = new OrderItem(
                            rs.getInt("Order_Item_ID"),
                            rs.getInt("Order_ID"),
                            rs.getInt("Product_ID"),
                            rs.getInt("Quantity"),
                            rs.getDouble("Unit_Price"),
                            rs.getString("Size")
                    );
                    orderItems.add(item);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderItems;
    }

    public List<Order> getPendingOrdersByUserId(int userId) {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM Orders WHERE User_ID = ? AND Status = 'pending' ORDER BY Order_Date DESC";

        try ( Connection conn = dbContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);

            try ( ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Order order = new Order(
                            rs.getInt("Order_ID"),
                            rs.getInt("User_ID"),
                            rs.getTimestamp("Order_Date"),
                            rs.getDouble("Total_Price"),
                            rs.getString("Status")
                    );
                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public Order getOrderById(int orderId) {
        Order order = null;
        String orderQuery = "SELECT * FROM Orders WHERE Order_ID = ?";
        String itemQuery = "SELECT oi.*, p.Product_Name "
                + "FROM Order_Items oi "
                + "JOIN Products p ON oi.Product_ID = p.Product_ID "
                + "WHERE oi.Order_ID = ?";

        try ( Connection conn = dbContext.getConnection();  PreparedStatement orderStmt = conn.prepareStatement(orderQuery)) {

            orderStmt.setInt(1, orderId);
            try ( ResultSet orderRs = orderStmt.executeQuery()) {
                if (orderRs.next()) {
                    order = new Order(
                            orderRs.getInt("Order_ID"),
                            orderRs.getInt("User_ID"),
                            orderRs.getTimestamp("Order_Date"),
                            orderRs.getDouble("Total_Price"),
                            orderRs.getString("Status")
                    );

                    List<OrderItem> items = new ArrayList<>();
                    try ( PreparedStatement itemStmt = conn.prepareStatement(itemQuery)) {
                        itemStmt.setInt(1, orderId);
                        try ( ResultSet itemRs = itemStmt.executeQuery()) {
                            while (itemRs.next()) {
                                OrderItem item = new OrderItem(
                                        itemRs.getInt("Order_Item_ID"),
                                        itemRs.getInt("Order_ID"),
                                        itemRs.getInt("Product_ID"),
                                        itemRs.getInt("Quantity"),
                                        itemRs.getDouble("Unit_Price"),
                                        itemRs.getString("Size"),
                                        itemRs.getString("Product_Name") // Lấy tên sản phẩm từ bảng Products
                                );
                                items.add(item);
                            }
                            order.setItems(items);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return order;
    }

    public List<Order> getCompletedOrdersByUserId(int userId) {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM Orders WHERE User_ID = ? AND Status = 'completed'";

        try ( Connection conn = dbContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            try ( ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Order order = new Order(
                            rs.getInt("Order_ID"),
                            rs.getInt("User_ID"),
                            rs.getTimestamp("Order_Date"),
                            rs.getDouble("Total_Price"),
                            rs.getString("Status")
                    );
                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public List<Order> getCompletedOrdersWithDetailsByUserId(int userId) {
        List<Order> orders = new ArrayList<>();
        String orderQuery = "SELECT * FROM Orders WHERE User_ID = ? AND Status = 'completed'";
        String itemQuery = "SELECT * FROM Order_Items WHERE Order_ID = ?";

        try ( Connection conn = dbContext.getConnection();  PreparedStatement orderStmt = conn.prepareStatement(orderQuery)) {

            orderStmt.setInt(1, userId);
            try ( ResultSet orderRs = orderStmt.executeQuery()) {
                while (orderRs.next()) {
                    Order order = new Order(
                            orderRs.getInt("Order_ID"),
                            orderRs.getInt("User_ID"),
                            orderRs.getTimestamp("Order_Date"),
                            orderRs.getDouble("Total_Price"),
                            orderRs.getString("Status")
                    );

                    // Lấy chi tiết sản phẩm trong đơn hàng
                    try ( PreparedStatement itemStmt = conn.prepareStatement(itemQuery)) {
                        itemStmt.setInt(1, order.getOrder_ID());
                        try ( ResultSet itemRs = itemStmt.executeQuery()) {
                            List<OrderItem> items = new ArrayList<>();
                            while (itemRs.next()) {
                                OrderItem item = new OrderItem(
                                        itemRs.getInt("Order_Item_ID"),
                                        itemRs.getInt("Order_ID"),
                                        itemRs.getInt("Product_ID"),
                                        itemRs.getInt("Quantity"),
                                        itemRs.getDouble("Unit_Price"),
                                        itemRs.getString("Size")
                                );
                                items.add(item);
                            }
                            order.setItems(items); // Gán danh sách sản phẩm vào đơn hàng
                        }
                    }

                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public List<Order> getOrdersByUserId(int userId) {
        List<Order> orders = new ArrayList<>();
        String orderQuery = "SELECT * FROM Orders WHERE User_ID = ? ORDER BY Order_Date DESC";
        String itemQuery = "SELECT oi.*, p.Product_Name "
                + "FROM Order_Items oi "
                + "JOIN Products p ON oi.Product_ID = p.Product_ID "
                + "WHERE oi.Order_ID = ?";

        try ( Connection conn = dbContext.getConnection();  PreparedStatement orderStmt = conn.prepareStatement(orderQuery)) {
            orderStmt.setInt(1, userId);
            try ( ResultSet orderRs = orderStmt.executeQuery()) {
                while (orderRs.next()) {
                    Order order = new Order(
                            orderRs.getInt("Order_ID"),
                            orderRs.getInt("User_ID"),
                            orderRs.getTimestamp("Order_Date"),
                            orderRs.getDouble("Total_Price"),
                            orderRs.getString("Status")
                    );

                    try ( PreparedStatement itemStmt = conn.prepareStatement(itemQuery)) {
                        itemStmt.setInt(1, order.getOrder_ID());
                        try ( ResultSet itemRs = itemStmt.executeQuery()) {
                            List<OrderItem> items = new ArrayList<>();
                            while (itemRs.next()) {
                                OrderItem item = new OrderItem(
                                        itemRs.getInt("Order_Item_ID"),
                                        itemRs.getInt("Order_ID"),
                                        itemRs.getInt("Product_ID"),
                                        itemRs.getInt("Quantity"),
                                        itemRs.getDouble("Unit_Price"),
                                        itemRs.getString("Size"),
                                        itemRs.getString("Product_Name") // Lấy tên sản phẩm
                                );
                                items.add(item);
                            }
                            order.setItems(items); // Gán danh sách sản phẩm vào đơn hàng
                        }
                    }
                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }
    // Trong lớp OrderDAO

    public int getLastOrderId(int userId) {
        String query = "SELECT TOP 1 Order_ID FROM Orders WHERE User_ID = ? ORDER BY Order_ID DESC";
        try ( Connection conn = dbContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(query)) {

            // Thiết lập tham số User_ID
            stmt.setInt(1, userId);

            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("Order_ID"); // Trả về Order_ID cuối cùng
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Nên thay bằng logger
        }
        return -1; // Trả về -1 nếu không tìm thấy
    }

    public int getUserIdByOrderId(int orderId) {
        String sql = "SELECT User_ID FROM Orders WHERE Order_ID = ?";
        try ( Connection conn = new DBContext().getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);

            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("User_ID"); // Trả về User_ID nếu tìm thấy
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Trả về -1 nếu không tìm thấy hoặc có lỗi
    }

    public List<Order> getAllOrdersWithDetails() {
        List<Order> orders = new ArrayList<>();
        String orderQuery = "SELECT * FROM Orders ORDER BY Order_Date DESC";
        String itemQuery = "SELECT oi.*, p.Product_Name "
                + "FROM Order_Items oi "
                + "JOIN Products p ON oi.Product_ID = p.Product_ID "
                + "WHERE oi.Order_ID = ?";

        try ( Connection conn = dbContext.getConnection();  PreparedStatement orderStmt = conn.prepareStatement(orderQuery)) {

            try ( ResultSet orderRs = orderStmt.executeQuery()) {
                while (orderRs.next()) {
                    Order order = new Order(
                            orderRs.getInt("Order_ID"),
                            orderRs.getInt("User_ID"),
                            orderRs.getTimestamp("Order_Date"),
                            orderRs.getDouble("Total_Price"),
                            orderRs.getString("Status")
                    );

                    // Lấy các sản phẩm của đơn hàng hiện tại
                    try ( PreparedStatement itemStmt = conn.prepareStatement(itemQuery)) {
                        itemStmt.setInt(1, order.getOrder_ID());
                        try ( ResultSet itemRs = itemStmt.executeQuery()) {
                            List<OrderItem> items = new ArrayList<>();
                            while (itemRs.next()) {
                                OrderItem item = new OrderItem(
                                        itemRs.getInt("Order_Item_ID"),
                                        itemRs.getInt("Order_ID"),
                                        itemRs.getInt("Product_ID"),
                                        itemRs.getInt("Quantity"),
                                        itemRs.getDouble("Unit_Price"),
                                        itemRs.getString("Size"),
                                        itemRs.getString("Product_Name") // Lấy tên sản phẩm
                                );
                                items.add(item);
                            }
                            order.setItems(items); // Gán danh sách sản phẩm vào đơn hàng
                        }
                    }
                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

// method lấy user từ Notification_ID
    public User getUserByNotificationId(int notificationId) {
        String query = "SELECT u.* FROM Users u "
                + "JOIN Notifications n ON u.User_ID = n.User_ID "
                + "WHERE n.Notification_ID = ?";
        try ( Connection conn = dbContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, notificationId);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("User_ID"),
                            rs.getString("Username"), // <-- đảm bảo đúng tên cột ở đây
                            rs.getString("Password"),
                            rs.getString("Email"),
                            rs.getString("Phone"),
                            rs.getString("Address"),
                            rs.getString("DOB"),
                            rs.getString("Role")
                    );
                } else {
                    System.out.println("No user found for notificationId: " + notificationId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
