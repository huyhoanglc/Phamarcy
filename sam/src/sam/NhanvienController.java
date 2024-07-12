/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sam;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author ADMIN
 */
public class NhanvienController implements Initializable {
    
    @FXML
    private StackPane mainUser;
    @FXML
    private void handleChamcongButtonClick() {
        try {
            // Load the FXML file for Nhanvien interface
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLChamcong.fxml"));
            Parent chamcong = loader.load();

            // Get the controller of Nhanvien interface
//            NhanvienController nhanvienController = loader.getController();

            // Replace main pane with Nhanvien interface
            mainUser.getChildren().clear();
            mainUser.getChildren().add(chamcong);

            // Display opacityPane if needed
//            opacityPane.setVisible(true);

        } catch (IOException e) {
            e.printStackTrace();
            // Handle failure to load Nhanvien interface
        }

    }
    
        @FXML
    private void handleBangluongButtonClick() {
        try {
            // Load the FXML file for Nhanvien interface
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLBangL.fxml"));
            Parent bangluong = loader.load();

            // Get the controller of Nhanvien interface
//            NhanvienController nhanvienController = loader.getController();

            // Replace main pane with Nhanvien interface
            mainUser.getChildren().clear();
            mainUser.getChildren().add(bangluong);

            // Display opacityPane if needed
//            opacityPane.setVisible(true);

        } catch (IOException e) {
            e.printStackTrace();
            // Handle failure to load Nhanvien interface
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}