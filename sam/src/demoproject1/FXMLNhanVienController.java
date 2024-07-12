/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package demoproject1;

import DB.ConnectDB;
import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.Date;
import java.util.Optional;
import javafx.event.ActionEvent;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import javafx.scene.image.Image;

import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import sam.BillsController;
import sam.FXMLMainController;
import static sam.Sam.main;

/**
 * FXML Controller class
 *
 * @author nguye
 */
public class FXMLNhanVienController implements Initializable {

    private BillsController billsController;

    public void setBillsController(BillsController billsController) {
        this.billsController = billsController;
    }
    @FXML
    private FXMLMainController mainController;
    @FXML
    private AnchorPane top_main;

    @FXML
    private Button btnExport;

    @FXML
    private Button btnreset;

    @FXML
    private Button btnsua;

    @FXML
    private TextField txtCardNumber;

    @FXML
    private Button btnxoa;

    @FXML
    private RadioButton rbActive;

    @FXML
    private RadioButton rbInactive;

    @FXML
    private RadioButton rbNam;

    @FXML
    private RadioButton rbNu;

    @FXML
    private Button btnPayroll;

    @FXML
    private Button btnTimekeeping;


    @FXML
    private TableColumn<TableNhanvien, String> cotaddress;

    @FXML
    private TableColumn<TableNhanvien, Date> cotbirthday;

    @FXML
    private TableColumn<TableNhanvien, String> cotemail;

    @FXML
    private TableColumn<TableNhanvien, String> cotgender;

    @FXML
    private TableColumn<TableNhanvien, String> cotsdt;

    @FXML
    private TableColumn<TableNhanvien, String> cotstaffid;

    @FXML
    private TableColumn<TableNhanvien, String> cotstaffname;

    @FXML
    private TableColumn<TableNhanvien, String> cotstatus;

    @FXML
    private DatePicker datengaysinh;

    @FXML
    private ImageView imageview;

    @FXML
    private TableView<TableNhanvien> tablenhanvien;

    @FXML
    private TextField txtdiachi;

    @FXML
    private TextField txtemail;

    @FXML
    private TextField txtmanv;

    @FXML
    private TextField txtsdt;

    @FXML
    private TextField txttennv;
    @FXML
    private Label file_path;
    @FXML
    private TextField txttukhoa;

    /**
     * Initializes the controller class.
     */
    public ConnectDB connect = new ConnectDB();
    private Connection conn;
    private PreparedStatement pst;
    private ResultSet rs;

    public ObservableList<TableNhanvien> dataList() {

//goi ham
        ObservableList<TableNhanvien> dataList = FXCollections.observableArrayList();

        String sql = "select * from tblStaff";

        try {
            conn = connect.getConnectDB();
            pst = conn.prepareStatement(sql);

            rs = pst.executeQuery();

            TableNhanvien data;

            while (rs.next()) {

                data = new TableNhanvien(rs.getString("StaffID"),
                        rs.getString("Staffname"),
                        rs.getString("Gender"),
                        rs.getString("Address"),
                        rs.getString("Numberphone"),
                        rs.getDate("Birthday"),
                        rs.getString("Gmail"),
                        rs.getString("Image"),
                        rs.getString("Status"),
                        rs.getInt("CardNumber"));
                dataList.add(data);

            }

        } catch (Exception e) {

            System.out.println(e.getMessage());

        }

        return dataList;
    }

    public void showData() {

        ObservableList<TableNhanvien> showList = dataList();

        cotstaffid.setCellValueFactory(new PropertyValueFactory<>("StaffID"));

        cotstaffname.setCellValueFactory(new PropertyValueFactory<>("Staffname"));

        cotgender.setCellValueFactory(new PropertyValueFactory<>("Gender"));

        cotsdt.setCellValueFactory(new PropertyValueFactory<>("Numberphone"));

        cotaddress.setCellValueFactory(new PropertyValueFactory<>("Address"));

        cotemail.setCellValueFactory(new PropertyValueFactory<>("Gmail"));

        cotbirthday.setCellValueFactory(new PropertyValueFactory<>("Birthday"));

        cotstatus.setCellValueFactory(new PropertyValueFactory<>("Status"));

        tablenhanvien.setItems(showList);

    }

    public void ThemStaff() {
        Thuvien tv = new Thuvien();

        // Kiểm tra xem tất cả các trường thông tin có được điền đầy đủ hay không
        if (txtmanv.getText().isEmpty() || txtCardNumber.getText().isEmpty() || txttennv.getText().isEmpty() || txtsdt.getText().isEmpty()
                || txtdiachi.getText().isEmpty() || txtemail.getText().isEmpty() || datengaysinh.getValue() == null) {
            tv.showAlert("Please enter complete information");
        } else if (!txtmanv.getText().matches("^NV\\d+$")) {
            tv.showAlert("StaffID must start with 'NV' followed by numbers!");
        } else if (!txtsdt.getText().matches("^\\d{1,11}$")) {
            tv.showAlert("Phone number must contain only digits and be up to 11 digits long!");
        } else if (!(rbNam.isSelected() ^ rbNu.isSelected())) {
            tv.showAlert("Please select either 'Male' or 'Female'!");
        } else if (!(rbActive.isSelected() ^ rbInactive.isSelected())) {
            tv.showAlert("Please select either 'Active' or 'InActive'!");
        } else if (!txtemail.getText().matches("^[\\w.-]+@gmail\\.com$")) {
            tv.showAlert("Email must be in the format 'example@gmail.com'!");
        } else {
            // Kiểm tra xem StaffID, Gmail, Số điện thoại và CardNumber có bị trùng lặp không
            String checkStaffIDSQL = "SELECT COUNT(*) AS count FROM tblStaff WHERE StaffID = ?";
            String checkGmailSQL = "SELECT COUNT(*) AS count FROM tblStaff WHERE Gmail = ?";
            String checkPhoneSQL = "SELECT COUNT(*) AS count FROM tblStaff WHERE Numberphone = ?";
            String checkCardNumberSQL = "SELECT COUNT(*) AS count FROM tblStaff WHERE CardNumber = ?";

            try {
                conn = connect.getConnectDB();

                // Kiểm tra StaffID
                pst = conn.prepareStatement(checkStaffIDSQL);
                pst.setString(1, txtmanv.getText());
                ResultSet rs = pst.executeQuery();
                if (rs.next() && rs.getInt("count") > 0) {
                    tv.showAlert("StaffID already exists. Please enter a unique StaffID.");
                    return;
                }

                // Kiểm tra Gmail
                pst = conn.prepareStatement(checkGmailSQL);
                pst.setString(1, txtemail.getText());
                rs = pst.executeQuery();
                if (rs.next() && rs.getInt("count") > 0) {
                    tv.showAlert("Gmail already exists. Please enter a unique Gmail.");
                    return;
                }

                // Kiểm tra số điện thoại
                pst = conn.prepareStatement(checkPhoneSQL);
                pst.setString(1, txtsdt.getText());
                rs = pst.executeQuery();
                if (rs.next() && rs.getInt("count") > 0) {
                    tv.showAlert("Phone number already exists. Please enter a unique phone number.");
                    return;
                }

                // Kiểm tra CardNumber
                pst = conn.prepareStatement(checkCardNumberSQL);
                pst.setString(1, txtCardNumber.getText());
                rs = pst.executeQuery();
                if (rs.next() && rs.getInt("count") > 0) {
                    tv.showAlert("CardNumber already exists. Please enter a unique CardNumber.");
                    return;
                }

            } catch (Exception e) {
                System.out.println("Error while checking data: " + e.getMessage());
                return;
            }

            // Nếu không có dữ liệu bị trùng lặp, tiếp tục thêm mới nhân viên
            String sql = "INSERT INTO tblStaff (StaffID, Staffname, Gender, Numberphone, Address, Gmail, Birthday, Status, Image, CardNumber) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try {
                pst = conn.prepareStatement(sql);

                pst.setString(1, txtmanv.getText()); // StaffID
                pst.setString(2, txttennv.getText()); // Staffname

                String gender = rbNam.isSelected() ? "Male" : "Female";
                pst.setString(3, gender); // Gender

                pst.setString(4, txtsdt.getText()); // Numberphone
                pst.setString(5, txtdiachi.getText()); // Address
                pst.setString(6, txtemail.getText()); // Gmail

                // Chuyển đổi giá trị DatePicker thành java.sql.Date
                pst.setDate(7, java.sql.Date.valueOf(datengaysinh.getValue())); // Birthday

                String status = rbActive.isSelected() ? "Active" : "InActive";
                pst.setString(8, status); // Status

                pst.setString(9, file_path.getText()); // Image, bạn có thể thay đổi giá trị này để lấy đường dẫn thực tế từ btnchonhinh

                pst.setString(10, txtCardNumber.getText()); // CardNumber

                pst.executeUpdate(); // Thực thi câu lệnh SQL
                tv.showAlert("Staff added successfully!");

                // Cập nhật lại bảng sau khi thêm mới nhân viên
                showData();
                // Reset các trường dữ liệu sau khi thêm nhân viên
                resetData();

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void insertImage() {
        FileChooser open = new FileChooser();
        Stage stage = (Stage) top_main.getScene().getWindow();
        File file = open.showOpenDialog(stage);
        if (file != null) {
            String path = file.getAbsolutePath();
            path = path.replace("\\", "\\\\");
            file_path.setText(path);
            Image image = new Image(file.toURI().toString(), 110, 110, false, true);
            imageview.setImage(image);

        } else {

            System.out.println("NO DATA EXIST!");
        }
    }

    //load du lieu len khi nhan vo tung dòng
    public void selectData() {
        TableNhanvien data = tablenhanvien.getSelectionModel().getSelectedItem();
        int num = tablenhanvien.getSelectionModel().getSelectedIndex();
        if (num >= 0) {
            txtmanv.setText(data.getStaffID());
            txtmanv.setEditable(false); // Vô hiệu hóa chỉnh sửa
            txttennv.setText(data.getStaffname());
            txtdiachi.setText(data.getAddress());
            txtsdt.setText(data.getNumberphone());
            txtemail.setText(data.getGmail());
            // Set CardNumber
            txtCardNumber.setText(String.valueOf(data.getCardNumber()));

            // Set Gender RadioButtons
            if (data.getGender().equalsIgnoreCase("Male")) {
                rbNam.setSelected(true);
                rbNu.setSelected(false);
            } else if (data.getGender().equalsIgnoreCase("Female")) {
                rbNam.setSelected(false);
                rbNu.setSelected(true);
            }

            // Set DatePicker value without using toLocalDate()
            if (data.getBirthday() != null) {
                java.sql.Date sqlDate = (java.sql.Date) data.getBirthday();
                LocalDate localDate = LocalDate.of(sqlDate.getYear() + 1900, sqlDate.getMonth() + 1, sqlDate.getDate());
                datengaysinh.setValue(localDate);
            } else {
                datengaysinh.setValue(null);
            }

            // Set Status RadioButtons
            if (data.getStatus().equalsIgnoreCase("Active")) {
                rbActive.setSelected(true);
                rbInactive.setSelected(false);
            } else if (data.getStatus().equalsIgnoreCase("InActive")) {
                rbActive.setSelected(false);
                rbInactive.setSelected(true);
            }
            String picture = "file:" + data.getImage();
            Image image = new Image(picture, 110, 110, false, true);
            imageview.setImage(image);
            String path = data.getImage();
            file_path.setText(path);
        }
    }

    public void resetData() {
        txtmanv.clear();
        txttennv.clear();
        txtdiachi.clear();
        txtsdt.clear();
        txtemail.clear();
        txtCardNumber.clear();
        txtmanv.setEditable(true); 
        datengaysinh.setValue(null);
        rbNam.setSelected(false);
        rbNu.setSelected(false);
        rbActive.setSelected(false);
        rbInactive.setSelected(false);
        imageview.setImage(null);
        file_path.setText("");
    }

    public void deleteData() {
        Thuvien tv = new Thuvien();
        // Lấy dữ liệu đã chọn từ bảng TableView
        TableNhanvien selectedData = tablenhanvien.getSelectionModel().getSelectedItem();
        if (selectedData != null) { // Kiểm tra xem có hàng nào được chọn hay không
            String staffID = selectedData.getStaffID(); // Lấy StaffID từ dữ liệu đã chọn

            // Hiển thị thông báo xác nhận
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete this employee's information?");

            // Thêm các nút OK và Cancel vào thông báo
            ButtonType buttonTypeOK = new ButtonType("OK");
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(buttonTypeOK, buttonTypeCancel);

            // Lấy kết quả từ thông báo
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == buttonTypeOK) {
                // Câu lệnh SQL để kiểm tra xem nhân viên có dữ liệu trong tblPayroll hay không
                String sqlCheck = "SELECT * FROM tblPayroll WHERE StaffID = ?";

                try {
                    conn = connect.getConnectDB(); // Kết nối tới cơ sở dữ liệu
                    pst = conn.prepareStatement(sqlCheck); // Chuẩn bị câu lệnh SQL
                    pst.setString(1, staffID); // Thiết lập giá trị cho tham số đầu tiên trong câu lệnh SQL

                    rs = pst.executeQuery(); // Thực thi câu lệnh SQL và nhận kết quả trả về

                    if (rs.next()) { // Nếu có dữ liệu trong tblPayroll
                        tv.showAlert("Cannot delete staff. Please delete payroll data first."); // Thông báo không thể xóa vì chưa xóa bảng lương
                    } else {
                        // Câu lệnh SQL để xóa dữ liệu từ bảng tblStaff dựa trên StaffID
                        String sqlDeleteStaff = "DELETE FROM tblStaff WHERE StaffID = ?";

                        pst = conn.prepareStatement(sqlDeleteStaff); // Chuẩn bị câu lệnh SQL để xóa nhân viên
                        pst.setString(1, staffID); // Thiết lập giá trị cho tham số đầu tiên trong câu lệnh SQL

                        int rowsAffected = pst.executeUpdate(); // Thực thi câu lệnh SQL và trả về số hàng bị ảnh hưởng

                        if (rowsAffected > 0) { // Kiểm tra xem có hàng nào bị xóa không
                            tv.showAlert("Data deleted successfully!"); // Thông báo xóa thành công

                            showData(); // Cập nhật lại bảng sau khi xóa
                            resetData(); // Xóa dữ liệu trong các ô nhập liệu
                        } else {
                            tv.showAlert("No data found to delete!"); // Thông báo không tìm thấy dữ liệu để xóa
                        }
                    }
                } catch (Exception e) { // Bắt lỗi nếu có
                    System.out.println("Error while deleting data: " + e.getMessage()); // Thông báo lỗi
                } finally {
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
                    } catch (Exception ex) {
                        System.out.println("Error while closing resources: " + ex.getMessage());
                    }
                }
            } else {
                tv.showAlert("Delete operation cancelled."); // Thông báo nếu người dùng hủy bỏ thao tác xóa
            }
        } else {
            tv.showAlert("Please select a row to delete."); // Thông báo nếu không có hàng nào được chọn
        }
             txtmanv.setEditable(true); 

    }

    public void updateData() {
        TableNhanvien selectedData = tablenhanvien.getSelectionModel().getSelectedItem();

        if (selectedData != null) {
            Thuvien tv = new Thuvien();

            // Kiểm tra xem tất cả các trường thông tin có được điền đầy đủ hay không
            if (txtmanv.getText().isEmpty() || txtCardNumber.getText().isEmpty() || txttennv.getText().isEmpty() || txtsdt.getText().isEmpty()
                    || txtdiachi.getText().isEmpty() || txtemail.getText().isEmpty() || datengaysinh.getValue() == null) {
                tv.showAlert("Please enter complete information");
            } else if (!txtmanv.getText().matches("^NV\\d+$")) {
                tv.showAlert("StaffID must start with 'NV' followed by numbers!");
            } else if (!txtsdt.getText().matches("^\\d{1,11}$")) {
                tv.showAlert("Phone number must contain only digits and be up to 11 digits long!");
            } else if (!(rbNam.isSelected() ^ rbNu.isSelected())) {
                tv.showAlert("Please select either 'Male' or 'Female'!");
            } else if (!(rbActive.isSelected() ^ rbInactive.isSelected())) {
                tv.showAlert("Please select either 'Active' or 'InActive'!");
            } else if (!txtemail.getText().matches("^[\\w.-]+@gmail\\.com$")) {
                tv.showAlert("Email must be in the format 'example@gmail.com'!");
            } else {
                // Kiểm tra StaffID, Gmail, Số điện thoại và CardNumber có bị trùng lặp không
                String checkGmailSQL = "SELECT COUNT(*) AS count FROM tblStaff WHERE Gmail = ? AND StaffID != ?";
                String checkPhoneSQL = "SELECT COUNT(*) AS count FROM tblStaff WHERE Numberphone = ? AND StaffID != ?";
                String checkCardNumberSQL = "SELECT COUNT(*) AS count FROM tblStaff WHERE CardNumber = ? AND StaffID != ?";

                try {
                    conn = connect.getConnectDB();

                    // Kiểm tra Gmail
                    pst = conn.prepareStatement(checkGmailSQL);
                    pst.setString(1, txtemail.getText());
                    pst.setString(2, selectedData.getStaffID());
                    ResultSet rs = pst.executeQuery();
                    if (rs.next() && rs.getInt("count") > 0) {
                        tv.showAlert("Gmail already exists. Please enter a unique Gmail.");
                        return;
                    }

                    // Kiểm tra số điện thoại
                    pst = conn.prepareStatement(checkPhoneSQL);
                    pst.setString(1, txtsdt.getText());
                    pst.setString(2, selectedData.getStaffID());
                    rs = pst.executeQuery();
                    if (rs.next() && rs.getInt("count") > 0) {
                        tv.showAlert("Phone number already exists. Please enter a unique phone number.");
                        return;
                    }
// Kiểm tra CardNumber
                    pst = conn.prepareStatement(checkCardNumberSQL);
                    pst.setString(1, txtCardNumber.getText());
                    pst.setString(2, selectedData.getStaffID());
                    rs = pst.executeQuery();
                    if (rs.next() && rs.getInt("count") > 0) {
                        tv.showAlert("CardNumber already exists. Please enter a unique CardNumber.");
                        return;
                    }

                    String sql = "UPDATE tblStaff SET Staffname = ?, Gender = ?, Numberphone = ?, Address = ?, Gmail = ?, Birthday = ?, Status = ?, Image = ?, CardNumber = ? WHERE StaffID = ?";
                    pst = conn.prepareStatement(sql);

                    pst.setString(1, txttennv.getText());

                    String gender = rbNam.isSelected() ? "Male" : "Female";
                    pst.setString(2, gender);

                    pst.setString(3, txtsdt.getText());
                    pst.setString(4, txtdiachi.getText());
                    pst.setString(5, txtemail.getText());
                    pst.setDate(6, java.sql.Date.valueOf(datengaysinh.getValue()));

                    String status = rbActive.isSelected() ? "Active" : "InActive";
                    pst.setString(7, status);

                    pst.setString(8, file_path.getText());
                    pst.setString(9, txtCardNumber.getText());
                    pst.setString(10, selectedData.getStaffID());

                    int rowsAffected = pst.executeUpdate(); // Thực thi câu lệnh SQL và trả về số hàng bị ảnh hưởng

                    if (rowsAffected > 0) {
                        tv.showAlert("Data updated successfully!");

                        showData(); // Cập nhật lại bảng sau khi cập nhật dữ liệu
                        resetData(); // Xóa dữ liệu trong các ô nhập liệu
                    } else {
                        tv.showAlert("No data found to update!");
                    }
                } catch (Exception e) {
                    System.out.println("Error while updating data: " + e.getMessage());
                } finally {
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
                    } catch (Exception e) {
                        System.out.println("Error while closing connection: " + e.getMessage());
                    }
                }
            }
        } else {
            Thuvien tv = new Thuvien();
            tv.showAlert("Please select a row to update.");
        }
    }

    public void searchStaff() {
        String keyword = txttukhoa.getText().toLowerCase();

        ObservableList<TableNhanvien> filteredList = FXCollections.observableArrayList();

        for (TableNhanvien staff : dataList()) {
            if (staff.getStaffID().toLowerCase().contains(keyword)
                    || staff.getStaffname().toLowerCase().contains(keyword)) {
                filteredList.add(staff);
            }
        }

        tablenhanvien.setItems(filteredList);
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
                TableView<TableNhanvien> tableView = tablenhanvien;
                ObservableList<TableColumn<TableNhanvien, ?>> columns = tableView.getColumns();
                for (int i = 0; i < columns.size(); i++) {
                    TableColumn<TableNhanvien, ?> column = columns.get(i);
                    if (i > 0) {
                        writer.write(",");
                    }
                    writer.write(column.getText());
                }
                writer.write("\n");

                // Ghi dữ liệu từng dòng
                ObservableList<TableNhanvien> dataList = tableView.getItems();
                for (TableNhanvien staff : dataList) {
                    for (int i = 0; i < columns.size(); i++) {
                        TableColumn<TableNhanvien, ?> column = columns.get(i);
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

    public void setMainController(FXMLMainController mainController) {
        this.mainController = mainController;
    }

    //chuyen form bang luong
    @FXML
    private void goToChamluongForm() {
        if (mainController != null) {
            mainController.loadFXML("/demoproject1/FXMLChamluong.fxml");
        } else {
            System.err.println("FXMLNhanVienController: MainController is null, cannot load FXMLChamluong.fxml");
        }
    }

    @FXML
    private void goToBangluongForm() {
        if (mainController != null) {
            mainController.loadFXML("/demoproject1/FXMLBangluong.fxml");
        } else {
            System.err.println("FXMLNhanVienController: MainController is null, cannot load FXMLBangluong.fxml");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Các phần khởi tạo khác
        btnreset.setOnAction(event -> resetData());
        btnxoa.setOnAction(event -> deleteData());
        btnsua.setOnAction(event -> updateData());
        btnExport.setOnAction(event -> exportToCSV()); //xuất ra excel
        showData();

        // Gọi phương thức tìm kiếm khi người dùng nhập từ khóa
        txttukhoa.textProperty().addListener((observable, oldValue, newValue) -> {
            searchStaff();
        });

    }
}

//(?=.*[A-Z]): ít nhất một chữ cái viết hoa.
//(?=.*[a-zA-Z]): ít nhất một chữ cái (viết thường hoặc viết hoa).
//(?=.*\\d): ít nhất một số.
//[A-Za-z\\d]{8,}$: tổng cộng ít nhất 8 ký tự chỉ bao gồm chữ cái và số.
