package eproject;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import java.sql.*;
import java.sql.Connection;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author 3D
 */
public class ImportOrderReceiptController implements Initializable {

    @FXML
    private TextField SearchReceipt;
    @FXML
    private TableView<ModelReceipt> TableReceipt;
    @FXML
    private TableColumn<ModelReceipt, String> col_ReceiptID;
    @FXML
    private TableColumn<ModelReceipt, Date> col_orderDate;
    @FXML
    private TableColumn<ModelReceipt, Date> col_receiveDate;
    @FXML
    private TableColumn<ModelReceipt, String> col_supplier;
    @FXML
    private TableColumn<ModelReceipt, Integer> col_Total;
    @FXML
    private TableColumn<ModelReceipt, String> col_Status;
    @FXML
    private AnchorPane add_receipt;
    @FXML
    private AnchorPane edit_receipt;
    @FXML
    private AnchorPane delete_receipt;
    @FXML
    private Text txtMedicineName;
    @FXML
    private TextField Qty01;
    @FXML
    private TextField Qty02;
    @FXML
    private TextField Qty03;
    @FXML
    private Text txtUnitPrice;
    @FXML
    private Button btnAddItem;
    @FXML
    private Button btnAddReceipt;
    @FXML
    private Button btnEditItem;
    @FXML
    private Button btnDeleteItem;
    @FXML
    private Text txtExpectTotal;
    @FXML
    private Button btnFinishOrder;
    @FXML
    private Button btnReceiptDelete;
    @FXML
    private Button btnOrder;
    @FXML
    private ComboBox<String> SelectMedicine;
    @FXML
    private ComboBox<String> SelectSupplier;
    @FXML
    private Text txtReceiptID;
    @FXML
    private TableView<ModelRDetail> TableReceiptDetail;
    @FXML
    private TableColumn<ModelRDetail, String> col_RMedicineID;
    @FXML
    private TableColumn<ModelRDetail, String> col_RMediName;
    @FXML
    private TableColumn<ModelRDetail, Integer> col_Qty01;
    @FXML
    private TableColumn<ModelRDetail, Integer> col_Qty02;
    @FXML
    private TableColumn<ModelRDetail, Integer> col_Qty03;
    @FXML
    private TableColumn<ModelRDetail, Integer> col_ImportPrice;
    @FXML
    private Button btnPrint;

    private Connection conn;
    private PreparedStatement pst;
    private ResultSet rs;
    private Statement stm;

    public ObservableList<ModelReceipt> ReceiptList() {
        //goi ham
        ObservableList<ModelReceipt> receiptList = FXCollections.observableArrayList();
        String sql = "select * from ImportOrderReceipt inner join Supplier on ImportOrderReceipt.SupplierID=Supplier.SupplierID";
        try {
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            ModelReceipt listR;
            while (rs.next()) {
                listR = new ModelReceipt(rs.getString("ReceiptID"),
                        rs.getDate("OrderDate"),
                        rs.getDate("ReceiveDate"),
                        rs.getInt("UserID"),
                        rs.getString("SupplierID"),
                        rs.getString("SupplierName"),
                        rs.getInt("TotalSpend"),
                        rs.getString("Status")
                );
                receiptList.add(listR);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return receiptList;
    }

    public void showRList() {
        ObservableList<ModelReceipt> showRList = ReceiptList();
        col_ReceiptID.setCellValueFactory(new PropertyValueFactory<>("ReceiptID"));
        col_orderDate.setCellValueFactory(new PropertyValueFactory<>("OrderDate"));
        col_receiveDate.setCellValueFactory(new PropertyValueFactory<>("ReceiveDate"));
        col_supplier.setCellValueFactory(new PropertyValueFactory<>("SupplierName"));
        col_Total.setCellValueFactory(new PropertyValueFactory<>("TotalSpend"));
        col_Status.setCellValueFactory(new PropertyValueFactory<>("Status"));
        TableReceipt.setItems(showRList);
    }

    public ObservableList<ModelRDetail> DetailList(String selectReceiptID) {
        //goi ham
        ObservableList<ModelRDetail> detailtList = FXCollections.observableArrayList();
        String sql = "select * from ReceiptDetail inner join tblProduct on ReceiptDetail.ItemID=tblProduct.MedicineID where ReceiptDetail.ReceiptID= ? ";
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, selectReceiptID);
            rs = pst.executeQuery();
            ModelRDetail data;
            while (rs.next()) {
                data = new ModelRDetail(rs.getString("ReceiptID"),
                        rs.getString("MedicineID"),
                        rs.getString("MedicineName"),
                        rs.getInt("OQty01"),
                        rs.getInt("OQty02"),
                        rs.getInt("OQty03"),
                        rs.getInt("ITotalPrice"),
                        rs.getInt("CostPrice"),
                        rs.getInt("Quantity"),
                        rs.getInt("Subunitsperunit"),
                        rs.getInt("Itemspersubunit")
                );
                detailtList.add(data);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return detailtList;
    }

    public ObservableList<ModelRDetail> DetailList() {
        //goi ham
        ObservableList<ModelRDetail> detailtList = FXCollections.observableArrayList();
        String sql = "select * from ReceiptDetail inner join tblProduct on ReceiptDetail.ItemID=tblProduct.MedicineID ";
        try {
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            ModelRDetail data;
            while (rs.next()) {
                data = new ModelRDetail(rs.getString("ReceiptID"),
                        rs.getString("MedicineID"),
                        rs.getString("MedicineName"),
                        rs.getInt("OQty01"),
                        rs.getInt("OQty02"),
                        rs.getInt("OQty03"),
                        rs.getInt("ITotalPrice"),
                        rs.getInt("CostPrice"),
                        rs.getInt("Quantity"),
                        rs.getInt("Subunitsperunit"),
                        rs.getInt("Itemspersubunit")
                );
                detailtList.add(data);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return detailtList;
    }

    public void showRDetail(String selectReceiptID) {
        ObservableList<ModelRDetail> showRDetal = DetailList(selectReceiptID);
        col_RMedicineID.setCellValueFactory(new PropertyValueFactory<>("MedicineID"));
        col_RMediName.setCellValueFactory(new PropertyValueFactory<>("MedicineName"));
        col_Qty01.setCellValueFactory(new PropertyValueFactory<>("OQty01"));
        col_Qty02.setCellValueFactory(new PropertyValueFactory<>("OQty02"));
        col_Qty03.setCellValueFactory(new PropertyValueFactory<>("OQty03"));
        col_ImportPrice.setCellValueFactory(new PropertyValueFactory<>("ITotalPrice"));
        TableReceiptDetail.setItems(showRDetal);

        purchaseDisplayTotal();
    }

    public void selectMedicine() {
        ObservableList<String> listMedicine = FXCollections.observableArrayList();
        String sql = "select MedicineName from tblProduct";
        try {
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                listMedicine.add(rs.getString("MedicineName"));
            }
            SelectMedicine.setItems(listMedicine);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void selectSupplier() {
        ObservableList<String> listSupplier = FXCollections.observableArrayList();
        String sql = "select SupplierName from Supplier";
        try {
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                listSupplier.add(rs.getString("SupplierName"));
            }
            SelectSupplier.setItems(listSupplier);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        conn = DB.ConnectDB.getConnectDB();
        showRList();
        selectMedicine();
        selectSupplier();
        System.out.println("ok");
    }

    public ObservableList<ModelReceipt> SearchReceiptList(String keyword) {
        ObservableList<ModelReceipt> receiptList = FXCollections.observableArrayList();
        String sql = "select * from ImportOrderReceipt inner join Supplier on ImportOrderReceipt.SupplierID=Supplier.SupplierID";
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql += " where lower(ReceiptID) like ? or lower(SupplierName) like ? or lower(Status) like ?";
        }
        try {
            pst = conn.prepareStatement(sql);
            if (keyword != null && !keyword.trim().isEmpty()) {
                String searchPattern = "%" + keyword.toLowerCase() + "%";
                pst.setString(1, searchPattern);
                pst.setString(2, searchPattern);
                pst.setString(3, searchPattern);
            }
            rs = pst.executeQuery();
            ModelReceipt listR;
            while (rs.next()) {
                listR = new ModelReceipt(rs.getString("ReceiptID"),
                        rs.getDate("OrderDate"),
                        rs.getDate("ReceiveDate"),
                        rs.getInt("UserID"),
                        rs.getString("SupplierID"),
                        rs.getString("SupplierName"),
                        rs.getInt("TotalSpend"),
                        rs.getString("Status")
                );
                receiptList.add(listR);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return receiptList;
    }

    public void showsearchRList(String keyword) {
        ObservableList<ModelReceipt> showsearchRList;
        if (keyword == null || keyword.trim().isEmpty()) {
            showsearchRList = ReceiptList(); // Lấy danh sách đầy đủ nếu không có từ khóa
        } else {
            showsearchRList = SearchReceiptList(keyword); // Lấy danh sách dựa trên từ khóa
        }

        col_ReceiptID.setCellValueFactory(new PropertyValueFactory<>("ReceiptID"));
        col_orderDate.setCellValueFactory(new PropertyValueFactory<>("OrderDate"));
        col_receiveDate.setCellValueFactory(new PropertyValueFactory<>("ReceiveDate"));
        col_supplier.setCellValueFactory(new PropertyValueFactory<>("SupplierName"));
        col_Total.setCellValueFactory(new PropertyValueFactory<>("TotalSpend"));
        col_Status.setCellValueFactory(new PropertyValueFactory<>("Status"));

        TableReceipt.setItems(showsearchRList);
    }

    @FXML
    public void search_receipt(ActionEvent event) {
        SearchReceipt.textProperty().addListener((observable, oldValue, newValue) -> {
            showsearchRList(newValue); // Gọi lại showRList với từ khóa tìm kiếm mới
        });
    }

    @FXML
    void selectReceipt(MouseEvent event) {
        if (event.getClickCount() == 2) { // Chỉ xử lý khi người dùng chọn một lần
            ModelReceipt data = TableReceipt.getSelectionModel().getSelectedItem();
            if (data != null) {
                txtReceiptID.setText(data.getReceiptID());
                showRDetail(data.getReceiptID());
            }
        }
    }

    @FXML
    void selectItem(MouseEvent event) {
        if (event.getClickCount() == 2) { // Chỉ xử lý khi người dùng chọn một lần
            ModelRDetail data = TableReceiptDetail.getSelectionModel().getSelectedItem();
            if (data != null) {
                txtMedicineName.setText(data.getMedicineName());
                txtUnitPrice.setText(data.getCostPrice().toString() + " đ");
                Qty01.setText(data.getOQty01().toString());
                Qty02.setText(data.getOQty02().toString());
                Qty03.setText(data.getOQty03().toString());
            }
        }
    }

    @FXML
    public void search(ActionEvent event) {
    }

    private String ReceiptID;

    public String getHighestReceiptID() {
        String highestID = "R0000"; // Default value if there are no records
        String sql = "SELECT MAX(ReceiptID) AS HighestID FROM ImportOrderReceipt";
        try {
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                highestID = rs.getString("HighestID");
                if(highestID == null){
                    highestID = "O0000";
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return highestID;
    }

    public String generateNextReceiptID() {
        String highestID = getHighestReceiptID();

        // Kiểm tra nếu highestID là null hoặc không phải độ dài mong muốn
        int number = Integer.parseInt(highestID.substring(1));
        number++;
        return String.format("O%04d", number);
    }

    @FXML
    void btn_add_receipt(ActionEvent event) {
        txtMedicineName.setText(null);
        txtUnitPrice.setText(null);
        Qty01.setText(null);
        Qty02.setText(null);
        Qty03.setText(null);
        TableReceiptDetail.getItems().clear();
        String newReceiptID = generateNextReceiptID();
        txtReceiptID.setText(newReceiptID);
    }

    private int totalPriceD;

    public void purchaseDisplayTotal() {

        String sql = "SELECT SUM(ITotalPrice) as total FROM ReceiptDetail WHERE ReceiptID = '" + txtReceiptID.getText() + "'";

        try {
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            if (rs.next()) {
                totalPriceD = rs.getInt("total");
            }
            txtExpectTotal.setText(String.valueOf(totalPriceD));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String ItemID;
    private String SupplierID;
    private int ITotal;

    @FXML
    public void btn_add_item(ActionEvent event) {
        String sql = "INSERT INTO ReceiptDetail (ReceiptID, ItemID, OQTy01, OQTy02, OQTy03, ITotalPrice) "
                + "VALUES(?,?,?,?,?,?)";
        String sql2 = "INSERT INTO ImportOrderReceipt (ReceiptID,SupplierID) "
                + "VALUES(?,?)";
        try {

            Alert alert;

            if (SelectMedicine.getSelectionModel().getSelectedItem() == null
                    || Qty01.getText().isEmpty()
                    || Qty02.getText().isEmpty()
                    || Qty03.getText().isEmpty()
                    || SelectSupplier.getSelectionModel().getSelectedItem() == null) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            } else {
                String checkReceipt = "SELECT ReceiptID FROM ImportOrderReceipt WHERE ReceiptID = '"
                        + txtReceiptID.getText() + "'";
                stm = conn.createStatement();
                rs = stm.executeQuery(checkReceipt);

                if (!rs.next()) {
                    pst = conn.prepareStatement(sql2);
                    pst.setString(1, txtReceiptID.getText());
                    String checkISupplier = "SELECT SupplierID FROM Supplier WHERE SupplierName = '"
                            + SelectSupplier.getSelectionModel().getSelectedItem() + "'";

                    stm = conn.createStatement();
                    rs = stm.executeQuery(checkISupplier);
                    if (rs.next()) {
                        SupplierID = rs.getString("SupplierID");
                    }
                    pst.setString(2, String.valueOf(SupplierID));
                    pst.executeUpdate();
                }

                pst = conn.prepareStatement(sql);
                pst.setString(1, txtReceiptID.getText());
                String checkIName = "SELECT MedicineID FROM tblProduct WHERE MedicineName = '"
                        + SelectMedicine.getSelectionModel().getSelectedItem() + "'";

                stm = conn.createStatement();
                rs = stm.executeQuery(checkIName);
                if (rs.next()) {
                    ItemID = rs.getString("MedicineID");
                }
                pst.setString(2, String.valueOf(ItemID));
                pst.setString(3, Qty01.getText());
                pst.setString(4, Qty02.getText());
                pst.setString(5, Qty03.getText());
                String checkData = "SELECT * FROM tblProduct WHERE MedicineName = '"
                        + SelectMedicine.getSelectionModel().getSelectedItem() + "'";

                stm = conn.createStatement();
                rs = stm.executeQuery(checkData);
                int priceD = 0;
                int RQty02 = 0;
                int RQty03 = 0;
                int OQty01 = Integer.parseInt(Qty01.getText());
                int OQty02 = Integer.parseInt(Qty02.getText());
                int OQty03 = Integer.parseInt(Qty03.getText());
                if (rs.next()) {
                    priceD = rs.getInt("CostPrice");
                    RQty02 = rs.getInt("Subunitsperunit");
                    RQty03 = rs.getInt("Itemspersubunit");
                }
                if (OQty01 == 0) {
                    OQty01 = 1;
                    RQty02 = 1;
                }
                if (OQty02 == 0) {
                    OQty02 = 1;
                    RQty03 = 1;
                }
                if (OQty03 == 0) {
                    OQty03 = 1;
                }
                ITotal = (priceD * RQty02 * RQty03 * OQty01 * OQty02 * OQty03);

                pst.setString(6, String.valueOf(ITotal));

                pst.executeUpdate();

                showRList();
                showRDetail(txtReceiptID.getText());
                purchaseDisplayTotal();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void btn_edit_item(ActionEvent event) {

        try {
            Alert alert;

            if (Qty01.getText().isEmpty() || Qty02.getText().isEmpty() || Qty03.getText().isEmpty()) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
                return;
            }

            String checkIName = "SELECT MedicineID FROM tblProduct WHERE MedicineName = ?";
            try (PreparedStatement pst = conn.prepareStatement(checkIName)) {
                pst.setString(1, txtMedicineName.getText());
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        ItemID = rs.getString("MedicineID");
                    } else {
                        alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Error Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Item not found");
                        alert.showAndWait();
                        return;
                    }
                }
            }

            String checkData = "SELECT * FROM tblProduct WHERE MedicinesName = ?";
            int priceD = 0;
            int RQty02 = 0;
            int RQty03 = 0;
            int OQty01 = Integer.parseInt(Qty01.getText());
            int OQty02 = Integer.parseInt(Qty02.getText());
            int OQty03 = Integer.parseInt(Qty03.getText());

            try (PreparedStatement pst = conn.prepareStatement(checkData)) {
                pst.setString(1, txtMedicineName.getText());
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        priceD = rs.getInt("CostPrice");
                        RQty02 = rs.getInt("Subunitsperunit");
                        RQty03 = rs.getInt("Itemspersubunit");
                    }
                }
            }

            if (OQty01 == 0) {
                OQty01 = 1;
                RQty02 = 1;
            }
            if (OQty02 == 0) {
                OQty02 = 1;
                RQty03 = 1;
            }
            if (OQty03 == 0) {
                OQty03 = 1;
            }

            ITotal = priceD * RQty02 * RQty03 * OQty01 * OQty02 * OQty03;

            alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to UPDATE Medicine: " + txtMedicineName.getText() + "?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.isPresent() && option.get() == ButtonType.OK) {
                String sql = "UPDATE ReceiptDetail SET OQty01 = ?, OQty02 = ?, OQty03 = ?, ITotalPrice = ? WHERE ReceiptID = ? AND ItemID = ?";
                try (PreparedStatement pst = conn.prepareStatement(sql)) {
                    pst.setString(1, Qty01.getText());
                    pst.setString(2, Qty02.getText());
                    pst.setString(3, Qty03.getText());
                    pst.setInt(4, ITotal);
                    pst.setString(5, txtReceiptID.getText());
                    pst.setString(6, ItemID);

                    int affectedRows = pst.executeUpdate();
                    if (affectedRows > 0) {
                        alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Information Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Successfully Updated!");
                        alert.showAndWait();

                        showRList();
                        showRDetail(txtReceiptID.getText());
                        purchaseDisplayTotal();
                    } else {
                        alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Error Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Update failed, no rows affected");
                        alert.showAndWait();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btn_delete_item(ActionEvent event) {
        try {
            Alert alert;

            if (txtReceiptID.getText().isEmpty()) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please select an item and ensure the ReceiptID is filled");
                alert.showAndWait();
                return;
            }

            String checkIName = "SELECT MedicineID FROM tblProduct WHERE MedicineName = ?";
            try (PreparedStatement pst = conn.prepareStatement(checkIName)) {
                pst.setString(1, txtMedicineName.getText());
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        ItemID = rs.getString("MedicineID");
                    } else {
                        alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Error Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Item not found");
                        alert.showAndWait();
                        return;
                    }
                }
            }

            alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to DELETE the selected item?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.isPresent() && option.get() == ButtonType.OK) {
                String sql = "DELETE FROM ReceiptDetail WHERE ReceiptID = ? AND ItemID = ?";
                try (PreparedStatement pst = conn.prepareStatement(sql)) {
                    pst.setString(1, txtReceiptID.getText());
                    pst.setString(2, ItemID);

                    int affectedRows = pst.executeUpdate();
                    if (affectedRows > 0) {
                        alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Information Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Successfully Deleted!");
                        alert.showAndWait();

                        showRList();
                        showRDetail(txtReceiptID.getText());
                        purchaseDisplayTotal();
                    } else {
                        alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Error Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Delete failed, no rows affected");
                        alert.showAndWait();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void showMedicine(MouseEvent event) {
        if (event.getClickCount() == 1) { // Chỉ xử lý khi người dùng chọn một lần
            SelectMedicine.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
                if (newValue != null) {
                    String sql = "SELECT * FROM tblProduct WHERE MedicineName = ?";
                    try {
                        pst = conn.prepareStatement(sql);
                        pst.setString(1, newValue);
                        rs = pst.executeQuery();
                        if (rs.next()) {
                            String ItemName = rs.getString("MedicineName");
                            txtMedicineName.setText(ItemName);
                            String ImportPrice = rs.getString("CostPrice");
                            txtUnitPrice.setText(ImportPrice.toString() + " đ");
                        }
                        Qty01.setText("0");
                        Qty02.setText("0");
                        Qty03.setText("0");
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            });
        }
    }

    @FXML
    void showSupplier(ActionEvent event) {

    }

    @FXML
    public void btn_finish(ActionEvent event) {
        try {
            Alert alert;

            if (TableReceiptDetail.getItems().isEmpty()) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("This order item first");
                alert.showAndWait();
                return;
            }

            String checkStatus = "SELECT Status FROM ImportOrderReceipt WHERE ReceiptID = ?";
            try (PreparedStatement pst = conn.prepareStatement(checkStatus)) {
                pst.setString(1, txtReceiptID.getText());
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        String status = rs.getString("Status");
                        if ("Finishing".equals(status)) {
                            alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error Message");
                            alert.setHeaderText(null);
                            alert.setContentText("This Order is already finished");
                            alert.showAndWait();
                            return;
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("An error occurred: " + e.getMessage());
                alert.showAndWait();
            }

            alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to finish this ORDER: " + txtReceiptID.getText() + "?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.isPresent() && option.get() == ButtonType.OK) {
                String sql = "UPDATE ImportOrderReceipt SET ReceiveDate = ?, TotalSpend = ?, Status= ? WHERE ReceiptID = ?";
                try (PreparedStatement pst = conn.prepareStatement(sql)) {
                    LocalDate currentDate = LocalDate.now();
                    Date sqlDate = Date.valueOf(currentDate);
                    pst.setDate(1, sqlDate);
                    pst.setString(2, txtExpectTotal.getText());
                    pst.setString(3, "Finishing");
                    pst.setString(4, txtReceiptID.getText());

                    int affectedRows = pst.executeUpdate();
                    if (affectedRows > 0) {
                        alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Information Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Successfully Updated!");
                        alert.showAndWait();

                        showRList();
                        showRDetail(txtReceiptID.getText());
                        purchaseDisplayTotal();
                    } else {
                        alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Error Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Update failed, no rows affected");
                        alert.showAndWait();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void btn_Rdelete(ActionEvent event) {
        try {
            Alert alert;

            String checkStatus = "SELECT Status FROM ImportOrderReceipt WHERE ReceiptID = ?";
            try (PreparedStatement pst = conn.prepareStatement(checkStatus)) {
                pst.setString(1, txtReceiptID.getText());
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        String status = rs.getString("Status");
                        if ("Finishing".equals(status)) {
                            alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error Message");
                            alert.setHeaderText(null);
                            alert.setContentText("This Order is already finished");
                            alert.showAndWait();
                            return;
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("An error occurred: " + e.getMessage());
                alert.showAndWait();
            }

            alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete this ORDER: " + txtReceiptID.getText() + "?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.isPresent() && option.get() == ButtonType.OK) {
                String sql1 = "DELETE FROM ReceiptDetail WHERE ReceiptID = ?";
                String sql2 = "DELETE FROM ImportOrderReceipt WHERE ReceiptID = ?";

                try (PreparedStatement pst1 = conn.prepareStatement(sql1); PreparedStatement pst2 = conn.prepareStatement(sql2)) {

                    conn.setAutoCommit(false);  // Start transaction

                    String receiptID = txtReceiptID.getText();
                    pst1.setString(1, receiptID);
                    pst2.setString(1, receiptID);

                    int affectedRows1 = pst1.executeUpdate();
                    int affectedRows2 = pst2.executeUpdate();
                    if (affectedRows1 > 0 && affectedRows2 > 0) {
                        conn.commit();
                        alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Information Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Successfully Delete!");
                        alert.showAndWait();

                        showRList();
                        showRDetail(txtReceiptID.getText());
                        purchaseDisplayTotal();
                    } else {
                        alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Error Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Delete failed, no rows affected");
                        alert.showAndWait();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void btn_order(ActionEvent event) {
        try {
            Alert alert;

            if (TableReceiptDetail.getItems().isEmpty() || SelectSupplier.getSelectionModel().getSelectedItem() == null) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please add Item or Supplier");
                alert.showAndWait();
                return;
            }

            String checkISupplier = "SELECT SupplierID FROM Supplier WHERE SupplierName = ?";
            try (PreparedStatement pst = conn.prepareStatement(checkISupplier)) {
                pst.setString(1, SelectSupplier.getSelectionModel().getSelectedItem());
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        SupplierID = rs.getString("SupplierID");
                    } else {
                        alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Error Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Supplier not found");
                        alert.showAndWait();
                        return;
                    }
                }
            }

            String checkStatus = "SELECT Status FROM ImportOrderReceipt WHERE ReceiptID = ?";
            try (PreparedStatement pst = conn.prepareStatement(checkStatus)) {
                pst.setString(1, txtReceiptID.getText());
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        String status = rs.getString("Status");
                        if ("Finishing".equals(status)) {
                            alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error Message");
                            alert.setHeaderText(null);
                            alert.setContentText("This Order is already finished");
                            alert.showAndWait();
                            return;
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("An error occurred: " + e.getMessage());
                alert.showAndWait();
            }

            alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to ORDER: " + txtReceiptID.getText() + "?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.isPresent() && option.get() == ButtonType.OK) {
                String sql = "UPDATE ImportOrderReceipt SET OrderDate = ?, SupplierID = ?, TotalSpend = ?, Status= ? WHERE ReceiptID = ?";
                try (PreparedStatement pst = conn.prepareStatement(sql)) {
                    LocalDate currentDate = LocalDate.now();
                    Date sqlDate = Date.valueOf(currentDate);
                    pst.setDate(1, sqlDate);
                    pst.setString(2, SupplierID);
                    pst.setString(3, txtExpectTotal.getText());
                    pst.setString(4, "Ordering");
                    pst.setString(5, txtReceiptID.getText());

                    int affectedRows = pst.executeUpdate();
                    if (affectedRows > 0) {
                        alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Information Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Successfully Updated!");
                        alert.showAndWait();

                        showRList();
                        showRDetail(txtReceiptID.getText());
                        purchaseDisplayTotal();
                    } else {
                        alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Error Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Update failed, no rows affected");
                        alert.showAndWait();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void btn_print(ActionEvent event) {
    }

}
