/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package demoproject1;

import java.util.Date;

/**
 *
 * @author nguye
 */
public class TableNhanvien {
    private  String StaffID;


    private  String Staffname;
    
    private String Gender;

    private  String Address;

    private  String Numberphone;

    private  Date Birthday;

    private  String Gmail;

    private  String Image;
            
    
    private String Status;
    
    private Integer CardNumber;

    public TableNhanvien(String StaffID, String Staffname, String Gender, String Address, String Numberphone, Date Birthday, String Gmail, String Image, String Status, Integer CardNumber) {
        this.StaffID = StaffID;
        this.Staffname = Staffname;
        this.Gender = Gender;
        this.Address = Address;
        this.Numberphone = Numberphone;
        this.Birthday = Birthday;
        this.Gmail = Gmail;
        this.Image = Image;
        this.Status = Status;
        this.CardNumber = CardNumber;
    }

    public String getStaffID() {
        return StaffID;
    }

    public void setStaffID(String StaffID) {
        this.StaffID = StaffID;
    }

    public String getStaffname() {
        return Staffname;
    }

    public void setStaffname(String Staffname) {
        this.Staffname = Staffname;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String Gender) {
        this.Gender = Gender;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public String getNumberphone() {
        return Numberphone;
    }

    public void setNumberphone(String Numberphone) {
        this.Numberphone = Numberphone;
    }

    public Date getBirthday() {
        return Birthday;
    }

    public void setBirthday(Date Birthday) {
        this.Birthday = Birthday;
    }

    public String getGmail() {
        return Gmail;
    }

    public void setGmail(String Gmail) {
        this.Gmail = Gmail;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String Image) {
        this.Image = Image;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public Integer getCardNumber() {
        return CardNumber;
    }

    public void setCardNumber(Integer CardNumber) {
        this.CardNumber = CardNumber;
    }

  

   

    
      
}

    

    

    

    

