/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sam;

import Login.LoginController;
import demoproject1.FXMLBangluongController;
import demoproject1.FXMLChamluongController;
import demoproject1.FXMLNhanVienController;
import java.sql.Date;
import java.sql.*;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author ADMIN
 */
public class FXMLStaffController implements Initializable {

    public void setBillsController(BillsController billsController) {
        this.billsController = billsController;
    }
    private CustomerController customerController;
    private OrderTransactionController orderTransactionController;
    private boolean drawerImageOpen = false;
    @FXML
    private ImageView Bill;

    @FXML
    private ImageView Customer;

    @FXML
    private ImageView Home;

    @FXML
    private ImageView Logout;

    @FXML
    private ImageView Order;
    @FXML
    private Label Time;
    private Timeline timeline;
    @FXML
    private Label expiryCountLabel;
    @FXML
    private Button btnLogOut;
    @FXML
    private Button admin;
    @FXML
    private Label totalBillsLabel;
    @FXML
    private Label UserNameLB;
    @FXML
    private Label totalRevenueLabel;
    @FXML
    private Button drawerImage;
    @FXML
    private Button Selling;
    @FXML
    private AnchorPane drawerPane;
    @FXML
    private AnchorPane main;
    private LoginController loginController;
    private BillsController billsController;
    @FXML
    private AnchorPane opacityPane;

    @FXML
    private ImageView exit;

    public void setFullName(String fullName) {
        UserNameLB.setText(fullName);
        UserNameLB.setFont(Font.font("System", 20.0));
    }

    public void handleDashboardButtonClick() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sam/FXMLStaff.fxml"));
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
    private void handleCustomer() {
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
    private void handleBills() {
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

    public void updateLabels() {
        // Call updateTotalBillsAndRevenue from BillsController
        billsController.updateTotalBillsAndRevenue();

        // Retrieve updated values
        int totalBills = billsController.getTotalBills();
        int totalRevenue = billsController.getTotalRevenue();

        // Update labels in FXMLStaff
        totalBillsLabel.setText(String.format("%d Invoices", totalBills));
        totalRevenueLabel.setText(String.format("%dđ", totalRevenue));
    }

    @FXML
    private void handleSelling() {
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
    private LineChart<String, Number> revenueChart;
    private final CategoryAxis xAxis = new CategoryAxis();
    private final NumberAxis yAxis = new NumberAxis();

    public void updateRevenueChart() {
        revenueChart.getData().clear(); // Xóa dữ liệu hiện tại trên biểu đồ

        // Lấy dữ liệu tổng doanh thu theo từng ngày trong tháng hiện tại
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1); // Đặt ngày là ngày đầu tiên của tháng

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
    public void initialize(URL url, ResourceBundle rb) {
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
        //productExpiry
        checkAllProductsExpiryInfo();
        //linechar
        revenueChart.getData().clear();
        updateRevenueChart();

        exit.setOnMouseClicked(event -> {
            System.exit(0);
        });
        opacityPane.setVisible(false);

        // Initial fade out and translation of the drawer
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
                translateTransition1.setByX(600); // Move drawerPane to the right
                translateTransition1.play();

                drawerImageOpen = true; // Set state to open
            } else { // Close drawer if it's open
                FadeTransition fadeTransition1 = new FadeTransition(Duration.seconds(0.5), opacityPane);
                fadeTransition1.setFromValue(0.15);
                fadeTransition1.setToValue(0);
                fadeTransition1.setOnFinished(event1 -> opacityPane.setVisible(false));
                fadeTransition1.play();

                TranslateTransition translateTransition1 = new TranslateTransition(Duration.seconds(0.1), drawerPane);
                translateTransition1.setByX(-600); // Move drawerPane to the left
                translateTransition1.play();

                drawerImageOpen = false; // Set state to close
            }
        });
        Home.setOnMouseClicked(event -> {
            loadFXML("/sam/FXMLMain.fxml");
        });
        Customer.setOnMouseClicked(event -> {
            loadFXML("/sam/Customer.fxml");
        });
        Order.setOnMouseClicked(event -> {
            loadFXML("/sam/OrderTransaction.fxml");
        });
        Bill.setOnMouseClicked(event -> {
            loadFXML("/sam/Bills.fxml");
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

   

    @FXML
    private void checkAllProductsExpiryInfo() {
        List<ProductExpiryTable> expiringProductsList = fetchExpiringProductsFromDatabase();

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

        try (Connection conn = DB.ConnectDB.getConnectDB(); PreparedStatement pst = conn.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {

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

    private void updateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm d MMMM yyyy");
        String formattedDateTime = now.format(formatter);
        Time.setText(formattedDateTime); // Đặt giá trị vào Label có fx:id là Time
    }

        public void loadFXML(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();

            main.getChildren().clear();
            main.getChildren().add(root);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
