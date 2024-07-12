/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package eproject;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.sql.*;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author 3D
 */
public class UpdateSupplierController implements Initializable {

    @FXML
    private TextField txtSupplierName;
    @FXML
    private Button btnCancle;
    @FXML
    private TextField txtPhone;
    @FXML
    private TextField txtAddress;
    @FXML
    private TextField txtEmail;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;
    @FXML
    private TextField txtSupplierID;

    private Connection conn;
    private PreparedStatement pst;
    private ResultSet rs;
    private SupplierController supplierController;
    private String supplierID;

    public void setSupplierController(SupplierController supplierController) {
        this.supplierController = supplierController;
    }
    
    public void setSupplierData(String supplierID, String supplierName, String phone, String address, String email) {
        this.supplierID = supplierID;
        txtSupplierID.setText(supplierID);
        txtSupplierID.setEditable(false);
        txtSupplierName.setText(supplierName);
        txtPhone.setText(phone);
        txtAddress.setText(address);
        txtEmail.setText(email);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        conn=DB.ConnectDB.getConnectDB();
    }    

    @FXML
    private void btn_Cancle(ActionEvent event) {
        Stage stage = (Stage) btnCancle.getScene().getWindow();
        stage.close();
    }
    
    private boolean validatePhone(String phone) {
        // Số điện thoại hợp lệ phải có 10 chữ số và bắt đầu bằng số 0
        return phone.matches("0[0-9]{9}");
    }

    private boolean validateEmail(String email) {
        // Email hợp lệ theo định dạng phổ biến
        return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void btn_update(ActionEvent event) {
        String supplierName = txtSupplierName.getText();
        String phone = txtPhone.getText();
        String address = txtAddress.getText();
        String email = txtEmail.getText();

        if (!validatePhone(phone)) {
            showAlert("Invalid Phone Number", "Phone number must be 10 digits and start with 0.");
            return;
        }

        if (!validateEmail(email)) {
            showAlert("Invalid Email Address", "Please enter a valid email address.");
            return;
        }

        String sql = "UPDATE Supplier SET SupplierName=?, PhoneNumber=?, Address=?, Email=? WHERE SupplierID=?";
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, supplierName);
            pst.setString(2, phone);
            pst.setString(3, address);
            pst.setString(4, email);
            pst.setString(5, supplierID);
            pst.executeUpdate();
            System.out.println("Supplier updated successfully!");

            // Cập nhật bảng trong SupplierController
            if (supplierController != null) {
                supplierController.showSList();
            }

            // Đóng cửa sổ hiện tại
            Stage stage = (Stage) btnUpdate.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void btn_delete(ActionEvent event) {
        String sql = "DELETE FROM Supplier WHERE SupplierID=?";
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, supplierID);
            pst.executeUpdate();
            System.out.println("Supplier deleted successfully!");

            // Cập nhật bảng trong SupplierController
            if (supplierController != null) {
                supplierController.showSList();
            }

            // Đóng cửa sổ hiện tại
            Stage stage = (Stage) btnDelete.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
}
