package sam;

import Data.DruglistData;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.Callback;

public class BillsController implements Initializable {

    @FXML
    private Button Export;
    @FXML
    private TextField OrderSearchItem;
    @FXML
    private Text TotalPrice;
    @FXML
    private TableView<TableBills> tableBills;

    @FXML
    private TableColumn<TableBills, String> columnBillID;
    @FXML
    private TableColumn<TableBills, String> columnPhone;
    @FXML
    private TableColumn<TableBills, String> columnCustomerName;

    @FXML
    private TableColumn<TableBills, Integer> columnTotalPrice;

    @FXML
    private TableColumn<TableBills, String> columnPaymentMethod;

    @FXML
    private TableColumn<TableBills, String> columnBillDate;

    @FXML
    private VBox vboxBillItems;

    @FXML
    void Table(MouseEvent event) {

    }

    @FXML
    void osearch_item() {
        String searchText = normalizeSearchText(OrderSearchItem.getText());

        if (searchText.isEmpty()) {
            // If search text is empty, show all bills
            tableBills.setItems(bills);
            return;
        }
        String[] keywords = searchText.split("\\s+");

        ObservableList<TableBills> filteredList = FXCollections.observableArrayList();

        for (TableBills bill : bills) {
            String customerPhone = normalizeSearchText(bill.getCustomerPhone());
            String billID = normalizeSearchText(bill.getBillID());
            boolean matchesAnyKeyword = false;

            // Check if any keyword is found in customer phone or BillID
            for (String keyword : keywords) {
                if (customerPhone.contains(keyword) || billID.contains(keyword)) {
                    matchesAnyKeyword = true;
                    break;
                }
            }

            if (matchesAnyKeyword) {
                filteredList.add(bill);
            }
        }
        // Reset TableView with the filtered list
        tableBills.setItems(filteredList);
    }

    private String normalizeSearchText(String text) {
        if (text == null) {
            return "";
        }
        // Normalize text: remove accents and convert to lowercase
        String normalizedText = Normalizer.normalize(text, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        return normalizedText.toLowerCase().trim();
    }

    private ObservableList<TableBills> bills = FXCollections.observableArrayList();
    private ObservableList<TableBillsItem> billItems = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeBillTable();
    }

    private void initializeBillTable() {
        columnBillID.setCellValueFactory(new PropertyValueFactory<>("billID"));
        columnCustomerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        columnPhone.setCellValueFactory(new PropertyValueFactory<>("customerPhone"));
        columnTotalPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        columnPaymentMethod.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));
        columnBillDate.setCellValueFactory(new PropertyValueFactory<>("billDate"));

        loadBillsFromDatabase();
        tableBills.setItems(bills);

        // Double-click to show bill items
        tableBills.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                TableBills selectedBill = tableBills.getSelectionModel().getSelectedItem();
                if (selectedBill != null) {
                    loadBillItemsFromDatabase(selectedBill.getBillID());
                    displayTotalPayment(selectedBill.getTotalPrice());
                }
            }
        });
    }

    private void displayTotalPayment(double totalPayment) {
        TotalPrice.setText(String.format("%.1f đ", totalPayment));
        TotalPrice.setStyle("-fx-font-size: 20px;");
    }

    private void loadBillsFromDatabase() {
        bills.clear();
        String sql = "SELECT * FROM Bills";
        try {
            
            Connection conn = DB.ConnectDB.getConnectDB();
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                java.sql.Date billDateSql = rs.getDate("BillDate");
                TableBills bill = new TableBills(
                        rs.getString("BillID"),
                        rs.getString("CustomerId"),
                        rs.getString("CustomerName"),
                        rs.getString("PhoneCustomer"),
                        rs.getInt("TotalPrice"),
                        rs.getString("PaymentMethod"),
                        billDateSql // Giữ nguyên là java.sql.Date
                );
                bills.add(bill);
            }

            pst.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadBillItemsFromDatabase(String billID) {
        vboxBillItems.getChildren().clear(); // Clear previous items
        String sql = "SELECT * FROM BillItems WHERE BillID = ?";
        try {
            
            Connection conn = DB.ConnectDB.getConnectDB();
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, billID);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                String itemName = rs.getString("ItemName");
                int quantity = rs.getInt("Quantity");
                int unitPrice = rs.getInt("UnitPrice");
                String itemType = rs.getString("ItemType");
                String medicineID = rs.getString("MedicineID");

                TableBillsItem billItem = new TableBillsItem(itemName, quantity, unitPrice, itemType ,medicineID);
                AnchorPane pane = createBillItemPane(billItem);
                vboxBillItems.getChildren().add(pane);
            }

            pst.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private AnchorPane createBillItemPane(TableBillsItem billItem) {
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefHeight(75.0);
        anchorPane.setPrefWidth(285.0);
        anchorPane.setStyle("-fx-border-color: blue; -fx-border-radius: 10px; -fx-border-width: 1px;");
        anchorPane.setMinWidth(285.0);
        anchorPane.setMaxWidth(285.0);
        anchorPane.setMinHeight(75.0);
        anchorPane.setMaxHeight(75.0);

        // Tạo các thành phần trong AnchorPane
        Text itemNameText = new Text(billItem.getItemName());
        itemNameText.setId("itemNameText");
        itemNameText.setLayoutX(14.0);
        itemNameText.setLayoutY(18.0);
        itemNameText.setFont(Font.font("Arial Black", 12.0));
        anchorPane.getChildren().add(itemNameText);
        
        Text medicineIdText = new Text(billItem.getMedicineID());
        medicineIdText.setId("medicineID");
        medicineIdText.setLayoutX(200.0);
        medicineIdText.setLayoutY(18.0);
        medicineIdText.setFont(Font.font("Arial Black", 12.0));
        anchorPane.getChildren().add(medicineIdText);

        Text quantityText = new Text("Quantity");
        quantityText.setLayoutX(14.0);
        quantityText.setLayoutY(41.0);
        anchorPane.getChildren().add(quantityText);

        TextField quantityField = new TextField(String.valueOf(billItem.getQuantity()));
        quantityField.setId("quantityField");
        quantityField.setLayoutX(64.0);
        quantityField.setLayoutY(20.0);
        quantityField.setPrefHeight(18.0);
        quantityField.setPrefWidth(45.0);
        quantityField.setStyle("-fx-text-fill: black; -fx-border-color: black; -fx-border-width: 1px; -fx-border-radius: 2px; -fx-alignment: center;");
        anchorPane.getChildren().add(quantityField);

        Text unitText = new Text(billItem.getItemtype());
        unitText.setLayoutX(115.0);
        unitText.setLayoutY(41.0);
        anchorPane.getChildren().add(unitText);
        Text priceText = new Text("Price:");
        priceText.setLayoutX(170.0);
        priceText.setLayoutY(67.0);
        priceText.setFont(Font.font("Arial Bold", 14.0));
        anchorPane.getChildren().add(priceText);

        Text itemPriceText = new Text(billItem.getUnitPrice() + "đ");
        itemPriceText.setId("unitPriceText");
        itemPriceText.setLayoutX(220.0);
        itemPriceText.setLayoutY(67.0);
        itemPriceText.setFont(Font.font("Arial Bold", 14.0));
        anchorPane.getChildren().add(itemPriceText);

        VBox.setMargin(anchorPane, new Insets(2.0));

        return anchorPane;
    }

    private int totalBills;
    private int totalRevenue;

    public void updateTotalBillsAndRevenue() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = sdf.format(new Date());

        String countSql = "SELECT COUNT(*) AS TotalBills FROM Bills WHERE BillDate = ?";
        String sumSql = "SELECT SUM(TotalPrice) AS TotalRevenue FROM Bills WHERE BillDate = ?";

        try {
            
            Connection conn = DB.ConnectDB.getConnectDB();

            PreparedStatement countPst = conn.prepareStatement(countSql);
            countPst.setString(1, currentDate);
            ResultSet countRs = countPst.executeQuery();
            if (countRs.next()) {
                totalBills = countRs.getInt("TotalBills");
            }

            countPst.close();

            // Sum total revenue
            PreparedStatement sumPst = conn.prepareStatement(sumSql);
            sumPst.setString(1, currentDate);
            ResultSet sumRs = sumPst.executeQuery();
            if (sumRs.next()) {
                totalRevenue = sumRs.getInt("TotalRevenue");
            }

            sumPst.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Getter methods for totalBills and totalRevenue
    public int getTotalBills() {
        return totalBills;
    }

    public int getTotalRevenue() {
        return totalRevenue;
    }

    public void exportToPDF() {
        AlertMessage alert = new AlertMessage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PDF");
        String fontPath = "src/font/arial.ttf";
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File selectedFile = fileChooser.showSaveDialog(Export.getScene().getWindow());

        if (selectedFile != null) {
            try {

                PdfFont font = PdfFontFactory.createFont(fontPath, PdfEncodings.IDENTITY_H, true);

                PdfWriter writer = new PdfWriter(selectedFile.getAbsolutePath());
                PdfDocument pdf = new PdfDocument(writer);
                Document document = new Document(pdf);

                Paragraph title = new Paragraph("Bills Transaction").setBold().setFontSize(18);
                document.add(title);

                // Tạo tiêu đề cho các cột
                

                float[] columnWidths = {100, 100, 100, 100, 100, 100};
                Table table = new Table(columnWidths);

                TableView<TableBills> tableView = tableBills;
                for (TableColumn<TableBills, ?> column : tableView.getColumns()) {
                    
                        table.addCell(new Cell().add(new Paragraph(column.getText()).setFont(font)));
                    
                }


                ObservableList<TableBills> dataList = tableView.getItems();
                for (TableBills data : dataList) {
                    table.addCell(new Cell().add(new Paragraph(data.getBillID())));
                    table.addCell(new Cell().add(new Paragraph(data.getCustomerName())));
                    table.addCell(new Cell().add(new Paragraph(data.getCustomerPhone())));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(data.getTotalPrice()))));
                    table.addCell(new Cell().add(new Paragraph(data.getPaymentMethod())));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(data.getBillDate()))));

                }
                document.add(table);
                document.close();
                Desktop.getDesktop().open(selectedFile);

                alert.successMessage("Export successful!");
            } catch (IOException e) {
                alert.errorMessage("Export failed: " + e.getMessage());
            }
        }
    }
}
