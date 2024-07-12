package sam;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import sam.TableProduct;
import java.sql.Types;
import java.sql.SQLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.UUID;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import javafx.scene.input.KeyEvent;

public class OrderTransactionController implements Initializable {

    @FXML
    private RadioButton PaymentCard;

    @FXML
    private RadioButton PaymentCash;

    @FXML
    private TextField OrderSearchItem;
    @FXML
    private Text TotalPrice;
    @FXML
    private TextField OrderSearchCus;

    private CustomerController customerController = new CustomerController();

// Phương thức để inject CustomerController vào OrderTransactionController
    public void setCustomerController(CustomerController customerController) {
        this.customerController = customerController;
    }

    @FXML
    private TableColumn<TableProduct, String> Column_Batch;

    @FXML
    private TableColumn<TableProduct, Date> Column_ExpDate;

    @FXML
    private TableColumn<TableProduct, String> Column_ItemID;

    @FXML
    private TableColumn<TableProduct, String> Column_ItemName;

    @FXML
    private TableColumn<TableProduct, String> Column_Item;

    @FXML
    private TableColumn<TableProduct, Integer> Column_Price;

    @FXML
    private TableColumn<TableProduct, Integer> Column_Quantity;

    @FXML
    private TableView<TableProduct> tableProduct;

    @FXML
    private VBox vbox;

    private ObservableList<TableProduct> productList = FXCollections.observableArrayList();

    public ObservableList<TableProduct> datalist() {
        ObservableList<TableProduct> dataList = FXCollections.observableArrayList();

        String sql = "SELECT p.MedicineID, pd.Barcode ,p.Item, p.Unit, p.MedicineName, p.Subunit, pd.BatchNo, pd.ExpireDate, p.SellingPrice, pd.Quantity1, pd.Quantity2, pd.Quantity3 "
                + "FROM tblProduct p "
                + "INNER JOIN tblProductDetails pd ON p.MedicineID = pd.MedicineID "
                + "WHERE pd.Status <> 'Unavailable'";

        try {
            
            Connection conn = DB.ConnectDB.getConnectDB();
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                String unit = rs.getString("Item");
                Integer quantity = rs.getInt("Quantity3");
                if (unit == null || unit.isEmpty()) {
                    unit = rs.getString("Subunit");
                    quantity = rs.getInt("Quantity2");
                    if (unit == null || unit.isEmpty()) {
                        unit = rs.getString("Unit");
                        quantity = rs.getInt("Quantity1");
                    }
                }

                TableProduct data = new TableProduct(
                        rs.getString("MedicineID"),
                        rs.getString("MedicineName"),
                        rs.getString("Barcode"),
                        rs.getString("BatchNo"),
                        rs.getDate("ExpireDate"),
                        unit,
                        rs.getInt("SellingPrice"),
                        quantity
                );
                dataList.add(data);
            }
            conn.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return dataList;
    }

    private void showdata() {
        productList = datalist();
        Column_ItemID.setCellValueFactory(new PropertyValueFactory<>("productId"));
        Column_ItemName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        Column_Batch.setCellValueFactory(new PropertyValueFactory<>("batchNo"));
        Column_ExpDate.setCellValueFactory(new PropertyValueFactory<>("expDate"));
        Column_Item.setCellValueFactory(new PropertyValueFactory<>("item"));
        Column_Price.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        Column_Quantity.setCellValueFactory(new PropertyValueFactory<>("quantityInStock"));

        tableProduct.setItems(productList);
    }

    public AnchorPane createProductAnchorPane(String itemName, String itemPrice, String itemType, String medicineID) {
        // Tạo AnchorPane mới
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefHeight(75.0);
        anchorPane.setPrefWidth(290.0);
        anchorPane.setStyle("-fx-border-color: blue; -fx-border-radius: 8px; -fx-border-width: 1px;");
        anchorPane.setMinWidth(290.0);
        anchorPane.setMaxWidth(290.0);
        anchorPane.setMinHeight(75.0);
        anchorPane.setMaxHeight(75.0);

        // Tạo các thành phần trong AnchorPane
        Text itemNameText = new Text(itemName);
        itemNameText.setId("itemNameText");
        itemNameText.setLayoutX(14.0);
        itemNameText.setLayoutY(18.0);
        itemNameText.setFont(Font.font("Arial Black", 12.0));
        anchorPane.getChildren().add(itemNameText);

        Text medicineIdText = new Text(medicineID);
        medicineIdText.setId("medicineID");
        medicineIdText.setLayoutX(200.0);
        medicineIdText.setLayoutY(18.0);
        medicineIdText.setFont(Font.font("Arial Black", 12.0));
        anchorPane.getChildren().add(medicineIdText);

        Text quantityText = new Text("Quantity");
        quantityText.setLayoutX(14.0);
        quantityText.setLayoutY(41.0);
        anchorPane.getChildren().add(quantityText);

        TextField quantityField = new TextField("1");
        quantityField.setId("quantityField");
        quantityField.setLayoutX(64.0);
        quantityField.setLayoutY(20.0);
        quantityField.setPrefHeight(18.0);
        quantityField.setPrefWidth(45.0);
        quantityField.setStyle("-fx-text-fill: black; -fx-border-color: black; -fx-border-width: 1px; -fx-border-radius: 2px; -fx-alignment: center;");
        anchorPane.getChildren().add(quantityField);

        Text unitText = new Text(itemType);
        unitText.setId("itemTypeText");
        unitText.setLayoutX(115.0);
        unitText.setLayoutY(41.0);
        anchorPane.getChildren().add(unitText);

        Text priceText = new Text("Price:");
        priceText.setLayoutX(170.0);
        priceText.setLayoutY(67.0);
        priceText.setFont(Font.font("Arial Bold", 14.0));
        anchorPane.getChildren().add(priceText);

        Text itemPriceText = new Text(itemPrice + "đ");
        itemPriceText.setId("unitPriceText");
        itemPriceText.setLayoutX(220.0);
        itemPriceText.setLayoutY(67.0);
        itemPriceText.setFont(Font.font("Arial Bold", 14.0));
        anchorPane.getChildren().add(itemPriceText);

        FontAwesomeIcon trashIcon = new FontAwesomeIcon();
        trashIcon.setGlyphName("TRASH");
        trashIcon.setLayoutX(14.0);
        trashIcon.setLayoutY(69.0);
        trashIcon.setSize("20px");
        anchorPane.getChildren().add(trashIcon);

        VBox.setMargin(anchorPane, new Insets(2.0));

        trashIcon.setOnMouseClicked(event -> {
            Node source = (Node) event.getSource();
            AnchorPane paneToRemove = (AnchorPane) source.getParent();

            Text itemNameTextToRemove = (Text) paneToRemove.lookup("#itemNameText");
            if (itemNameTextToRemove != null) {
                String itemNameToRemove = itemNameTextToRemove.getText().trim();

                // Tìm và xóa sản phẩm trong selectedProducts dựa trên productName
                TableProduct productToRemove = null;
                for (TableProduct product : selectedProducts) {
                    if (product.getProductName().equals(itemNameToRemove)) {
                        productToRemove = product;
                        break;
                    }
                }

                if (productToRemove != null) {
                    selectedProducts.remove(productToRemove);
                }

                vbox.getChildren().remove(paneToRemove);
                calculateTotalPrice();

                // Cho phép thêm sản phẩm mới sau khi xóa
                vbox.setDisable(false); // Cho phép sự kiện double-click hoạt động lại
            }
        });

        // Thêm sự kiện thay đổi số lượng
        quantityField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                int quantity = Integer.parseInt(newValue);
                if (quantity < 0) {
                    quantityField.setText(oldValue);
                    return;
                }
                double totalPrice = quantity * Double.parseDouble(itemPrice);
                itemPriceText.setText(totalPrice + "đ");

                // Gọi phương thức tính tổng giá sau khi cập nhật giá
                calculateTotalPrice();
            } catch (NumberFormatException e) {
                if (!newValue.isEmpty()) {
                    quantityField.setText(oldValue);
                }
            }
        });

        return anchorPane;
    }

    private List<TableProduct> selectedProducts = new ArrayList<>();

    @FXML
    public void Table(MouseEvent event) {
        if (!vbox.isDisabled() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
            TableProduct product = tableProduct.getSelectionModel().getSelectedItem();
            if (product != null && !selectedProducts.contains(product)) {
                // Tạo AnchorPane cho sản phẩm
                String itemType = product.getItem();
                AnchorPane anchorPane = createProductAnchorPane(product.getProductName(), String.valueOf(product.getUnitPrice()), itemType, product.getProductId());
                vbox.getChildren().add(anchorPane); // Thêm anchorPane vào VBox

                // Thêm sản phẩm vào danh sách đã chọn
                selectedProducts.add(product);

                // Tính toán tổng giá sau khi thêm sản phẩm
                calculateTotalPrice();
            }
        }
    }

    @FXML
    private void searchItem() {
        String searchItem = OrderSearchItem.getText().trim().replaceAll("\\s+", ""); // Loại bỏ khoảng trắng trong từ khóa tìm kiếm
        productList.clear(); // Xóa dữ liệu cũ

        if (searchItem.isEmpty()) {
            // Nếu không có gì được nhập, hiển thị lại toàn bộ dữ liệu
            productList.addAll(datalist());
        } else {
            // Nếu có từ khóa tìm kiếm, thực hiện truy vấn để lấy dữ liệu tương ứng
            String sql = "SELECT p.MedicineID, pd.Barcode, p.Item ,p.MedicineName, pd.BatchNo, pd.ExpireDate, p.SellingPrice, pd.Quantity1 "
                    + "FROM tblProduct p "
                    + "INNER JOIN tblProductDetails pd ON p.MedicineID = pd.MedicineID";

            try {
               
                Connection conn = DB.ConnectDB.getConnectDB();
                PreparedStatement pst = conn.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    String medicineName = rs.getString("MedicineName").replaceAll("\\s+", ""); // Loại bỏ khoảng trắng trong dữ liệu
                    String barCode = rs.getString("Barcode").replaceAll("\\s+", "");
                    if (medicineName.toLowerCase().contains(searchItem.toLowerCase()) || barCode.toLowerCase().contains(searchItem.toLowerCase())) {
                        TableProduct data = new TableProduct(rs.getString("MedicineID"),
                                rs.getString("MedicineName"),
                                rs.getString("Barcode"),
                                rs.getString("BatchNo"),
                                rs.getDate("ExpireDate"),
                                rs.getString("Item"),
                                rs.getInt("SellingPrice"),
                                rs.getInt("Quantity1"));
                        productList.add(data);
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        tableProduct.setItems(productList);
    }

    private void calculateTotalPrice() {
        double totalPrice = 0;

        for (Node node : vbox.getChildren()) {
            if (node instanceof AnchorPane) {
                AnchorPane anchorPane = (AnchorPane) node;
                for (Node child : anchorPane.getChildren()) {
                    if (child instanceof Text && ((Text) child).getText().contains("đ")) {
                        String priceText = ((Text) child).getText().replace("đ", "").trim();
                        try {
                            double price = Double.parseDouble(priceText);
                            totalPrice += price;
                        } catch (NumberFormatException e) {
                            // Ignore invalid price text
                        }
                    }
                }
            }
        }

        TotalPrice.setText(totalPrice + "đ");
    }

    private void updateQuantityInDatabase(String itemName, int quantityToDeduct, String unit) {
        String sql = "";
        String quantityColumn = "";

        if (unit.equalsIgnoreCase("Pill")) {
            quantityColumn = "Quantity3";
        } else if (unit.equalsIgnoreCase("Blister")) {
            quantityColumn = "Quantity2";
        } else if (unit.equalsIgnoreCase("Box")) {
            quantityColumn = "Quantity1";
        } else {
            // Handle default case if needed
        }

        if (!quantityColumn.isEmpty()) {
            sql = "UPDATE pd SET " + quantityColumn + " = " + quantityColumn + " - ? "
                    + "FROM tblProductDetails pd "
                    + "JOIN tblProduct p ON pd.MedicineID = p.MedicineID "
                    + "WHERE p.MedicineName = ? AND " + quantityColumn + " >= ?";
        } else {
            // Handle case where quantityColumn is not set
            System.out.println("Không xác định được cột số lượng phù hợp cho đơn vị " + unit);
            return;
        }

        try {
            
            Connection conn = DB.ConnectDB.getConnectDB();
            PreparedStatement pst = conn.prepareStatement(sql);

            // Thiết lập các tham số trong câu lệnh SQL
            pst.setInt(1, quantityToDeduct);
            pst.setString(2, itemName);
            pst.setInt(3, quantityToDeduct); // Ensure enough quantity to deduct

            int updatedRows = pst.executeUpdate();
            if (updatedRows > 0) {
                System.out.println("Cập nhật số lượng thành công.");
                showdata(); // Refresh the table data
            } else {
                System.out.println("Không thành công khi cập nhật số lượng sản phẩm trong cơ sở dữ liệu.");
            }
            pst.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showdata();

        tableProduct.setOnMouseClicked(this::Table); // Ensure the event is set here
    }

    @FXML
    void cancle_all(ActionEvent event) {
        vbox.getChildren().clear();
        vbox.setDisable(false);
        selectedProducts.clear();
        // Gọi phương thức tính tổng giá sau khi xóa hết anchorPane
        calculateTotalPrice();
    }

    @FXML
    private void handlePayment() {
        AlertMessage alert = new AlertMessage();

        String phone = OrderSearchCus.getText().trim();
        TableCustomer customer = null;
        int pointsToAdd = 0;

// Kiểm tra nếu số điện thoại không rỗng thì mới tiến hành tìm kiếm khách hàng
        if (!phone.isEmpty()) {
            customerController = new CustomerController();
            // Gọi phương thức findCustomerByPhone của customerController để tìm khách hàng
            customer = customerController.findCustomerByPhone(phone);
            if (customer != null) {
                // Tính tổng thanh toán từ các sản phẩm
                int totalPayment = calculateTotalPayment();
                // Tính điểm tích lũy dựa trên tổng thanh toán
                pointsToAdd = totalPayment / 10000; // Mỗi 10,000 VNĐ tương ứng với 1 điểm
                // Cập nhật điểm tích lũy cho khách hàng
                customerController.updateCustomerPoints(customer.getPhone(), pointsToAdd);
            } else {
                // Hiển thị thông báo lỗi nếu không tìm thấy khách hàng với số điện thoại nhập vào
                alert.errorMessage("Không tìm thấy khách hàng với số điện thoại: " + phone);
                return; // Thoát khỏi phương thức nếu không tìm thấy khách hàng
            }
        }
        int totalPayment = calculateTotalPayment();
        String billID = generateBillID();
        String paymentMethod = PaymentCash.isSelected() ? "Cash" : "Card";
        boolean billInserted = false;
        String CName = "";
        String CPhone = "";
        String cid = "";
        if (customer != null) {
            CName = customer.getName();
            CPhone = customer.getPhone();
            cid = customer.getId();
        }

        // Thêm hóa đơn vào cơ sở dữ liệu
        billInserted = insertIntoBills(billID, CName, CPhone, totalPayment, paymentMethod, new Date(System.currentTimeMillis()), cid);

        // Kiểm tra kết quả và xử lý khi không thêm được hóa đơn
        if (!billInserted) {
            // Xử lý khi không thêm được hóa đơn vào cơ sở dữ liệu
            System.out.println("Failed to insert bill into database.");
            // Hoặc có thể hiển thị thông báo lỗi cho người dùng
        } else {
            // Xử lý khi thêm hóa đơn thành công (ví dụ: hiển thị thông báo thành công)
            System.out.println("Bill inserted successfully.");
        }
        for (Node node : vbox.getChildren()) {
            if (node instanceof AnchorPane) {
                AnchorPane anchorPane = (AnchorPane) node;

                // Lấy thông tin sản phẩm từ AnchorPane
                Text itemNameText = (Text) anchorPane.lookup("#itemNameText");
                TextField quantityField = (TextField) anchorPane.lookup("#quantityField");
                Text unitPriceText = (Text) anchorPane.lookup("#unitPriceText");
                Text itemTypeText = (Text) anchorPane.lookup("#itemTypeText");
                Text medicineIdText = (Text) anchorPane.lookup("#medicineID");

                if (itemNameText != null && quantityField != null && unitPriceText != null && itemTypeText != null && medicineIdText != null) {
                    String itemName = itemNameText.getText().trim();
                    int quantity = 0;
                    try {
                        quantity = Integer.parseInt(quantityField.getText().trim());
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        continue; // Bỏ qua nếu không thể chuyển đổi thành số nguyên
                    }

                    int unitPrice = 0;
                    try {
                        String priceText = unitPriceText.getText().replace("đ", "").trim();
                        double unitPriceDouble = Double.parseDouble(priceText);
                        unitPrice = (int) unitPriceDouble;
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        continue;
                    }

                    String itemType = itemTypeText.getText().trim();
                    String productId = medicineIdText.getText().trim();
                    // Thêm vào bảng BillItems
                    boolean itemInserted = insertIntoBillItems(billID, itemName, quantity, unitPrice, itemType, productId);
                    if (!itemInserted) {
                        alert.errorMessage("Lỗi khi lưu thông tin chi tiết hóa đơn vào cơ sở dữ liệu.");
                        return;
                    }
                }
            }
        }

// Cập nhật số lượng sản phẩm trong cơ sở dữ liệu
        for (Node node : vbox.getChildren()) {
            if (node instanceof AnchorPane) {
                AnchorPane anchorPane = (AnchorPane) node;

                // Lấy thông tin từ AnchorPane
                Text itemNameText = (Text) anchorPane.lookup("#itemNameText");
                TextField quantityField = (TextField) anchorPane.lookup("#quantityField");
                Text unitText = (Text) anchorPane.lookup("#itemTypeText");

                if (itemNameText != null && quantityField != null && unitText != null) {
                    String itemName = itemNameText.getText().trim();
                    int quantityToDeduct = 0;
                    try {
                        quantityToDeduct = Integer.parseInt(quantityField.getText().trim());
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        continue; // Bỏ qua nếu không thể chuyển đổi thành số nguyên
                    }

                    // Lấy đơn vị sản phẩm từ Text
                    String unit = unitText.getText().trim();

                    // Cập nhật số lượng trong cơ sở dữ liệu
                    updateQuantityInDatabase(itemName, quantityToDeduct, unit);
                }
            }
        }

        selectedProducts.clear();
        vbox.getChildren().clear();
        vbox.setDisable(false);
        OrderSearchCus.setText("");
        TotalPrice.setText("0đ");
        alert.successMessage("Payment Successfully");
        tableProduct.refresh();
    }

    private int calculateTotalPayment() {
        int totalPayment = 0;

        for (Node node : vbox.getChildren()) {
            if (node instanceof AnchorPane) {
                AnchorPane anchorPane = (AnchorPane) node;
                for (Node child : anchorPane.getChildren()) {
                    if (child instanceof Text && ((Text) child).getText().contains("đ")) {
                        String priceText = ((Text) child).getText().replace("đ", "").trim();
                        try {
                            double price = Double.parseDouble(priceText);
                            totalPayment += price;
                        } catch (NumberFormatException e) {
                            // Ignore invalid price text
                        }
                    }
                }
            }
        }

        return totalPayment;
    }

    private String generateBillID() {
        // Sinh ngẫu nhiên 5 số
        String randomDigits = String.format("%05d", new Random().nextInt(100000));

        // Ghép thành mã BillID
        String billID = "MB" + randomDigits;
        return billID;
    }

    private boolean insertIntoBills(String billID, String customerName, String customerPhone, int totalPrice, String paymentMethod, Date billDate, String customerId) {
        AlertMessage alert = new AlertMessage();
        if (!(PaymentCash.isSelected() ^ PaymentCard.isSelected())) {
            alert.errorMessage("Please select either 'Cash' or 'Card'");
            return false;
        } else {
            String sql = "INSERT INTO Bills (BillID, CustomerName, TotalPrice, PaymentMethod, BillDate, PhoneCustomer, CustomerId) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try {
               
                Connection conn = DB.ConnectDB.getConnectDB();
                PreparedStatement pst = conn.prepareStatement(sql);

                pst.setString(1, billID);
                pst.setString(2, customerName); // CustomerName từ tham số truyền vào
                pst.setInt(3, totalPrice);
                pst.setString(4, paymentMethod);
                pst.setDate(5, billDate);
                pst.setString(6, customerPhone); // PhoneCustomer từ tham số truyền vào

                if (customerId != null && !customerId.isEmpty()) {
                    pst.setString(7, customerId);
                } else {
                    pst.setNull(7, Types.NVARCHAR); // Set customerId là null nếu không có giá trị, ví dụ loại dữ liệu là VARCHAR
                }

                int rowsAffected = pst.executeUpdate();
                return rowsAffected > 0;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    private boolean insertIntoBillItems(String billID, String itemName, int quantity, int unitPrice, String itemType, String medicineID) {
        String sql = "INSERT INTO BillItems (BillID, ItemName, Quantity, UnitPrice, ItemType,MedicineID) VALUES (?, ?, ?, ?, ?,?)";
        try {
            
            Connection conn = DB.ConnectDB.getConnectDB();
            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setString(1, billID);
            pst.setString(2, itemName);
            pst.setInt(3, quantity);
            pst.setInt(4, unitPrice);
            pst.setString(5, itemType); // Thêm loại sản phẩm vào câu SQL INSERT
            pst.setString(6, medicineID);
            int rowsAffected = pst.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @FXML
    void osearch_cus(ActionEvent event) {

    }

    @FXML
    void osearch_item(ActionEvent event) {
        searchItem();
    }

    @FXML
    void handleKeyReleased(KeyEvent event) {
        searchItem();
    }

    @FXML
    void payment_card(ActionEvent event) {

    }

    @FXML
    void payment_cash(ActionEvent event) {

    }

    @FXML
    void payment_trans(ActionEvent event) {

    }

    @FXML
    void quantity01(ActionEvent event) {

    }

    @FXML
    void quantity02(ActionEvent event) {

    }

    @FXML
    void quantity03(ActionEvent event) {

    }
}
