package sam;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import java.sql.Statement;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

// AddcustomerController.java
public class AddcustomerController implements Initializable {

    private CustomerUpdateListener listener;
    @FXML
    private ImageView exit;
    @FXML
    private Button btnadd;

    @FXML
    private TextField txtname;

    @FXML
    private TextField txtaddress;

    @FXML
    private RadioButton rdmale;

    @FXML
    private RadioButton rdfemale;

    @FXML
    private TextField txtphone;

    @FXML
    private TextField txtemail;

    @FXML
    private TextArea txtpoint;

    public void setCustomerUpdateListener(CustomerUpdateListener listener) {
        this.listener = listener;
    }

    @FXML
    private void addCustomer() {
        AlertMessage alert = new AlertMessage();
        if (txtname.getText().isEmpty() || txtaddress.getText().isEmpty() || txtphone.getText().isEmpty() || txtemail.getText().isEmpty()
                || txtpoint.getText().isEmpty()) {
            alert.errorMessage("Please enter complete information");
            return;
        }

        // Kiểm tra xem 'Nam' hoặc 'Nữ' đã được chọn chưa
        if (!(rdmale.isSelected() ^ rdfemale.isSelected())) {
            alert.errorMessage("Please select 'Male' or 'Female'");
            return;
        }

        // Kiểm tra định dạng email có hợp lệ không
        if (!txtemail.getText().matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
            alert.errorMessage("Please enter the correct Email");
            return;
        }

        // Kiểm tra xem số điện thoại đã tồn tại chưa
        String checkPhoneQuery = "SELECT * FROM Customer WHERE Phone = ?";
        try {
           
            Connection conn = DB.ConnectDB.getConnectDB();

            PreparedStatement checkPhoneStmt = conn.prepareStatement(checkPhoneQuery);
            checkPhoneStmt.setString(1, txtphone.getText());
            ResultSet existingPhone = checkPhoneStmt.executeQuery();
            if (existingPhone.next()) {
                alert.errorMessage("Phone number already exists");
                return; // Thoát phương thức nếu số điện thoại đã tồn tại
            }

            // Kiểm tra xem email đã tồn tại chưa
            String checkEmailQuery = "SELECT * FROM Customer WHERE Email = ?";
            PreparedStatement checkEmailStmt = conn.prepareStatement(checkEmailQuery);
            checkEmailStmt.setString(1, txtemail.getText());
            ResultSet existingEmail = checkEmailStmt.executeQuery();
            if (existingEmail.next()) {
                alert.errorMessage("Email address already exists");
                return; // Thoát phương thức nếu địa chỉ email đã tồn tại
            }

            // Nếu các kiểm tra đều đúng, tiến hành thêm vào cơ sở dữ liệu
            String sql = "INSERT INTO Customer(CustomerName, Address, Gender, Phone, Email, Point) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, txtname.getText());
            pst.setString(2, txtaddress.getText());
            String gender = rdmale.isSelected() ? "Male" : "Female";
            pst.setString(3, gender);
            pst.setString(4, txtphone.getText());
            pst.setString(5, txtemail.getText());
            pst.setString(6, txtpoint.getText());
            pst.executeUpdate();

            // Lấy ID được tạo tự động
            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                String formattedId = String.format("%04d", id);

                // Thêm khách hàng mới vào ObservableList
                TableCustomer newCustomer = new TableCustomer(formattedId,
                        txtname.getText(),
                        txtaddress.getText(),
                        gender,
                        txtpoint.getText(),
                        txtphone.getText(),
                        txtemail.getText(),
                        "Hành động");
                if (listener != null) {
                    listener.onUpdate(); // Thông báo cho CustomerController cập nhật dữ liệu
                }
                alert.successMessage("Successfully added customers");
                // Đóng form
                Stage stage = (Stage) btnadd.getScene().getWindow();
                stage.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleCancel() {
        Stage stage = (Stage) txtname.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        exit.setOnMouseClicked(event -> {
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.close();
        });
    }
}
