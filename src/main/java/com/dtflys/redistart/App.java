package com.dtflys.redistart;

import com.dtflys.redistart.utils.ResizeUtils;
import com.dtflys.redistart.view.MainView;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class App extends AbstractJavaFxApplicationSupport {

    @Override
    public void start(Stage stage) throws Exception {
        super.start(stage);
    }

    @Override
    public void beforeInitialView(Stage stage, ConfigurableApplicationContext ctx) {
//        stage.initStyle(StageStyle.UNDECORATED);
//        stage.setResizable(true);
//        ResizeUtils.addResizeListener(stage, stage.getScene().getRoot());
    }

    public static void launchApp(String[] args) {
        launch(App.class, MainView.class, args);
    }

}
