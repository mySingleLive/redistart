package com.dtflys.redistart;

import com.dtflys.redistart.script.RedisScriptScanner;
import com.dtflys.redistart.view.MainView;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
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

    @Bean
    public RedisScriptScanner redisScriptScanner() {
        RedisScriptScanner redisScriptScanner = new RedisScriptScanner("classpath*:lua/*.lua");
        return redisScriptScanner;
    }

    public static void launchApp(String[] args) {
        launch(App.class, MainView.class, args);
    }

}
