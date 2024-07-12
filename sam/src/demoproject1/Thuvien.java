/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package demoproject1;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


public class Thuvien {
    public  void showAlert(String noidung){
        Alert  alert=new Alert(AlertType.INFORMATION);
        alert.setTitle("Thong bao");
        alert.setHeaderText(null);
        alert.setContentText(noidung);
        alert.showAndWait();    
    }
}
