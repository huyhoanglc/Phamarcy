/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package AccountInfo;

import Data.AccountData;
import Data.ControllerHolder;
import Data.Data;
import Data.DruglistData;
import Data.Message;
import java.net.URL;
import java.sql.Timestamp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author Admin
 */
public class AccountInfoController implements Initializable {

      @FXML
    private TableColumn<AccountData, Void> Acc_Col_AC;

    @FXML
    private TableColumn<AccountData, String> Acc_Col_Address;

    @FXML
    private TableColumn<AccountData, LocalDateTime> Acc_Col_DateCreated;

    @FXML
    private TableColumn<AccountData, String> Acc_Col_Email;

    @FXML
    private TableColumn<AccountData, String> Acc_Col_FullName;

    @FXML
    private TableColumn<AccountData, String> Acc_Col_Password;

    @FXML
    private TableColumn<AccountData, String> Acc_Col_Phone;

    @FXML
    private TableColumn<AccountData, String> Acc_Col_Role;

    @FXML
    private Pagination pgtAL;

    @FXML
    private TextField searchAL;

    @FXML
    private TableView<AccountData> tableaccoutlist;

    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;
    Message alert = new Message();
    
    public ObservableList<AccountData> DataList() {
        ObservableList<AccountData> Datalist = FXCollections.observableArrayList();
        String sql = "SELECT i.ID, i.Fullname, i.Phone, i.Email, i.Address, i.Date_Created, l.Password, r.Role " +
                 "FROM Information i " +
                 "JOIN Login l ON i.Phone = l.username " +
                 "JOIN Role r ON l.username = r.UserName";
        try {
            conn = DB.ConnectDB.getConnectDB();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            AccountData data;
            while (rs.next()) {
                Timestamp timestamp = rs.getTimestamp("Date_Created");
                LocalDateTime datecreated = timestamp.toLocalDateTime();
                data = new AccountData(rs.getInt("ID"),
                        rs.getString("FullName"),
                        rs.getString("Email"),
                        rs.getString("Phone"),
                        rs.getString("Address"),
                        rs.getString("Password"),
                        rs.getString("Role"),
                        datecreated
                        
                );
                Datalist.add(data);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }

        }
        return Datalist;
    }
    
    public void showData() {
        ObservableList<AccountData> showList = DataList();
        
        Acc_Col_Address.setCellValueFactory(new PropertyValueFactory<>("Address"));
        Acc_Col_DateCreated.setCellValueFactory(new PropertyValueFactory<>("DateCreated"));
        Acc_Col_Email.setCellValueFactory(new PropertyValueFactory<>("Email"));
        Acc_Col_FullName.setCellValueFactory(new PropertyValueFactory<>("FullName"));
        Acc_Col_Password.setCellValueFactory(new PropertyValueFactory<>("Password"));
        Acc_Col_Phone.setCellValueFactory(new PropertyValueFactory<>("Phone"));
        Acc_Col_Role.setCellValueFactory(new PropertyValueFactory<>("Role"));
        Acc_Col_AC.setCellFactory(new Callback<TableColumn<AccountData, Void>, TableCell<AccountData, Void>>() {
            @Override
            public TableCell<AccountData, Void> call(final TableColumn<AccountData, Void> param) {
                final TableCell<AccountData, Void> cell = new TableCell<AccountData, Void>() {

                    private final Button btnUpdate = new Button("Update");
                    private final Button btnDelete = new Button("Delete");

                    {
                        btnUpdate.getStyleClass().add("btn_update");
                        btnDelete.getStyleClass().add("btn_delete");
                        btnUpdate.setOnAction((ActionEvent event) -> {
                            AccountData pdata = getTableView().getItems().get(getIndex());
                            openUpdateForm(pdata);
                        });

                        btnDelete.setOnAction((ActionEvent event) -> {
                            AccountData pdata = getTableView().getItems().get(getIndex());
                            if (alert.confirmMessage("Are you sure want to delete User: " + pdata.getPhone() + "?")) {
                                deleteData(pdata.getPhone());
                                showData(); // Refresh table
                                Pagination();
                            }
                        });
                        btnUpdate.setAlignment(Pos.CENTER);
                        btnDelete.setAlignment(Pos.CENTER);
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            HBox hbox = new HBox(5);
                            hbox.getChildren().addAll(btnUpdate, btnDelete);
                            hbox.setAlignment(Pos.CENTER);
                            setGraphic(hbox);
                        }
                    }
                };
                return cell;
            }
        });

        tableaccoutlist.setItems(showList);
    }
    
    private void deleteData(String username) {
    String sqlDeleteRole = "DELETE FROM Role WHERE Username = ?";
    String sqlDeleteInformation = "DELETE FROM Information WHERE Phone = ?";
    String sqlDeleteLogin = "DELETE FROM Login WHERE Username = ?";

    try {
        conn = DB.ConnectDB.getConnectDB();

        // Bắt đầu giao dịch
        conn.setAutoCommit(false);

        // Xóa từ bảng Role
        ps = conn.prepareStatement(sqlDeleteRole);
        ps.setString(1, username);
        ps.executeUpdate();
        ps.close();

        // Xóa từ bảng Infomation
        ps = conn.prepareStatement(sqlDeleteInformation);
        ps.setString(1, username);
        ps.executeUpdate();
        ps.close();

        // Xóa từ bảng Login
        ps = conn.prepareStatement(sqlDeleteLogin);
        ps.setString(1, username);
        ps.executeUpdate();

        // Hoàn thành giao dịch
        conn.commit();

        alert.successMessage("Deleted Successfully");
    } catch (SQLException e) {
        e.printStackTrace();
        try {
            // Rollback giao dịch nếu có lỗi
            if (conn != null) {
                conn.rollback();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    } finally {
        try {
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

    private void openUpdateForm(AccountData pdata) {
        try {
            Data.al_ID = pdata.getID();
            Data.al_email = pdata.getEmail();
            Data.al_datecreated = pdata.getDateCreated();
            Data.al_role = pdata.getRole();
            Data.al_address = pdata.getAddress();
            Data.al_password = pdata.getPassword();
            Data.al_phone = pdata.getPhone();
            Data.al_fullname = pdata.getFullName();
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AccountInfo/UpdateInfo.fxml"));

            Parent root = loader.load();

            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    private void filterData(String keyword) {
        ObservableList<AccountData> filteredList = FXCollections.observableArrayList();
        String sql = "SELECT i.ID, i.Fullname, i.Email, i.Phone, i.Address, l.Password, r.Role, i.Date_Created " +
             "FROM Information i " +
             "INNER JOIN Login l ON i.Phone = l.Username " + // Thêm khoảng trắng sau "l.Username"
             "INNER JOIN Role r ON l.Username = r.Username " + // Thêm khoảng trắng sau "r.Username"
             "WHERE i.Fullname LIKE ? OR i.Phone LIKE ? OR i.Email LIKE ?";
        try {
            conn = DB.ConnectDB.getConnectDB();
            ps = conn.prepareStatement(sql);
            String searchKeyword = "%" + keyword + "%";
            ps.setString(1, searchKeyword);
            ps.setString(2, searchKeyword);
            ps.setString(3, searchKeyword);
            rs = ps.executeQuery();

            while (rs.next()) {
                Timestamp timestamp = rs.getTimestamp("Date_Created");
                LocalDateTime datecreated = timestamp.toLocalDateTime();
                AccountData data = new AccountData(
                        rs.getInt("ID"),
                        rs.getString("FullName"),
                        rs.getString("Email"),
                        rs.getString("Phone"),
                        rs.getString("Address"),
                        rs.getString("Password"),
                        rs.getString("Role"),
                        datecreated
                );
                filteredList.add(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        tableaccoutlist.setItems(filteredList);
    }

    public void Search() {
        searchAL.textProperty().addListener((observable, oldValue, newValue) -> {
            filterData(newValue);
        });
    }
    
    private static final int itemsperpage = 23;

    private int getPageCount() {
        return (int) Math.ceil((double) DataList().size() / itemsperpage);
    }

    private void updateTable(int pageIndex) {
        int fromIndex = pageIndex * itemsperpage;
        int toIndex = Math.min(fromIndex + itemsperpage, DataList().size());
        tableaccoutlist.setItems(FXCollections.observableArrayList(DataList().subList(fromIndex, toIndex)));
    }

    private Node createPage(int pageIndex) {
        updateTable(pageIndex);
        return tableaccoutlist;
    }

    public void Pagination() {
        pgtAL.setPageCount(getPageCount());
        pgtAL.setPageFactory(this::createPage);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ControllerHolder.getInstance().setAccountinfoController(this);
        showData();
        Pagination();
        Search();
    }    
    
}
