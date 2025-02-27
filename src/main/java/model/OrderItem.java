package model;

public class OrderItem {
    private int Order_Item_Id;
    private int Order_Id;
    private int Product_Id;
    private int Quantity;
    private double UnitPrice;
    private String Size;
private String productName; // Thêm thuộc tính tên sản phẩm
    // Constructors

    public OrderItem() {
    }

    public OrderItem(int Order_Item_Id, int Order_Id, int Product_Id, int Quantity, double UnitPrice, String Size) {
        this.Order_Item_Id = Order_Item_Id;
        this.Order_Id = Order_Id;
        this.Product_Id = Product_Id;
        this.Quantity = Quantity;
        this.UnitPrice = UnitPrice;
        this.Size = Size;
    }

    public OrderItem(int Order_Item_Id, int Order_Id, int Product_Id, int Quantity, double UnitPrice, String Size, String productName) {
        this.Order_Item_Id = Order_Item_Id;
        this.Order_Id = Order_Id;
        this.Product_Id = Product_Id;
        this.Quantity = Quantity;
        this.UnitPrice = UnitPrice;
        this.Size = Size;
        this.productName = productName;
    }
    

    public int getOrder_Item_Id() {
        return Order_Item_Id;
    }

    public void setOrder_Item_Id(int Order_Item_Id) {
        this.Order_Item_Id = Order_Item_Id;
    }

    public int getOrder_Id() {
        return Order_Id;
    }

    public void setOrder_Id(int Order_Id) {
        this.Order_Id = Order_Id;
    }

    public int getProduct_Id() {
        return Product_Id;
    }

    public void setProduct_Id(int Product_Id) {
        this.Product_Id = Product_Id;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int Quantity) {
        this.Quantity = Quantity;
    }

    public double getUnitPrice() {
        return UnitPrice;
    }

    public void setUnitPrice(double UnitPrice) {
        this.UnitPrice = UnitPrice;
    }

    public String getSize() {
        return Size;
    }

    public void setSize(String Size) {
        this.Size = Size;
    }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    
    }

