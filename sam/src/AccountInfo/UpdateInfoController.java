/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package AccountInfo;

import Data.ControllerHolder;
import Data.Data;
import Data.Message;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Admin
 */
public class UpdateInfoController implements Initializable {

     @FXML
    private Button btnCancelUpdate;

    @FXML
    private Button btnUpdate;

    @FXML
    private Button btnUpdateClose;

    @FXML
    private ComboBox<String> cbRole;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtFullName;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtPhone;
    
      @FXML
    private TextField txtEmail;

    @FXML
    void Cancelbtn(ActionEvent event) {
        updateDruglistForm();
        Stage stage = (Stage) btnCancelUpdate.getScene().getWindow();
        stage.close();
    }

    @FXML
    void Closebtn(ActionEvent event) {
        updateDruglistForm();
        Stage stage = (Stage) btnUpdateClose.getScene().getWindow();
        stage.close();
    }

    
    
    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;
    Message alert = new Message();
    
    public void setField() {
        txtFullName.setText(Data.al_fullname);
        txtAddress.setText(Data.al_address);
        txtPhone.setText(Data.al_phone);
        txtPassword.setText(Data.al_password);
        cbRole.setValue(Data.al_role);
        txtEmail.setText(Data.al_email);
    }
    
    public class ValidateRegister {

        public static boolean isNotEmpty(String input) {
            return input != null && !input.trim().isEmpty();
        }

        
        public static boolean isNumeric(String input) {
            return input != null && input.matches("\\d+");
        }

        public static boolean isValidEmail(String email) {
            String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
            Pattern pat = Pattern.compile(emailRegex);
            return email != null && pat.matcher(email).matches();
        }
    }
    
    private boolean isPhoneAlreadyExists(String phone, int currentUserId) {
    try (Connection conn = DB.ConnectDB.getConnectDB()) {
        String sqlCheckPhone = "SELECT COUNT(*) FROM Information WHERE Phone = ? AND ID <> ?";
        try (PreparedStatement stmt = conn.prepareStatement(sqlCheckPhone)) {
            stmt.setString(1, phone);
            stmt.setInt(2, currentUserId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}

    private boolean isEmailAlreadyExists(String email, int currentUserId) {
    try (Connection conn = DB.ConnectDB.getConnectDB()) {
        String sqlCheckEmail = "SELECT COUNT(*) FROM Information WHERE Email = ? AND ID <> ?";
        try (PreparedStatement stmt = conn.prepareStatement(sqlCheckEmail)) {
            stmt.setString(1, email);
            stmt.setInt(2, currentUserId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}
    
    public void updatebtn() {
         Integer ID = Data.al_ID;
        if (txtFullName.getText().isEmpty()
                || txtAddress.getText().isEmpty()
                || txtPhone.getText().isEmpty()
                || txtPassword.getText().isEmpty()
                || cbRole.getValue() == null
                || txtEmail.getText().isEmpty()) {
            alert.errorMessage("Please fill all blank fields");
        } 
        else if (!ValidateRegister.isNumeric(txtPhone.getText())) {
            alert.errorMessage( "Phone must be a number");
            
        }
        if (isPhoneAlreadyExists(txtPhone.getText(), ID)) {
            alert.errorMessage( "Phone already exists in the database");
          
        }
         if (!ValidateRegister.isValidEmail(txtEmail.getText())) {
            alert.errorMessage( "Email is not valid");
            
        }

        if (isEmailAlreadyExists(txtEmail.getText(), ID)) {
            alert.errorMessage( "Email already exists in the database");
           
        }
            String fullname = txtFullName.getText();
            String address = txtAddress.getText();
            String email = txtEmail.getText();
            String password = txtPassword.getText();
            String phone = txtPhone.getText();
            String role = cbRole.getValue();
             String sqlUpdateInformation = "UPDATE Information SET FullName = ?, Address = ?, Email = ?, Phone = ? WHERE ID = ?";
            conn = DB.ConnectDB.getConnectDB();
            try {
                if (alert.confirmMessage("Are you sure want to update User: " + phone + "?")) {
                try(PreparedStatement psInfo = conn.prepareStatement(sqlUpdateInformation)){
                    
                    ps = conn.prepareStatement(sqlUpdateInformation);
                    ps.setString(1, fullname);
                    ps.setString(2, address);
                    ps.setString(3, email);
                    
                    ps.setString(4, phone);
                    ps.setInt(5, ID);
                    
                    
                    ps.executeUpdate();
                }
                     String sqlUpdateRole = "UPDATE Role SET Role = ?, Username=?  WHERE Username = ?";
            try (PreparedStatement psRole = conn.prepareStatement(sqlUpdateRole)) {
                psRole.setString(1, role);
                psRole.setString(2, phone);
                psRole.setString(3, phone);
                psRole.executeUpdate();
            }

            String currentRmbPassword = getRmbPassword(phone);

            if (currentRmbPassword != null && !currentRmbPassword.trim().isEmpty()) {
                String sqlUpdateLogin = "UPDATE Login SET Password = ?, Username = ?, RmbPassword = ? WHERE Username = ?";
                try (PreparedStatement psLogin = conn.prepareStatement(sqlUpdateLogin)) {
                    psLogin.setString(1, password);
                    psLogin.setString(2, phone);
                    psLogin.setString(3, password);
                    psLogin.setString(4, phone);
                    psLogin.executeUpdate();
                }
            } else {
                String sqlUpdateLogin = "UPDATE Login SET Password = ?, Username = ? WHERE Username = ?";
                try (PreparedStatement psLogin = conn.prepareStatement(sqlUpdateLogin)) {
                    psLogin.setString(1, password);
                    psLogin.setString(2, phone);
                    psLogin.setString(3, phone);
                    psLogin.executeUpdate();
                }
            }
                    alert.successMessage("Update successfully");
                    Stage stage = (Stage) btnUpdate.getScene().getWindow();
                    stage.close();
                    updateDruglistForm();
                } else {
                    alert.errorMessage("Failed to update drug");
                }

            } catch (Exception e) {
                e.printStackTrace();
                alert.errorMessage("Error adding drug: " + e.getMessage());
            }
        }
    
    private String getRmbPassword(String username) {
    String rmbPassword = null;
    String sqlFetchRmbPassword = "SELECT RmbPassword FROM Login WHERE Username = ?";
    try (Connection conn = DB.ConnectDB.getConnectDB();
         PreparedStatement ps = conn.prepareStatement(sqlFetchRmbPassword)) {
        ps.setString(1, username);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                rmbPassword = rs.getString("rmbPassword");
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return rmbPassword;
}
    
    private void updateDruglistForm() {

        AccountInfoController accountinfoController = ControllerHolder.getInstance().getAccountinfoController();
        if (accountinfoController != null) {
            accountinfoController.showData();
            accountinfoController.Pagination();
        }
    }
    
    public void RoleList() {
        List<String> ListS = new ArrayList<>();

        for (String data : Data.role) {
            ListS.add(data);
        }
        ObservableList ListData = FXCollections.observableArrayList(ListS);
        cbRole.setItems(ListData);
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        RoleList();
        setField();
    }    
    
}
