/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sam;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ADMIN
 */
public class ProductExpiryInfoController {
    @FXML
    private Button exit;
    @FXML
    private TableColumn<ProductExpiryTable, Long> daysLeftColumn;

    @FXML
    private TableColumn<ProductExpiryTable, LocalDate> expireDateColumn;

    @FXML
    private TableView<ProductExpiryTable> expiringProductsTable;

    @FXML
    private TableColumn<ProductExpiryTable, LocalDate> manufacturingDateColumn;

    @FXML
    private TableColumn<ProductExpiryTable, String> medicineIDColumn;

    @FXML
    private TableColumn<ProductExpiryTable, String> medicineNameColumn;

    private List<ProductExpiryTable> expiringProducts;

    public void initialize() {
        // Initialize table columns
        medicineIDColumn.setCellValueFactory(new PropertyValueFactory<>("medicineID"));
        medicineNameColumn.setCellValueFactory(new PropertyValueFactory<>("medicineName"));
        manufacturingDateColumn.setCellValueFactory(new PropertyValueFactory<>("manufacturingDate"));
        expireDateColumn.setCellValueFactory(new PropertyValueFactory<>("expireDate"));
        daysLeftColumn.setCellValueFactory(new PropertyValueFactory<>("daysLeft"));
        
        exit.setOnMouseClicked(event -> {
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.close();
        });
    }

    public void setExpiringProducts(List<ProductExpiryTable> products) {
        expiringProductsTable.setItems(FXCollections.observableArrayList(products));
    }

}
