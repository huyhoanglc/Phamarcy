/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sam;

import java.util.Date;

/**
 *
 * @author ADMIN
 */
public class TableBillsItem {

    private String itemName;
    private int quantity;
    private int unitPrice;
    private String itemType;
    private String medicineID;

    public TableBillsItem(String itemName, int quantity, int unitPrice, String itemType,String medicineID) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.itemType = itemType;
        this.medicineID = medicineID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getItemtype() {
        return itemType;
    }

    public void setItemtype(String itemtype) {
        this.itemType = itemtype;
    }

    public String getMedicineID() {
        return medicineID;
    }

    public void setMedicineID(String medicineID) {
        this.medicineID = medicineID;
    }


 
}
