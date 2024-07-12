package sam;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.sql.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;

public class UpdateCustomerController implements Initializable {
    
    @FXML
    private ImageView exit;
    @FXML
    private TextField txtName;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtPhone;

    @FXML
    private TextField txtEmail;

    @FXML
    private RadioButton radioMale;

    @FXML
    private RadioButton radioFemale;

    @FXML
    private TextArea txtpoint;
    @FXML
    private TableView<TableCustomer> tableCustomer;
    public TableCustomer customer;
    public CustomerUpdateListener listener;

    public void setCustomerUpdateListener(CustomerUpdateListener listener) {
        this.listener = listener;
    }
    

    public void setCustomer(TableCustomer customer) {
        this.customer = customer;

        txtName.setText(customer.getName());
        txtAddress.setText(customer.getAddress());
        txtPhone.setText(customer.getPhone());
        txtEmail.setText(customer.getEmail());
        if ("Male".equalsIgnoreCase(customer.getGender())) {
            radioMale.setSelected(true);
        } else if ("Female".equalsIgnoreCase(customer.getGender())) {
            radioFemale.setSelected(true);
        }
        txtpoint.setText(customer.getPoint());
    }

    @FXML
    public void handleUpdate() {
        AlertMessage alert = new AlertMessage();

        // Kiểm tra dữ liệu nhập vào
        if (txtName.getText().isEmpty() || txtAddress.getText().isEmpty() || txtPhone.getText().isEmpty() || txtEmail.getText().isEmpty()
                || txtpoint.getText().isEmpty()) {
            alert.errorMessage("Please enter complete information");
            return;
        }

        // Kiểm tra xem 'Nam' hoặc 'Nữ' đã được chọn chưa
        if (!(radioMale.isSelected() ^ radioFemale.isSelected())) {
            alert.errorMessage("Please select 'Male' or 'Female'");
            return;
        }

        // Kiểm tra định dạng email có hợp lệ không
        if (!txtEmail.getText().matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
            alert.errorMessage("Please enter the correct Email");
            return;
        }

        // Kiểm tra số điện thoại trùng
        String checkPhoneQuery = "SELECT * FROM Customer WHERE Phone = ? AND ID != ?";
        try {
            
            Connection conn = DB.ConnectDB.getConnectDB();
            PreparedStatement checkPhoneStmt = conn.prepareStatement(checkPhoneQuery);
            checkPhoneStmt.setString(1, txtPhone.getText());
            checkPhoneStmt.setString(2, customer.getId()); // customer.getId() là phương thức lấy ID của khách hàng
            ResultSet existingPhone = checkPhoneStmt.executeQuery();
            if (existingPhone.next()) {
                alert.errorMessage("Phone number already exists");
                return; // Thoát phương thức nếu số điện thoại đã tồn tại
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        // Kiểm tra email trùng
        String checkEmailQuery = "SELECT * FROM Customer WHERE Email = ? AND ID != ?";
        try {
            
            Connection conn = DB.ConnectDB.getConnectDB();
            PreparedStatement checkEmailStmt = conn.prepareStatement(checkEmailQuery);
            checkEmailStmt.setString(1, txtEmail.getText());
            checkEmailStmt.setString(2, customer.getId()); // customer.getId() là phương thức lấy ID của khách hàng
            ResultSet existingEmail = checkEmailStmt.executeQuery();
            if (existingEmail.next()) {
                alert.errorMessage("Email address already exists");
                return; // Thoát phương thức nếu địa chỉ email đã tồn tại
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        // Cập nhật thông tin khách hàng
        customer.setName(txtName.getText());
        customer.setAddress(txtAddress.getText());
        customer.setPhone(txtPhone.getText());
        customer.setEmail(txtEmail.getText());
        customer.setGender(radioMale.isSelected() ? "Nam" : "Nữ");
        customer.setPoint(txtpoint.getText());

        // Gọi phương thức onUpdate của listener để thông báo cập nhật
        if (listener != null) {
            listener.onUpdate(customer);
        }

        // Hiển thị thông báo cập nhật thành công và đóng cửa sổ
        alert.successMessage("Successfully update Customers");
        Stage stage = (Stage) txtName.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void handleCancel() {
        Stage stage = (Stage) txtName.getScene().getWindow();
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
