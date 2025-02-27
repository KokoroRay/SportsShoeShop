package model;

import java.util.Date;
import java.util.List;

public class Order {
    private int Order_ID;
    private int User_ID;
    private Date Order_Date;
    private double Total_Price;
    private String Status;
      private List<OrderItem> items;

    public Order(int Order_ID, int User_ID, Date Order_Date, double Total_Price, String Status) {
        this.Order_ID = Order_ID;
        this.User_ID = User_ID;
        this.Order_Date = Order_Date;
        this.Total_Price = Total_Price;
        this.Status = Status;
    }

    public Order() {
    }

    public int getOrder_ID() {
        return Order_ID;
    }

    public void setOrder_ID(int Order_ID) {
        this.Order_ID = Order_ID;
    }

    public int getUser_ID() {
        return User_ID;
    }

    public void setUser_ID(int User_ID) {
        this.User_ID = User_ID;
    }

    public Date getOrder_Date() {
        return Order_Date;
    }

    public void setOrder_Date(Date Order_Date) {
        this.Order_Date = Order_Date;
    }

    public double getTotal_Price() {
        return Total_Price;
    }

    public void setTotal_Price(double Total_Price) {
        this.Total_Price = Total_Price;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }
 public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
   
}
