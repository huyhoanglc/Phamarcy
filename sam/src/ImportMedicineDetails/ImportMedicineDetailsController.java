package ImportMedicineDetails;

import java.time.LocalDate;
import Data.MedicineDetailsData;
import Data.Message;
import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Pagination;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import org.controlsfx.control.textfield.TextFields;
import org.krysalis.barcode4j.BarcodeException;
import org.krysalis.barcode4j.BarcodeGenerator;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;

public class ImportMedicineDetailsController implements Initializable {

    @FXML
    private TableColumn<MedicineDetailsData, Void> MD_Col_AC;

    @FXML
    private TableColumn<MedicineDetailsData, String> MD_Col_Barcode;

    @FXML
    private TableColumn<MedicineDetailsData, String> MD_Col_Batchno;

    @FXML
    private TableColumn<MedicineDetailsData, String> MD_Col_MedicineName;

    @FXML
    private TableColumn<MedicineDetailsData, Integer> MD_Col_Quantity1;

    @FXML
    private TableColumn<MedicineDetailsData, Integer> MD_Col_Quantity2;

    @FXML
    private TableColumn<MedicineDetailsData, Integer> MD_Col_Quantity3;

    @FXML
    private TableColumn<MedicineDetailsData, String> MD_Col_Status;

    @FXML
    private TableColumn<MedicineDetailsData, String> MD_Col_Unit1;

    @FXML
    private TableColumn<MedicineDetailsData, String> MD_Col_Unit2;

    @FXML
    private TableColumn<MedicineDetailsData, String> MD_Col_Unit3;

    @FXML
    private TableColumn<MedicineDetailsData, Date> MD_Col_ExpD;

    @FXML
    private TableColumn<MedicineDetailsData, Date> MD_Col_ManuD;

    @FXML
    private Button btnBG;

    @FXML
    private Button btnMDAdd;

    @FXML
    private Button btnMDUpdate;

    @FXML
    private Button btnMDcancel;

    @FXML
    private Button btnTransHistory;

    @FXML
    private Pagination pgtMD;

    @FXML
    private TextField searchMD;

    @FXML
    private TableView<MedicineDetailsData> tablemedicinedeteailslist;

    @FXML
    private TextField txtMDBarcode;

    @FXML
    private TextField txtMDBatchno;

    @FXML
    private DatePicker txtMDExpireDate;

    @FXML
    private DatePicker txtMDManDate;

    @FXML
    private TextField txtMDMedicineName;

    @FXML
    private ComboBox<String> txtMDStatus;

    @FXML
    private TextField txtQuan1;

    @FXML
    private TextField txtQuan2;

    @FXML
    private TextField txtQuan3;

    @FXML
    private TextField txtUnit1;

    @FXML
    private TextField txtUnit2;

    @FXML
    private TextField txtUnit3;

    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;
    Message alert = new Message();

    public boolean isPositiveNumber(String value) {
        try {
            int intValue = Integer.parseInt(value);
            return intValue >= 0;
        } catch (Exception e) {
            return false; // Nếu không phải số, trả về false
        }
    }

    public boolean containsOnlyLetters(String text) {
        return text.matches("[a-zA-Z]+");

    }

    public ObservableList<MedicineDetailsData> DataList() {
        ObservableList<MedicineDetailsData> Datalist = FXCollections.observableArrayList();
        String sql = "SELECT pd.ID, pd.MedicineID, pd.BarCode, pd.Batchno, pd.Quantity1, pd.Unit1, pd.Quantity2, pd.Unit2, pd.Quantity3, pd.Unit3, pd.ManufacturingDate, pd.ExpireDate, pd.Status, p.MedicineName "
                + "FROM tblProductDetails pd "
                + "JOIN tblProduct p ON pd.MedicineID = p.MedicineID";
        try {
            conn = DB.ConnectDB.getConnectDB();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                LocalDate manufacturingDate = rs.getDate("ManufacturingDate").toLocalDate();
                LocalDate expireDate = rs.getDate("ExpireDate").toLocalDate();
                MedicineDetailsData data = new MedicineDetailsData(
                        rs.getInt("ID"),
                        rs.getString("MedicineID"),
                        rs.getString("MedicineName"),
                        rs.getString("BarCode"),
                        rs.getString("Batchno"),
                        rs.getInt("Quantity1"),
                        rs.getString("Unit1"),
                        rs.getInt("Quantity2"),
                        rs.getString("Unit2"),
                        rs.getInt("Quantity3"),
                        rs.getString("Unit3"),
                        manufacturingDate,
                        expireDate,
                        rs.getString("Status")
                );
                Datalist.add(data);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
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
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
        return Datalist;
    }

    public void showData() {
        ObservableList<MedicineDetailsData> showList = DataList();
        MD_Col_MedicineName.setCellValueFactory(new PropertyValueFactory<>("MedicineName"));
        MD_Col_Barcode.setCellValueFactory(new PropertyValueFactory<>("Barcode"));
        MD_Col_Batchno.setCellValueFactory(new PropertyValueFactory<>("BatchNo"));
        MD_Col_ManuD.setCellValueFactory(new PropertyValueFactory<>("ManufacturingDate"));
        MD_Col_ExpD.setCellValueFactory(new PropertyValueFactory<>("ExpireDate"));
        MD_Col_Quantity1.setCellValueFactory(new PropertyValueFactory<>("Quantity1"));
        MD_Col_Unit1.setCellValueFactory(new PropertyValueFactory<>("Unit1"));
        MD_Col_Quantity2.setCellValueFactory(new PropertyValueFactory<>("Quantity2"));
        MD_Col_Unit2.setCellValueFactory(new PropertyValueFactory<>("Unit2"));
        MD_Col_Quantity3.setCellValueFactory(new PropertyValueFactory<>("Quantity3"));
        MD_Col_Unit3.setCellValueFactory(new PropertyValueFactory<>("Unit3"));
        MD_Col_Status.setCellValueFactory(new PropertyValueFactory<>("Status"));

        MD_Col_AC.setCellFactory(new Callback<TableColumn<MedicineDetailsData, Void>, TableCell<MedicineDetailsData, Void>>() {
            @Override
            public TableCell<MedicineDetailsData, Void> call(final TableColumn<MedicineDetailsData, Void> param) {
                final TableCell<MedicineDetailsData, Void> cell = new TableCell<MedicineDetailsData, Void>() {
                    private final Button btnDelete = new Button("Delete");

                    {
                        btnDelete.getStyleClass().add("btn_delete");
                        btnDelete.setOnAction((ActionEvent event) -> {
                            try {
                                MedicineDetailsData pdata = getTableView().getItems().get(getIndex());
                                if (alert.confirmMessage("Are you sure you want to delete ID: " + pdata.getID() + "?")) {
                                    deleteData(pdata.getID());
                                    showData(); // Refresh table
                                    Pagination(); // Refresh Pagination
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                        btnDelete.setAlignment(Pos.CENTER);
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            HBox hbox = new HBox(5);
                            hbox.getChildren().addAll(btnDelete);
                            hbox.setAlignment(Pos.CENTER);
                            setGraphic(hbox);
                        }
                    }
                };
                return cell;
                
            }
        });

        tablemedicinedeteailslist.setItems(showList);
    }

    public void MedicineDetailsSelect() {
        tablemedicinedeteailslist.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Lấy dữ liệu từ hàng được chọn và đổ vào các TextField và DatePicker
                MedicineDetailsData selectedData = newSelection;
                txtMDMedicineName.setText(selectedData.getMedicineName());
                txtMDMedicineName.setDisable(true);
                txtMDBarcode.setText(selectedData.getBarcode());
                txtMDBatchno.setText(selectedData.getBatchNo());
                txtMDManDate.setValue(selectedData.getManufacturingDate());
                txtMDExpireDate.setValue(selectedData.getExpireDate());
                txtQuan1.setText(String.valueOf(selectedData.getQuantity1()));
                txtUnit1.setText(selectedData.getUnit1());
                txtQuan2.setText(String.valueOf(selectedData.getQuantity2()));
                txtUnit2.setText(selectedData.getUnit2());
                txtQuan3.setText(String.valueOf(selectedData.getQuantity3()));
                txtUnit3.setText(selectedData.getUnit3());
                txtMDStatus.getSelectionModel().select(selectedData.getStatus());

                // Disable fields based on txtUnit2 and txtUnit3 conditions
                if (txtUnit2.getText().isEmpty()) {
                    txtQuan2.setDisable(true);
                    txtUnit2.setDisable(true);
                    txtQuan3.setDisable(true);
                    txtUnit3.setDisable(true);
                } else {
                    txtQuan2.setDisable(false);
                    txtUnit2.setDisable(false);
                }

                if (txtUnit3.getText().isEmpty()) {
                    txtQuan3.setDisable(true);
                    txtUnit3.setDisable(true);
                } else {
                    txtQuan3.setDisable(false);
                    txtUnit3.setDisable(false);
                }
            } else {
                // Nếu không có hàng được chọn, xóa trắng các TextField và DatePicker
                clearFields();
            }
        });
    }

    @FXML
    void MDCancel(ActionEvent event) {
        showData();
        Pagination();
        clearFields();
    }

    private void deleteData(int id) {
        String sqlDeleteProductDetails = "DELETE FROM tblProductDetails WHERE ID = ?";

        try {

            conn = DB.ConnectDB.getConnectDB();
            ps = conn.prepareStatement(sqlDeleteProductDetails);
            ps.setInt(1, id);
            ps.executeUpdate();
            ps.close();

            alert.successMessage("Deleted Successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void setupAutoSuggestion() {
        TextFields.bindAutoCompletion(txtMDMedicineName, suggestionRequest -> {
            ObservableList<String> suggestions = FXCollections.observableArrayList();
            String query = suggestionRequest.getUserText();
            if (query.length() > 1) { // Fetch suggestions when input length is greater than 2
                suggestions.addAll(fetchMedicineNames(query));
            }
            return suggestions;
        }).setOnAutoCompleted(event -> {
            String selectedMedicineName = event.getCompletion();
            fetchAndSetMedicineID(selectedMedicineName);
        });
    }

    private ObservableList<String> fetchMedicineNames(String query) {
        ObservableList<String> medicineNames = FXCollections.observableArrayList();
        String sql = "SELECT MedicineName FROM tblProduct WHERE MedicineName LIKE ?";
        try {
            conn = DB.ConnectDB.getConnectDB();
            ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + query + "%");
            rs = ps.executeQuery();
            while (rs.next()) {
                medicineNames.add(rs.getString("MedicineName"));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching medicine names: " + e.getMessage());
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
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
        return medicineNames;
    }

    private void fetchAndSetMedicineID(String medicineName) {
        String sql = "SELECT MedicineID, Unit, Subunit, Item FROM tblProduct WHERE MedicineName = ?";
        try {
            conn = DB.ConnectDB.getConnectDB();
            ps = conn.prepareStatement(sql);
            ps.setString(1, medicineName);
            rs = ps.executeQuery();
            if (rs.next()) {
                String medicineID = rs.getString("MedicineID");
                String unit = rs.getString("Unit");
                String subunit = rs.getString("Subunit");
                String item = rs.getString("Item");
                txtUnit1.setText(unit);

                if (subunit != null && !subunit.isEmpty()) {
                    txtUnit2.setText(subunit);
                    txtQuan2.setDisable(false);
                    txtUnit2.setDisable(false);
                } else {
                    txtQuan2.setDisable(true);
                    txtUnit2.setDisable(true);
                }

                if (item != null && !item.isEmpty()) {
                    txtUnit3.setText(item);
                    txtQuan3.setDisable(false);
                    txtUnit3.setDisable(false);
                } else {
                    txtQuan3.setDisable(true);
                    txtUnit3.setDisable(true);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching medicine ID: " + e.getMessage());
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
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    private String fetchAndValidateMedicineID(String medicineName) {
        String sql = "SELECT MedicineID FROM tblProduct WHERE MedicineName = ?";
        try {
            conn = DB.ConnectDB.getConnectDB();
            ps = conn.prepareStatement(sql);
            ps.setString(1, medicineName);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("MedicineID");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching medicine ID: " + e.getMessage());
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
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
        return null; // Return null if MedicineID not found
    }

    @FXML
    void AddMD(ActionEvent event) {
        // Fetch or validate MedicineID

        String medicineID = fetchAndValidateMedicineID(txtMDMedicineName.getText());

        if (medicineID == null) {
            // Handle case when MedicineID is not found or invalid
            alert.errorMessage("Invalid Medicine Name. Please select a valid medicine.");
            return;
        }

        if (txtMDMedicineName.getText().isEmpty()
                || txtMDBarcode.getText().isEmpty()
                || txtMDBatchno.getText().isEmpty()
                || txtMDManDate.getValue() == null
                || txtMDExpireDate.getValue() == null
                || txtQuan1.getText().isEmpty()
                || txtUnit1.getText().isEmpty()
                || txtMDStatus.getValue() == null) {
            alert.errorMessage("Please fill all blank fields");
            return;
        } else if (containsOnlyLetters(txtQuan1.getText())) {
            alert.errorMessage("Please enter Number for Quantity only");
            return;
        } else if (Integer.parseInt(txtQuan1.getText()) <= 0) {
            alert.errorMessage("Please enter valid positive number");
            return;
        } else if (!containsOnlyLetters(txtUnit1.getText())) {
            alert.errorMessage("Please enter Letters for Unit only");
            return;
        }
        String barcode = txtMDBarcode.getText();
        String batchNo = txtMDBatchno.getText();
        LocalDate manufacturingDate = txtMDManDate.getValue();
        LocalDate expireDate = txtMDExpireDate.getValue();
//        if (!isValidDate(txtMDManDate.getEditor().getText()) || !isValidDate(txtMDExpireDate.getEditor().getText())) {
//            alert.errorMessage("Invalid date format. Please enter dates in the format dd/MM/yyyy.");
//            return;
//        }

        if (manufacturingDate.isAfter(expireDate)) {
            alert.errorMessage("Expiration date must be after manufacturing date.");
            return;
        }

        int quantity1 = Integer.parseInt(txtQuan1.getText());
        String unit1 = txtUnit1.getText();
        int quantity2 = 0;
        String unit2 = "";
        int quantity3 = 0;
        String unit3 = "";
        String status = txtMDStatus.getValue();
        if (!txtUnit2.isDisable()) {
            if (txtUnit2.getText().isBlank()
                    || txtQuan2.getText().isBlank()) {
                alert.errorMessage("Please fill all blank 1 fields");
                return;
            } else if (containsOnlyLetters(txtQuan2.getText())) {
                alert.errorMessage("Please enter Number for Quantity only");
                return;
            } else if (Integer.parseInt(txtQuan2.getText()) <= 0) {
                alert.errorMessage("Please enter valid positive number");
                return;
            } else if (!containsOnlyLetters(txtUnit2.getText())) {
                alert.errorMessage("Please enter Letters for Unit only");
                return;
            }
            quantity2 = Integer.parseInt(txtQuan2.getText());
            unit2 = txtUnit2.getText();
            if (!txtUnit3.isDisable()) {
                if (txtQuan3.getText().isBlank()
                        || txtUnit3.getText().isBlank()) {
                    alert.errorMessage("Please fill all blank fields");
                    return;
                } else if (containsOnlyLetters(txtQuan3.getText())) {
                    alert.errorMessage("Please enter Number for Quantity only");
                    return;
                } else if (Integer.parseInt(txtQuan3.getText()) <= 0) {
                    alert.errorMessage("Please enter valid positive number");
                    return;
                } else if (!containsOnlyLetters(txtUnit3.getText())) {
                    alert.errorMessage("Please enter Letters for Unit only");
                    return;
                }
                quantity3 = Integer.parseInt(txtQuan3.getText());
                unit3 = txtUnit3.getText();
            }
        }
        if (!isBarcodeUnique(barcode)) {
            alert.errorMessage("Barcode already exists. Please use a different Barcode.");
            return;
        }
        // Insert data into database
        String sql = "INSERT INTO tblProductDetails (MedicineID, BarCode, Batchno, Quantity1, Unit1, Quantity2, Unit2, Quantity3, Unit3, ManufacturingDate, ExpireDate, Status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            conn = DB.ConnectDB.getConnectDB();
            ps = conn.prepareStatement(sql);
            ps.setString(1, medicineID);
            ps.setString(2, barcode);
            ps.setString(3, batchNo);
            ps.setInt(4, quantity1);
            ps.setString(5, unit1);
            ps.setInt(6, quantity2);
            ps.setString(7, unit2);
            ps.setInt(8, quantity3);
            ps.setString(9, unit3);
            ps.setDate(10, Date.valueOf(manufacturingDate));
            ps.setDate(11, Date.valueOf(expireDate));
            ps.setString(12, status);

            int result = ps.executeUpdate();
            if (result > 0) {
                alert.successMessage("Medicine details added successfully.");
                showData(); // Refresh table view
                Pagination(); // Refresh Pagination
                clearFields(); // Optional: Clear input fields after successful addition
            } else {
                alert.errorMessage("Failed to add medicine details.");
            }
        } catch (SQLException e) {
            System.out.println("Error adding medicine details: " + e.getMessage());
            alert.errorMessage("Error adding medicine details. Please try again.");
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
    }

//    private boolean isValidDate(String dateStr) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//        try {
//            LocalDate.parse(dateStr, formatter);
//            return true;
//        } catch (DateTimeParseException e) {
//            return false;
//        }
//    }

    private boolean isBarcodeUnique(String barcode) {
        String sql = "SELECT COUNT(*) AS count FROM tblProductDetails WHERE BarCode = ?";
        try {
            conn = DB.ConnectDB.getConnectDB();
            ps = conn.prepareStatement(sql);
            ps.setString(1, barcode);
            rs = ps.executeQuery();
            if (rs.next()) {
                int count = rs.getInt("count");
                return count == 0; // True if no matching records found (Barcode is unique)
            }
        } catch (SQLException e) {
            System.out.println("Error checking Barcode uniqueness: " + e.getMessage());
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
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
        return false; // Default to false if an error occurs
    }

    private void clearFields() {
        txtMDMedicineName.setDisable(false);
        txtMDBarcode.clear();
        txtMDBatchno.clear();
        txtMDExpireDate.setValue(null);
        txtMDManDate.setValue(null);
        txtMDMedicineName.clear();
        txtQuan1.clear();
        txtUnit1.clear();
        txtQuan2.clear();
        txtUnit2.clear();
        txtQuan3.clear();
        txtUnit3.clear();
        txtMDStatus.getSelectionModel().clearSelection();
    }

    //filter
    private void filterData(String keyword) {
        ObservableList<MedicineDetailsData> filteredList = FXCollections.observableArrayList();
        String sql = "SELECT pd.ID, pd.MedicineID, pd.BarCode, pd.Batchno, pd.Quantity1, pd.Unit1, pd.Quantity2, pd.Unit2, pd.Quantity3, pd.Unit3, pd.ManufacturingDate, pd.ExpireDate, pd.Status, p.MedicineName "
                + "FROM tblProductDetails pd "
                + "JOIN tblProduct p ON pd.MedicineID = p.MedicineID "
                + "WHERE pd.MedicineID LIKE ? OR p.MedicineName LIKE ? OR pd.Barcode LIKE ? OR pd.Batchno LIKE ?";

        try {
            conn = DB.ConnectDB.getConnectDB();
            ps = conn.prepareStatement(sql);
            String searchKeyword = "%" + keyword + "%";
            ps.setString(1, searchKeyword);
            ps.setString(2, searchKeyword);
            ps.setString(3, searchKeyword);
            ps.setString(4, searchKeyword);
            rs = ps.executeQuery();

            while (rs.next()) {
                LocalDate manufacturingDate = rs.getDate("ManufacturingDate").toLocalDate();
                LocalDate expireDate = rs.getDate("ExpireDate").toLocalDate();
                MedicineDetailsData data = new MedicineDetailsData(
                        rs.getInt("ID"),
                        rs.getString("MedicineID"),
                        rs.getString("MedicineName"),
                        rs.getString("BarCode"),
                        rs.getString("Batchno"),
                        rs.getInt("Quantity1"),
                        rs.getString("Unit1"),
                        rs.getInt("Quantity2"),
                        rs.getString("Unit2"),
                        rs.getInt("Quantity3"),
                        rs.getString("Unit3"),
                        manufacturingDate,
                        expireDate,
                        rs.getString("Status")
                );
                filteredList.add(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
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
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        tablemedicinedeteailslist.setItems(filteredList);
    }

    public void Search() {
        searchMD.textProperty().addListener((observable, oldValue, newValue) -> {
            filterData(newValue);
        });
    }

    public void StatusList() {
        List<String> ListS = new ArrayList<>();

        for (String data : Data.Data.status) {
            ListS.add(data);
        }
        ObservableList ListData = FXCollections.observableArrayList(ListS);
        txtMDStatus.setItems(ListData);
    }

    private static final int itemsperpage = 21;

    private int getPageCount() {
        return (int) Math.ceil((double) DataList().size() / itemsperpage);
    }

    private void updateTable(int pageIndex) {
        int fromIndex = pageIndex * itemsperpage;
        int toIndex = Math.min(fromIndex + itemsperpage, DataList().size());
        tablemedicinedeteailslist.setItems(FXCollections.observableArrayList(DataList().subList(fromIndex, toIndex)));
    }

    private Node createPage(int pageIndex) {
        updateTable(pageIndex);
        return tablemedicinedeteailslist;
    }

    public void Pagination() {
        pgtMD.setPageCount(getPageCount());
        pgtMD.setPageFactory(this::createPage);
    }

    @FXML
    void UpdateMD(ActionEvent event) {
        // Kiểm tra nếu người dùng đã chọn một hàng từ bảng
        MedicineDetailsData selectedData = tablemedicinedeteailslist.getSelectionModel().getSelectedItem();
        if (selectedData == null) {
            alert.errorMessage("Please select a medicine detail to update.");
            return;
        }
        if (txtMDMedicineName.getText().isEmpty()
                || txtMDBarcode.getText().isEmpty()
                || txtMDBatchno.getText().isEmpty()
                || txtMDManDate.getValue() == null
                || txtMDExpireDate.getValue() == null
                || txtQuan1.getText().isEmpty()
                || txtUnit1.getText().isEmpty()
                || txtMDStatus.getValue() == null) {
            alert.errorMessage("Please fill all blank fields");
            return;
        } else if (containsOnlyLetters(txtQuan1.getText())) {
            alert.errorMessage("Please enter Number for Quantity only");
            return;
        } else if (Integer.parseInt(txtQuan1.getText()) <= 0) {
            alert.errorMessage("Please enter valid positive number");
            return;
        } else if (!containsOnlyLetters(txtUnit1.getText())) {
            alert.errorMessage("Please enter Letters for Unit only");
            return;
        }
        // Lấy dữ liệu từ các trường nhập
        int id = selectedData.getID();
        String barcode = txtMDBarcode.getText();
        String batchNo = txtMDBatchno.getText();
        LocalDate manufacturingDate = txtMDManDate.getValue();
        LocalDate expireDate = txtMDExpireDate.getValue();
        String medicineName = txtMDMedicineName.getText();

        // Kiểm tra dữ liệu hợp lệ
//        if (!isValidDate(txtMDManDate.getEditor().getText()) || !isValidDate(txtMDExpireDate.getEditor().getText())) {
//            alert.errorMessage("Invalid date format. Please enter dates in the format dd/MM/yyyy.");
//            return;
//        }

        if (manufacturingDate.isAfter(expireDate)) {
            alert.errorMessage("Expiration date must be after manufacturing date.");
            return;
        }

        int quantity1 = Integer.parseInt(txtQuan1.getText());
        String unit1 = txtUnit1.getText();
        int quantity2 = 0;
        String unit2 = "";
        int quantity3 = 0;
        String unit3 = "";
        String status = txtMDStatus.getValue();
        if (!txtUnit2.isDisable()) {
            if (txtUnit2.getText().isBlank()
                    || txtQuan2.getText().isBlank()) {
                alert.errorMessage("Please fill all blank 1 fields");
                return;
            } else if (containsOnlyLetters(txtQuan2.getText())) {
                alert.errorMessage("Please enter Number for Quantity only");
                return;
            } else if (Integer.parseInt(txtQuan2.getText()) <= 0) {
                alert.errorMessage("Please enter valid positive number");
                return;
            } else if (!containsOnlyLetters(txtUnit2.getText())) {
                alert.errorMessage("Please enter Letters for Unit only");
                return;
            }
            quantity2 = Integer.parseInt(txtQuan2.getText());
            unit2 = txtUnit2.getText();
            if (!txtUnit3.isDisable()) {
                if (txtQuan3.getText().isBlank()
                        || txtUnit3.getText().isBlank()) {
                    alert.errorMessage("Please fill all blank fields");
                    return;
                } else if (containsOnlyLetters(txtQuan3.getText())) {
                    alert.errorMessage("Please enter Number for Quantity only");
                    return;
                } else if (Integer.parseInt(txtQuan3.getText()) <= 0) {
                    alert.errorMessage("Please enter valid positive number");
                    return;
                } else if (!containsOnlyLetters(txtUnit3.getText())) {
                    alert.errorMessage("Please enter Letters for Unit only");
                    return;
                }
                quantity3 = Integer.parseInt(txtQuan3.getText());
                unit3 = txtUnit3.getText();
            }
        }
        if (!isBarcodeUniqueForId(barcode, id)) {
            alert.errorMessage("Barcode already exists. Please use a different Barcode.");
            return;
        }
        // Cập nhật dữ liệu vào cơ sở dữ liệu
        String sql = "UPDATE tblProductDetails SET BarCode = ?, Batchno = ?, Quantity1 = ?, Unit1 = ?, Quantity2 = ?, Unit2 = ?, Quantity3 = ?, Unit3 = ?, ManufacturingDate = ?, ExpireDate = ?, Status = ? WHERE ID = ?";
        try {
            conn = DB.ConnectDB.getConnectDB();
            ps = conn.prepareStatement(sql);
            ps.setString(1, barcode);
            ps.setString(2, batchNo);
            ps.setInt(3, quantity1);
            ps.setString(4, unit1);
            ps.setInt(5, quantity2);
            ps.setString(6, unit2);
            ps.setInt(7, quantity3);
            ps.setString(8, unit3);
            ps.setDate(9, Date.valueOf(manufacturingDate));
            ps.setDate(10, Date.valueOf(expireDate));
            ps.setString(11, status);
            ps.setInt(12, id);

            int result = ps.executeUpdate();
            if (result > 0) {
                alert.successMessage("Medicine details updated successfully.");
                showData(); // Refresh table view
                Pagination(); // Refresh Pagination
                clearFields(); // Optional: Clear input fields after successful update
            } else {
                alert.errorMessage("Failed to update medicine details.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating medicine details: " + e.getMessage());
            alert.errorMessage("Error updating medicine details. Please try again.");
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    private boolean isBarcodeUniqueForId(String barcode, int id) {
        String sql = "SELECT COUNT(*) AS count FROM tblProductDetails WHERE BarCode = ? AND ID <> ?";
        try {
            conn = DB.ConnectDB.getConnectDB();
            ps = conn.prepareStatement(sql);
            ps.setString(1, barcode);
            ps.setInt(2, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                int count = rs.getInt("count");
                return count == 0; // True if no matching records found (Barcode is unique for given ID)
            }
        } catch (SQLException e) {
            System.out.println("Error checking Barcode uniqueness for ID: " + e.getMessage());
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
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
        return false; // Default to false if an error occurs
    }

    @FXML
    private void generateAndSaveBarcode() throws SQLException {
        // Generate a unique numeric barcode
        String barcode = generateBarcode();

        // Check if barcode generation failed
        if (barcode == null) {
            alert.errorMessage("Failed to generate a unique numeric barcode.");
            return;
        }

        // Save generated barcode image to file
        try {
            Code128Bean barcodeGenerator = new Code128Bean();
            final int dpi = 90;
            barcodeGenerator.setModuleWidth(0.6);
            barcodeGenerator.doQuietZone(true);

            BitmapCanvasProvider canvas = new BitmapCanvasProvider(dpi, BufferedImage.TYPE_BYTE_BINARY, false, 0);
            barcodeGenerator.generateBarcode(canvas, barcode);

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Barcode Image");
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
            fileChooser.getExtensionFilters().add(extFilter);
            File barcodeFile = fileChooser.showSaveDialog(null);

            if (barcodeFile != null) {
                FileOutputStream fos = new FileOutputStream(barcodeFile);
                canvas.finish();
                ImageIO.write(canvas.getBufferedImage(), "png", fos);
                fos.close();

                // Save barcode details to the database
                saveBarcodeDetails(barcode);

                alert.successMessage("Barcode Saved Successfully.");
                Desktop.getDesktop().open(barcodeFile); // Open the saved barcode image
            } else {
                alert.errorMessage("File save cancelled.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            alert.errorMessage("Error generating or saving barcode: " + e.getMessage());
        }
    }

    private String generateBarcode() throws SQLException {
        String barcode;
        do {
            barcode = generateNumericBarcode();
        } while (!BarcodeUnique(barcode));

        return barcode;
    }

    private String generateNumericBarcode() {
        // Generate a random numeric barcode (10 digits)
        Random random = new Random();
        int numDigits = 12;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numDigits; i++) {
            sb.append(random.nextInt(10)); // Generate random digit (0-9)
        }
        return sb.toString();
    }

    private boolean BarcodeUnique(String barcode) throws SQLException {
        // Check if the barcode exists in the database
        String sql = "SELECT * FROM tblBarcode WHERE Barcode = ?";
        conn = DB.ConnectDB.getConnectDB();
        ps = conn.prepareStatement(sql);
        ps.setString(1, barcode);
        ResultSet rs = ps.executeQuery();
        boolean unique = !rs.next(); // true if ResultSet is empty (barcode is unique)
        rs.close();
        ps.close();
        conn.close();
        return unique;
    }

    private void saveBarcodeDetails(String barcode) throws SQLException {
        // Save barcode details to the database
        String sql = "INSERT INTO tblBarcode (Barcode) VALUES (?)";
        conn = DB.ConnectDB.getConnectDB();
        ps = conn.prepareStatement(sql);
        ps.setString(1, barcode);
        ps.executeUpdate();
        ps.close();
        conn.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showData();
        setupAutoSuggestion();
        StatusList();
        MedicineDetailsSelect();
        Search();
        Pagination();
    }
}
