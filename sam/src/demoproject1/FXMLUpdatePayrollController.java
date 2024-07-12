package demoproject1;

import DB.ConnectDB;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FXMLUpdatePayrollController implements Initializable {

    @FXML
    private Button btnUpdatepayroll; // Nút để cập nhật thông tin lương

    @FXML
    private Button btncancel; // Nút để hủy bỏ và đóng form

    @FXML
    private DatePicker datePayend; // DatePicker cho ngày kết thúc kỳ trả lương

    @FXML
    private DatePicker datePaystart; // DatePicker cho ngày bắt đầu kỳ trả lương

    @FXML
    private TextField txtStaffID; // TextField cho ID nhân viên

    @FXML
    private TextField txtstandardday; // TextField cho số ngày tiêu chuẩn

    @FXML
    private TextField txttotalworkday;

    @FXML
    private TextField txtsalary;

    @FXML
    private TextField txtovertime;

    public ConnectDB connect = new ConnectDB(); // Kết nối cơ sở dữ liệu
    private Connection conn; // Kết nối SQL
    private PreparedStatement pst; // Câu lệnh SQL đã chuẩn bị
    private ResultSet rs; // Kết quả trả về từ câu lệnh SQL

    @FXML
    public void handleCancel() {
        Stage stage = (Stage) btncancel.getScene().getWindow();
        stage.close();
    }

    private TableBangluong payrollData; // Lưu trữ dữ liệu từ bảng lương

    private FXMLBangluongController parentController; // Tham chiếu đến controller cha để làm mới bảng

    public void setParentController(FXMLBangluongController parentController) {
        this.parentController = parentController;
    }

    public void setUpdatePayroll(TableBangluong data) {
        payrollData = data; // Lưu dữ liệu từ dòng được chọn
        txtStaffID.setText(data.getStaffID());
        txtStaffID.setEditable(false); // Không cho phép chỉnh sửa trường Staff ID

        java.util.Date payPeriodStartDate = new java.util.Date(data.getPayPeriodStart().getTime());
        java.util.Date payPeriodEndDate = new java.util.Date(data.getPayPeriodEnd().getTime());

        datePaystart.setValue(payPeriodStartDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        datePayend.setValue(payPeriodEndDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

        txtstandardday.setText(String.valueOf(data.getStandardDays()));
        txttotalworkday.setText(String.valueOf(data.getTotalWorkDays()));
        txtsalary.setText(String.valueOf(data.getSalary()));
        txtovertime.setText(String.valueOf(data.getOvertime()));
    }

    @FXML
    public void handleUpdate() {
        if (txtStaffID.getText().isEmpty() || datePaystart.getValue() == null || datePayend.getValue() == null
                || txtstandardday.getText().isEmpty() || txttotalworkday.getText().isEmpty() || txtovertime.getText().isEmpty() || txtsalary.getText().isEmpty()) {
            showAlert("Please enter complete information");
        } else {
            try {
                conn = connect.getConnectDB();
                if (conn == null) {
                    return;
                }

                String sql = "UPDATE tblPayroll SET PayPeriodStart = ?, PayPeriodEnd = ?, StandardDays = ?, StandardHours = ?, TotalWorkDays = ?, Overtime = ?, TotalPay = ?, Salary = ? WHERE StaffID = ?";

                pst = conn.prepareStatement(sql);

                pst.setDate(1, java.sql.Date.valueOf(datePaystart.getValue())); // PayPeriodStart
                pst.setDate(2, java.sql.Date.valueOf(datePayend.getValue())); // PayPeriodEnd

                int standardDays = Integer.parseInt(txtstandardday.getText());
                pst.setInt(3, standardDays); // StandardDays

                int defaultStandardHours = 8;
                pst.setInt(4, defaultStandardHours); // StandardHours

                BigDecimal totalWorkDays = new BigDecimal(txttotalworkday.getText());
                pst.setBigDecimal(5, totalWorkDays); // TotalWorkDays

                BigDecimal overtime = new BigDecimal(txtovertime.getText());
                pst.setBigDecimal(6, overtime); // Overtime

                BigDecimal salary = new BigDecimal(txtsalary.getText());
                pst.setBigDecimal(8, salary); // Salary

                BigDecimal totalPay = calculateTotalPay(salary, standardDays, defaultStandardHours, totalWorkDays, overtime);
                pst.setBigDecimal(7, totalPay); // TotalPay

                pst.setString(9, txtStaffID.getText()); // StaffID

                int rowsAffected = pst.executeUpdate();
                if (rowsAffected > 0) {
                    showAlert("Payroll data updated successfully.");

                    payrollData.setPayPeriodStart(java.sql.Date.valueOf(datePaystart.getValue()));
                    payrollData.setPayPeriodEnd(java.sql.Date.valueOf(datePayend.getValue()));
                    payrollData.setStandardDays(standardDays);
                    payrollData.setTotalWorkDays(totalWorkDays);
                    payrollData.setOvertime(overtime);
                    payrollData.setSalary(salary);
                    payrollData.setTotalPay(totalPay);

                    if (parentController != null) {
                        parentController.showDataBangluong();
                    }

                    Stage stage = (Stage) btnUpdatepayroll.getScene().getWindow();
                    stage.close();
                }
            } catch (SQLException e) {
                System.out.println("Error while updating payroll: " + e.getMessage());
            } finally {
                closeResources();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnUpdatepayroll.setOnAction(event -> handleUpdate());
        btncancel.setOnAction(event -> handleCancel());
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private BigDecimal calculateTotalPay(BigDecimal salary, int standardDays, int standardHours, BigDecimal totalWorkDays, BigDecimal overtimeHours) {
        // Số tiền làm được trong 1 giờ
        BigDecimal hourlyRate = salary.divide(BigDecimal.valueOf(standardDays), BigDecimal.ROUND_HALF_UP)
                .divide(BigDecimal.valueOf(standardHours), BigDecimal.ROUND_HALF_UP);

        // Tổng số giờ làm việc trong tháng
        BigDecimal totalActualHours = totalWorkDays.multiply(BigDecimal.valueOf(standardHours));

        // Tính toán lương từ số giờ làm việc thực tế và số giờ tăng ca
        BigDecimal totalPay = hourlyRate.multiply(totalActualHours)
                .add(hourlyRate.multiply(overtimeHours));

        return totalPay;
    }

    private void closeResources() {
        try {
            if (pst != null) {
                pst.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("Error while closing connection: " + e.getMessage());
        }
    }
}
