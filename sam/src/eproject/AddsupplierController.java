/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package eproject;

import java.net.URL;
import java.sql.Connection;
import java.sql.*;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author 3D
 */
public class AddsupplierController implements Initializable {

    @FXML
    private TextField txtSupplierName;
    @FXML
    private Button BtnAdd;
    @FXML
    private Button BtnCancle;
    @FXML
    private TextField txtPhone;
    @FXML
    private TextField txtAddress;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtSupplierID;
    
    private Connection conn;
    private PreparedStatement pst;
    private ResultSet rs;
    private Statement stm;
    
    private SupplierController supplierController;

    private String SuppliertID;
    public String getHighestSupplierID() {
        String highestID = "S0000"; // Default value if there are no records
        String sql = "SELECT MAX(SupplierID) AS HighestID FROM Supplier";
        try {
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                highestID = rs.getString("HighestID");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return highestID;
    }

    public String generateNextSupplierID() {
    String highestID = getHighestSupplierID();
    int number = 1; // Mặc định bắt đầu từ số 1 nếu không có bản ghi nào tồn tại
    if (highestID != null && highestID.length() > 1) {
        number = Integer.parseInt(highestID.substring(1)) + 1;
    }
    return String.format("S%04d", number);
}
    
    private boolean validatePhone(String phone) {
        // Số điện thoại hợp lệ phải có 10 chữ số và bắt đầu bằng số 0
        return phone.matches("0[0-9]{9}");
    }

    private boolean validateEmail(String email) {
        // Email hợp lệ theo định dạng phổ biến
        return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
    }
    
    private boolean isPhoneExists(String phone) {
        String sql = "SELECT COUNT(*) FROM Supplier WHERE PhoneNumber = ?";
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, phone);
            rs = pst.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    private boolean isEmailExists(String email) {
        String sql = "SELECT COUNT(*) FROM Supplier WHERE Email = ?";
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, email);
            rs = pst.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        conn=DB.ConnectDB.getConnectDB();
        String newSupplierID = generateNextSupplierID();
        txtSupplierID.setText(newSupplierID);
        txtSupplierID.setEditable(false);
    }   
    
    public void setSupplierController(SupplierController supplierController) {
        this.supplierController = supplierController;
    }
    
    @FXML
    private void btn_add(ActionEvent event) {
        if (!validatePhone(txtPhone.getText())) {
            showAlert("Invalid Phone Number", "Phone number must be 10 digits and start with 0.");
            return;
        }

        if (!validateEmail(txtEmail.getText())) {
            showAlert("Invalid Email Address", "Please enter a valid email address.");
            return;
        }
        
        if (isPhoneExists(txtPhone.getText())) {
            showAlert("Phone Number Exists", "The phone number already exists. Please enter a different phone number.");
            return;
        }

        if (isEmailExists(txtEmail.getText())) {
            showAlert("Email Address Exists", "The email address already exists. Please enter a different email address.");
            return;
        }

        String sql = "INSERT INTO Supplier (SupplierID, SupplierName, PhoneNumber, Address, Email) VALUES (?, ?, ?, ?, ?)";
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, txtSupplierID.getText());
            pst.setString(2, txtSupplierName.getText());
            pst.setString(3, txtPhone.getText());
            pst.setString(4, txtAddress.getText());
            pst.setString(5, txtEmail.getText());
            pst.executeUpdate();
            System.out.println("Supplier added successfully!");
            
            if (supplierController != null) {
                supplierController.showSList();;
            }
            
            Stage stage = (Stage) BtnCancle.getScene().getWindow();
            stage.close();
            
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        private void showAlert(String title, String message) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
    }

    @FXML
    private void btn_cancle(ActionEvent event) {
        Stage stage = (Stage) BtnCancle.getScene().getWindow();
        stage.close();
    }
    
}
