package sam;

import Login.LoginController;
import demoproject1.FXMLBangluongController;
import demoproject1.FXMLChamluongController;
import demoproject1.FXMLNhanVienController;
import java.io.IOException;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import javafx.scene.chart.LineChart;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class FXMLMainController implements Initializable {

    private LoginController loginController;
    private BillsController billsController;

    public void setBillsController(BillsController billsController) {
        this.billsController = billsController;
    }

    public FXMLMainController() {

    }
    private CustomerController customerController;
    private OrderTransactionController orderTransactionController;

    private static FXMLMainController instance;
    private boolean drawerImageOpen = false;
    @FXML
    private ImageView Bills;

    @FXML
    private ImageView Customer;

    @FXML
    private ImageView Home;

    @FXML
    private ImageView Logout;

    @FXML
    private ImageView OrderReceipt;

    @FXML
    private ImageView Product;

    @FXML
    private ImageView ProductDetails;
    @FXML
    private ImageView Setting;
    @FXML
    private ImageView Selling;

    @FXML
    private ImageView Staff;

    @FXML
    private ImageView Supplier;

    @FXML
    private Button BtnSetting;
    @FXML
    private Label Time;
    private Timeline timeline;
    @FXML
    private Label expiryCountLabel;
    @FXML
    private Button btnLogOut;
    @FXML
    private Button Export;
    @FXML
    private Button User;
    @FXML
    private TextField textField;
    @FXML
    public AnchorPane opacityPane, drawerPane, main;
    @FXML
    private Button drawerImage;
    @FXML
    private Label UsernameTF;
    @FXML
    private ImageView exit;
    @FXML
    private TextArea expiringProductsDetails;
    @FXML
    private Label orderCountLabelInvoide;
    @FXML
    private Label orderCountLabel;

    public static FXMLMainController getInstance() {
        return instance;
    }

    public void setFullName(String fullName) {
        UsernameTF.setText(fullName);
        UsernameTF.setFont(Font.font("System", 20.0));
    }

    public void loadFXML(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();

            if (fxml.equals("/demoproject1/FXMLChamluong.fxml")) {
                FXMLChamluongController controller = loader.getController();
                controller.setMainController(this); // Thiết lập mainController cho FXMLChamluongController
            } else if (fxml.equals("/demoproject1/FXMLNhanVien.fxml")) {
                FXMLNhanVienController controller = loader.getController();
                controller.setMainController(this); // Thiết lập mainController cho FXMLNhanvienController
            } else if (fxml.equals("/demoproject1/FXMLBangluong.fxml")) {
                FXMLBangluongController controller = loader.getController();
                controller.setMainController(this); // Thiết lập mainController cho FXMLBangluongController
            }

            main.getChildren().clear();
            main.getChildren().add(root);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void handleSupplier() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/eproject/Supplier.fxml"));
            Parent productPane = loader.load();

            main.getChildren().clear();
            main.getChildren().add(productPane);
            opacityPane.setVisible(true);

        } catch (IOException e) {
            e.printStackTrace();
        }
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), opacityPane);
        fadeTransition.setFromValue(0.15);
        fadeTransition.setToValue(0);
        fadeTransition.setOnFinished(event -> opacityPane.setVisible(false));
        fadeTransition.play();

        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.1), drawerPane);
        translateTransition.setByX(-600);
        translateTransition.play();

        drawerImageOpen = false;
    }

    @FXML
    private void handleOrderReceipt() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/eproject/ImportOrderReceipt.fxml"));
            Parent productPane = loader.load();

            main.getChildren().clear();
            main.getChildren().add(productPane);
            opacityPane.setVisible(true);

        } catch (IOException e) {
            e.printStackTrace();
        }
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), opacityPane);
        fadeTransition.setFromValue(0.15);
        fadeTransition.setToValue(0);
        fadeTransition.setOnFinished(event -> opacityPane.setVisible(false));
        fadeTransition.play();

        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.1), drawerPane);
        translateTransition.setByX(-600);
        translateTransition.play();

        drawerImageOpen = false;
    }

    @FXML
    private void handleProduct() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Druglist/Druglist.fxml"));
            Parent productPane = loader.load();

            main.getChildren().clear();
            main.getChildren().add(productPane);
            opacityPane.setVisible(true);

        } catch (IOException e) {
            e.printStackTrace();
        }
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), opacityPane);
        fadeTransition.setFromValue(0.15);
        fadeTransition.setToValue(0);
        fadeTransition.setOnFinished(event -> opacityPane.setVisible(false));
        fadeTransition.play();

        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.1), drawerPane);
        translateTransition.setByX(-600);
        translateTransition.play();

        drawerImageOpen = false;
    }

    @FXML
    private void handleSetting() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AccountInfo/AccountInfo.fxml"));
            Parent productDetails = loader.load();

            main.getChildren().clear();
            main.getChildren().add(productDetails);
            opacityPane.setVisible(true);

        } catch (IOException e) {
            e.printStackTrace();
        }
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), opacityPane);
        fadeTransition.setFromValue(0.15);
        fadeTransition.setToValue(0);
        fadeTransition.setOnFinished(event -> opacityPane.setVisible(false));
        fadeTransition.play();

        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.1), drawerPane);
        translateTransition.setByX(-600);
        translateTransition.play();

        drawerImageOpen = false;
    }

    @FXML
    private void handleProductDetails() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ImportMedicineDetails/ImportMedicineDetails.fxml"));
            Parent productDetails = loader.load();

            main.getChildren().clear();
            main.getChildren().add(productDetails);
            opacityPane.setVisible(true);

        } catch (IOException e) {
            e.printStackTrace();
        }
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), opacityPane);
        fadeTransition.setFromValue(0.15);
        fadeTransition.setToValue(0);
        fadeTransition.setOnFinished(event -> opacityPane.setVisible(false));
        fadeTransition.play();

        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.1), drawerPane);
        translateTransition.setByX(-600);
        translateTransition.play();

        drawerImageOpen = false;
    }

    @FXML
    private void handleSellingButtonClick() {
        try {
            // Load the OrderTransaction.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("OrderTransaction.fxml"));
            Parent orderTransactionPane = loader.load();

            // Access the controller instance from the loader
            OrderTransactionController orderTransactionController = loader.getController();

            // Set the CustomerController instance in OrderTransactionController
            orderTransactionController.setCustomerController(new CustomerController());

            // Clear the main pane and add OrderTransaction.fxml to it
            main.getChildren().clear();
            main.getChildren().add(orderTransactionPane);

            // Show the opacity pane
            opacityPane.setVisible(true);

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Animation to fade out opacity pane
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), opacityPane);
        fadeTransition.setFromValue(0.15);
        fadeTransition.setToValue(0);
        fadeTransition.setOnFinished(event -> opacityPane.setVisible(false));
        fadeTransition.play();

        // Animation to slide drawer pane out of view
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.1), drawerPane);
        translateTransition.setByX(-600);
        translateTransition.play();

        drawerImageOpen = false;
    }

    @FXML
    private void handleCustomerClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Customer.fxml"));
            Parent customerPane = loader.load();

            main.getChildren().clear();
            main.getChildren().add(customerPane);
            opacityPane.setVisible(true);

        } catch (IOException e) {
            e.printStackTrace();
        }
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), opacityPane);
        fadeTransition.setFromValue(0.15);
        fadeTransition.setToValue(0);
        fadeTransition.setOnFinished(event -> opacityPane.setVisible(false));
        fadeTransition.play();

        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.1), drawerPane);
        translateTransition.setByX(-600);
        translateTransition.play();

        drawerImageOpen = false;
    }

    @FXML
    private void handleBillsClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Bills.fxml"));
            Parent customerPane = loader.load();

            main.getChildren().clear();
            main.getChildren().add(customerPane);
            opacityPane.setVisible(true);

        } catch (IOException e) {
            e.printStackTrace();
        }
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), opacityPane);
        fadeTransition.setFromValue(0.15);
        fadeTransition.setToValue(0);
        fadeTransition.setOnFinished(event -> opacityPane.setVisible(false));
        fadeTransition.play();

        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.1), drawerPane);
        translateTransition.setByX(-600);
        translateTransition.play();

        drawerImageOpen = false;
    }

    @FXML
    private void handleDashboardButtonClick() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLMain.fxml"));
            Parent mainPane = loader.load();

            main.getChildren().clear();
            main.getChildren().add(mainPane);

            opacityPane.setVisible(true);

        } catch (IOException e) {
            e.printStackTrace();

        }

        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), opacityPane);
        fadeTransition.setFromValue(0.15);
        fadeTransition.setToValue(0);
        fadeTransition.setOnFinished(event -> opacityPane.setVisible(false));
        fadeTransition.play();

        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.1), drawerPane);
        translateTransition.setByX(-600); // Move drawerPane to the left
        translateTransition.play();

        drawerImageOpen = false;
    }

    @FXML
    private void handleUserButtonClick() {

        loadFXML("/demoproject1/FXMLNhanVien.fxml");
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), opacityPane);
        fadeTransition.setFromValue(0.15);
        fadeTransition.setToValue(0);
        fadeTransition.setOnFinished(event -> opacityPane.setVisible(false));
        fadeTransition.play();

        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.1), drawerPane);
        translateTransition.setByX(-600);
        translateTransition.play();

        drawerImageOpen = false;
    }

    public void updateLabels() {
        // Call updateTotalBillsAndRevenue from BillsController
        billsController.updateTotalBillsAndRevenue();
        // Retrieve updated values
        int totalBills = billsController.getTotalBills();
        int totalRevenue = billsController.getTotalRevenue();

        // Update labels in FXMLStaff
        orderCountLabel.setText(String.format("%d Invoices", totalBills));
        orderCountLabelInvoide.setText(String.format("%dđ", totalRevenue));
    }

    @FXML
    private LineChart<String, Number> revenueChart;
    private final CategoryAxis xAxis = new CategoryAxis();
    private final NumberAxis yAxis = new NumberAxis();

    public void updateRevenueChart() {
        revenueChart.getData().clear(); // Xóa dữ liệu hiện tại trên biểu đồ

        // Lấy dữ liệu tổng doanh thu theo từng ngày trong tháng hiện tại
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 4); // Đặt ngày là ngày đầu tiên của tháng

        try {
           
            Connection conn = DB.ConnectDB.getConnectDB();
            // Lặp qua từng ngày trong tháng và lấy tổng doanh thu
            while (calendar.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR)
                    && calendar.get(Calendar.MONTH) <= Calendar.getInstance().get(Calendar.MONTH)) {
                String currentDate = sdf.format(calendar.getTime());
                String sql = "SELECT SUM(TotalPrice) AS TotalRevenue FROM Bills WHERE BillDate = ?";

                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, currentDate);
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    int totalRevenue = rs.getInt("TotalRevenue");
                    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                    // Thêm dữ liệu vào series, chuyển totalRevenue thành Integer
                    series.getData().add(new XYChart.Data<>(currentDate, totalRevenue));
                }

                pst.close();
                calendar.add(Calendar.DAY_OF_MONTH, 1); // Chuyển sang ngày tiếp theo

            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Thêm series vào biểu đồ
        revenueChart.getData().add(series);
    }
  

    @FXML
    private void checkAllProductsExpiryInfo() {
        List<ProductExpiryTable> expiringProductsList = fetchExpiringProductsFromDatabase();

        // Update UI on the JavaFX Application Thread
        Platform.runLater(() -> {
            expiryCountLabel.setText("There are " + expiringProductsList.size() + " items that will expire within 1 month.");
        });
    }

    @FXML
    private void showProductExpiryInfoForm() {
        List<ProductExpiryTable> expiringProductsList = fetchExpiringProductsFromDatabase();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sam/ProductExpiryInfo.fxml"));
            Parent root = loader.load();
            ProductExpiryInfoController productExpiryInfoController = loader.getController();
            productExpiryInfoController.setExpiringProducts(expiringProductsList);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();

        } catch (IOException ex) {
            System.out.println("Error loading ProductExpiryInfo.fxml: " + ex.getMessage());
        }
    }

    private List<ProductExpiryTable> fetchExpiringProductsFromDatabase() {
        List<ProductExpiryTable> expiringProductsList = new ArrayList<>();
        String sql = "SELECT p.MedicineID, p.MedicineName, pd.ManufacturingDate, pd.ExpireDate "
                + "FROM tblProductDetails pd "
                + "JOIN tblProduct p ON pd.MedicineID = p.MedicineID";

        try (Connection conn =  DB.ConnectDB.getConnectDB(); PreparedStatement pst = conn.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                String medicineID = rs.getString("MedicineID");
                String medicineName = rs.getString("MedicineName");
                LocalDate manufacturingDate = rs.getDate("ManufacturingDate").toLocalDate();
                LocalDate expireDate = rs.getDate("ExpireDate").toLocalDate();

                long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), expireDate);

                if (daysLeft >= 0 && daysLeft <= 30) {
                    // Tính số ngày còn lại đến ngày hết hạn
                    long daysUntilExpired = ChronoUnit.DAYS.between(LocalDate.now(), expireDate);

                    // Tạo đối tượng Product và thêm vào danh sách
                    ProductExpiryTable product = new ProductExpiryTable(medicineID, medicineName, manufacturingDate, expireDate, daysUntilExpired);
                    expiringProductsList.add(product);
                }
            }

        } catch (Exception e) {
            System.out.println("Error fetching product details: " + e.getMessage());
        }

        return expiringProductsList;
    }

    @FXML
    private void handleLogout() {
        try {
            // Load login.fxml
            Parent root = FXMLLoader.load(getClass().getResource("/Login/Login.fxml"));
            Scene scene = new Scene(root);
            String cssPath = getClass().getResource("/sam/button-style.css").toExternalForm();
            scene.getStylesheets().add(cssPath);

            Stage stage = (Stage) btnLogOut.getScene().getWindow(); // Get the current stage
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle exception properly in your application
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //load
        //timeline
        timeline = new Timeline(
                new KeyFrame(Duration.millis(500), event -> {
                    updateTime(); // Cập nhật thời gian mỗi giây
                })
        );
        timeline.setCycleCount(Animation.INDEFINITE); // Lặp vô hạn
        timeline.play();
        //labelBills
        billsController = new BillsController();
        updateLabels();
        //linechar
        revenueChart.getData().clear();
        updateRevenueChart();
        //producExpiry
        checkAllProductsExpiryInfo();
        exit.setOnMouseClicked(event -> {
            System.exit(0);
        });

        opacityPane.setVisible(false);

        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), opacityPane);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.play();

        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.1), drawerPane);
        translateTransition.setByX(-600);
        translateTransition.play();

        drawerImage.setOnMouseClicked(event -> {
            if (!drawerImageOpen) {
                opacityPane.setVisible(true);
                FadeTransition fadeTransition1 = new FadeTransition(Duration.seconds(0.5), opacityPane);
                fadeTransition1.setFromValue(0);
                fadeTransition1.setToValue(0.15);
                fadeTransition1.play();

                TranslateTransition translateTransition1 = new TranslateTransition(Duration.seconds(0.1), drawerPane);
                translateTransition1.setByX(600);
                translateTransition1.play();

                drawerImageOpen = true;
            } else {
                FadeTransition fadeTransition1 = new FadeTransition(Duration.seconds(0.5), opacityPane);
                fadeTransition1.setFromValue(0.15);
                fadeTransition1.setToValue(0);
                fadeTransition1.setOnFinished(event1 -> opacityPane.setVisible(false));
                fadeTransition1.play();

                TranslateTransition translateTransition1 = new TranslateTransition(Duration.seconds(0.1), drawerPane);
                translateTransition1.setByX(-600); // Move drawerPane to the left
                translateTransition1.play();

                drawerImageOpen = false;
            }
        });
        Home.setOnMouseClicked(event -> {
            loadFXML("FXMLMain.fxml");

        });
        Staff.setOnMouseClicked(event -> {
            loadFXML("/demoproject1/FXMLNhanVien.fxml");
        });
        Customer.setOnMouseClicked(event -> {
            loadFXML("Customer.fxml");
        });
        Product.setOnMouseClicked(event -> {
            loadFXML("/Druglist/Druglist.fxml");
        });
        Bills.setOnMouseClicked(event -> {
            loadFXML("Bills.fxml");
        });
        ProductDetails.setOnMouseClicked(event -> {
            loadFXML("/ImportMedicineDetails/ImportMedicineDetails.fxml");
        });
        OrderReceipt.setOnMouseClicked(event -> {
            loadFXML("/eproject/ImportOrderReceipt.fxml");
        });
        Setting.setOnMouseClicked(event -> {
            loadFXML("/AccountInfo/AccountInfo.fxml");
        });
        Supplier.setOnMouseClicked(event -> {
            loadFXML("/eproject/Supplier.fxml");
        });
        Selling.setOnMouseClicked(event -> {
            loadFXML("OrderTransaction.fxml");
        });
        Logout.setOnMouseClicked(event -> {
            try {
                // Load login.fxml
                Parent root = FXMLLoader.load(getClass().getResource("/Login/Login.fxml"));
                Scene scene = new Scene(root);
//                String cssPath = getClass().getResource("/sam/button-style.css").toExternalForm();
//                scene.getStylesheets().add(cssPath);

                Stage stage = (Stage) Logout.getScene().getWindow(); // Get the current stage
                stage.setScene(scene);
                stage.centerOnScreen();
                stage.show();
            } catch (IOException e) {
                e.printStackTrace(); // Handle exception properly in your application
            }
        });
    }

    private void updateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm d MMMM yyyy");
        String formattedDateTime = now.format(formatter);
        Time.setText(formattedDateTime); // Đặt giá trị vào Label có fx:id là Time
    }
}
