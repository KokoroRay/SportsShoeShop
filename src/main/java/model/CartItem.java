package model;

public class CartItem {
    private int Product_Id;
    private String Product_Name;
    private double Price;
    private int Quantity;
    private String Size;

    public CartItem() {
    }

    public CartItem(int Product_Id, String Product_Name, double Price, int Quantity, String Size) {
        this.Product_Id = Product_Id;
        this.Product_Name = Product_Name;
        this.Price = Price;
        this.Quantity = Quantity;
        this.Size = Size;
    }

    public int getProduct_Id() {
        return Product_Id;
    }

    public void setProduct_Id(int Product_Id) {
        this.Product_Id = Product_Id;
    }

    public String getProduct_Name() {
        return Product_Name;
    }

    public void setProduct_Name(String Product_Name) {
        this.Product_Name = Product_Name;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double Price) {
        this.Price = Price;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int Quantity) {
        this.Quantity = Quantity;
    }

    public String getSize() {
        return Size;
    }

    public void setSize(String Size) {
        this.Size = Size;
    }
    public double getTotalPrice() {
        return Price * Quantity;
    }

    
}
