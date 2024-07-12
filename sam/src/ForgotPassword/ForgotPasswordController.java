/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package ForgotPassword;


import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.mail.Message.RecipientType;
import javax.mail.Transport;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author huypg
 */
public class ForgotPasswordController implements Initializable {

    @FXML
    private StackPane Fogot;

    @FXML
    private Button exit;
    @FXML
    private Button btnSendCode;

    @FXML
    private Button btnVerify;

    @FXML
    private AnchorPane forgotpane;

    @FXML
    private TextField hide;

    @FXML
    private TextField txtCode;

    @FXML
    private TextField txtemail;

    @FXML
    private AnchorPane verifypane;

    @FXML
    private AnchorPane resetpane;

    @FXML
    private ImageView EYE;

    @FXML
    private ImageView EYECF;

    @FXML
    private ImageView EYE_SLASH;

    @FXML
    private ImageView EYE_SLASHCF;

    @FXML
    private PasswordField NewPassword;

    @FXML
    private PasswordField ConfirmNewPassword;

    @FXML
    private TextField ShowConfirmNewPassword;

    @FXML
    private TextField Shownewpassword;

    @FXML
    private Button btnEYE;

    @FXML
    private Button btnEYECF;

    @FXML
    private Button btnEYE_SLASH;

    @FXML
    private Button btnEYE_SLASHCF;

    @FXML
    private Button btnResetPassword;

    private boolean isPasswordVisible = false;
    private boolean isCFPasswordVisible = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnResetPassword.setOnAction(event -> resetPassword());
        btnEYE.setOnAction(event -> togglePasswordVisibility());
        btnEYE_SLASH.setOnAction(event -> togglePasswordVisibility());
        EYE.setOnMouseClicked(event -> togglePasswordVisibility());
        EYE_SLASH.setOnMouseClicked(event -> togglePasswordVisibility());
        updatePasswordVisibility();
        btnEYECF.setOnAction(event -> toggleCFPasswordVisibility());
        btnEYE_SLASHCF.setOnAction(event -> toggleCFPasswordVisibility());
        EYECF.setOnMouseClicked(event -> toggleCFPasswordVisibility());
        EYE_SLASHCF.setOnMouseClicked(event -> toggleCFPasswordVisibility());
        updateCFPasswordVisibility();

        exit.setOnMouseClicked(event -> {
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.close();
        });
    }

    @FXML
    private void Sendcode(ActionEvent event) throws Exception {
        if (event.getSource() == btnSendCode) {

            forgotpane.setVisible(false);
            verifypane.setVisible(true);
            resetpane.setVisible(false);
            sendOTP();

        }
    }

    @FXML
    private void VerifyCode(ActionEvent event) throws Exception {
        if (event.getSource() == btnVerify) {

            String otp = hide.getText();
            String enteredOtp = txtCode.getText().trim();

            if (otp.equals(enteredOtp)) {
                // OTP is correct, load the Staff Dashboard
                forgotpane.setVisible(false);
                verifypane.setVisible(false);
                resetpane.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "Invalid OTP. Please try again.");
            }
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
    private void resetPassword() {
        String newPassword = NewPassword.getText().trim();
        String confirmNewPassword = ConfirmNewPassword.getText().trim();

        if (newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
            // Hiển thị thông báo lỗi: Cả hai trường phải được điền
            showAlert("Error", "Both fields must be filled.");
            return;
        }

        if (!newPassword.equals(confirmNewPassword)) {
            // Hiển thị thông báo lỗi: Mật khẩu không khớp
            showAlert("Error", "Passwords do not match.");
            return;
        }

        try (Connection connection = DB.ConnectDB.getConnectDB()) {
            // Bước 1: Lấy Phone từ bảng Information dựa trên email
            String phoneQuery = "SELECT Phone FROM Information WHERE BINARY Email = ?";
            try (PreparedStatement phoneStatement = connection.prepareStatement(phoneQuery)) {
                phoneStatement.setString(1, txtemail.getText());
                ResultSet resultSet = phoneStatement.executeQuery();
                if (resultSet.next()) {
                    String phone = resultSet.getString("Phone");

                    // Đặt username bằng giá trị phone
                    String username = phone;

                    // Bước 3: Cập nhật mật khẩu mới trong bảng Login
                    String updatePasswordQuery = "UPDATE Login SET Password = ? WHERE Username = ?";
                    try (PreparedStatement updatePasswordStatement = connection.prepareStatement(updatePasswordQuery)) {
                        updatePasswordStatement.setString(1, newPassword);
                        updatePasswordStatement.setString(2, username);
                        int rowsUpdated = updatePasswordStatement.executeUpdate();
                        if (rowsUpdated > 0) {
                            System.out.println("Password updated successfully for user: " + username);
                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login/Login.fxml"));
                                Parent root = loader.load();

                                Stage stage = new Stage();
                                stage.initStyle(StageStyle.UNDECORATED);
                                stage.setScene(new Scene(root));
                                stage.show();
                                clearRMPassword(username);

                            } catch (IOException e) {
                                JOptionPane.showMessageDialog(null, "Error loading verification screen: " + e.getMessage());
                            }
                        } else {
                            System.out.println("Failed to update password for user: " + username);
                        }
                    }
                } else {
                    System.out.println("No user found with phone: ");
                    // Xử lý khi không tìm thấy Username
                }
            }
        } catch (SQLException e) {
            // Xử lý ngoại lệ SQL
            e.printStackTrace();
        }
    }

    @FXML
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

    // Toggle visibility of NewPassword field
    @FXML
    private void togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible;
        updatePasswordVisibility();
    }

    private void updatePasswordVisibility() {
        if (isPasswordVisible) {
            NewPassword.setVisible(false);
            Shownewpassword.setVisible(true);
            Shownewpassword.setText(NewPassword.getText());
            btnEYE.setVisible(false);
            btnEYE_SLASH.setVisible(true);
        } else {
            NewPassword.setVisible(true);
            Shownewpassword.setVisible(false);
            btnEYE.setVisible(true);
            btnEYE_SLASH.setVisible(false);
        }
    }

    // Toggle visibility of ConfirmNewPassword field
    @FXML
    private void toggleCFPasswordVisibility() {
        isCFPasswordVisible = !isCFPasswordVisible;
        updateCFPasswordVisibility();
    }

    private void updateCFPasswordVisibility() {
        if (isCFPasswordVisible) {
            ConfirmNewPassword.setVisible(false);
            ShowConfirmNewPassword.setVisible(true);
            ShowConfirmNewPassword.setText(ConfirmNewPassword.getText());
            btnEYECF.setVisible(false);
            btnEYE_SLASHCF.setVisible(true);
        } else {
            ConfirmNewPassword.setVisible(true);
            ShowConfirmNewPassword.setVisible(false);
            btnEYECF.setVisible(true);
            btnEYE_SLASHCF.setVisible(false);
        }
    }
//    private boolean isValidEmailAddress(String email) {
//
//        return email != null && !email.isEmpty() && email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
//    }

    private String generateOTP(int len) {
        // All possible characters of my OTP
        String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789";
        int n = str.length();

        // String to hold my OTP
        StringBuilder otp = new StringBuilder();

        for (int i = 1; i <= len; i++) {
            otp.append(str.charAt((int) (Math.random() * str.length())));
        }
        return otp.toString();
    }

    public void sendOTP() {
        int len = 6;
        String otp = generateOTP(len);
        hide.setText(otp);
        String host = "smtp.gmail.com";
        final String username = "mingken036@gmail.com";
        final String password = "phch ducw memx hopt";
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", 465);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.debug", true);
        props.put("mail.smtp.socketFactory.port", 465);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", false);

        try {
            Session session = Session.getDefaultInstance(props, null);
            session.setDebug(true);

            MimeMessage message = new MimeMessage(session);
            message.setText("Your OTP is " + hide.getText());
            message.setSubject("OTP For your Reset Account");
            message.setFrom(new InternetAddress(username));
            message.addRecipient(RecipientType.TO, new InternetAddress(txtemail.getText().trim()));
            message.saveChanges();
            try {
                Transport transport = session.getTransport("smtp");
                transport.connect(host, username, password);
                transport.sendMessage(message, message.getAllRecipients());
                transport.close();
                JOptionPane.showMessageDialog(null, "OTP has send to your Email id");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Please check your internet connection");
            }

        } catch (Exception e) {

            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e);
        }

    }

}
