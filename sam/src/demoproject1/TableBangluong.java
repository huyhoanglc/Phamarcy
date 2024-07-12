/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package demoproject1;

import java.math.BigDecimal;
import java.util.Date;
/**
 *
 * @author nguye
 */
public class TableBangluong {
    private  Integer ID;
    private  String StaffID;
    private  Date  PayPeriodStart;
    private  Date  PayPeriodEnd;
    private  Integer  StandardDays;
    private  Integer  StandardHours;
    private  BigDecimal  TotalWorkDays;
    private  BigDecimal  Overtime;
    private  BigDecimal  TotalPay;
    private  BigDecimal  Salary;

    public TableBangluong(Integer ID, String StaffID, Date PayPeriodStart, Date PayPeriodEnd, Integer StandardDays, Integer StandardHours, BigDecimal TotalWorkDays, BigDecimal Overtime, BigDecimal TotalPay, BigDecimal Salary) {
        this.ID = ID;
        this.StaffID = StaffID;
        this.PayPeriodStart = PayPeriodStart;
        this.PayPeriodEnd = PayPeriodEnd;
        this.StandardDays = StandardDays;
        this.StandardHours = StandardHours;
        this.TotalWorkDays = TotalWorkDays;
        this.Overtime = Overtime;
        this.TotalPay = TotalPay;
        this.Salary = Salary;
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

    public Date getPayPeriodStart() {
        return PayPeriodStart;
    }

    public void setPayPeriodStart(Date PayPeriodStart) {
        this.PayPeriodStart = PayPeriodStart;
    }

    public Date getPayPeriodEnd() {
        return PayPeriodEnd;
    }

    public void setPayPeriodEnd(Date PayPeriodEnd) {
        this.PayPeriodEnd = PayPeriodEnd;
    }

    public Integer getStandardDays() {
        return StandardDays;
    }

    public void setStandardDays(Integer StandardDays) {
        this.StandardDays = StandardDays;
    }

    public Integer getStandardHours() {
        return StandardHours;
    }

    public void setStandardHours(Integer StandardHours) {
        this.StandardHours = StandardHours;
    }

    public BigDecimal getTotalWorkDays() {
        return TotalWorkDays;
    }

    public void setTotalWorkDays(BigDecimal TotalWorkDays) {
        this.TotalWorkDays = TotalWorkDays;
    }

    public BigDecimal getOvertime() {
        return Overtime;
    }

    public void setOvertime(BigDecimal Overtime) {
        this.Overtime = Overtime;
    }

    public BigDecimal getTotalPay() {
        return TotalPay;
    }

    public void setTotalPay(BigDecimal TotalPay) {
        this.TotalPay = TotalPay;
    }

    public BigDecimal getSalary() {
        return Salary;
    }

    public void setSalary(BigDecimal Salary) {
        this.Salary = Salary;
    }

    

    

}