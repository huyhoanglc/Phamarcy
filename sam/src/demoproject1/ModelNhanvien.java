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
public class ModelNhanvien {
    private  String Manhanvien;

    private  String Password;

    private  String Tennhanvien;

    private  Integer Gioitinh;

    private  String Diachi;

    private  Integer Dienthoai;

    private  Date Ngaysinh;

    private  String Email;

    private  String Hinh;

    public ModelNhanvien(String Manhanvien, String Password, String Tennhanvien, Integer Gioitinh, String Diachi, Integer Dienthoai, Date Ngaysinh, String Email, String Hinh) {
        this.Manhanvien = Manhanvien;
        this.Password = Password;
        this.Tennhanvien = Tennhanvien;
        this.Gioitinh = Gioitinh;
        this.Diachi = Diachi;
        this.Dienthoai = Dienthoai;
        this.Ngaysinh = Ngaysinh;
        this.Email = Email;
        this.Hinh = Hinh;
    }

    public String getManhanvien() {
        return Manhanvien;
    }

    public String getPassword() {
        return Password;
    }

    public String getTennhanvien() {
        return Tennhanvien;
    }

    public Integer getGioitinh() {
        return Gioitinh;
    }

    public String getDiachi() {
        return Diachi;
    }

    public Integer getDienthoai() {
        return Dienthoai;
    }

    public Date getNgaysinh() {
        return Ngaysinh;
    }

    public String getEmail() {
        return Email;
    }

    public String getHinh() {
        return Hinh;
    }
    
    
}
