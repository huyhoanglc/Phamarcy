/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Login;

import demoproject1.FXMLNhanVienController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import java.sql.Timestamp;
import java.time.Instant;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.sql.*;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import sam.FXMLMainController;
import sam.FXMLStaffController;
import sam.Sam;

/**
 *
 * @author WINDOWS 10
 */
public class LoginController implements Initializable {
    private FXMLNhanVienController nhanvienController;
    private FXMLMainController mainController;
    private FXMLStaffController staffController;

    public void setMainController(FXMLMainController mainController) {
        this.mainController = mainController;
    }
    @FXML
    private TextField username;

    @FXML
    private Button btnlogin;
    @FXML
    private Button btnsignup;

    @FXML
    private Button btnclose;
    @FXML
    private CheckBox ckremember;
    @FXML
    private ImageView EYE;

    @FXML
    private ImageView EYE_SLASH;
    @FXML
    private Button btnEYE;

    @FXML
    private Button btnEYE_SLASH;
    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField showpasswordField;
    @FXML
    private Hyperlink forgotpassword;
    private boolean isPasswordVisible = false;
//================================================
    // Connect Database
    /**
     *
     */
    
    private Connection conn;
    private PreparedStatement prepare;
    private ResultSet rs;

//================================================
    private static final String UPDATE_RMBPASSWORD_SQL = "UPDATE Login SET RmbPassword = ? WHERE Username = ?";
    private static final String SELECT_RMBPASSWORD_SQL = "SELECT RmbPassword FROM Login WHERE Username = ?";

    public LoginController() {
         this.conn = DB.ConnectDB.getConnectDB();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        btnclose.setOnAction(event -> btnclose());
        //Show/Hide Password
        btnEYE.setOnAction(event -> togglePasswordVisibility());
        btnEYE_SLASH.setOnAction(event -> togglePasswordVisibility());
        EYE.setOnMouseClicked(event -> togglePasswordVisibility());
        EYE_SLASH.setOnMouseClicked(event -> togglePasswordVisibility());
        updatePasswordVisibility();
        forgotpassword.setOnAction(event -> ForgotPassword());

        btnsignup.setOnAction(event -> CreateAdminAccount());
        btnlogin.setOnAction(event -> {
            try {
                login();
            } catch (IOException | SQLException ex) { // Print the exception for debugging purposes
                // Print the exception for debugging purposes
                AlertHelper.errorMessage("Error during login: " + ex.getMessage());
            }
        });

        passwordField.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            loadPassword();

        });
        passwordField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) { // Khi focus được nhận
                loadPassword();
                resetRemember();
            }
        });

        showpasswordField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (passwordField.getText() == null  || !passwordField.getText().equals(newValue)) {
                passwordField.setText(newValue);
            }
        });
    }
    private String fullName = "";

    public void login() throws IOException, SQLException {
        try {
            if (username.getText().isEmpty()) {
                AlertHelper.errorMessage("Username cannot be empty");
                return;
            }

            if (passwordField.getText().isEmpty()) {
                AlertHelper.errorMessage("Password cannot be empty");
                return;
            }

            // Verify username and password
            String selectLogin = "SELECT Username FROM Login WHERE Username = ? AND Password = ?";
            conn = DB.ConnectDB.getConnectDB();
            prepare = conn.prepareStatement(selectLogin);
            prepare.setString(1, username.getText());
            prepare.setString(2, passwordField.getText());
            rs = prepare.executeQuery();

            if (rs.next()) {
                // Username and password match, now fetch the role
                String selectRole = "SELECT Role FROM Role WHERE Username = ?";
                prepare = conn.prepareStatement(selectRole);
                prepare.setString(1, username.getText());
                rs = prepare.executeQuery();

                if (rs.next()) {
                    String role = rs.getString("Role");
                    String fxmlFile = "";

                    if (!"Manager".equals(role)) {
                        if ("Staff".equals(role)) {
                            fxmlFile = "/sam/FXMLStaff.fxml";
                        } else {
                            AlertHelper.errorMessage("Unknown role: " + role);
                            return;
                        }
                    } else {
                        fxmlFile = "/sam/FXMLMain.fxml";
                    }
                    // lấy thông tin username
                    String selectUserInfo = "SELECT Fullname FROM Information WHERE Phone = ?";
                    prepare = conn.prepareStatement(selectUserInfo);
                    prepare.setString(1, username.getText());   
                    rs = prepare.executeQuery();

                    if (rs.next()) {
                        fullName = rs.getString("Fullname");
                    }

                    if (ckremember.isSelected()) {
                        savePassword(username.getText(), passwordField.getText());
                    } else {
                        clearRMPassword(username.getText());
                    }

//                    // Update login time
//                    String updateLoginTime = "UPDATE Login SET Login_Time = ? WHERE Username = ?";
//                    prepare = conn.prepareStatement(updateLoginTime);
//                    prepare.setTimestamp(1, Timestamp.from(Instant.now()));
//                    prepare.setString(2, username.getText());
//                    prepare.executeUpdate();
                    // Load the appropriate dashboard scene
                    FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
                    Parent root = loader.load();
                    Scene scene = new Scene(root);
                    String cssPath = getClass().getResource("/sam/button-style.css").toExternalForm();
                    scene.getStylesheets().add(cssPath);

                    Stage stage = (Stage) username.getScene().getWindow(); // Get the current stage

                    stage.setResizable(true);

                    if("Staff".equals(role)){
                        FXMLStaffController staffController = loader.getController();
                    staffController.setFullName(fullName);
                    
                    }else{
                        FXMLMainController mainController = loader.getController();
                    mainController.setFullName(fullName);
                    }

                    Sam appInstance = new Sam();
                    appInstance.makeWindowDraggable(stage, root);

                    stage.setScene(scene);
                    stage.centerOnScreen();
                    stage.show();
                } else {
                    AlertHelper.errorMessage("Role not found for the username");
                }
            } else {
                AlertHelper.errorMessage("Incorrect username or password");
            }
        } catch (SQLException e) {
            AlertHelper.errorMessage("Database error: " + e.getMessage());
        } finally {
            closeResources(rs, prepare, conn);
        }
    }


    private void closeResources(ResultSet rs, PreparedStatement prepare, Connection conn) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (prepare != null) {
                prepare.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
        }
    }

    // RMP Function
    //
    private void resetRemember() {
        if (isPasswordRemembered()) {
            ckremember.setSelected(true);
        } else {
            ckremember.setSelected(false);
        }
    }

    private boolean isPasswordRemembered() {
        try (Connection conn = DB.ConnectDB.getConnectDB()) {
            if (conn == null) {
                System.out.println("Connection is null. Cannot check password.");
                return false;
            }

            String usernameText = username.getText();
            if (usernameText == null || usernameText.isEmpty()) {
                System.out.println("Username field is empty.");
                return false;
            }

            try (PreparedStatement selectStmt = conn.prepareStatement(SELECT_RMBPASSWORD_SQL)) {
                selectStmt.setString(1, usernameText);
                try (ResultSet rs = selectStmt.executeQuery()) {
                    if (rs.next()) {
                        String rmbPasswordText = rs.getString("RmbPassword");
                        return rmbPasswordText != null && !rmbPasswordText.isEmpty();
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL error: " + e.getMessage());
        }
        return false;
    }

    private void savePassword(String username, String password) {
        try (Connection conn = DB.ConnectDB.getConnectDB()) {
            if (conn == null) {
                System.out.println("Connection is null. Cannot save password.");
                return;
            }

            String query = UPDATE_RMBPASSWORD_SQL;
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, password);
                stmt.setString(2, username);
                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Password remembered successfully.");
                } else {
                    System.out.println("No user found with the specified username.");
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
    }

    private void clearRMPassword(String username) {
        try (Connection conn = DB.ConnectDB.getConnectDB()) {
            if (conn == null) {
                System.out.println("Connection is null. Cannot clear password.");
                return;
            }

            String query = "UPDATE Login SET RmbPassword = NULL WHERE Username = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, username);
                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Password cleared successfully.");
                } else {
                    System.out.println("No user found with the specified username.");
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
    }

    private void loadPassword() {
        try (Connection conn = DB.ConnectDB.getConnectDB()) {
            if (conn == null) {
                System.out.println("Connection is null. Cannot load password.");
                return;
            }

            String usernameText = username.getText();
            if (usernameText == null || usernameText.isEmpty()) {
                System.out.println("Username field is empty.");
                return;
            }

            try (PreparedStatement selectStmt = conn.prepareStatement(SELECT_RMBPASSWORD_SQL)) {
                selectStmt.setString(1, usernameText);
                try (ResultSet rs = selectStmt.executeQuery()) {
                    if (rs.next()) {
                        String rmbPasswordText = rs.getString("rmbpassword");
                        passwordField.setText(rmbPasswordText);
                        showpasswordField.setText(rmbPasswordText);
                        ckremember.setSelected(true);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL error: " + e.getMessage());
        }
    }

    // End RMP Fucntion
    // Show Password
    @FXML
    public void togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible;
        updatePasswordVisibility();
    }

    public void updatePasswordVisibility() {
        if (isPasswordVisible) {
            // Show password
            passwordField.setVisible(false);
            showpasswordField.setVisible(true);
            showpasswordField.setText(passwordField.getText());
            btnEYE.setVisible(false);
            btnEYE_SLASH.setVisible(true);
        } else {
            // Hide password
            passwordField.setVisible(true);
            showpasswordField.setVisible(false);
            btnEYE.setVisible(true);
            btnEYE_SLASH.setVisible(false);
        }
    }

    //End Show Password
    @FXML
    public void ForgotPassword() {
        try {
            // Tạo một FXMLLoader để tải tệp FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ForgotPassword/ForgotPassword.fxml"));

            // Tải tệp FXML và tạo một đối tượng Parent
            Parent root = loader.load();

            // Tạo một Scene mới từ đối tượng Parent
            Scene scene = new Scene(root);
            

            // Tạo một Stage mới
            Stage Stageforgot = new Stage();
            Stageforgot.setTitle("Forgot Password"); // Tiêu đề của cửa sổ mới
            Stageforgot.setScene(scene);
            Stageforgot.initStyle(StageStyle.UNDECORATED);

            // Đặt kiểu Modality cho Stage mới nếu bạn muốn khóa giao diện cũ cho đến khi giao diện mới được đóng
            Stageforgot.initModality(Modality.APPLICATION_MODAL);

            // Hiển thị Stage mới
            Stageforgot.show();
        } catch (IOException e) {

        }
    }

    @FXML
    public void CreateAdminAccount() {
        try {
            FXMLLoader loader;
            loader = new FXMLLoader(getClass().getResource("/Register/Register.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) username.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {

        }
    }

    public void btnclose() {
        System.exit(0);
    }

}
