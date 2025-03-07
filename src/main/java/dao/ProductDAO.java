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
        String query = "SELECT * FROM Products WHERE Product_ID = ?";
        DBContext context = new DBContext();
        try ( Connection conn = context.getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {

            // Sửa ở đây: chuyển đổi productId sang int
            ps.setInt(1, Integer.parseInt(productId));

            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
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
                    // Các thuộc tính khác nếu cần
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
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

    public int getProductsCount() {
        int count = 0;
        String sql = "SELECT TOP 10 P.Product_ID, P.Product_Name, P.Brand, P.Type, SUM(OI.Quantity) AS Total_Sold\n"
                + "FROM Order_Items OI\n"
                + "JOIN Products P ON OI.Product_ID = P.Product_ID\n"
                + "GROUP BY P.Product_ID, P.Product_Name, P.Brand, P.Type\n"
                + "ORDER BY Total_Sold DESC;\n"
                + "\n"
                + "";
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
        // SQL Server yêu cầu mệnh đề ORDER BY khi sử dụng OFFSET FETCH.
        String sql = "SELECT * FROM Products ORDER BY Product_ID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        DBContext context = new DBContext();
        try ( Connection conn = context.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, offset);
            ps.setInt(2, limit);
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
        return products;
    }

    public List<Product> getBestsellerProducts() {
        List<Product> products = new ArrayList<>();
        // Giả sử tiêu chí bán chạy là Rate cao nhất. Bạn có thể thay đổi truy vấn theo cột Sales nếu có.
        String sql = "SELECT TOP 10 * FROM Products ORDER BY Rate DESC";
        DBContext context = new DBContext();
        try ( Connection conn = context.getConnection();  PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public List<Product> getRelatedProducts(int categoryId, int productId) {
        List<Product> relatedProducts = new ArrayList<>();
        String query = "SELECT * FROM Products WHERE Type = (SELECT Type FROM Products WHERE Product_ID = ?) "
                + "AND Product_ID != ? LIMIT 4";
        DBContext context = new DBContext();
        try ( Connection conn = context.getConnection();  PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, productId);
            stmt.setInt(2, productId);
            ResultSet rs = stmt.executeQuery();
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
                relatedProducts.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return relatedProducts;
    }

    public List<Product> getBestSellingProducts() {
        List<Product> bestSellers = new ArrayList<>();
        String query = "SELECT p.* FROM Products p "
                + "JOIN Order_Items oi ON p.Product_ID = oi.Product_ID "
                + "GROUP BY p.Product_ID "
                + "ORDER BY SUM(oi.Quantity) DESC LIMIT 4";
        DBContext context = new DBContext();
        try ( Connection conn = context.getConnection();  PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
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
                bestSellers.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bestSellers;
    }

    public List<Product> getProductsByBrand(String brand, int productId) {
        List<Product> brandProducts = new ArrayList<>();
        String query = "SELECT * FROM Products WHERE Brand = ? AND Product_ID != ? LIMIT 4";
        DBContext context = new DBContext();
        try ( Connection conn = context.getConnection();  PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, brand);
            stmt.setInt(2, productId);
            ResultSet rs = stmt.executeQuery();
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
        String sql = "SELECT p.*, COUNT(f.User_ID) AS FavoriteCount "
                + "FROM Products p "
                + "LEFT JOIN Favorites f ON p.Product_ID = f.Product_ID "
                + "GROUP BY p.Product_ID, p.Product_Name, p.Brand, p.Price, p.Image, p.Description, p.Size, p.Quantity, p.Rate, p.Type "
                + "ORDER BY FavoriteCount DESC";

        try ( Connection conn = new DBContext().getConnection();  PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {

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
                p.setFavoriteCount(rs.getInt("FavoriteCount")); // Thêm dòng này
                products.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
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
