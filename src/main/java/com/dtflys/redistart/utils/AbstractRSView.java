package com.dtflys.redistart.utils;

import de.felixroske.jfxsupport.AbstractFxmlView;
import de.felixroske.jfxsupport.FXMLView;
import de.felixroske.jfxsupport.GUIState;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class AbstractRSView extends AbstractFxmlView {

    private Parent load(String path, Map<String, Object> args) {
        FXMLLoader loader = new FXMLLoader(AbstractRSView.class.getResource(path));
        Parent root = null;

        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Object controller = loader.getController();
        if (controller instanceof RSController) {
            Class clazz = controller.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                String fname = field.getName();
                Object val = args.get(fname);
                if (val != null) {
                    boolean canNotAccess = false;
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                        canNotAccess = true;
                    }
                    try {
                        field.set(controller, val);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } finally {
                        if (canNotAccess) {
                            field.setAccessible(false);
                        }
                    }
                }
            }
            ((RSController) controller).init(args);
        }
        return root;
    }

    private void showView(String path, String title, StageStyle style, final Modality mode, Map<String, Object> args) {
        Parent root = load(path, args);
        Scene scene = new Scene(root);
        Stage newStage = new Stage();
        newStage.setScene(scene);
        newStage.initModality(mode);
        newStage.initOwner(GUIState.getStage());
        newStage.setTitle(title);
        newStage.initStyle(style);
        newStage.showAndWait();
    }

    public Parent loadAsParent() {
        return loadAsParent(new HashMap<>());
    }

    public Parent loadAsParent(Map<String, Object> args) {
        FXMLView fxmlView = getClass().getAnnotation(FXMLView.class);
        String path = fxmlView.value();
        return load(path, args);
    }

    public void showStage(final Modality mode, Map<String, Object> args) {
        FXMLView fxmlView = getClass().getAnnotation(FXMLView.class);
        String path = fxmlView.value();
        String title = MapUtils.getString(args, "title");
        if (StringUtils.isEmpty(title)) {
            title = fxmlView.title();
        }
        String style = fxmlView.stageStyle();
        StageStyle stageStyle = StageStyle.valueOf(style.toUpperCase());
        showView(path, title, stageStyle, mode, args);
    }

    public void showStage(Map<String, Object> args) {
        FXMLView fxmlView = getClass().getAnnotation(FXMLView.class);
        String path = fxmlView.value();
        String title = fxmlView.title();
        String style = fxmlView.stageStyle();
        StageStyle stageStyle = StageStyle.valueOf(style.toUpperCase());
        showView(path, title, stageStyle, Modality.NONE, args);
    }

}
