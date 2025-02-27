package dao;

import connect.DBContext;
import model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    private final DBContext dbContext = new DBContext();

    public User getUserByEmail(String email) {
    String query = "SELECT * FROM Users WHERE Email = ?";
    try (Connection connection = dbContext.getConnection();
         PreparedStatement statement = connection.prepareStatement(query)) {

        statement.setString(1, email);
        ResultSet resultSet = statement.executeQuery();
        
        if (resultSet.next()) {
            User user = new User();
            user.setUserId(resultSet.getInt("User_ID"));
            user.setUserName(resultSet.getString("User_Name"));
            user.setEmail(email);
            // Set other user fields as necessary
            return user;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}


    public User getUserByUsername(String username) throws SQLException {
        String query = "SELECT * FROM Users WHERE User_Name = ?";

        try ( Connection conn = dbContext.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        }
        return null;
    }

    // Lấy thông tin người dùng theo ID
    public User getUserById(int userId) throws SQLException {
        String query = "SELECT * FROM Users WHERE User_ID = ?";

        try ( Connection conn = dbContext.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        }
        return null;
    }

    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM Users";

        try ( Connection conn = dbContext.getConnection();  PreparedStatement ps = conn.prepareStatement(query);  ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("User_ID"));
                user.setUserName(rs.getString("User_Name"));
                user.setEmail(rs.getString("Email"));
                user.setPhone(rs.getString("Phone"));
                user.setAddress(rs.getString("Address"));
                user.setRole(rs.getString("Role"));
                users.add(user);
            }
        }
        return users;
    }

    // Phương thức tiện ích để ánh xạ từ ResultSet sang đối tượng User
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("User_ID"));
        user.setUserName(rs.getString("User_Name"));
        user.setPassword(rs.getString("Password"));
        user.setEmail(rs.getString("Email"));
        user.setPhone(rs.getString("Phone"));
        user.setAddress(rs.getString("Address"));
        user.setDob(rs.getString("DOB"));
        user.setRole(rs.getString("Role"));
        return user;
    }

    public boolean updatePassword(String email, String hashedPassword) throws SQLException {
        String query = "UPDATE Users SET Password = ? WHERE Email = ?";

        try ( Connection conn = dbContext.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, hashedPassword);
            ps.setString(2, email);

            return ps.executeUpdate() > 0;
        }
    }
// Thêm người dùng mới vào database

    public boolean insertUser(User user) throws SQLException {
        String query = "INSERT INTO Users (User_Name, Password, Email, Phone, Address, DOB, Role) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try ( Connection conn = dbContext.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, user.getUserName());
            ps.setString(2, user.getPassword());  // Mật khẩu đã được mã hóa MD5 hoặc SHA
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPhone());
            ps.setString(5, user.getAddress());
            ps.setString(6, user.getDob());
            ps.setString(7, user.getRole());

            return ps.executeUpdate() > 0;
        }
    }

    // Cập nhật thông tin người dùng
    public boolean updateUser(User user) throws SQLException {
        String query = "UPDATE Users SET User_Name = ?, Email = ?, Phone = ?, Address = ?, DOB = ?, Role = ? WHERE User_ID = ?";

        try ( Connection conn = dbContext.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, user.getUserName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPhone());
            ps.setString(4, user.getAddress());
            ps.setString(5, user.getDob());
            ps.setString(6, user.getRole());
            ps.setInt(7, user.getUserId());

            return ps.executeUpdate() > 0;
        }
    }

    // Xóa người dùng khỏi database
    public boolean deleteUser(int userId) throws SQLException {
        String query = "DELETE FROM Users WHERE User_ID = ?";

        try ( Connection conn = dbContext.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        }
    }
    public boolean usernameExists(String username) {
    String query = "SELECT COUNT(*) FROM Users WHERE User_Name = ?";
    try (Connection connection = dbContext.getConnection();
         PreparedStatement statement = connection.prepareStatement(query)) {

        statement.setString(1, username);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt(1) > 0;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}

public boolean emailExists(String email) {
    String query = "SELECT COUNT(*) FROM Users WHERE Email = ?";
    try (Connection connection = dbContext.getConnection();
         PreparedStatement statement = connection.prepareStatement(query)) {

        statement.setString(1, email);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt(1) > 0;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}
}
