/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import connect.DBContext;
import model.Trademark;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrademarkDAO {

    private Connection conn;
    DBContext context = new DBContext();

    public TrademarkDAO() {
        try {

            this.conn = context.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Lấy tất cả thương hiệu với phân trang
    public List<Trademark> getAllTrademarks(int offset, int limit) {
        List<Trademark> list = new ArrayList<>();
        String sql = "SELECT * FROM Trademark ORDER BY Trademark_ID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, offset);
            ps.setInt(2, limit);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Trademark t = new Trademark();
                t.setTrademarkId(rs.getInt("Trademark_ID"));
                t.setTrademarkName(rs.getString("Trademark_Name"));
                list.add(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Thêm thương hiệu mới
// Trong class TrademarkDAO
    public boolean addTrademark(String trademarkName) {
        String sql = "INSERT INTO Trademark (Trademark_Name) VALUES (?)";
        try ( Connection conn = context.getConnection();  PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, trademarkName);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cập nhật thương hiệu
    public boolean updateTrademark(int id, String newName) {
        String sql = "UPDATE Trademark SET Trademark_Name = ? WHERE Trademark_ID = ?";
        try ( Connection conn = context.getConnection();  PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newName);
            pstmt.setInt(2, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Xóa thương hiệu
    public boolean deleteTrademark(int trademarkId) {
        String sql = "DELETE FROM Trademark WHERE Trademark_ID = ?";
        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, trademarkId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Đếm tổng số thương hiệu
    public int getTrademarksCount() {
        String sql = "SELECT COUNT(*) FROM Trademark";
        try ( PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
