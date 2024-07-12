/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Data;

/**
 *
 * @author Admin
 */
public class DruglistData {

    private String MedicineID;
    private String MedicineName;
    private String API;
    private Integer Quantity;
    private String Unit;
    private Integer Subunitsperunit;
    private String Subunit;
    private Integer Itemspersubunit;
    private String Item;
    private Integer CostPrice;
    private Integer SellingPrice;

    public DruglistData(String MedicineID, String MedicineName, String API, Integer Quantity, String Unit, Integer Subunitsperunit, String Subunit, Integer Itemspersubunit, String Item, Integer CostPrice, Integer SellingPrice) {
        this.MedicineID = MedicineID;
        this.MedicineName = MedicineName;
        this.API = API;
        this.Quantity = Quantity;
        this.Unit = Unit;
        this.Subunitsperunit = Subunitsperunit;
        this.Subunit = Subunit;
        this.Itemspersubunit = Itemspersubunit;
        this.Item = Item;
        this.CostPrice = CostPrice;
        this.SellingPrice = SellingPrice;
    }

    public String getMedicineID() {
        return MedicineID;
    }

    public String getMedicineName() {
        return MedicineName;
    }

    public String getAPI() {
        return API;
    }

    public Integer getQuantity() {
        return Quantity;
    }

    public String getUnit() {
        return Unit;
    }

    public Integer getSubunitsperunit() {
        return Subunitsperunit;
    }

    public String getSubunit() {
        return Subunit;
    }

    public Integer getItemspersubunit() {
        return Itemspersubunit;
    }

    public String getItem() {
        return Item;
    }

    public Integer getCostPrice() {
        return CostPrice;
    }

    public Integer getSellingPrice() {
        return SellingPrice;
    }

}
