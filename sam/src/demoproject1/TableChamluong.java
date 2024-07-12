/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package demoproject1;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author nguye
 */
public class TableChamluong {
    
    private Integer ID;
    
    private  String StaffID;

    private  String Staffname;
    
    private Date Day;
    
    private Date Checkin;
    
    private Date Checkout;
    

    public TableChamluong(Integer ID, String StaffID, String Staffname, Date Day, Date Checkin, Date Checkout) {
        this.ID = ID;
        this.StaffID = StaffID;
        this.Staffname = Staffname;
        this.Day = Day;
        this.Checkin = Checkin;
        this.Checkout = Checkout;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
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

    public Date getDay() {
        return Day;
    }

    public void setDay(Date Day) {
        this.Day = Day;
    }

    public Date getCheckin() {
        return Checkin;
    }

    public void setCheckin(Date Checkin) {
        this.Checkin = Checkin;
    }

    public Date getCheckout() {
        return Checkout;
    }

    public void setCheckout(Date Checkout) {
        this.Checkout = Checkout;
    }


    
    }

    
    

    
    
    
    
    

