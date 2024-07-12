/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eproject;

import java.util.Date;

/**
 *
 * @author 3D
 */
public class ModelReceipt {
    private String ReceiptID;
    private Date OrderDate;
    private Date ReceiveDate;
    private Integer UserID;
    private String SupplierID;
    private String SupplierName;
    private Integer TotalSpend;
    private String Status;

    public ModelReceipt(String ReceiptID, Date OrderDate, Date ReceiveDate, Integer UserID, String SupplierID, String SupplierName, Integer TotalSpend, String Status) {
        this.ReceiptID = ReceiptID;
        this.OrderDate = OrderDate;
        this.ReceiveDate = ReceiveDate;
        this.UserID = UserID;
        this.SupplierID = SupplierID;
        this.SupplierName = SupplierName;
        this.TotalSpend = TotalSpend;
        this.Status = Status;
    }

    public String getReceiptID() {
        return ReceiptID;
    }

    public Date getOrderDate() {
        return OrderDate;
    }

    public Date getReceiveDate() {
        return ReceiveDate;
    }

    public Integer getUserID() {
        return UserID;
    }

    public String getSupplierID() {
        return SupplierID;
    }

    public String getSupplierName() {
        return SupplierName;
    }

    public Integer getTotalSpend() {
        return TotalSpend;
    }

    public String getStatus() {
        return Status;
    }
    
    
    

   
    
}
