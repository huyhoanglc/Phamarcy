/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package eproject;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.*;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author 3D
 */
public class SupplierController implements Initializable {

    @FXML
    private TextField SearchSupplier;
    @FXML
    private Button btnAddSupplier;
    @FXML
    private TableView<ModelSupplier> TableSupplier;
    @FXML
    private TableColumn<ModelSupplier, String> col_SupplierID;
    @FXML
    private TableColumn<ModelSupplier, String> col_SupplierName;
    @FXML
    private TableColumn<ModelSupplier, String> col_Address;
    @FXML
    private TableColumn<ModelSupplier, String> col_Phone;
    @FXML
    private TableColumn<ModelSupplier, String> col_Email;

    private Connection conn;
    private PreparedStatement pst;
    private ResultSet rs;
    private Statement stm;
    
    public ObservableList<ModelSupplier> SuppliertList(){
        //goi ham
        ObservableList<ModelSupplier> suppliertList = FXCollections.observableArrayList();
        String sql = "select * from Supplier";
        try{
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            ModelSupplier listS;
            while(rs.next()){
                listS = new ModelSupplier(rs.getString("SupplierID"), 
                        rs.getString("SupplierName"), 
                        rs.getString("Address"), 
                        rs.getString("PhoneNumber"), 
                        rs.getString("Email")
                );
                suppliertList.add(listS);
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return suppliertList;
    }
    
    
    public void showSList(){
        ObservableList<ModelSupplier> showSList = SuppliertList();
        col_SupplierID.setCellValueFactory(new PropertyValueFactory<>("SupplierID"));
        col_SupplierName.setCellValueFactory(new PropertyValueFactory<>("SupplierName"));
        col_Address.setCellValueFactory(new PropertyValueFactory<>("Address"));
        col_Phone.setCellValueFactory(new PropertyValueFactory<>("PhoneNumber"));
        col_Email.setCellValueFactory(new PropertyValueFactory<>("Email"));
        
        TableSupplier.setItems(showSList);  
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        conn=DB.ConnectDB.getConnectDB();
       showSList();
    }    

    @FXML
    private void btn_add_supplier(MouseEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Addsupplier.fxml"));
            Parent root = loader.load();

            AddsupplierController controller = loader.getController();
            controller.setSupplierController(this);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Add Supplier");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void sellectCustomer(MouseEvent event) {
         if (event.getClickCount() == 2) {
            openUpdateSupplierWindow();
        }
    }
    
    @FXML
    private void openUpdateSupplierWindow() {
        ModelSupplier selectedSupplier = TableSupplier.getSelectionModel().getSelectedItem();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdateSupplier.fxml"));
            Parent root = loader.load();

            UpdateSupplierController controller = loader.getController();
            controller.setSupplierController(this);
            controller.setSupplierData(selectedSupplier.getSupplierID(),
                                       selectedSupplier.getSupplierName(),
                                       selectedSupplier.getPhoneNumber(),
                                       selectedSupplier.getAddress(),
                                       selectedSupplier.getEmail());

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Update Supplier");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
