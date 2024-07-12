package demoproject1;

import DB.ConnectDB;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
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

public class FXMLCheckoutController implements Initializable {

    @FXML
    private Button btnCheckout;

    @FXML
    private Button btncancel;

    @FXML
    private TextField txtStaffID;

    @FXML
    private TextField txtStaffname;

    private FXMLChamluongController parentController;
    private TableChamluong selectedRecord;

    public ConnectDB connect = new ConnectDB();
    private Connection conn;
    private PreparedStatement pst;

    public void setParentController(FXMLChamluongController parentController) {
        this.parentController = parentController;
    }

    public void setSelectedRecord(TableChamluong selectedRecord) {
        this.selectedRecord = selectedRecord;
        // Kiểm tra xem selectedRecord có null hay không
        if (selectedRecord != null) {
            // Hiển thị dữ liệu lên các ô nhập
            txtStaffID.setText(selectedRecord.getStaffID());
            txtStaffname.setText(selectedRecord.getStaffname());
        } else {
            System.out.println("selectedRecord is null");
        }
    }

    @FXML
    public void handleCancel() {
        Stage stage = (Stage) btncancel.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void handleCheckout() {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // Cập nhật dữ liệu vào tblTimekeeping
        String sql = "UPDATE tblTimekeeping SET Checkout = ? WHERE ID = ?";

        try {
            conn = connect.getConnectDB();
            pst = conn.prepareStatement(sql);
            pst.setString(1, dateFormat.format(currentDate));
            pst.setInt(2, selectedRecord.getID());
            pst.executeUpdate();

            // Đóng cửa sổ sau khi checkout thành công
            Stage stage = (Stage) btnCheckout.getScene().getWindow();
            stage.close();

            // Cập nhật lại TableView trong FXMLChamluongController
            if (parentController != null) {
                parentController.refreshTable();
            }

        } catch (SQLException ex) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error when performing Checkout:" + ex.getMessage());
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
