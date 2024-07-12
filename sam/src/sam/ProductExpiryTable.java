/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sam;

import java.time.LocalDate;

/**
 *
 * @author ADMIN
 */
public class ProductExpiryTable {
     private String medicineID;
    private String medicineName;
    private LocalDate manufacturingDate;
    private LocalDate expireDate;
    private long daysLeft;

    // Constructors
    public ProductExpiryTable(String medicineID, String medicineName, LocalDate manufacturingDate, LocalDate expireDate, long daysLeft) {    
        this.medicineID = medicineID;
        this.medicineName = medicineName;
        this.manufacturingDate = manufacturingDate;
        this.expireDate = expireDate;
        this.daysLeft = daysLeft;
    }

    // Getters and Setters

    public String getMedicineID() {
        return medicineID;
    }

    public void setMedicineID(String medicineID) {
        this.medicineID = medicineID;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public LocalDate getManufacturingDate() {
        return manufacturingDate;
    }

    public void setManufacturingDate(LocalDate manufacturingDate) {
        this.manufacturingDate = manufacturingDate;
    }

    public LocalDate getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(LocalDate expireDate) {
        this.expireDate = expireDate;
    }

    public long getDaysLeft() {
        return daysLeft;
    }

    public void setDaysLeft(long daysLeft) {
        this.daysLeft = daysLeft;
    }
    
}
