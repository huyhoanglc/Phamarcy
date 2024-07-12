package demoproject1;

import DB.ConnectDB;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author nguye
 */
public class FXMLThemPayrollController implements Initializable {

    @FXML
    private Button btncancel;

    @FXML
    private Button btnAddpayroll;

    @FXML
    private DatePicker datePayend;

    @FXML
    private TextField txtsalary;

    @FXML
    private DatePicker datePaystart;

    @FXML
    private TextField txtStaffID;

    @FXML
    private TextField txtstandardday;

    @FXML
    private TextField txttotalworkday;

    @FXML
    private TextField txtovertime;

    /**
     * Initializes the controller class.
     */
    public ConnectDB connect = new ConnectDB();
    private Connection conn;
    private PreparedStatement pst;
    private ResultSet rs;

    @FXML
    public void handleCancel() {
        // Lấy Stage hiện tại từ Button
        Stage stage = (Stage) btncancel.getScene().getWindow();

        // Đóng Stage (form) hiện tại
        stage.close();
    }

    @FXML
    public void handleAddPayroll() {
        Thuvien tv = new Thuvien();

        // Kiểm tra xem tất cả các trường thông tin có được điền đầy đủ hay không
        if (txtStaffID.getText().isEmpty() || datePaystart.getValue() == null || datePayend.getValue() == null
                || txtstandardday.getText().isEmpty() || txttotalworkday.getText().isEmpty() || txtovertime.getText().isEmpty() || txtsalary.getText().isEmpty()) {
            tv.showAlert("Please enter complete information");
        } else {
            try {
                // Thiết lập kết nối cơ sở dữ liệu trước khi thực hiện bất kỳ thao tác nào
                conn = connect.getConnectDB();
                if (conn == null) {
                    return;
                }

                // Kiểm tra xem StaffID có tồn tại trong tblStaff không
                String checkStaffSQL = "SELECT COUNT(*) AS count FROM tblStaff WHERE StaffID = ?";
                try (PreparedStatement checkStmt = conn.prepareStatement(checkStaffSQL)) {
                    checkStmt.setString(1, txtStaffID.getText());
                    ResultSet rs = checkStmt.executeQuery();
                    if (rs.next() && rs.getInt("count") == 0) {
                        tv.showAlert("StaffID does not exist in tblStaff. Please enter a valid StaffID.");
                        return; // Thoát phương thức nếu StaffID không tồn tại
                    }
                } catch (Exception e) {
                    System.out.println("Error while checking StaffID: " + e.getMessage());
                    return;
                }

                // Chuỗi truy vấn SQL để thêm bảng lương vào tblPayroll
                String sql = "INSERT INTO tblPayroll (StaffID, PayPeriodStart, PayPeriodEnd, StandardDays, StandardHours, TotalWorkDays, Overtime, TotalPay, Salary) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                try {
                    pst = conn.prepareStatement(sql);

                    pst.setString(1, txtStaffID.getText()); // StaffID

                    // Chuyển đổi giá trị DatePicker thành java.sql.Date
                    pst.setDate(2, java.sql.Date.valueOf(datePaystart.getValue())); // PayPeriodStart
                    pst.setDate(3, java.sql.Date.valueOf(datePayend.getValue())); // PayPeriodEnd

                    pst.setInt(4, Integer.parseInt(txtstandardday.getText())); // StandardDays

                    // Thiết lập giá trị mặc định cho StandardHours là 8
                    int defaultStandardHours = 8;
                    pst.setInt(5, defaultStandardHours); // StandardHours

                    // Thiết lập giá trị TotalWorkDays từ người dùng nhập vào
                    int totalWorkDays = Integer.parseInt(txttotalworkday.getText());
                    pst.setInt(6, totalWorkDays); // TotalWorkDays

                    // Thiết lập giá trị Overtime từ người dùng nhập vào
                    BigDecimal overtime = new BigDecimal(txtovertime.getText());
                    pst.setBigDecimal(7, overtime); // Overtime

                    // Thiết lập giá trị Salary từ người dùng nhập vào
                    BigDecimal salary = new BigDecimal(txtsalary.getText());
                    pst.setBigDecimal(9, salary); // Salary

                    // Tính toán và thiết lập giá trị cho TotalPay
                    BigDecimal totalPay = calculateTotalPay(salary, Integer.parseInt(txtstandardday.getText()), defaultStandardHours, totalWorkDays, overtime);
                    pst.setBigDecimal(8, totalPay); // TotalPay

                    // Thực thi câu lệnh SQL
                    pst.executeUpdate();
                    tv.showAlert("Payroll added successfully!");

                    // Đóng cửa sổ hiện tại của FXMLThemPayrollController
                    Stage stage = (Stage) btnAddpayroll.getScene().getWindow();
                    stage.close();

                    // Cập nhật lại bảng sau khi thêm mới Payroll
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLBangluong.fxml"));
                    Parent parent = loader.load();
                    FXMLBangluongController controller = loader.getController();
                    controller.showDataBangluong(); // Gọi phương thức cập nhật bảng

                } catch (Exception e) {
                    System.out.println("Error while adding payroll: " + e.getMessage());
                }
            } catch (Exception ex) {
                System.out.println("Error while establishing database connection: " + ex.getMessage());
            }
        }
    }

    private BigDecimal calculateTotalPay(BigDecimal salary, int standardDays, int standardHours, int totalWorkDays, BigDecimal overtimeHours) {
        // Tính số tiền làm được trong 1 giờ
        BigDecimal hourlyRate = salary.divide(BigDecimal.valueOf(standardDays), BigDecimal.ROUND_HALF_UP)
                .divide(BigDecimal.valueOf(standardHours), BigDecimal.ROUND_HALF_UP);

        // Tính tổng số giờ làm việc trong tháng
        int totalActualHours = totalWorkDays * standardHours;

        // Tính toán lương từ số giờ làm việc thực tế và số giờ tăng ca
        BigDecimal totalPay = hourlyRate.multiply(BigDecimal.valueOf(totalActualHours))
                .add(hourlyRate.multiply(overtimeHours));

        return totalPay;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btncancel.setOnAction(event -> handleCancel());
        btnAddpayroll.setOnAction(event -> handleAddPayroll());
    }
}
//số tiền làm được trong 1 h = salary chia cho standarddays sau đó chia tiếp cho standardhours 
// Tổng số giờ làm việc trong tháng = standardhours nhân cho TotalworkDays.  
//tính totalPay = (số tiền làm được trong 1h nhân cho tổng số giờ làm việc trong tháng) sau đó cộng với (số tiền làm việc trong 1h nhân cho số giờ tăng ca overtime) 