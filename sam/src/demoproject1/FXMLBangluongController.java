package demoproject1;

import DB.ConnectDB;
import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import sam.FXMLMainController;

public class FXMLBangluongController implements Initializable {

    @FXML
    private FXMLMainController mainController;
    @FXML
    private Button btnExport;

    @FXML
    private Button btnthempayroll;

    @FXML
    private TableColumn<TableBangluong, Integer> cotgiotieuchuan;

    @FXML
    private TableColumn<TableBangluong, String> cotstaffid;

    @FXML
    private TableColumn<TableBangluong, Date> cotngaybatdau;

    @FXML
    private TableColumn<TableBangluong, Date> cotngayketthuc;

    @FXML
    private TableColumn<TableBangluong, Integer> cotngaytieuchuan;

    @FXML
    private TableColumn<TableBangluong, BigDecimal> cotsongaylam;

    @FXML
    private TableColumn<TableBangluong, BigDecimal> cottangca;

    @FXML
    private TableColumn<TableBangluong, BigDecimal> cottraluong;

    @FXML
    private TableColumn<TableBangluong, Integer> cotID;

    @FXML
    private TableColumn<TableBangluong, BigDecimal> cotSalary;

    @FXML
    private TableView<TableBangluong> tablepayroll;

    @FXML
    private TextField txttukhoa;

    @FXML
    private Button btnStaff;

    @FXML
    private Button btnTimekeeping;

    @FXML
    private TableColumn<TableBangluong, Void> cotaction; // Để sử dụng Void vì không cần dữ liệu, chỉ cần hiển thị nút

    public ConnectDB connect = new ConnectDB();
    private Connection conn;
    private PreparedStatement pst;
    private ResultSet rs;

    public ObservableList<TableBangluong> dataList() {
        ObservableList<TableBangluong> dataList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM tblPayroll";

        try {
            conn = connect.getConnectDB();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                TableBangluong data = new TableBangluong(
                        rs.getInt("ID"),
                        rs.getString("StaffID"),
                        rs.getDate("PayPeriodStart"),
                        rs.getDate("PayPeriodEnd"),
                        rs.getInt("StandardDays"),
                        rs.getInt("StandardHours"),
                        rs.getBigDecimal("TotalWorkDays"),
                        rs.getBigDecimal("Overtime"),
                        rs.getBigDecimal("TotalPay"),
                        rs.getBigDecimal("Salary")
                );
                dataList.add(data);
            }
        } catch (Exception e) {
            System.out.println("Error while fetching data: " + e.getMessage());
        }

        return dataList;
    }

    public void showDataBangluong() {
        ObservableList<TableBangluong> showList = dataList();
        cotID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        cotstaffid.setCellValueFactory(new PropertyValueFactory<>("staffID"));
        cotngaybatdau.setCellValueFactory(new PropertyValueFactory<>("payPeriodStart"));
        cotngayketthuc.setCellValueFactory(new PropertyValueFactory<>("payPeriodEnd"));
        cotngaytieuchuan.setCellValueFactory(new PropertyValueFactory<>("standardDays"));
        cotgiotieuchuan.setCellValueFactory(new PropertyValueFactory<>("standardHours"));
        cotsongaylam.setCellValueFactory(new PropertyValueFactory<>("totalWorkDays"));
        cottangca.setCellValueFactory(new PropertyValueFactory<>("overtime"));
        cottraluong.setCellValueFactory(new PropertyValueFactory<>("totalPay"));
        cotSalary.setCellValueFactory(new PropertyValueFactory<>("Salary"));

        cotID.setCellFactory(column -> {
            return new TableCell<TableBangluong, Integer>() {
                @Override
                protected void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        setText(String.valueOf(getIndex() + 1));
                    }
                }
            };
        });

        tablepayroll.setItems(showList);
    }

    //search
    public void searchStaff() {
        String keyword = txttukhoa.getText().toLowerCase().trim();

        ObservableList<TableBangluong> filteredList = FXCollections.observableArrayList();

        for (TableBangluong staff : dataList()) {
            if (staff.getStaffID().toLowerCase().contains(keyword)) {
                filteredList.add(staff);
            }
        }

        tablepayroll.setItems(filteredList);
    }

    //chức năng xuất dữ liêu ra excel bằng nút Export
    public void exportToCSV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save CSV File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File selectedFile = fileChooser.showSaveDialog(btnExport.getScene().getWindow());

        if (selectedFile != null) {
            try {
                Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(selectedFile), StandardCharsets.UTF_8));
                writer.write('\uFEFF'); // BOM for UTF-8

                // Ghi tiêu đề cột
                TableView<TableBangluong> tableView = tablepayroll;
                ObservableList<TableColumn<TableBangluong, ?>> columns = tableView.getColumns();
                for (int i = 0; i < columns.size(); i++) {
                    TableColumn<TableBangluong, ?> column = columns.get(i);
                    if (i > 0) {
                        writer.write(",");
                    }
                    writer.write(column.getText());
                }
                writer.write("\n");

                // Ghi dữ liệu từng dòng
                ObservableList<TableBangluong> dataList = tableView.getItems();
                for (TableBangluong staff : dataList) {
                    for (int i = 0; i < columns.size(); i++) {
                        TableColumn<TableBangluong, ?> column = columns.get(i);
                        if (i > 0) {
                            writer.write(",");
                        }
                        // Lấy giá trị từ cell của bảng và ghi vào writer
                        Object cellValue = column.getCellData(staff);
                        writer.write(cellValue != null ? cellValue.toString() : "");
                    }
                    writer.write("\n");
                }
                writer.flush();
                writer.close();

                Thuvien tv = new Thuvien(); // Thêm dòng này nếu Thuvien cần được sử dụng
                tv.showAlert("Data exported to CSV successfully!");

                // Mở file CSV sau khi export
                Desktop.getDesktop().open(selectedFile);

            } catch (IOException e) {
                System.out.println("Error while exporting to CSV: " + e.getMessage());
            }
        }
    }

    //action
    public void setupActionColumn() {
        // Thiết lập cell factory cho cột hành động.
        cotaction.setCellFactory(new Callback<TableColumn<TableBangluong, Void>, TableCell<TableBangluong, Void>>() {
            @Override
            public TableCell<TableBangluong, Void> call(TableColumn<TableBangluong, Void> param) {
                // Tạo một TableCell mới để chứa các nút hành động.
                return new TableCell<TableBangluong, Void>() {
                    // Khai báo và khởi tạo các nút Update và Delete.
                    private final Button btnUpdate = new Button("Update");
                    private final Button btnDelete = new Button("Delete");

                    {
                        // Đặt style cho các nút.
                        btnUpdate.getStyleClass().add("button-update");
                        btnDelete.getStyleClass().add("button-delete");

                        // Thiết lập sự kiện khi nhấn nút Update.
                        btnUpdate.setOnAction(event -> {
                            // Lấy dữ liệu của dòng hiện tại.
                            TableBangluong data = getTableView().getItems().get(getIndex());
                            try {
                                // Tải FXML của form cập nhật.
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLUpdatePayroll.fxml"));
                                Parent root = loader.load();

                                // Lấy controller của form cập nhật và thiết lập dữ liệu cần cập nhật.
                                FXMLUpdatePayrollController controller = loader.getController();
                                controller.setUpdatePayroll(data);
                                controller.setParentController(FXMLBangluongController.this);

                                // Tạo một stage mới và thiết lập các thuộc tính.
                                Scene scene = new Scene(root);
                                Stage newStage = new Stage();
                                newStage.setScene(scene);
                                newStage.initModality(Modality.APPLICATION_MODAL);
                                newStage.initStyle(StageStyle.UNDECORATED);

                                // Lấy vị trí của cửa sổ hiện tại để đặt cửa sổ mới ở giữa màn hình.
                                Stage currentStage = (Stage) btnUpdate.getScene().getWindow();
                                double centerXPosition = currentStage.getX() + currentStage.getWidth() / 2d;
                                double centerYPosition = currentStage.getY() + currentStage.getHeight() / 2d;
                                newStage.setX(centerXPosition - root.prefWidth(-1) / 2d);
                                newStage.setY(centerYPosition - root.prefHeight(-1) / 2d);
                                newStage.show();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });

                        // Thiết lập sự kiện khi nhấn nút Delete.
                        btnDelete.setOnAction(event -> {
                            // Lấy dữ liệu của dòng hiện tại.
                            TableBangluong data = getTableView().getItems().get(getIndex());
                            // Hiển thị hộp thoại xác nhận xóa.
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Confirm Delete");
                            alert.setHeaderText("Are you sure you want to delete this record?");
                            alert.setContentText("This action cannot be undone.");

                            // Xử lý khi người dùng xác nhận xóa.
                            alert.showAndWait().ifPresent(response -> {
                                if (response == ButtonType.OK) {
                                    try {
                                        // Kết nối cơ sở dữ liệu và thực hiện câu lệnh xóa.
                                        conn = connect.getConnectDB();
                                        String sql = "DELETE FROM tblPayroll WHERE StaffID = ?";
                                        pst = conn.prepareStatement(sql);
                                        pst.setString(1, data.getStaffID());
                                        pst.executeUpdate();

                                        // Xóa dòng dữ liệu khỏi bảng.
                                        getTableView().getItems().remove(data);
                                        System.out.println("Delete clicked for " + data.getStaffID());
                                    } catch (Exception e) {
                                        System.out.println("Error while deleting payroll: " + e.getMessage());
                                    } finally {
                                        try {
                                            if (pst != null) {
                                                pst.close();
                                            }
                                            if (conn != null) {
                                                conn.close();
                                            }
                                        } catch (Exception e) {
                                            System.out.println("Error while closing connection: " + e.getMessage());
                                        }
                                    }
                                }
                            });
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);

                        // Nếu dòng hiện tại không có dữ liệu, ẩn các nút.
                        if (empty) {
                            setGraphic(null);
                        } else {
                            // Nếu có dữ liệu, hiển thị các nút Update và Delete.
                            HBox hbox = new HBox(btnUpdate, btnDelete);
                            hbox.setSpacing(5);
                            setGraphic(hbox);
                        }
                    }
                };
            }
        });
    }

    public void setMainController(FXMLMainController mainController) {
        this.mainController = mainController;
    }

    //chuyen form nhan vien
    @FXML
    private void goToNhanvienForm() {
        if (mainController != null) {
            mainController.loadFXML("/demoproject1/FXMLNhanVien.fxml");
        } else {
            System.err.println("FXMLBangluongController: MainController is null, cannot load FXMLNhanVien.fxml");
        }
    }

    @FXML
    private void goToChamluongFormFromBangluong() {
        if (mainController != null) {
            mainController.loadFXML("/demoproject1/FXMLChamluong.fxml");
        } else {
            System.err.println("FXMLBangluongController: MainController is null, cannot load FXMLChamluong.fxml");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showDataBangluong();
        setupActionColumn(); //them nut vao action
        //search
        txttukhoa.textProperty().addListener((observable, oldValue, newValue) -> {
            searchStaff();
        });
        //xuất ra excel
        btnExport.setOnAction(event -> exportToCSV());

        // Bắt sự kiện khi nhấn nút Chuyển form
        btnthempayroll.setOnAction(event -> {
            try {
                // Load file FXML của form mới
                FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLThemPayroll.fxml"));
                Parent root = loader.load();

                // Tạo một Scene mới
                Scene scene = new Scene(root);

                // Tạo một Stage mới cho form mới
                Stage newStage = new Stage();

                // Đặt modality để đảm bảo form mới đè lên và không thao tác được form cũ
                newStage.initModality(Modality.APPLICATION_MODAL);

                // Đặt style để không có nút đóng góc
                newStage.initStyle(StageStyle.UNDECORATED);

                // Đặt Scene vào Stage mới
                newStage.setScene(scene);

                // Lấy Stage hiện tại từ Button
                Stage currentStage = (Stage) btnthempayroll.getScene().getWindow();

                // Tính toán vị trí để đưa form mới vào giữa màn hình
                double centerXPosition = currentStage.getX() + currentStage.getWidth() / 2d;
                double centerYPosition = currentStage.getY() + currentStage.getHeight() / 2d;

                // Thiết lập vị trí của Stage mới ở giữa màn hình
                newStage.setX(centerXPosition - root.prefWidth(-1) / 2d);
                newStage.setY(centerYPosition - root.prefHeight(-1) / 2d);

                // Hiển thị Stage mới và không đóng Stage cũ
                newStage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }
}
