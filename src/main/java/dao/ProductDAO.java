/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import connect.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Product;

/**
 *
 * @author ADMIN
 */
public class ProductDAO {

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        DBContext context = new DBContext();
        try ( Connection conn = context.getConnection()) {
            String sql = "SELECT * FROM Products";
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Product p = new Product(
                        rs.getInt("Product_ID"),
                        rs.getString("Brand"),
                        rs.getString("Product_Name"),
                        rs.getDouble("Price"),
                        rs.getInt("Quantity"),
                        rs.getString("Size"),
                        rs.getString("Description"),
                        rs.getString("Image"),
                        rs.getDouble("Rate"),
                        rs.getString("Type"));
                products.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products;
    }

    public Product getProductById(String productId) {
        Product product = null;
        String query = "SELECT * FROM products WHERE Product_ID = ?";

        DBContext context = new DBContext();
        try ( Connection conn = context.getConnection(); // Giả sử bạn có lớp Database quản lý kết nối
                  PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, productId);  // Thiết lập giá trị cho tham số đầu vào

            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Lấy dữ liệu từ ResultSet và khởi tạo đối tượng Product
                    product = new Product();
                    product.setProduct_ID(rs.getInt("Product_ID"));
                    product.setProduct_Name(rs.getString("Product_Name"));
                    product.setDescription(rs.getString("Description"));
                    product.setPrice(rs.getDouble("Price"));
                    product.setImage(rs.getString("Image"));
                    product.setBrand(rs.getString("Brand"));
                    product.setSize(rs.getString("Size"));
                    product.setQuantity(rs.getInt("Quantity"));
                    product.setRate(rs.getDouble("Rate"));
                    // Thêm các thuộc tính khác nếu có
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return product;  // Trả về đối tượng Product hoặc null nếu không tìm thấy
    }

    public List<Product> getProductsByBrand(String brand) {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products WHERE Brand = ?";
        DBContext context = new DBContext();
        try ( Connection conn = context.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, brand);  // Thiết lập giá trị cho tham số brand

            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product p = new Product(
                            rs.getInt("Product_ID"),
                            rs.getString("Brand"),
                            rs.getString("Product_Name"),
                            rs.getDouble("Price"),
                            rs.getInt("Quantity"),
                            rs.getString("Size"),
                            rs.getString("Description"),
                            rs.getString("Image"),
                            rs.getDouble("Rate"),
                            rs.getString("Type")
                    );
                    products.add(p);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;  // Trả về danh sách sản phẩm của thương hiệu cụ thể
    }

    public List<Product> getProductsByType(String Type) {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products WHERE Type = ?";
        DBContext context = new DBContext();
        try ( Connection conn = context.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, Type);  // Thiết lập giá trị cho tham số brand

            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product p = new Product(
                            rs.getInt("Product_ID"),
                            rs.getString("Brand"),
                            rs.getString("Product_Name"),
                            rs.getDouble("Price"),
                            rs.getInt("Quantity"),
                            rs.getString("Size"),
                            rs.getString("Description"),
                            rs.getString("Image"),
                            rs.getDouble("Rate"),
                            rs.getString("Type")
                    );
                    products.add(p);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;  // Trả về danh sách sản phẩm của thương hiệu cụ thể
    }

    public List<Product> getProductsByName(String productName) {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products WHERE Product_Name LIKE ?";
        DBContext context = new DBContext();
        try ( Connection conn = context.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, "%" + productName + "%");  // Sử dụng dấu '%' để tìm kiếm tên gần đúng

            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product p = new Product(
                            rs.getInt("Product_ID"),
                            rs.getString("Brand"),
                            rs.getString("Product_Name"),
                            rs.getDouble("Price"),
                            rs.getInt("Quantity"),
                            rs.getString("Size"),
                            rs.getString("Description"),
                            rs.getString("Image"),
                            rs.getDouble("Rate"),
                            rs.getString("Type")
                    );
                    products.add(p);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;  // Trả về danh sách sản phẩm có tên phù hợp
    }
     public boolean insertProduct(Product product) {
        String query = "INSERT INTO Products (Brand, Product_Name, Price, Quantity, Size, Description, Image, Rate) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        DBContext context = new DBContext();
        try ( Connection conn = context.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, product.getBrand());
            ps.setString(2, product.getProduct_Name());
            ps.setDouble(3, product.getPrice());
            ps.setInt(4, product.getQuantity());
            ps.setString(5, product.getSize());
            ps.setString(6, product.getDescription());
            ps.setString(7, product.getImage());
            ps.setDouble(8, product.getRate());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateProduct(Product product) {
        String query = "UPDATE Products SET Brand = ?, Product_Name = ?, Price = ?, Quantity = ?, Size = ?, Description = ?, Image = ?, Rate = ?, Type = ? WHERE Product_ID = ?";
        DBContext context = new DBContext();

        try ( Connection conn = context.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, product.getBrand());
            ps.setString(2, product.getProduct_Name());
            ps.setDouble(3, product.getPrice());
            ps.setInt(4, product.getQuantity());
            ps.setString(5, product.getSize());
            ps.setString(6, product.getDescription());
            ps.setString(7, product.getImage());
            ps.setDouble(8, product.getRate());
            ps.setString(9, product.getType());
            ps.setInt(10, product.getProduct_ID());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteProduct(int Product_ID) {
        String query = "DELETE FROM Products WHERE Product_ID = ?";
        DBContext context = new DBContext();
        try ( Connection conn = context.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, Product_ID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
public void rateProduct(int productId, int rating) {
    String query = "UPDATE Products SET Rate = ? WHERE Product_ID = ?";
    DBContext context = new DBContext();
        try ( Connection conn = context.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {

        ps.setInt(1, rating);
        ps.setInt(2, productId);
        ps.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

}
