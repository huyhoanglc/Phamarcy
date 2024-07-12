/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package DrugList;

import AddDrug.AddDrugController;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import java.sql.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import static javafx.scene.text.Font.font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import Data.ControllerHolder;
import Data.Data;
import Data.DruglistData;
import Data.Message;
import javafx.stage.StageStyle;

/**
 *
 * @author Admin
 */
public class DruglistController implements Initializable {

    @FXML
    private Pagination pgtDL;

    @FXML
    private TableColumn<DruglistData, Void> DL_Col_AC;

    @FXML
    private TableColumn<DruglistData, String> DL_Col_API;

    @FXML
    private TableColumn<DruglistData, Integer> DL_Col_CP;

    @FXML
    private TableColumn<DruglistData, Integer> DL_Col_IPSU;

    @FXML
    private TableColumn<DruglistData, String> DL_Col_Item;

    @FXML
    private TableColumn<DruglistData, String> DL_Col_MedicineID;

    @FXML
    private TableColumn<DruglistData, String> DL_Col_MedicineName;

    @FXML
    private TableColumn<DruglistData, Integer> DL_Col_Quantity;

    @FXML
    private TableColumn<DruglistData, Integer> DL_Col_SP;

    @FXML
    private TableColumn<DruglistData, String> DL_Col_SU;

    @FXML
    private TableColumn<DruglistData, Integer> DL_Col_SUPU;

    @FXML
    private TableColumn<DruglistData, String> DL_Col_Unit;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnExportDL;

    @FXML
    private TextField searchDL;

    @FXML
    private TableView<DruglistData> tabledruglist;

    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;
    Message alert = new Message();

    public ObservableList<DruglistData> DataList() {
        ObservableList<DruglistData> Datalist = FXCollections.observableArrayList();
        String sql = "select * from tblProduct";
        try {
            conn = DB.ConnectDB.getConnectDB();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            DruglistData data;
            while (rs.next()) {
                data = new DruglistData(rs.getString("MedicineID"),
                        rs.getString("MedicineName"),
                        rs.getString("API"),
                        rs.getInt("Quantity"),
                        rs.getString("Unit"),
                        rs.getInt("Subunitsperunit"),
                        rs.getString("Subunit"),
                        rs.getInt("Itemspersubunit"),
                        rs.getString("Item"),
                        rs.getInt("CostPrice"),
                        rs.getInt("SellingPrice")
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
        ObservableList<DruglistData> showList = DataList();
        DL_Col_MedicineID.setCellValueFactory(new PropertyValueFactory<>("MedicineID"));
        DL_Col_MedicineName.setCellValueFactory(new PropertyValueFactory<>("MedicineName"));
        DL_Col_API.setCellValueFactory(new PropertyValueFactory<>("API"));
        DL_Col_Quantity.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
        DL_Col_Unit.setCellValueFactory(new PropertyValueFactory<>("Unit"));
        DL_Col_SUPU.setCellValueFactory(new PropertyValueFactory<>("Subunitsperunit"));
        DL_Col_SU.setCellValueFactory(new PropertyValueFactory<>("Subunit"));
        DL_Col_IPSU.setCellValueFactory(new PropertyValueFactory<>("Itemspersubunit"));
        DL_Col_Item.setCellValueFactory(new PropertyValueFactory<>("Item"));
        DL_Col_CP.setCellValueFactory(new PropertyValueFactory<>("CostPrice"));
        DL_Col_SP.setCellValueFactory(new PropertyValueFactory<>("SellingPrice"));
        DL_Col_AC.setCellFactory(new Callback<TableColumn<DruglistData, Void>, TableCell<DruglistData, Void>>() {
            @Override
            public TableCell<DruglistData, Void> call(final TableColumn<DruglistData, Void> param) {
                final TableCell<DruglistData, Void> cell = new TableCell<DruglistData, Void>() {

                    private final Button btnUpdate = new Button("Update");
                    private final Button btnDelete = new Button("Delete");

                    {
                        btnUpdate.getStyleClass().add("btn_update");
                        btnDelete.getStyleClass().add("btn_delete");
                        btnUpdate.setOnAction((ActionEvent event) -> {
                            DruglistData pdata = getTableView().getItems().get(getIndex());
                            openUpdateForm(pdata);
                        });

                        btnDelete.setOnAction((ActionEvent event) -> {
                            DruglistData pdata = getTableView().getItems().get(getIndex());
                            if (alert.confirmMessage("Are you sure want to delete Medicine ID: " + pdata.getMedicineID() + "?")) {
                                deleteData(pdata.getMedicineID());
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

        tabledruglist.setItems(showList);
    }

    private void deleteData(String medicineID) {
        String sqlDeleteProductDetails = "DELETE FROM tblProductDetails WHERE MedicineID = ?";
        String sqlDeleteProduct = "DELETE FROM tblProduct WHERE MedicineID = ?";
        try {

            conn = DB.ConnectDB.getConnectDB();
            ps = conn.prepareStatement(sqlDeleteProductDetails);
            ps.setString(1, medicineID);
            ps.executeUpdate();
            ps.close();

            ps = conn.prepareStatement(sqlDeleteProduct);
            ps.setString(1, medicineID);
            ps.executeUpdate();
            alert.successMessage("Deleted Successfully");
        } catch (SQLException e) {
            e.printStackTrace();
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

    private void openUpdateForm(DruglistData pdata) {
        try {
            Data.dl_MedicineID = pdata.getMedicineID();
            Data.dl_MedicineName = pdata.getMedicineName();
            Data.dl_API = pdata.getAPI();
            Data.dl_Costprice = pdata.getCostPrice();
            Data.dl_Sellingprice = pdata.getSellingPrice();
            Data.dl_Quantity = pdata.getQuantity();
            Data.dl_Subunitsperunit = pdata.getSubunitsperunit();
            Data.dl_Itemspersubunit = pdata.getItemspersubunit();
            Data.dl_Unit = pdata.getUnit();
            Data.dl_Subunit = pdata.getSubunit();
            Data.dl_Item = pdata.getItem();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateDrug/UpdateDrug.fxml"));

            Parent root = loader.load();

            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    void showformAdd(MouseEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddDrug/AddDrug.fxml"));
            Parent root = loader.load();
            AddDrugController addDrugController = ControllerHolder.getInstance().getAddDrugController();
            addDrugController.CheckBoxHandlers();

            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void filterData(String keyword) {
        ObservableList<DruglistData> filteredList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM tblProduct WHERE MedicineID LIKE ? OR MedicineName LIKE ? OR API LIKE ?";

        try {
            conn = DB.ConnectDB.getConnectDB();
            ps = conn.prepareStatement(sql);
            String searchKeyword = "%" + keyword + "%";
            ps.setString(1, searchKeyword);
            ps.setString(2, searchKeyword);
            ps.setString(3, searchKeyword);
            rs = ps.executeQuery();

            while (rs.next()) {
                DruglistData data = new DruglistData(
                        rs.getString("MedicineID"),
                        rs.getString("MedicineName"),
                        rs.getString("API"),
                        rs.getInt("Quantity"),
                        rs.getString("Unit"),
                        rs.getInt("Subunitsperunit"),
                        rs.getString("Subunit"),
                        rs.getInt("Itemspersubunit"),
                        rs.getString("Item"),
                        rs.getInt("CostPrice"),
                        rs.getInt("SellingPrice")
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

        tabledruglist.setItems(filteredList);
    }

    public void Search() {
        searchDL.textProperty().addListener((observable, oldValue, newValue) -> {
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
        tabledruglist.setItems(FXCollections.observableArrayList(DataList().subList(fromIndex, toIndex)));
    }

    private Node createPage(int pageIndex) {
        updateTable(pageIndex);
        return tabledruglist;
    }

    public void Pagination() {
        pgtDL.setPageCount(getPageCount());
        pgtDL.setPageFactory(this::createPage);
    }

    public void exportToPDF() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PDF");
        String fontPath = "src/font/arial.ttf";
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File selectedFile = fileChooser.showSaveDialog(btnExportDL.getScene().getWindow());

        if (selectedFile != null) {
            try {

                PdfFont font = PdfFontFactory.createFont(fontPath, PdfEncodings.IDENTITY_H, true);

                PdfWriter writer = new PdfWriter(selectedFile.getAbsolutePath());
                PdfDocument pdf = new PdfDocument(writer);
                Document document = new Document(pdf);

                Paragraph title = new Paragraph("Drug List").setBold().setFontSize(18);
                document.add(title);

                float[] columnWidths = {100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100};
                Table table = new Table(columnWidths);

                TableView<DruglistData> tableView = tabledruglist;
                for (TableColumn<DruglistData, ?> column : tableView.getColumns()) {
                    if (!column.getText().equals("Action")) {
                        table.addCell(new Cell().add(new Paragraph(column.getText()).setFont(font)));
                    }
                }

                ObservableList<DruglistData> dataList = tableView.getItems();
                for (DruglistData data : dataList) {
                    table.addCell(new Cell().add(new Paragraph(data.getMedicineID())));
                    table.addCell(new Cell().add(new Paragraph(data.getMedicineName())));
                    table.addCell(new Cell().add(new Paragraph(data.getAPI())));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(data.getQuantity()))));
                    table.addCell(new Cell().add(new Paragraph(data.getUnit())));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(data.getSubunitsperunit()))));
                    table.addCell(new Cell().add(new Paragraph(data.getSubunit())));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(data.getItemspersubunit()))));
                    table.addCell(new Cell().add(new Paragraph(data.getItem())));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(data.getCostPrice()))));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(data.getSellingPrice()))));
                }
                document.add(table);
                document.close();
                Desktop.getDesktop().open(selectedFile);

                alert.successMessage("Export successful!");
            } catch (IOException e) {
                alert.errorMessage("Export failed: " + e.getMessage());
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ControllerHolder.getInstance().setDruglistController(this);
        showData();
        Search();
        Pagination();
    }

}
