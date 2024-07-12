/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Data;

import java.time.LocalDateTime;

/**
 *
 * @author Admin
 */
public class AccountData {
    private Integer ID;
    private String FullName;
    private String Email;
    private String Phone;
    private String Address;
    private String Password;
    private String Role;
    private LocalDateTime DateCreated;

    public AccountData(Integer ID, String FullName, String Email, String Phone, String Address, String Password, String Role, LocalDateTime DateCreated) {
        this.ID = ID;
        this.FullName = FullName;
        this.Email = Email;
        this.Phone = Phone;
        this.Address = Address;
        this.Password = Password;
        this.Role = Role;
        this.DateCreated = DateCreated;
    }

    public Integer getID() {
        return ID;
    }

    public String getFullName() {
        return FullName;
    }

    public String getEmail() {
        return Email;
    }

    public String getPhone() {
        return Phone;
    }

    public String getAddress() {
        return Address;
    }

    public String getPassword() {
        return Password;
    }

    public String getRole() {
        return Role;
    }

    public LocalDateTime getDateCreated() {
        return DateCreated;
    }

    
            
            
}
