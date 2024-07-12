/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Register;

import java.io.IOException;
import java.net.URL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import java.time.Instant;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.util.regex.Pattern;

/**
 * FXML Controller class
 *
 * @author huypg
 */
public class RegisterController implements Initializable {

    //Choice Box
    @FXML
    private ImageView EYE;

    @FXML
    private ImageView EYE_SLASH;

    @FXML
    private Button btnEYE;

    @FXML
    private Button btnEYE_SLASH;

    @FXML
    private ImageView EYECF;

    @FXML
    private ImageView EYE_SLASHCF;

    @FXML
    private Button btnEYECF;

    @FXML
    private Button btnEYE_SLASHCF;

    @FXML
    private Button btnSignin;

    @FXML
    private Button btnSignup;

    @FXML
    private ComboBox<String> cbbRole;

    @FXML
    private Button close;

    @FXML
    private TextField txtFullname;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private PasswordField txtCFPassword;

    @FXML
    private TextField txtShowPassword;

    @FXML
    private TextField txtshowCFPassword;

    @FXML
    private TextField txtPhone;

    @FXML
    private TextField txtID;

    @FXML

    private boolean isPasswordVisible = false;
    private boolean isCFPasswordVisible = false;

    private Connection conn;
    private PreparedStatement prepare;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        conn = DB.ConnectDB.getConnectDB();
        cbbRole.getItems().addAll("Staff", "Manager");
        cbbRole.setValue("Staff");
        btnSignup.setOnAction(event -> handleRegisterButton());
        btnSignin.setOnAction(event -> ReturntoLogin());
        close.setOnAction(event -> handleCloseButton());
        //Password
        btnEYE.setOnAction(event -> togglePasswordVisibility());
        btnEYE_SLASH.setOnAction(event -> togglePasswordVisibility());
        EYE.setOnMouseClicked(event -> togglePasswordVisibility());
        EYE_SLASH.setOnMouseClicked(event -> togglePasswordVisibility());
        updatePasswordVisibility();
        //Confirm Password
        btnEYECF.setOnAction(event -> toggleCFPVisibility());
        btnEYE_SLASHCF.setOnAction(event -> toggleCFPVisibility());
        EYECF.setOnMouseClicked(event -> toggleCFPVisibility());
        EYE_SLASHCF.setOnMouseClicked(event -> toggleCFPVisibility());
        updateCFPVisibility();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public class ValidateRegister {

        public static boolean isNotEmpty(String input) {
            return input != null && !input.trim().isEmpty();
        }

        public static boolean isPasswordConfirmed(String password, String confirmPassword) {
            return isNotEmpty(password) && password.equals(confirmPassword);
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

    @FXML
    private void handleRegisterButton() {
        saveUserData();
    }

    @FXML
    private void handleCloseButton() {
        System.exit(0);
    }

    public void ReturntoLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login/Login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) close.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {

        }
    }

    public void saveUserData() {
        String password = txtPassword.getText();
        String email = txtEmail.getText();
        String phone = txtPhone.getText();
        String address = txtAddress.getText();
        String fullname = txtFullname.getText();
        String role = cbbRole.getValue(); // Get the selected value from ComboBox
      

        if (!ValidateRegister.isNotEmpty(txtFullname.getText())) {
            showAlert("Validation Error", "Fullname cannot be empty");
            return;
        }

        if (!ValidateRegister.isNotEmpty(txtPassword.getText())) {
            showAlert("Validation Error", "Password cannot be empty");
            return;
        }

        if (!ValidateRegister.isPasswordConfirmed(txtPassword.getText(), txtCFPassword.getText())) {
            showAlert("Validation Error", "Confirm Password must match Password and cannot be empty");
            return;
        }

        if (!ValidateRegister.isNotEmpty(txtPhone.getText())) {
            showAlert("Validation Error", "Phone cannot be empty");
            return;
        }

        if (!ValidateRegister.isNumeric(txtPhone.getText())) {
            showAlert("Validation Error", "Phone must be a number");
            return;
        }
        if (isPhoneAlreadyExists(txtPhone.getText())) {
            showAlert("Validation Error", "Phone already exists in the database");
            return;
        }

        

        if (!ValidateRegister.isValidEmail(txtEmail.getText())) {
            showAlert("Validation Error", "Email is not valid");
            return;
        }

        if (isEmailAlreadyExists(txtEmail.getText())) {
            showAlert("Validation Error", "Email already exists in the database");
            return;
        }
        if (!ValidateRegister.isNotEmpty(txtAddress.getText())) {
            showAlert("Validation Error", "Address cannot be empty");
            return;
        }
        

        // Additional input validation can be added here
        Timestamp dateCreated = Timestamp.from(Instant.now());

        try {
            conn = DB.ConnectDB.getConnectDB();
            // Insert into Login table
            String sqlLogin = "INSERT INTO Login (Username, Password) VALUES (?, ?)";
            prepare = conn.prepareStatement(sqlLogin);
            prepare.setString(1, phone);
            prepare.setString(2, password);
            prepare.executeUpdate();
            
            
            // Insert into Information table
            String sqlInformation = "INSERT INTO Information ( Fullname, Phone, Email, Address, Date_Created) VALUES ( ?, ?, ?, ?, ?)";
            prepare = conn.prepareStatement(sqlInformation);
            prepare.setString(1, fullname);
            prepare.setString(2, phone);
            prepare.setString(3, email);
            prepare.setString(4, address);
            prepare.setTimestamp(5, dateCreated);
            prepare.executeUpdate();

            // Insert into Role table
            String sqlRole = "INSERT INTO Role (Role, Username) VALUES (?, ?)";
            prepare = conn.prepareStatement(sqlRole);
            prepare.setString(1, role);
            prepare.setString(2, phone);
            prepare.executeUpdate();

            

            System.out.println("Data saved successfully!");

            // Optionally switch to another scene or give feedback to the user
            showAlert("SuccessFully", "Congratulation!!");
            ReturntoLogin(); // Example function to return to login screen
            
        } catch (SQLException e) {
            // Log detailed SQL exception for troubleshooting
            // Handle SQL exception properly in your application (e.g., show error message to user)
            System.out.println(""+e.getMessage());
        } finally {
            try {
                if (prepare != null) {
                    prepare.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                // Log detailed exception for resource closing issues
                // Handle closing exception properly in your application

            }
        }
    }


    private boolean isIdAlreadyExists(String id) {
        try (Connection conn = DB.ConnectDB.getConnectDB()) {
            String sqlCheckId = "SELECT COUNT(*) FROM Information WHERE ID = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlCheckId)) {
                stmt.setString(1, id);
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

    private boolean isPhoneAlreadyExists(String phone) {
        try (Connection conn = DB.ConnectDB.getConnectDB()) {
            String sqlCheckPhone = "SELECT COUNT(*) FROM Information WHERE Phone = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlCheckPhone)) {
                stmt.setString(1, phone);
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

    private boolean isEmailAlreadyExists(String email) {
        try (Connection conn = DB.ConnectDB.getConnectDB()) {
            String sqlCheckEmail = "SELECT COUNT(*) FROM Information WHERE BINARY  Email = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlCheckEmail)) {
                stmt.setString(1, email);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        int count = rs.getInt(1);
                        return count > 0;
                    }
                }
            }
        } catch (SQLException e) {
        }
        return false;
    }

    // Show Password
    @FXML
    public void togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible;
        updatePasswordVisibility();
    }

    public void updatePasswordVisibility() {
        if (isPasswordVisible) {
            // Show password
            txtPassword.setVisible(false);
            txtShowPassword.setVisible(true);
            txtShowPassword.setText(txtPassword.getText());
            btnEYE.setVisible(false);
            btnEYE_SLASH.setVisible(true);
        } else {
            // Hide password
            txtPassword.setVisible(true);
            txtShowPassword.setVisible(false);
            btnEYE.setVisible(true);
            btnEYE_SLASH.setVisible(false);
        }
    }

    //End Show Password
    // Show Confirm Password
    @FXML
    public void toggleCFPVisibility() {
        isCFPasswordVisible = !isCFPasswordVisible;
        updateCFPVisibility();
    }

    public void updateCFPVisibility() {
        if (isCFPasswordVisible) {
            // Show password
            txtCFPassword.setVisible(false);
            txtshowCFPassword.setVisible(true);
            txtshowCFPassword.setText(txtCFPassword.getText());
            btnEYECF.setVisible(false);
            btnEYE_SLASHCF.setVisible(true);
        } else {
            // Hide password
            txtCFPassword.setVisible(true);
            txtshowCFPassword.setVisible(false);
            btnEYECF.setVisible(true);
            btnEYE_SLASHCF.setVisible(false);
        }
    }

    //End Show Confirm Password
}
