package agh.ostatni5.eomc.viewFx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MyApp extends Application {
    MyApp() {
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Evolution of moving creatures");

        Button button = new Button("click");
        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(button);

        Scene scene = new Scene(stackPane, 300, 300);

        stage.setScene(scene);
        stage.show();
    }


}
