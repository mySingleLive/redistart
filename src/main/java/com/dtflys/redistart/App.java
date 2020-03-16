package com.dtflys.redistart;

import com.dtflys.redistart.view.MainView;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App extends AbstractJavaFxApplicationSupport {

    @Override
    public void start(Stage stage) throws Exception {
        super.start(stage);
    }

    public static void launchApp(String[] args) {
        launch(App.class, MainView.class, args);
    }

}
