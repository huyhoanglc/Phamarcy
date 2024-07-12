/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package UpdateDrug;

import AddDrug.AddDrugController;
import Data.ControllerHolder;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import Data.Data;
import Data.Message;
import DrugList.DruglistController;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Admin
 */
public class UpdateDrugController implements Initializable {

    @FXML
    private Button btnUpdateClose;

    @FXML
    private Button btnCancelUpdate;

    @FXML
    private Button btnUpdate;

    @FXML
    private CheckBox ckUPitem;

    @FXML
    private CheckBox ckUPsubunit;

    @FXML
    private TextField txtUPAPI;

    @FXML
    private TextField txtUPCostprice;

    @FXML
    private TextField txtUPDrugName;

    @FXML
    private TextField txtUPItem;

    @FXML
    private TextField txtUPItempersubunit;

    @FXML
    private TextField txtUPQuantity;

    @FXML
    private TextField txtUPSellingprice;

    @FXML
    private TextField txtUPSubunit;

    @FXML
    private TextField txtUPSubunitsperunit;

    @FXML
    private TextField txtUPUnit;

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

    private String MedicineID;

    Message alert = new Message();
    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;

    private boolean isPositiveNumber(String value) {
        try {
            int intValue = Integer.parseInt(value);
            return intValue >= 0;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean containsOnlyLetters(String text) {
        return text.matches("[a-zA-Z\\s]+");

    }

    private void updateDruglistForm() {

        DruglistController druglistController = ControllerHolder.getInstance().getDruglistController();
        if (druglistController != null) {
            druglistController.showData();
            druglistController.Pagination();
        }
    }

    public boolean isDrugNameExists(String drugName, String medicineID) {
        try {
            String sqlCheck = "SELECT COUNT(*) FROM tblProduct WHERE MedicineName = ? AND MedicineID <> ?";
            conn = DB.ConnectDB.getConnectDB();
            ps = conn.prepareStatement(sqlCheck);
            ps.setString(1, drugName);
            ps.setString(2, medicineID);
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

    public void updatebtn() {
        if (txtUPDrugName.getText().isEmpty()
                || txtUPAPI.getText().isEmpty()
                || txtUPAPI.getText().isEmpty()
                || txtUPCostprice.getText().isEmpty()
                || txtUPSellingprice.getText().isEmpty()
                || txtUPQuantity.getText().isEmpty()
                || txtUPUnit.getText().isEmpty()) {
            alert.errorMessage("Please fill all blank fields");
        } else if (containsOnlyLetters(txtUPCostprice.getText())
                || containsOnlyLetters(txtUPSellingprice.getText())
                || containsOnlyLetters(txtUPQuantity.getText())) {
            alert.errorMessage("Please enter Number for price only");
        } else if (containsOnlyLetters(txtUPQuantity.getText())) {
            alert.errorMessage("Please enter Number for Quantity only");
        } else if (!isPositiveNumber(txtUPCostprice.getText())
                || !isPositiveNumber(txtUPSellingprice.getText())
                || Integer.parseInt(txtUPQuantity.getText()) <= 0) {
            alert.errorMessage("Please enter valid positive number");
        } else if (!containsOnlyLetters(txtUPUnit.getText())) {
            System.out.println(txtUPUnit.getText());
            alert.errorMessage("Please enter Letters for Unit 1 only");
        } else {

            String DrugName = txtUPDrugName.getText();
            if (isDrugNameExists(DrugName, MedicineID)) {
                alert.errorMessage("Drug name already exists. Please enter a different name.");
                return;
            }
            String API = txtUPAPI.getText();
            int CostPrice = Integer.parseInt(txtUPCostprice.getText());
            int SellingPrice = Integer.parseInt(txtUPSellingprice.getText());
            int Quantity = Integer.parseInt(txtUPQuantity.getText());
            String Unit = txtUPUnit.getText();
            int SubunitsPerUnit = 0;
            String Subunit = "";
            int ItemsPerSubunit = 0;
            String Item = "";

            if (ckUPsubunit.isSelected()) {
                if (txtUPSubunit.getText().isBlank()
                        || txtUPSubunitsperunit.getText().isBlank()) {
                    alert.errorMessage("Please fill all blank fields");
                    return;
                } else if (containsOnlyLetters(txtUPSubunitsperunit.getText())) {
                    alert.errorMessage("Please enter Number for Quantity only");
                    return;
                } else if (Integer.parseInt(txtUPSubunitsperunit.getText()) <= 0) {
                    alert.errorMessage("Please enter valid positive number");
                    return;
                } else if (!containsOnlyLetters(txtUPSubunit.getText())) {
                    alert.errorMessage("Please enter Letters for Unit only");
                    return;
                }
                SubunitsPerUnit = Integer.parseInt(txtUPSubunitsperunit.getText());
                Subunit = txtUPSubunit.getText();
                if (ckUPitem.isSelected()) {
                    if (txtUPItempersubunit.getText().isBlank()
                            || txtUPItem.getText().isBlank()) {
                        alert.errorMessage("Please fill all blank fields");
                        return;
                    } else if (containsOnlyLetters(txtUPItempersubunit.getText())) {
                        alert.errorMessage("Please enter Number for Quantity only");
                        return;
                    } else if (Integer.parseInt(txtUPItempersubunit.getText()) <= 0) {
                        alert.errorMessage("Please enter valid positive number");
                        return;
                    } else if (!containsOnlyLetters(txtUPItem.getText())) {
                        alert.errorMessage("Please enter Letters for Unit only");
                        return;
                    }
                    ItemsPerSubunit = Integer.parseInt(txtUPItempersubunit.getText());
                    Item = txtUPItem.getText();
                }
            }
            String sqlDrugListUpdate = "UPDATE tblProduct SET MedicineName = ? , API = ?, Quantity = ?, Unit = ?, Subunitsperunit = ?, Subunit = ?, Itemspersubunit = ?, Item = ?, CostPrice = ?, SellingPrice = ? WHERE MedicineID = '" + MedicineID + "' ";
            conn = DB.ConnectDB.getConnectDB();
            try {
                if (alert.confirmMessage("Are you sure want to update Medicine ID: " + MedicineID + "?")) {
                    ps = conn.prepareStatement(sqlDrugListUpdate);
                    ps.setString(1, DrugName);
                    ps.setString(2, API);
                    ps.setInt(3, Quantity);
                    ps.setString(4, Unit);
                    ps.setInt(5, SubunitsPerUnit);
                    ps.setString(6, Subunit);
                    ps.setInt(7, ItemsPerSubunit);
                    ps.setString(8, Item);
                    ps.setInt(9, CostPrice);
                    ps.setInt(10, SellingPrice);
                    ps.executeUpdate();
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
    }

    public void setField() {
        MedicineID = Data.dl_MedicineID;
        txtUPDrugName.setText(Data.dl_MedicineName);
        txtUPAPI.setText(Data.dl_API);
        txtUPCostprice.setText(String.valueOf(Data.dl_Costprice));
        txtUPSellingprice.setText(String.valueOf(Data.dl_Sellingprice));
        txtUPQuantity.setText(String.valueOf(Data.dl_Quantity));
        txtUPUnit.setText(Data.dl_Unit);
        txtUPSubunitsperunit.setText(String.valueOf(Data.dl_Subunitsperunit));
        txtUPSubunit.setText(Data.dl_Subunit);
        txtUPItempersubunit.setText(String.valueOf(Data.dl_Itemspersubunit));
        txtUPItem.setText(Data.dl_Item);
        if (Data.dl_Subunitsperunit == 0) {
            txtUPSubunitsperunit.setDisable(true);
            txtUPSubunit.setDisable(true);

        } else {
            txtUPSubunitsperunit.setDisable(false);
            txtUPSubunit.setDisable(false);
            ckUPsubunit.setSelected(true);

        }
        if (Data.dl_Itemspersubunit == 0) {
            txtUPItempersubunit.setDisable(true);
            txtUPItem.setDisable(true);
        } else {
            txtUPItempersubunit.setDisable(false);
            txtUPItem.setDisable(false);
            ckUPitem.setSelected(true);
            ckUPitem.setDisable(false);
        }

        ckUPsubunit.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> handleSubunitCheck(isNowSelected));
        ckUPitem.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> handleItemCheck(isNowSelected));
    }

    private void handleSubunitCheck(boolean isChecked) {
        if (isChecked) {
            txtUPSubunitsperunit.setDisable(false);
            txtUPSubunit.setDisable(false);
            ckUPitem.setDisable(false);
        } else {
            txtUPSubunitsperunit.setDisable(true);
            txtUPSubunit.setDisable(true);
            txtUPSubunitsperunit.clear();
            txtUPSubunit.clear();
            ckUPitem.setSelected(false);
            ckUPitem.setDisable(true);
            txtUPItempersubunit.setDisable(true);
            txtUPItem.setDisable(true);
            txtUPItempersubunit.clear();
            txtUPItem.clear();
        }
    }

    private void handleItemCheck(boolean isChecked) {
        if (isChecked) {
            txtUPItempersubunit.setDisable(false);
            txtUPItem.setDisable(false);
        } else {
            txtUPItempersubunit.setDisable(true);
            txtUPItem.setDisable(true);
            txtUPItempersubunit.clear();
            txtUPItem.clear();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setField();
    }

}
