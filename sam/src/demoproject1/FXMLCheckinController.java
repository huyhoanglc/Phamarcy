package demoproject1;

import DB.ConnectDB;

import java.net.URL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FXMLCheckinController implements Initializable {

    @FXML
    private Button btnCheckin;

    @FXML
    private TextField txtStaffID;

    @FXML
    private TextField txtStaffname;

    @FXML
    private TextField txtCheckin;

    @FXML
    private Button btncancel;

    private FXMLChamluongController parentController;

    public ConnectDB connect = new ConnectDB();
    private Connection conn;
    private PreparedStatement pst;
    private ResultSet rs;

    public void setParentController(FXMLChamluongController parentController) {
        this.parentController = parentController;
    }

    @FXML
    public void handleCancel() {
        Stage stage = (Stage) btncancel.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void handleCheckin() {
        String staffID = txtStaffID.getText();
        String staffname = txtStaffname.getText();
        String enteredCardNumber = txtCheckin.getText(); // Lấy giá trị CardNumber mà nhân viên nhập vào

        if (staffID.isEmpty() || staffname.isEmpty() || enteredCardNumber.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter Staff AND, Staff Name and Card Number.");
            return;
        }

        // Kiểm tra StaffID và Staffname có trong tblStaff không
        boolean validStaff = checkValidStaff(staffID, staffname);
        if (!validStaff) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid StaffID or Staffname.");
            return;
        }

        // Lấy CardNumber từ tblStaff và kiểm tra tính hợp lệ của CardNumber
        String cardNumber = getCardNumber(staffID, enteredCardNumber);
        if (cardNumber == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "CardNumber is invalid or does not match.");
            return;
        }

        // Kiểm tra đã checkin trong ngày hôm nay chưa
        boolean hasCheckedInToday = hasCheckedInToday(staffID);
        if (hasCheckedInToday) {
            showAlert(Alert.AlertType.ERROR, "Error", "Staff checked in today.");
            return;
        }

        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // Cập nhật dữ liệu vào tblTimekeeping
        String sql = "INSERT INTO tblTimekeeping (StaffID, Staffname, Day, Checkin) VALUES (?, ?, ?, ?)";

        try {
            conn = connect.getConnectDB();
            pst = conn.prepareStatement(sql);
            pst.setString(1, staffID);
            pst.setString(2, staffname);
            pst.setString(3, dateFormat.format(currentDate));
            pst.setString(4, dateFormat.format(currentDate));
            pst.executeUpdate();

            // Đóng cửa sổ sau khi checkin thành công
            Stage stage = (Stage) btnCheckin.getScene().getWindow();
            stage.close();

            // Cập nhật lại TableView trong FXMLChamluongController
            if (parentController != null) {
                parentController.refreshTable();
            }

        } catch (SQLException ex) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error when performing Checkin:" + ex.getMessage());
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println("Lỗi khi đóng kết nối: " + ex.getMessage());
            }
        }
    }

    private boolean hasCheckedInToday(String staffID) {
        String sql = "SELECT * FROM tblTimekeeping WHERE StaffID = ? AND CAST(Day AS DATE) = CAST(GETDATE() AS DATE)";

        try {
            conn = connect.getConnectDB();
            pst = conn.prepareStatement(sql);
            pst.setString(1, staffID);
            rs = pst.executeQuery();

            // Kiểm tra số lần đã checkin trong ngày hôm nay
            return rs.next(); // True nếu có kết quả từ câu truy vấn (đã checkin trong ngày hôm nay)

        } catch (SQLException ex) {
            System.out.println("Lỗi khi kiểm tra đã checkin: " + ex.getMessage());
            return false;
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
            } catch (SQLException ex) {
                System.out.println("Lỗi khi đóng kết nối: " + ex.getMessage());
            }
        }
    }

    private boolean checkValidStaff(String staffID, String staffname) {
        String sql = "SELECT * FROM tblStaff WHERE StaffID = ? AND Staffname = ?";
        try {
            conn = connect.getConnectDB();
            pst = conn.prepareStatement(sql);
            pst.setString(1, staffID);
            pst.setString(2, staffname);
            rs = pst.executeQuery();
            return rs.next(); // True nếu có kết quả từ câu truy vấn

        } catch (SQLException ex) {
            System.out.println("Lỗi khi kiểm tra nhân viên: " + ex.getMessage());
            return false;
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
            } catch (SQLException ex) {
                System.out.println("Lỗi khi đóng kết nối: " + ex.getMessage());
            }
        }
    }

    private String getCardNumber(String staffID, String enteredCardNumber) {
        String sql = "SELECT CardNumber FROM tblStaff WHERE StaffID = ?";
        try {
            conn = connect.getConnectDB();
            pst = conn.prepareStatement(sql);
            pst.setString(1, staffID);
            rs = pst.executeQuery();
            if (rs.next()) {
                String cardNumberFromDB = rs.getString("CardNumber"); // Loại bỏ khoảng trắng thừa từ cơ sở dữ liệu

                if (enteredCardNumber.equals(cardNumberFromDB.trim())) {
                    return cardNumberFromDB; // Trả về cardNumberFromDB nếu trùng khớp
                } else {
                    System.out.println("Error: CardNumber không trùng khớp. Input: " + enteredCardNumber + ", DB: " + cardNumberFromDB);
                    return null; // Không trùng khớp thì trả về null
                }
            }
            return null; // Không tìm thấy bản ghi có StaffID tương ứng
        } catch (SQLException ex) {
            System.out.println("Lỗi khi lấy CardNumber: " + ex.getMessage());
            return null;
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
            } catch (SQLException ex) {
                System.out.println("Lỗi khi đóng kết nối: " + ex.getMessage());
            }
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
}
