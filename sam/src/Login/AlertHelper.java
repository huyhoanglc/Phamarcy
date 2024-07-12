/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Login;

import javafx.scene.control.Alert;

/**
 *
 * @author huypg
 */
public class AlertHelper {

    public static void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void errorMessage(String message) {
        showAlert(Alert.AlertType.ERROR, message);
    }

    public static void successMessage(String message) {
        showAlert(Alert.AlertType.INFORMATION, message);
    }
}
