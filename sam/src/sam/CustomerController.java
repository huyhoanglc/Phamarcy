/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sam;

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
import java.sql.*;
import java.io.IOException;
import java.net.URL;
import java.text.Normalizer;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

// CustomerController.java
public class CustomerController implements Initializable, CustomerUpdateListener {

    public ObservableList<TableCustomer> customerList = FXCollections.observableArrayList();
    public CustomerUpdateListener listener;
    @FXML
    private Button Export;
    @FXML
    private TextField Search;
    @FXML
    private Button AddCus;
    @FXML
    private Pagination pagination;
    @FXML
    private TableColumn<TableCustomer, String> cotAction;

    @FXML
    private TableColumn<TableCustomer, String> cotAddress;

    @FXML
    private TableColumn<TableCustomer, String> cotGender;

    @FXML
    private TableColumn<TableCustomer, String> cotID;

    @FXML
    private TableColumn<TableCustomer, String> cotName;

    @FXML
    private TableColumn<TableCustomer, String> cotEmail;

    @FXML
    private TableColumn<TableCustomer, String> cotPhone;

    @FXML
    private TableColumn<TableCustomer, String> cotPoint;

    @FXML
    private TableView<TableCustomer> tableCustomer;

    public void setCustomerUpdateListener(CustomerUpdateListener listener) {
        this.listener = listener;
    }

    public void AddCustomer() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Addcustomer.fxml"));
            Parent AddCus = loader.load();

            AddcustomerController controller = loader.getController();
            controller.setCustomerUpdateListener(this);

            Stage addCusStage = new Stage();
            addCusStage.setTitle("Form Add");
            addCusStage.setScene(new Scene(AddCus));
            addCusStage.initStyle(StageStyle.UNDECORATED);
            addCusStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<TableCustomer> datalist() {
        ObservableList<TableCustomer> dataList = FXCollections.observableArrayList();

        String sql = "select * from Customer";

        try {
            
            Connection conn =DB.ConnectDB.getConnectDB();
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                TableCustomer data = new TableCustomer(String.format("%04d", rs.getInt("ID")),
                        rs.getString("CustomerName"),
                        rs.getString("Address"),
                        rs.getString("Gender"),
                        rs.getString("Point"),
                        rs.getString("Phone"),
                        rs.getString("Email"),
                        rs.getString("Action"));
                dataList.add(data);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return dataList;
    }

    public void showData() {
        // Update observable list from database
        customerList = datalist();

        // Set cell value factories and cell factories
        cotID.setCellValueFactory(new PropertyValueFactory<>("id"));
        cotName.setCellValueFactory(new PropertyValueFactory<>("name"));
        cotAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        cotGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        cotPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        cotEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        cotPoint.setCellValueFactory(new PropertyValueFactory<>("point"));
        cotAction.setCellValueFactory(new PropertyValueFactory<>("action"));

        // Set cell factories for action buttons
        setActionCellFactory();

        // Set TableView items and height
        tableCustomer.setItems(customerList);
        tableCustomer.setFixedCellSize(40); // Fixed cell height
        tableCustomer.prefHeightProperty().bind(Bindings.size(tableCustomer.getItems()).multiply(tableCustomer.getFixedCellSize()).add(30));

        // Update pagination and set to first page
        pagination.setPageCount(getPageCount());
        pagination.setPageFactory(this::createPage);
        pagination.setCurrentPageIndex(0);
    }

    public void setActionCellFactory() {
        Callback<TableColumn<TableCustomer, String>, TableCell<TableCustomer, String>> cellFactory
                = new Callback<TableColumn<TableCustomer, String>, TableCell<TableCustomer, String>>() {
            @Override
            public TableCell<TableCustomer, String> call(final TableColumn<TableCustomer, String> param) {
                final TableCell<TableCustomer, String> cell = new TableCell<>() {
                    private final Button updateButton = new Button("Update");
                    private final Button deleteButton = new Button("Delete");
                    private final HBox hbox = new HBox(10, updateButton, deleteButton);

                    {
                        // Button styling and actions
                        updateButton.getStyleClass().add("update-button");
                        deleteButton.getStyleClass().add("delete-button");

                        updateButton.setOnAction(event -> {
                            TableCustomer customer = getTableView().getItems().get(getIndex());
                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdateCustomer.fxml"));
                                Parent updateCustomerParent = loader.load();
                                

                                UpdateCustomerController controller = loader.getController();
                                controller.setCustomer(customer);
                                controller.setCustomerUpdateListener(CustomerController.this); // Pass listener reference

                                Stage updateStage = new Stage();
                                updateStage.setTitle("Update Customer");
                                updateStage.setScene(new Scene(updateCustomerParent));
                                updateStage.initStyle(StageStyle.UNDECORATED);
                                updateStage.show();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });

                        deleteButton.setOnAction(event -> {
                            TableCustomer customer = getTableView().getItems().get(getIndex());
                            deleteCustomer(customer);
                        });
                        StackPane.setAlignment(updateButton, Pos.CENTER);
                        StackPane.setAlignment(deleteButton, Pos.CENTER);
                    }

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                        // Set the cell's graphic based on whether the cell is empty
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(hbox);
                        }
                    }
                };

                return cell;
            }
        };

        // Set cell factory for action column
        cotAction.setCellFactory(cellFactory);
    }

    @FXML
    public void deleteCustomer(TableCustomer customer) {
        AlertMessage alert = new AlertMessage();
        String sql = "DELETE FROM Customer WHERE ID = ?";
        try {
            
            Connection conn =DB.ConnectDB.getConnectDB();
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(customer.getId()));
            pst.executeUpdate();

            // Remove customer from ObservableList and update TableView
            customerList.remove(customer);
            showData(); // Update TableView
            alert.successMessage("Customer deleted successfully");

        } catch (Exception e) {
            e.printStackTrace();
            alert.errorMessage("Error deleting customer: " + e.getMessage());
        }
    }

    @FXML
    public void updateCustomer(TableCustomer updatedCustomer) {
        AlertMessage alert = new AlertMessage();
        String sql = "UPDATE Customer SET CustomerName=?, Address=?, Gender=?, Phone=?, Email=?, Point=? WHERE ID=?";

        try {
            
            Connection conn = DB.ConnectDB.getConnectDB();
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, updatedCustomer.getName());
            pst.setString(2, updatedCustomer.getAddress());
            pst.setString(3, updatedCustomer.getGender());
            pst.setString(4, updatedCustomer.getPhone());
            pst.setString(5, updatedCustomer.getEmail());
            pst.setString(6, updatedCustomer.getPoint());
//            pst.setString(7, updatedCustomer.getAction());
            pst.setInt(7, Integer.parseInt(updatedCustomer.getId())); // ID is int type in database

            int affectedRows = pst.executeUpdate();
            if (affectedRows > 0) {

                if (listener != null) {
                    listener.onUpdate(updatedCustomer); // Notify listener to update TableView
                }
            } else {
                System.out.println("Failed to update customer.");
            }

        } catch (SQLException e) {
            System.out.println("Error updating customer: " + e.getMessage());
        }
    }

//    private void refreshTableView() {
//        // Refresh the TableView to display updated data
//        tableCustomer.getItems().clear();
//        tableCustomer.getItems().addAll(customerList);
//    }
    @FXML
    private void handleSearch() {
        String searchText = normalizeSearchText(Search.getText());

        if (searchText.isEmpty()) {
            // If search text is empty, show all customers
            tableCustomer.setItems(customerList);
            pagination.setPageFactory(this::createPage);
            return;
        }
        String[] keywords = searchText.split("\\s+");

        ObservableList<TableCustomer> filteredList = FXCollections.observableArrayList();

        for (TableCustomer customer : customerList) {
            String customerName = normalizeSearchText(customer.getName());
            boolean matchesAllKeywords = true;

            // Check if all keywords are found in customer name
            for (String keyword : keywords) {
                if (!customerName.contains(keyword)) {
                    matchesAllKeywords = false;
                    break;
                }
            }

            if (matchesAllKeywords) {
                filteredList.add(customer);
            }
        }

        // Reset TableView with the filtered list
        tableCustomer.setItems(filteredList);

    }

//    @FXML
//    private void clearSearch() {
//        Search.clear();
//        handleSearch(); // Call handleSearch to reset TableView and pagination
//    }
    private String normalizeSearchText(String text) {
        // Normalize text: remove accents and convert to lowercase
        String normalizedText = Normalizer.normalize(text, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        return normalizedText.toLowerCase().trim();
    }

    public void refreshCustomerList() {
        customerList.clear(); // Xóa danh sách khách hàng hiện tại
        customerList.addAll(datalist()); // Cập nhật lại danh sách từ cơ sở dữ liệu

    }

    public TableCustomer findCustomerByPhone(String phone) {
        refreshCustomerList();
        for (TableCustomer customer : customerList) {
            if (customer.getPhone().equals(phone)) {
                return customer;
            }
        }
        return null; // Trả về null nếu không tìm thấy khách hàng
    }

    public void updateCustomerPoints(String phoneNumber, int pointsToAdd) {
        // Tìm khách hàng dựa trên số điện thoại
        TableCustomer customer = findCustomerByPhone(phoneNumber);
        if (customer != null) {
            // Cập nhật điểm tích lũy cho khách hàng
            int currentPoints = Integer.parseInt(customer.getPoint());
            int newPoints = currentPoints + pointsToAdd;

            // Update điểm tích lũy vào cơ sở dữ liệu
            String sql = "UPDATE Customer SET Point = ? WHERE Phone = ?";
            try {
                
                Connection conn = DB.ConnectDB.getConnectDB();
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setInt(1, newPoints);
                pst.setString(2, phoneNumber);
                pst.executeUpdate();

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Không tìm thấy khách hàng với số điện thoại " + phoneNumber);
        }
    }

    private static final int itemsPerPage = 13; // Số lượng mục trên mỗi trang

    private ObservableList<TableCustomer> paginatedData; // Danh sách dữ liệu được phân trang

    private void setupPagination() {
        int pageCount = getPageCount();
        pagination.setPageCount(pageCount); // Thiết lập số lượng trang

        pagination.setPageFactory(this::createPage); // Thiết lập page factory để tạo nội dung từng trang
    }

    private int getPageCount() {
        // Return the number of pages based on your data and itemsPerPage
        // For example:
        int itemsPerPage = 13; // Số lượng dòng trên mỗi trang
        int pageCount = (int) Math.ceil((double) customerList.size() / itemsPerPage);
        return pageCount;
    }

    private Node createPage(int pageIndex) {
        int itemsPerPage = 13; // Số lượng dòng trên mỗi trang
        int fromIndex = pageIndex * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, customerList.size());
        List<TableCustomer> sublist = customerList.subList(fromIndex, toIndex);

        tableCustomer.setItems(FXCollections.observableArrayList(sublist));
        setActionCellFactory();
        return new BorderPane(tableCustomer);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupPagination(); // Khởi tạo phân trang
        customerList = datalist(); // Tải dữ liệu từ cơ sở dữ liệu
        showData(); // Hiển thị dữ liệu trong TableView
    }

    @Override
    public void onUpdate() {
        showData();

    }

    @Override
    public void onUpdate(TableCustomer UpdatedCustomer) {
        // Find the customer in the list and update its properties
        for (TableCustomer customer : customerList) {
            if (customer.getId().equals(UpdatedCustomer.getId())) {
                customer.setName(UpdatedCustomer.getName());
                customer.setAddress(UpdatedCustomer.getAddress());
                customer.setGender(UpdatedCustomer.getGender());
                customer.setPhone(UpdatedCustomer.getPhone());
                customer.setEmail(UpdatedCustomer.getEmail());
                customer.setPoint(UpdatedCustomer.getPoint());
                break;
            }
        }
        updateCustomer(UpdatedCustomer);

        showData();
    }

    private ObservableList<TableCustomer> getAllDataFromTableView(TableView<TableCustomer> tableView) {
        ObservableList<TableCustomer> allData = FXCollections.observableArrayList();
        allData.addAll(tableView.getItems());
        return allData;
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

                Paragraph title = new Paragraph("Customer").setBold().setFontSize(18);
                document.add(title);

                // Tạo tiêu đề cho các cột
                float[] columnWidths = {100, 100, 100, 100, 100, 100, 100};
                Table table = new Table(columnWidths);

                TableView<TableCustomer> tableView = tableCustomer;
                for (TableColumn<TableCustomer, ?> column : tableView.getColumns()) {
                    if (!column.getText().equals("Action")) {
                        table.addCell(new Cell().add(new Paragraph(column.getText()).setFont(font)));
                    }

                }

                ObservableList<TableCustomer> dataList = getAllDataFromTableView(tableView);
                for (TableCustomer data : dataList) {
                    table.addCell(new Cell().add(new Paragraph(data.getId())));
                    table.addCell(new Cell().add(new Paragraph(data.getName())));
                    table.addCell(new Cell().add(new Paragraph(data.getAddress())));
                    table.addCell(new Cell().add(new Paragraph(data.getGender())));
                    table.addCell(new Cell().add(new Paragraph(data.getPhone())));
                    table.addCell(new Cell().add(new Paragraph(data.getEmail())));
                    table.addCell(new Cell().add(new Paragraph(data.getPoint())));

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
