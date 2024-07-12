package sam;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.control.Button;

public class ExportController implements Initializable {
    @FXML
    private Button closeButton;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void closeForm() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}
