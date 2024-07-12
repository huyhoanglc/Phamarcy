package demoproject1;

import DB.ConnectDB;
import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sam.FXMLMainController;

/**
 * FXML Controller class
 *
 * @author nguye
 */
public class FXMLChamluongController implements Initializable {

    @FXML
    private FXMLMainController mainController;
    @FXML
    private Button btnPayroll;

    @FXML
    private Button btnCheckin;

    @FXML
    private Button btnStaff;
    @FXML
    private Button btnExport;

    @FXML
    private TableColumn<TableChamluong, Integer> cotID;

    @FXML
    private TableColumn<TableChamluong, String> cotStaffid;

    @FXML
    private TableColumn<TableChamluong, String> cotStaffname;

    @FXML
    private TableColumn<TableChamluong, Date> cotcheckin;

    @FXML
    private TableColumn<TableChamluong, Date> cotcheckout;

    @FXML
    private TableColumn<TableChamluong, Date> cotday;

    @FXML
    private TableView<TableChamluong> tableTimekeeping;

    @FXML
    private TextField txttukhoa;

    public ConnectDB connect = new ConnectDB();
    private Connection conn;
    private PreparedStatement pst;
    private ResultSet rs;

    /**
     * Initializes the controller class.
     */
    public ObservableList<TableChamluong> dataList() {
        ObservableList<TableChamluong> dataList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM tblTimekeeping";

        try {
            conn = connect.getConnectDB();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                TableChamluong data = new TableChamluong(
                        rs.getInt("ID"),
                        rs.getString("StaffID"),
                        rs.getString("Staffname"),
                        rs.getDate("Day"),
                        rs.getTime("Checkin"),
                        rs.getTime("Checkout")
                );
                dataList.add(data);
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi lấy dữ liệu: " + e.getMessage());
        } finally {
            closeResources();
        }

        return dataList;
    }

    public void showDataChamluong() {
        ObservableList<TableChamluong> showList = dataList();

        cotID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        cotStaffid.setCellValueFactory(new PropertyValueFactory<>("StaffID"));
        cotStaffname.setCellValueFactory(new PropertyValueFactory<>("Staffname"));
        cotday.setCellValueFactory(new PropertyValueFactory<>("Day"));
        cotcheckin.setCellValueFactory(new PropertyValueFactory<>("Checkin"));
        cotcheckout.setCellValueFactory(new PropertyValueFactory<>("Checkout"));

        cotID.setCellFactory(column -> {
            return new TableCell<TableChamluong, Integer>() {
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

        tableTimekeeping.setItems(showList);
    }

    @FXML
    public void refreshTable() {
        showDataChamluong();
    }

    public void searchStaff() {
        String keyword = txttukhoa.getText().toLowerCase().trim();

        if (keyword.isEmpty()) {
            showDataChamluong(); // Nếu ô tìm kiếm trống, hiển thị lại dữ liệu ban đầu
            return;
        }

        ObservableList<TableChamluong> filteredList = FXCollections.observableArrayList();

        // Thực hiện truy vấn tìm kiếm
        String sql = "SELECT * FROM tblTimekeeping WHERE StaffID LIKE ?";

        try {
            conn = connect.getConnectDB();
            pst = conn.prepareStatement(sql);
            pst.setString(1, "%" + keyword + "%");
            rs = pst.executeQuery();

            while (rs.next()) {
                TableChamluong data = new TableChamluong(
                        rs.getInt("ID"),
                        rs.getString("StaffID"),
                        rs.getString("Staffname"),
                        rs.getDate("Day"),
                        rs.getTime("Checkin"),
                        rs.getTime("Checkout")
                );
                filteredList.add(data);
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi tìm kiếm: " + e.getMessage());
        } finally {
            closeResources();
        }

        // Hiển thị kết quả tìm kiếm trong TableView
        tableTimekeeping.setItems(filteredList);
    }

    public void setMainController(FXMLMainController mainController) {
        this.mainController = mainController;
    }

     @FXML
    private void goToNhanvienForm() {
        if (mainController != null) {
            mainController.loadFXML("/demoproject1/FXMLNhanVien.fxml");
        } else {
            System.err.println("FXMLChamluongController: MainController is null, cannot load FXMLNhanVien.fxml");
        }
    }

    @FXML
    private void goToBangluongFormFromChamluong() {
        if (mainController != null) {
            mainController.loadFXML("/demoproject1/FXMLBangluong.fxml");
        } else {
            System.err.println("FXMLChamluongController: MainController is null, cannot load FXMLBangluong.fxml");
        }
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
                TableView<TableChamluong> tableView = tableTimekeeping;
                ObservableList<TableColumn<TableChamluong, ?>> columns = tableView.getColumns();
                for (int i = 0; i < columns.size(); i++) {
                    TableColumn<TableChamluong, ?> column = columns.get(i);
                    if (i > 0) {
                        writer.write(",");
                    }
                    writer.write(column.getText());
                }
                writer.write("\n");

                // Ghi dữ liệu từng dòng
                ObservableList<TableChamluong> dataList = tableView.getItems();
                for (TableChamluong staff : dataList) {
                    for (int i = 0; i < columns.size(); i++) {
                        TableColumn<TableChamluong, ?> column = columns.get(i);
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        txttukhoa.textProperty().addListener((observable, oldValue, newValue) -> {
            searchStaff();
        });

        showDataChamluong();

        // Bắt sự kiện khi nhấn nút Chuyển form
        btnCheckin.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLCheckin.fxml"));
                Parent root = loader.load();

                FXMLCheckinController controller = loader.getController();
                controller.setParentController(this); // Truyền FXMLChamluongController vào đây

                Scene scene = new Scene(root);
                Stage newStage = new Stage();
                newStage.initModality(Modality.APPLICATION_MODAL);
                newStage.initStyle(StageStyle.UNDECORATED);
                newStage.setScene(scene);

                Stage currentStage = (Stage) btnCheckin.getScene().getWindow();
                double centerXPosition = currentStage.getX() + currentStage.getWidth() / 2d;
                double centerYPosition = currentStage.getY() + currentStage.getHeight() / 2d;
                newStage.setX(centerXPosition - root.prefWidth(-1) / 2d);
                newStage.setY(centerYPosition - root.prefHeight(-1) / 2d);

                newStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Bắt sự kiện khi nhấn đúp vào hàng trong bảng
        tableTimekeeping.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                TableChamluong selectedRecord = tableTimekeeping.getSelectionModel().getSelectedItem();
                if (selectedRecord != null && selectedRecord.getCheckout() == null) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLCheckout.fxml"));
                        Parent root = loader.load();

                        FXMLCheckoutController controller = loader.getController();
                        controller.setParentController(this); // Truyền FXMLChamluongController vào đây
                        controller.setSelectedRecord(selectedRecord); // Truyền dữ liệu dòng được chọn vào controller của FXMLCheckout

                        Scene scene = new Scene(root);
                        Stage newStage = new Stage();
                        newStage.initModality(Modality.APPLICATION_MODAL);
                        newStage.initStyle(StageStyle.UNDECORATED);
                        newStage.setScene(scene);

                        Stage currentStage = (Stage) tableTimekeeping.getScene().getWindow();
                        double centerXPosition = currentStage.getX() + currentStage.getWidth() / 2d;
                        double centerYPosition = currentStage.getY() + currentStage.getHeight() / 2d;
                        newStage.setX(centerXPosition - root.prefWidth(-1) / 2d);
                        newStage.setY(centerYPosition - root.prefHeight(-1) / 2d);

                        newStage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "This employee has checked out.");
                }
            }
        });
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void closeResources() {
        try {
            if (rs != null) {
                rs.close();
            }
            if (pst != null) {
                pst.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi đóng các tài nguyên: " + e.getMessage());
        }
    }
}
