package dao;

import connect.DBContext;
import model.Notification;
import model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO {

    private final DBContext dbContext = new DBContext();

    public boolean createNotification(int orderId, int userId, String message, String status,
            String fullName, String phoneNumber, String address,
            String paymentMethod, String orderDetails, double totalPrice) {
        String query = "INSERT INTO Notifications (Order_ID, User_ID, Message, Status, FullName, PhoneNumber, Address, PaymentMethod, OrderDetails, TotalPrice) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try ( Connection conn = dbContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, orderId);
            stmt.setInt(2, userId);
            stmt.setString(3, message);
            stmt.setString(4, status);
            stmt.setString(5, fullName);
            stmt.setString(6, phoneNumber);
            stmt.setString(7, address);
            stmt.setString(8, paymentMethod);
            stmt.setString(9, orderDetails);
            stmt.setDouble(10, totalPrice);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateNotificationDetails(int orderId, String fullName, String phoneNumber, String address, String paymentMethod) {
        String query = "UPDATE Notifications SET FullName = ?, PhoneNumber = ?, Address = ?, PaymentMethod = ? WHERE Order_ID = ? AND Status = 'pending'";
        try ( Connection conn = dbContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, fullName);
            stmt.setString(2, phoneNumber);
            stmt.setString(3, address);
            stmt.setString(4, paymentMethod);
            stmt.setInt(5, orderId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateNotificationStatus(int notificationId, String status, int adminId) {
        String query = "UPDATE Notifications SET Status = ?, Admin_ID = ? WHERE Notification_ID = ?";
        try ( Connection conn = dbContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, status);
            stmt.setInt(2, adminId);
            stmt.setInt(3, notificationId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Notification getNotificationByOrderId(int orderId) {
        String query = "SELECT * FROM Notifications WHERE Order_ID = ?";
        try ( Connection conn = dbContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, orderId);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Notification(
                            rs.getInt("Notification_ID"),
                            rs.getInt("Order_ID"),
                            rs.getInt("User_ID"),
                            rs.getObject("Admin_ID", Integer.class),
                            rs.getString("Message"),
                            rs.getString("Status"),
                            rs.getTimestamp("Created_At"),
                            rs.getString("FullName"),
                            rs.getString("PhoneNumber"),
                            rs.getString("Address"),
                            rs.getString("PaymentMethod") != null ? rs.getString("PaymentMethod") : "Cash", // Fix lỗi null
                            rs.getString("OrderDetails"),
                            rs.getDouble("TotalPrice")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.err.println("Notification not found for Order ID: " + orderId);
        return null;
    }

    public Notification getNotificationById(int notificationId) {
        String query = "SELECT * FROM Notifications WHERE Notification_ID = ?";
        try ( Connection conn = dbContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, notificationId);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Notification(
                            rs.getInt("Notification_ID"),
                            rs.getInt("Order_ID"),
                            rs.getInt("User_ID"),
                            rs.getObject("Admin_ID", Integer.class),
                            rs.getString("Message"),
                            rs.getString("Status"),
                            rs.getTimestamp("Created_At"),
                            rs.getString("FullName"),
                            rs.getString("PhoneNumber"),
                            rs.getString("Address"),
                            rs.getString("PaymentMethod"),
                            rs.getString("OrderDetails"),
                            rs.getDouble("TotalPrice")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User getUserByNotificationId(int notificationId) {
        String query = "SELECT u.User_ID, u.User_Name, u.Password, u.Email, u.Phone, u.Address, u.DOB, u.Role "
                + "FROM Users u JOIN Notifications n ON u.User_ID = n.User_ID "
                + "WHERE n.Notification_ID = ?";
        try ( Connection conn = dbContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, notificationId);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("User_ID"),
                            rs.getString("User_Name"),
                            rs.getString("Password"),
                            rs.getString("Email"),
                            rs.getString("Phone"),
                            rs.getString("Address"),
                            rs.getString("DOB"),
                            rs.getString("Role")
                    );
                } else {
                    System.out.println("No matching user for notificationId: " + notificationId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Notification> getNotificationsForAdmin() {
        List<Notification> notifications = new ArrayList<>();
        String query = "SELECT * FROM Notifications WHERE Status = 'pending' ORDER BY Created_At DESC";
        try ( Connection conn = dbContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(query);  ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                notifications.add(new Notification(
                        rs.getInt("Notification_ID"),
                        rs.getInt("Order_ID"),
                        rs.getInt("User_ID"),
                        rs.getObject("Admin_ID", Integer.class),
                        rs.getString("Message"),
                        rs.getString("Status"),
                        rs.getTimestamp("Created_At"),
                        rs.getString("FullName"),
                        rs.getString("PhoneNumber"),
                        rs.getString("Address"),
                        rs.getString("PaymentMethod"),
                        rs.getString("OrderDetails"),
                        rs.getDouble("TotalPrice")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notifications;
    }

    public List<Notification> getNotificationsByUserId(int userId) {
        List<Notification> notifications = new ArrayList<>();
        String query = "SELECT * FROM Notifications WHERE User_ID = ? ORDER BY Created_At DESC";
        try ( Connection conn = dbContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            try ( ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    notifications.add(new Notification(
                            rs.getInt("Notification_ID"),
                            rs.getInt("Order_ID"),
                            rs.getInt("User_ID"),
                            rs.getObject("Admin_ID", Integer.class),
                            rs.getString("Message"),
                            rs.getString("Status"),
                            rs.getTimestamp("Created_At"),
                            rs.getString("FullName"),
                            rs.getString("PhoneNumber"),
                            rs.getString("Address"),
                            rs.getString("PaymentMethod"),
                            rs.getString("OrderDetails"),
                            rs.getDouble("TotalPrice")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notifications;
    }

}
