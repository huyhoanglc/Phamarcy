/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package AddDrug;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import Data.ControllerHolder;
import DrugList.DruglistController;
import Data.MedicineIDGenerator;
import Data.Message;

/**
 * FXML Controller class
 *
 * @author Admin
 */
public class AddDrugController implements Initializable {

    @FXML
    private Button btnClose;

    @FXML
    private Button btnAdddrug;

    @FXML
    private Button btnCancel;

    @FXML
    private CheckBox ckitem;

    @FXML
    private CheckBox cksubunit;

    @FXML
    private TextField txtAPI;

    @FXML
    private TextField txtCostprice;

    @FXML
    private TextField txtDrugName;

    @FXML
    private TextField txtItem;

    @FXML
    private TextField txtItempersubunit;

    @FXML
    private TextField txtQuantity;

    @FXML
    private TextField txtSellingprice;

    @FXML
    private TextField txtSubunit;

    @FXML
    private TextField txtSubunitsperunit;

    @FXML
    private TextField txtUnit;

    @FXML
    void Cancelbtn(ActionEvent event) {
        updateDruglistForm();
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();

    }

    @FXML
    void Closebtn(ActionEvent event) {
        updateDruglistForm();
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();

    }

    private void updateDruglistForm() {

        DruglistController druglistController = ControllerHolder.getInstance().getDruglistController();
        if (druglistController != null) {
            druglistController.showData();
            druglistController.Pagination();
        }
    }
    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;
    Message alert = new Message();

    public boolean isPositiveNumber(String value) {
        try {
            int intValue = Integer.parseInt(value);
            return intValue >= 0;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean containsOnlyLetters(String text) {
        return text.matches("[a-zA-Z]+");

    }

    public void AddDrug() {
        if (txtDrugName.getText().isEmpty()
                || txtAPI.getText().isEmpty()
                || txtAPI.getText().isEmpty()
                || txtCostprice.getText().isEmpty()
                || txtSellingprice.getText().isEmpty()
                || txtQuantity.getText().isEmpty()
                || txtUnit.getText().isEmpty()) {
            alert.errorMessage("Please fill all blank fields");
            return;
        } else if (containsOnlyLetters(txtCostprice.getText())
                || containsOnlyLetters(txtSellingprice.getText())
                || containsOnlyLetters(txtQuantity.getText())) {
            alert.errorMessage("Please enter Number for price only");
            return;
        } else if (containsOnlyLetters(txtQuantity.getText())) {
            alert.errorMessage("Please enter Number for Quantity only");
            return;
        } else if (!isPositiveNumber(txtCostprice.getText())
                || !isPositiveNumber(txtSellingprice.getText())
                || Integer.parseInt(txtQuantity.getText()) <= 0) {
            alert.errorMessage("Please enter valid positive number");
            return;
        } else if (!containsOnlyLetters(txtUnit.getText())) {
            alert.errorMessage("Please enter Letters for Unit only");
            return;
        } else {
            try {
                // Lấy dữ liệu từ các trường nhập liệu
                String DrugName = txtDrugName.getText();
                if (isDrugNameExists(DrugName)) {
                    alert.errorMessage("Drug name already exists. Please enter a different name.");
                    return;
                }
                String API = txtAPI.getText();
                int CostPrice = Integer.parseInt(txtCostprice.getText());
                int SellingPrice = Integer.parseInt(txtSellingprice.getText());
                int Quantity = Integer.parseInt(txtQuantity.getText());
                String Unit = txtUnit.getText();
                int SubunitsPerUnit = 0;
                String Subunit = "";
                int ItemsPerSubunit = 0;
                String Item = "";

                if (cksubunit.isSelected()) {
                    if (txtSubunit.getText().isBlank()
                            || txtSubunitsperunit.getText().isBlank()) {
                        alert.errorMessage("Please fill all blank fields");
                        return;
                    } else if (containsOnlyLetters(txtSubunitsperunit.getText())) {
                        alert.errorMessage("Please enter Number for Quantity only");
                        return;
                    } else if (Integer.parseInt(txtSubunitsperunit.getText()) <= 0) {
                        alert.errorMessage("Please enter valid positive number");
                        return;
                    } else if (!containsOnlyLetters(txtSubunit.getText())) {
                        alert.errorMessage("Please enter Letters for Unit only");
                        return;
                    }
                    SubunitsPerUnit = Integer.parseInt(txtSubunitsperunit.getText());
                    Subunit = txtSubunit.getText();
                    if (ckitem.isSelected()) {
                        if (txtItempersubunit.getText().isBlank()
                                || txtItem.getText().isBlank()) {
                            alert.errorMessage("Please fill all blank fields");
                            return;
                        } else if (containsOnlyLetters(txtItempersubunit.getText())) {
                            alert.errorMessage("Please enter Number for Quantity only");
                            return;
                        } else if (Integer.parseInt(txtItempersubunit.getText()) <= 0) {
                            alert.errorMessage("Please enter valid positive number");
                            return;
                        } else if (!containsOnlyLetters(txtItem.getText())) {
                            alert.errorMessage("Please enter Letters for Unit only");
                            return;
                        }
                        ItemsPerSubunit = Integer.parseInt(txtItempersubunit.getText());
                        Item = txtItem.getText();
                    }
                }

                String MedID = MedicineIDGenerator.generateID();

                String sqlDrugListInsert = "INSERT INTO tblProduct (MedicineID, MedicineName, API, Quantity, Unit, Subunitsperunit, Subunit, Itemspersubunit, Item, CostPrice, SellingPrice) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                conn = DB.ConnectDB.getConnectDB();
                ps = conn.prepareStatement(sqlDrugListInsert);
                ps.setString(1, MedID);
                ps.setString(2, DrugName);
                ps.setString(3, API);
                ps.setInt(4, Quantity);
                ps.setString(5, Unit);
                ps.setInt(6, SubunitsPerUnit);
                ps.setString(7, Subunit);
                ps.setInt(8, ItemsPerSubunit);
                ps.setString(9, Item);
                ps.setInt(10, CostPrice);
                ps.setInt(11, SellingPrice);
                int affectedRows = ps.executeUpdate();

                if (affectedRows > 0) {
                    alert.successMessage("Drug added successfully");
                    updateDruglistForm();
                    clearFields();
                } else {
                    alert.errorMessage("Failed to add drug");
                }

            } catch (Exception e) {
                e.printStackTrace();
                alert.errorMessage("Error adding drug: " + e.getMessage());
            } finally {
                try {
                    if (ps != null) {
                        ps.close();
                    }
                    if (conn != null) {
                        conn.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean isDrugNameExists(String drugName) {
        try {
            String sqlCheck = "SELECT COUNT(*) FROM tblProduct WHERE MedicineName = ?";
            conn = DB.ConnectDB.getConnectDB();
            ps = conn.prepareStatement(sqlCheck);
            ps.setString(1, drugName);
            rs = ps.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            alert.errorMessage("Error checking drug name: " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public void CheckBoxHandlers() {

        ckitem.setDisable(true);
        cksubunit.setOnAction(event -> {
            boolean subunitSelected = cksubunit.isSelected();
            txtSubunitsperunit.setDisable(!subunitSelected);
            txtSubunit.setDisable(!subunitSelected);
            ckitem.setDisable(!subunitSelected);
            if (!subunitSelected) {
                ckitem.setSelected(false);
            }

            if (subunitSelected && ckitem.isSelected()) {
                txtItempersubunit.setDisable(false);
                txtItem.setDisable(false);
            } else {
                txtItempersubunit.setDisable(true);
                txtItem.setDisable(true);

            }
        });

        ckitem.setOnAction(event -> {
            boolean itemSelected = ckitem.isSelected();

            if (cksubunit.isSelected() && itemSelected) {
                txtItempersubunit.setDisable(false);
                txtItem.setDisable(false);
            } else {
                txtItempersubunit.setDisable(true);
                txtItem.setDisable(true);
            }
        });
    }

    @FXML
    void AddDrug(ActionEvent event) {
        AddDrug();
    }

    private void clearFields() {
        // Clear all input fields
        txtDrugName.clear();
        txtAPI.clear();
        txtCostprice.clear();
        txtSellingprice.clear();
        txtQuantity.clear();
        txtUnit.clear();
        txtSubunitsperunit.clear();
        txtSubunit.clear();
        txtItempersubunit.clear();
        txtItem.clear();

        // Reset checkbox states
        cksubunit.setSelected(false);
        ckitem.setSelected(false);

        // Disable subunit and item related fields
        txtSubunitsperunit.setDisable(true);
        txtSubunit.setDisable(true);
        txtItempersubunit.setDisable(true);
        txtItem.setDisable(true);
        ckitem.setDisable(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        ControllerHolder.getInstance().setAddDrugController(this);

    }

}
