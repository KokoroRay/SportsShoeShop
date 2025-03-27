/*
     * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
     * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author NguyenMinhTam CE181522
 */
public class Product {

    private int Product_ID;
    private String Brand;
    private String Product_Name;
    private double Price;
    private int Quantity;
    private String Size;
    private String Description;
    private String Image;
    private double Rate;
    private String Type;
    private double originalPrice;
    private double discountPercent;
    private Date discountStartDate;
    private Date discountEndDate;
    private int favoriteCount;

    public Product() {
    }

    public Product(int Product_ID, String Brand, String Product_Name, double Price, int Quantity, String Size, String Description, String Image, double Rate, String Type) {
        this.Product_ID = Product_ID;
        this.Brand = Brand;
        this.Product_Name = Product_Name;
        this.Price = Price;
        this.Quantity = Quantity;
        this.Size = Size;
        this.Description = Description;
        this.Image = Image;
        this.Rate = Rate;
        this.Type = Type;
    }

    public String getType() {
        return Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }

    public int getProduct_ID() {
        return Product_ID;
    }

    public void setProduct_ID(int Product_ID) {
        this.Product_ID = Product_ID;
    }

    public String getBrand() {
        return Brand;
    }

    public void setBrand(String Brand) {
        this.Brand = Brand;
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

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String Image) {
        this.Image = Image;
    }

    public double getRate() {
        return Rate;
    }

    public void setRate(double Rate) {
        this.Rate = Rate;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(double discountPercent) {
        this.discountPercent = discountPercent;
    }

    public Date getDiscountStartDate() {
        return discountStartDate;
    }

    public void setDiscountStartDate(Date discountStartDate) {
        this.discountStartDate = discountStartDate;
    }

    public Date getDiscountEndDate() {
        return discountEndDate;
    }

    public void setDiscountEndDate(Date discountEndDate) {
        this.discountEndDate = discountEndDate;
    }

    public void setString(String string) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public long getRemainingDiscountDays() {
        if (discountStartDate == null || discountEndDate == null) {
            return -1;
        }
        Date now = new Date();
        if (now.before(discountStartDate) || now.after(discountEndDate)) {
            return -1;
        }
        long diff = discountEndDate.getTime() - now.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

}
