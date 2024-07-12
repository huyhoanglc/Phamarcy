/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Data;

import java.time.LocalDate;
import java.util.Date;

/**
 *
 * @author Admin
 */
public class MedicineDetailsData {

    private Integer ID;
    private String MedicineID;
    private String MedicineName;
    private String Barcode;
    private String BatchNo;
    private Integer Quantity1;
    private String Unit1;
    private Integer Quantity2;
    private String Unit2;
    private Integer Quantity3;
    private String Unit3;
    private LocalDate ManufacturingDate;
    private LocalDate ExpireDate;
    private String Status;

    public MedicineDetailsData(Integer ID, String MedicineID, String MedicineName, String Barcode, String BatchNo, Integer Quantity1, String Unit1, Integer Quantity2, String Unit2, Integer Quantity3, String Unit3, LocalDate ManufacturingDate, LocalDate ExpireDate, String Status) {
        this.ID = ID;
        this.MedicineID = MedicineID;
        this.MedicineName = MedicineName;
        this.Barcode = Barcode;
        this.BatchNo = BatchNo;
        this.Quantity1 = Quantity1;
        this.Unit1 = Unit1;
        this.Quantity2 = Quantity2;
        this.Unit2 = Unit2;
        this.Quantity3 = Quantity3;
        this.Unit3 = Unit3;
        this.ManufacturingDate = ManufacturingDate;
        this.ExpireDate = ExpireDate;
        this.Status = Status;
    }

    public Integer getID() {
        return ID;
    }

    public String getMedicineID() {
        return MedicineID;
    }

    public String getMedicineName() {
        return MedicineName;
    }

    public String getBarcode() {
        return Barcode;
    }

    public String getBatchNo() {
        return BatchNo;
    }

    public Integer getQuantity1() {
        return Quantity1;
    }

    public String getUnit1() {
        return Unit1;
    }

    public Integer getQuantity2() {
        return Quantity2;
    }

    public String getUnit2() {
        return Unit2;
    }

    public Integer getQuantity3() {
        return Quantity3;
    }

    public String getUnit3() {
        return Unit3;
    }

    public LocalDate getManufacturingDate() {
        return ManufacturingDate;
    }

    public LocalDate getExpireDate() {
        return ExpireDate;
    }

    public String getStatus() {
        return Status;
    }

}
