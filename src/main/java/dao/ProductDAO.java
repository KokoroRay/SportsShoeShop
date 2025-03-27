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
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

/**
 *
 * @author ADMIN
 */
public class ProductDAO {

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.*, t.Trademark_Name FROM Products p JOIN Trademark t ON p.Trademark_ID = t.Trademark_ID";
        DBContext context = new DBContext();
        try ( Connection conn = context.getConnection();  PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Product p = new Product(
                        rs.getInt("Product_ID"),
                        rs.getString("Trademark_Name"), // Lấy từ Trademark
                        rs.getString("Product_Name"),
                        rs.getDouble("Price"),
                        rs.getInt("Quantity"),
                        rs.getString("Size"),
                        rs.getString("Description"),
                        rs.getString("Image"),
                        rs.getDouble("Rate"),
                        rs.getString("Type"));
                p.setOriginalPrice(rs.getDouble("Original_Price"));
                p.setDiscountPercent(rs.getDouble("Discount_Percent"));
                p.setDiscountStartDate(rs.getDate("Discount_Start_Date"));
                p.setDiscountEndDate(rs.getDate("Discount_End_Date"));
                products.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products;
    }

    public Product getProductById(String productId) {
        Product product = null;
        String query = "SELECT p.*, t.Trademark_Name FROM Products p JOIN Trademark t ON p.Trademark_ID = t.Trademark_ID WHERE Product_ID = ?";
        DBContext context = new DBContext();
        try ( Connection conn = context.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, Integer.parseInt(productId));

            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    product = new Product();
                    product.setProduct_ID(rs.getInt("Product_ID"));
                    product.setProduct_Name(rs.getString("Product_Name"));
                    product.setDescription(rs.getString("Description"));
                    product.setPrice(rs.getDouble("Price"));
                    product.setImage(rs.getString("Image"));
                    product.setBrand(rs.getString("Trademark_Name")); // Lấy từ Trademark
                    product.setSize(rs.getString("Size"));
                    product.setQuantity(rs.getInt("Quantity"));
                    product.setRate(rs.getDouble("Rate"));
                    product.setOriginalPrice(rs.getDouble("Original_Price"));
                    product.setDiscountPercent(rs.getDouble("Discount_Percent"));
                    product.setDiscountStartDate(rs.getDate("Discount_Start_Date"));
                    product.setDiscountEndDate(rs.getDate("Discount_End_Date"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }

    public List<Product> getProductsByBrand(String brand) {
        List<Product> products = new ArrayList<>();
        String query = "SELECT p.*, t.Trademark_Name FROM Products p "
                + "JOIN Trademark t ON p.Trademark_ID = t.Trademark_ID "
                + "WHERE t.Trademark_Name = ?";
        DBContext context = new DBContext();
        try ( Connection conn = context.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, brand);
            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product p = new Product(
                            rs.getInt("Product_ID"),
                            rs.getString("Trademark_Name"), // Lấy từ Trademark
                            rs.getString("Product_Name"),
                            rs.getDouble("Price"),
                            rs.getInt("Quantity"),
                            rs.getString("Size"),
                            rs.getString("Description"),
                            rs.getString("Image"),
                            rs.getDouble("Rate"),
                            rs.getString("Type")
                    );
                    // Các thuộc tính khác
                    products.add(p);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public List<Product> getProductsByBrands(List<String> brands) {
        List<Product> products = new ArrayList<>();
        try ( Connection conn = new DBContext().getConnection()) {
            StringBuilder sql = new StringBuilder(
                    "SELECT p.*, t.Trademark_Name FROM Products p "
                    + "JOIN Trademark t ON p.Trademark_ID = t.Trademark_ID "
            );

            if (brands != null && !brands.isEmpty()) {
                sql.append("WHERE t.Trademark_Name IN (");
                for (int i = 0; i < brands.size(); i++) {
                    sql.append("?");
                    if (i < brands.size() - 1) {
                        sql.append(",");
                    }
                }
                sql.append(")");
            }

            PreparedStatement ps = conn.prepareStatement(sql.toString());

            if (brands != null && !brands.isEmpty()) {
                for (int i = 0; i < brands.size(); i++) {
                    ps.setString(i + 1, brands.get(i));
                }
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Product p = new Product(
                        rs.getInt("Product_ID"),
                        rs.getString("Trademark_Name"),
                        rs.getString("Product_Name"),
                        rs.getDouble("Price"),
                        rs.getInt("Quantity"),
                        rs.getString("Size"),
                        rs.getString("Description"),
                        rs.getString("Image"),
                        rs.getDouble("Rate"),
                        rs.getString("Type")
                );
                // Thêm các thuộc tính khác nếu cần
                products.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public List<Product> getProductsByType(String Type) {
        List<Product> products = new ArrayList<>();
        String query = "SELECT p.*, t.Trademark_Name FROM Products p JOIN Trademark t ON p.Trademark_ID = t.Trademark_ID WHERE Type = ?";
        DBContext context = new DBContext();
        try ( Connection conn = context.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, Type);

            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product p = new Product(
                            rs.getInt("Product_ID"),
                            rs.getString("Trademark_Name"),
                            rs.getString("Product_Name"),
                            rs.getDouble("Price"),
                            rs.getInt("Quantity"),
                            rs.getString("Size"),
                            rs.getString("Description"),
                            rs.getString("Image"),
                            rs.getDouble("Rate"),
                            rs.getString("Type"));
                    p.setOriginalPrice(rs.getDouble("Original_Price"));
                    p.setDiscountPercent(rs.getDouble("Discount_Percent"));
                    p.setDiscountStartDate(rs.getDate("Discount_Start_Date"));
                    p.setDiscountEndDate(rs.getDate("Discount_End_Date"));
                    products.add(p);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public List<Product> getProductsByName(String productName) {
        List<Product> products = new ArrayList<>();
        String query = "SELECT p.*, t.Trademark_Name FROM Products p JOIN Trademark t ON p.Trademark_ID = t.Trademark_ID WHERE Product_Name LIKE ?";
        DBContext context = new DBContext();
        try ( Connection conn = context.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, "%" + productName + "%");

            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product p = new Product(
                            rs.getInt("Product_ID"),
                            rs.getString("Trademark_Name"),
                            rs.getString("Product_Name"),
                            rs.getDouble("Price"),
                            rs.getInt("Quantity"),
                            rs.getString("Size"),
                            rs.getString("Description"),
                            rs.getString("Image"),
                            rs.getDouble("Rate"),
                            rs.getString("Type"));
                    p.setOriginalPrice(rs.getDouble("Original_Price"));
                    p.setDiscountPercent(rs.getDouble("Discount_Percent"));
                    p.setDiscountStartDate(rs.getDate("Discount_Start_Date"));
                    p.setDiscountEndDate(rs.getDate("Discount_End_Date"));
                    products.add(p);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public int getProductsCount() {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM Products"; // Sửa truy vấn đơn giản
        DBContext context = new DBContext();
        try ( Connection conn = context.getConnection();  PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public List<Product> getProducts(int offset, int limit) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.*, t.Trademark_Name FROM Products p JOIN Trademark t ON p.Trademark_ID = t.Trademark_ID ORDER BY Product_ID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        DBContext context = new DBContext();
        try ( Connection conn = context.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, offset);
            ps.setInt(2, limit);
            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product p = new Product(
                            rs.getInt("Product_ID"),
                            rs.getString("Trademark_Name"), // Gán vào trường brand
                            rs.getString("Product_Name"),
                            rs.getDouble("Price"),
                            rs.getInt("Quantity"),
                            rs.getString("Size"),
                            rs.getString("Description"),
                            rs.getString("Image"),
                            rs.getDouble("Rate"),
                            rs.getString("Type"));
                    p.setOriginalPrice(rs.getDouble("Original_Price"));
                    p.setDiscountPercent(rs.getDouble("Discount_Percent"));
                    p.setDiscountStartDate(rs.getDate("Discount_Start_Date"));
                    p.setDiscountEndDate(rs.getDate("Discount_End_Date"));
                    products.add(p);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

public List<Product> getBestsellerProducts() {
    List<Product> products = new ArrayList<>();
    String sql = "SELECT TOP 10 p.Product_ID, p.Trademark_ID, t.Trademark_Name, " +
                 "p.Product_Name, p.Price, p.Quantity, p.Size, p.Description, p.Image, " +
                 "p.Rate, p.Type, p.Original_Price, p.Discount_Percent, " +
                 "p.Discount_Start_Date, p.Discount_End_Date, " +
                 "SUM(oi.Quantity) AS TotalSold " +  // Tổng số lượng bán ra
                 "FROM Products p " +
                 "JOIN Trademark t ON p.Trademark_ID = t.Trademark_ID " +
                 "JOIN Order_Items oi ON p.Product_ID = oi.Product_ID " +
                 "GROUP BY p.Product_ID, p.Trademark_ID, t.Trademark_Name, " +
                 "p.Product_Name, p.Price, p.Quantity, p.Size, p.Description, p.Image, " +
                 "p.Rate, p.Type, p.Original_Price, p.Discount_Percent, " +
                 "p.Discount_Start_Date, p.Discount_End_Date " +
                 "ORDER BY TotalSold DESC"; // Sắp xếp theo tổng số lượng bán

    try (Connection conn = new DBContext().getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            Product p = new Product(
                rs.getInt("Product_ID"),
                rs.getString("Trademark_Name"), // Gán tên thương hiệu
                rs.getString("Product_Name"),
                rs.getDouble("Price"),
                rs.getInt("Quantity"),
                rs.getString("Size"),
                rs.getString("Description"),
                rs.getString("Image"),
                rs.getDouble("Rate"),
                rs.getString("Type")
            );
            p.setOriginalPrice(rs.getDouble("Original_Price"));
            p.setDiscountPercent(rs.getDouble("Discount_Percent"));
            p.setDiscountStartDate(rs.getDate("Discount_Start_Date"));
            p.setDiscountEndDate(rs.getDate("Discount_End_Date"));
            products.add(p);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return products;
}


    public List<Product> getRelatedProducts(int categoryId, int productId) {
        List<Product> relatedProducts = new ArrayList<>();
        String query = "SELECT TOP 4 p.*, t.Trademark_Name FROM Products p JOIN Trademark t ON p.Trademark_ID = t.Trademark_ID WHERE Type = (SELECT Type FROM Products WHERE Product_ID = ?) AND Product_ID != ?";
        DBContext context = new DBContext();
        try ( Connection conn = context.getConnection();  PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, productId);
            stmt.setInt(2, productId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Product p = new Product(
                        rs.getInt("Product_ID"),
                        rs.getString("Trademark_Name"),
                        rs.getString("Product_Name"),
                        rs.getDouble("Price"),
                        rs.getInt("Quantity"),
                        rs.getString("Size"),
                        rs.getString("Description"),
                        rs.getString("Image"),
                        rs.getDouble("Rate"),
                        rs.getString("Type"));
                p.setOriginalPrice(rs.getDouble("Original_Price"));
                p.setDiscountPercent(rs.getDouble("Discount_Percent"));
                p.setDiscountStartDate(rs.getDate("Discount_Start_Date"));
                p.setDiscountEndDate(rs.getDate("Discount_End_Date"));
                relatedProducts.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return relatedProducts;
    }

    public List<Product> getBestSellingProducts() {
        List<Product> bestSellers = new ArrayList<>();
        String query = "SELECT top 8 p.Product_ID, p.Product_Name, p.Price, p.Image, "
                + "SUM(oi.Quantity) AS TotalSold "
                + "FROM Products p "
                + "JOIN Order_Items oi ON p.Product_ID = oi.Product_ID "
                + "GROUP BY p.Product_ID, p.Product_Name, p.Price, p.Image "
                + "ORDER BY TotalSold DESC";
        try ( Connection conn = new DBContext().getConnection();  PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Product p = new Product();
                p.setProduct_ID(rs.getInt("Product_ID"));
                p.setProduct_Name(rs.getString("Product_Name"));
                p.setPrice(rs.getDouble("Price"));
                p.setImage(rs.getString("Image"));
                bestSellers.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Thêm logging chi tiết
            System.err.println("Lỗi truy vấn Best Selling: " + e.getMessage());
        }
        return bestSellers;
    }

    public List<Product> getProductsByBrand(String brand, int productId) {
        List<Product> brandProducts = new ArrayList<>();
        String query = "SELECT TOP 8 p.*, t.Trademark_Name FROM Products p JOIN Trademark t ON p.Trademark_ID = t.Trademark_ID WHERE t.Trademark_Name = ? AND Product_ID != ?";
        DBContext context = new DBContext();
        try ( Connection conn = context.getConnection();  PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, brand);
            stmt.setInt(2, productId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Product p = new Product(
                        rs.getInt("Product_ID"),
                        rs.getString("Trademark_Name"),
                        rs.getString("Product_Name"),
                        rs.getDouble("Price"),
                        rs.getInt("Quantity"),
                        rs.getString("Size"),
                        rs.getString("Description"),
                        rs.getString("Image"),
                        rs.getDouble("Rate"),
                        rs.getString("Type"));
                p.setOriginalPrice(rs.getDouble("Original_Price"));
                p.setDiscountPercent(rs.getDouble("Discount_Percent"));
                p.setDiscountStartDate(rs.getDate("Discount_Start_Date"));
                p.setDiscountEndDate(rs.getDate("Discount_End_Date"));
                brandProducts.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return brandProducts;
    }

    public List<String> getSizesByProductId(int productId) {
        List<String> sizes = new ArrayList<>();
        String query = "SELECT Size FROM Products where Product_ID = ?";
        DBContext context = new DBContext();

        try ( Connection conn = context.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, productId);

            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String sizeString = rs.getString("Size");
                    if (sizeString != null && !sizeString.isEmpty()) {
                        String[] sizeArray = sizeString.split(",\\s*"); // Tách size dựa vào dấu `,`
                        sizes.addAll(Arrays.asList(sizeArray));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sizes;
    }

    public List<Product> getMostFavoritedProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.Product_ID, t.Trademark_Name, p.Product_Name, p.Price, p.Quantity, p.Size, "
                + "p.Description, p.Image, p.Rate, p.Type, p.Original_Price, p.Discount_Percent, "
                + "p.Discount_Start_Date, p.Discount_End_Date, COUNT(f.User_ID) AS FavoriteCount "
                + "FROM Products p "
                + "JOIN Trademark t ON p.Trademark_ID = t.Trademark_ID "
                + "LEFT JOIN Favorites f ON p.Product_ID = f.Product_ID "
                + "GROUP BY p.Product_ID, t.Trademark_Name, p.Product_Name, p.Price, p.Quantity, p.Size, "
                + "p.Description, p.Image, p.Rate, p.Type, p.Original_Price, p.Discount_Percent, "
                + "p.Discount_Start_Date, p.Discount_End_Date "
                + "ORDER BY FavoriteCount DESC";

        try ( Connection conn = new DBContext().getConnection();  PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Product p = new Product(
                        rs.getInt("Product_ID"),
                        rs.getString("Trademark_Name"),
                        rs.getString("Product_Name"),
                        rs.getDouble("Price"),
                        rs.getInt("Quantity"),
                        rs.getString("Size"),
                        rs.getString("Description"),
                        rs.getString("Image"),
                        rs.getDouble("Rate"),
                        rs.getString("Type")
                );

                // Thiết lập các thuộc tính khuyến mãi
                p.setOriginalPrice(rs.getDouble("Original_Price"));
                p.setDiscountPercent(rs.getDouble("Discount_Percent"));
                p.setDiscountStartDate(rs.getDate("Discount_Start_Date"));
                p.setDiscountEndDate(rs.getDate("Discount_End_Date"));

                // Thiết lập số lượt yêu thích
                p.setFavoriteCount(rs.getInt("FavoriteCount"));

                products.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public double getDiscountedPrice(Product product) {
        if (isDiscountActive(product)) {
            return product.getPrice() * (1 - product.getDiscountPercent() / 100);
        }
        return product.getPrice();
    }

    public List<String> getAllBrands() {
        List<String> brands = new ArrayList<>();
        String sql = "SELECT Trademark_Name FROM Trademark";
        try ( Connection conn = new DBContext().getConnection();  PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                brands.add(rs.getString("Trademark_Name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return brands;
    }

    public List<Product> getHotSellProducts() {
        List<Product> hotSellProducts = new ArrayList<>();
        String query = "SELECT TOP 8 p.Product_ID, p.Product_Name, p.Price, p.Image, p.Original_Price, "
                + "p.Discount_Percent, p.Discount_Start_Date, p.Discount_End_Date, t.Trademark_Name "
                + "FROM Products p "
                + "JOIN Trademark t ON p.Trademark_ID = t.Trademark_ID "
                + "WHERE p.Discount_Percent > 0 "
                + // Chỉ lấy sản phẩm có giảm giá
                "ORDER BY p.Discount_Percent DESC"; // Sắp xếp giảm dần theo phần trăm giảm giá

        try ( Connection conn = new DBContext().getConnection();  PreparedStatement stmt = conn.prepareStatement(query);  ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Product p = new Product(
                        rs.getInt("Product_ID"),
                        rs.getString("Trademark_Name"),
                        rs.getString("Product_Name"),
                        rs.getDouble("Price"),
                        0, // Không lấy Quantity từ bảng Products
                        "", // Không cần Size
                        "", // Không cần Description
                        rs.getString("Image"),
                        0, // Không lấy Rate
                        "" // Không cần Type
                );
                p.setOriginalPrice(rs.getDouble("Original_Price"));
                p.setDiscountPercent(rs.getDouble("Discount_Percent"));
                p.setDiscountStartDate(rs.getDate("Discount_Start_Date"));
                p.setDiscountEndDate(rs.getDate("Discount_End_Date"));
                hotSellProducts.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hotSellProducts;
    }

    public boolean updateProductDiscount(Product product) {
        String sql = "UPDATE Products SET "
                + "Discount_Percent = ?, "
                + "Discount_Start_Date = ?, "
                + "Discount_End_Date = ?, "
                + "Price = ?, "
                + "Original_Price = CASE WHEN ? = 0 THEN Original_Price ELSE ? END "
                + "WHERE Product_ID = ?";

        try ( Connection conn = new DBContext().getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, product.getDiscountPercent());
            ps.setDate(2, new java.sql.Date(product.getDiscountStartDate().getTime()));
            ps.setDate(3, new java.sql.Date(product.getDiscountEndDate().getTime()));
            ps.setDouble(4, product.getPrice());
            ps.setDouble(5, product.getDiscountPercent()); // Điều kiện (khi discount = 0)
            ps.setDouble(6, product.getOriginalPrice());   // Giá trị mới (khi discount > 0)
            ps.setInt(7, product.getProduct_ID());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isDiscountActive(Product product) {
        Date now = new Date();

        // Kiểm tra cả ngày bắt đầu và kết thúc
        if (product.getDiscountStartDate() == null
                || product.getDiscountEndDate() == null) {
            return false;
        }

        // So sánh cả ngày và giờ
        return now.after(product.getDiscountStartDate())
                && now.before(product.getDiscountEndDate());
    }

    public boolean insertProduct(Product product) {
        String sql = "INSERT INTO Products (Trademark_ID, Product_Name, Price, Quantity, Size, Description, Image, Rate, Type) "
                + "VALUES ((SELECT Trademark_ID FROM Trademark WHERE Trademark_Name = ?), ?, ?, ?, ?, ?, ?, ?, ?)";
        try ( Connection conn = new DBContext().getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, product.getBrand());
            ps.setString(2, product.getProduct_Name());
            ps.setDouble(3, product.getPrice());
            ps.setInt(4, product.getQuantity());
            ps.setString(5, product.getSize());
            ps.setString(6, product.getDescription());
            ps.setString(7, product.getImage());
            ps.setDouble(8, product.getRate());
            ps.setString(9, product.getType());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateProduct(Product product) {
        String query = "UPDATE Products SET "
                + "Trademark_ID = (SELECT Trademark_ID FROM Trademark WHERE Trademark_Name = ?), "
                + "Product_Name = ?, Price = ?, Quantity = ?, Size = ?, Description = ?, Image = ?, Rate = ?, Type = ?, "
                + "Original_Price = ?, Discount_Percent = ?, Discount_Start_Date = ?, Discount_End_Date = ? "
                + "WHERE Product_ID = ?";
        DBContext context = new DBContext();
        try ( Connection conn = context.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {

            // Lấy Trademark_ID từ Trademark_Name
            ps.setString(1, product.getBrand()); // Tham số 1: Trademark_Name
            ps.setString(2, product.getProduct_Name());
            ps.setDouble(3, product.getPrice());
            ps.setInt(4, product.getQuantity());
            ps.setString(5, product.getSize());
            ps.setString(6, product.getDescription());
            ps.setString(7, product.getImage());
            ps.setDouble(8, product.getRate());
            ps.setString(9, product.getType());
            ps.setDouble(10, product.getOriginalPrice());
            ps.setDouble(11, product.getDiscountPercent());
            ps.setDate(12, new java.sql.Date(product.getDiscountStartDate().getTime()));
            ps.setDate(13, new java.sql.Date(product.getDiscountEndDate().getTime()));
            ps.setInt(14, product.getProduct_ID());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void updateExpiredDiscounts() {
        String sql = "UPDATE Products SET Price = Original_Price, Discount_Percent = 0, "
                + "Discount_Start_Date = NULL, Discount_End_Date = NULL "
                + "WHERE Discount_End_Date < CURDATE()";
        try ( Connection conn = new DBContext().getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
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
