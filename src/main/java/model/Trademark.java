/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Nguyen Minh Tam - CE181522
 */
public class Trademark {

    private int trademarkId;
    private String trademarkName;

    public Trademark() {
    }

    public Trademark(int trademarkId, String trademarkName) {
        this.trademarkId = trademarkId;
        this.trademarkName = trademarkName;
    }

    public int getTrademarkId() {
        return trademarkId;
    }

    public void setTrademarkId(int trademarkId) {
        this.trademarkId = trademarkId;
    }

    public String getTrademarkName() {
        return trademarkName;
    }

    public void setTrademarkName(String trademarkName) {
        this.trademarkName = trademarkName;
    }

}
