package sam;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Sam extends Application {

    private static double x, y = 0;

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login/Login.fxml"));
            Parent root = loader.load();
            primaryStage.initStyle(StageStyle.UNDECORATED);

            makeWindowDraggable(primaryStage, root);

            Scene scene = new Scene(root);
            String cssPath = getClass().getResource("button-style.css").toExternalForm();
            scene.getStylesheets().add(cssPath);

            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
        e.printStackTrace();
            // Handle FXML loading exception
        }
    }

    public void makeWindowDraggable(Stage stage, Parent root) {
        root.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });

        root.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - x);
            stage.setY(event.getScreenY() - y);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
