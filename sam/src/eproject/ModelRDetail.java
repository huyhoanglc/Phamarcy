/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eproject;

/**
 *
 * @author 3D
 */
public class ModelRDetail {
    private String ReceiptID;
    private String MedicineID;
    private String MedicineName;
    private Integer OQty01;
    private Integer OQty02;
    private Integer OQty03;
    private Integer ITotalPrice;
    private Integer CostPrice;
    private Integer Quantity;
    private Integer Subunitsperunit;
    private Integer Itemspersubunit;

    public ModelRDetail(String ReceiptID, String MedicineID, String MedicineName, Integer OQty01, Integer OQty02, Integer OQty03, Integer ITotalPrice, Integer CostPrice, Integer Quantity, Integer Subunitsperunit, Integer Itemspersubunit) {
        this.ReceiptID = ReceiptID;
        this.MedicineID = MedicineID;
        this.MedicineName = MedicineName;
        this.OQty01 = OQty01;
        this.OQty02 = OQty02;
        this.OQty03 = OQty03;
        this.ITotalPrice = ITotalPrice;
        this.CostPrice = CostPrice;
        this.Quantity = Quantity;
        this.Subunitsperunit = Subunitsperunit;
        this.Itemspersubunit = Itemspersubunit;
    }

    public String getReceiptID() {
        return ReceiptID;
    }

    public String getMedicineID() {
        return MedicineID;
    }

    public String getMedicineName() {
        return MedicineName;
    }

    public Integer getOQty01() {
        return OQty01;
    }

    public Integer getOQty02() {
        return OQty02;
    }

    public Integer getOQty03() {
        return OQty03;
    }

    public Integer getITotalPrice() {
        return ITotalPrice;
    }

    public Integer getCostPrice() {
        return CostPrice;
    }

    public Integer getQuantity() {
        return Quantity;
    }

    public Integer getSubunitsperunit() {
        return Subunitsperunit;
    }

    public Integer getItemspersubunit() {
        return Itemspersubunit;
    }

    
}
