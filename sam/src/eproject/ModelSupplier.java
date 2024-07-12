/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eproject;

/**
 *
 * @author 3D
 */
public class ModelSupplier {
    
    private String SupplierID;
    private String SupplierName;
    private String Address;
    private String PhoneNumber;
    private String Email;

    public ModelSupplier(String SupplierID, String SupplierName, String Address, String PhoneNumber, String Email) {
        this.SupplierID = SupplierID;
        this.SupplierName = SupplierName;
        this.Address = Address;
        this.PhoneNumber = PhoneNumber;
        this.Email = Email;
    }

    public String getSupplierID() {
        return SupplierID;
    }

    public String getSupplierName() {
        return SupplierName;
    }

    public String getAddress() {
        return Address;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public String getEmail() {
        return Email;
    }
    
    
}
