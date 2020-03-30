package com.dtflys.redistart.utils;

import com.dtflys.redistart.controller.NavigationController;
import com.dtflys.redistart.controls.pane.RSBorderlessPane;
import de.felixroske.jfxsupport.AbstractFxmlView;
import de.felixroske.jfxsupport.FXMLView;
import de.felixroske.jfxsupport.GUIState;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.annotation.Resource;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class AbstractRSView extends AbstractFxmlView {

    private ApplicationContext applicationContext;

    private static Parent load(String path, Map<String, Object> args, ApplicationContext applicationContext) {
        FXMLLoader loader = new FXMLLoader(AbstractRSView.class.getResource(path));
        Parent root = null;
        loader.setControllerFactory(type -> {
            System.out.println("Do Controller Factory: " + type.getName());
            if (type == NavigationController.class) {
                System.out.println("");
            }
            Object newController = null;
            try {
                newController = type.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (newController != null) {
                initController(newController, args, applicationContext);
            }
            return newController;
        });
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        Object controller = loader.getController();
//        initController(controller, args, applicationContext);
        return root;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        super.setApplicationContext(applicationContext);
        this.applicationContext = applicationContext;
    }

    private static void initController(Object controller, Map<String, Object> args, ApplicationContext applicationContext) {
        Class clazz = controller.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String fname = field.getName();
            Object val = args.get(fname);
            if (applicationContext != null) {
                Resource resourceAnn = field.getAnnotation(Resource.class);
                Autowired autowiredAnn = field.getAnnotation(Autowired.class);
                if (val == null && resourceAnn != null) {
                    String name = resourceAnn.name();
                    if (StringUtils.isBlank(name)) {
                        name = fname;
                    }
                    val = applicationContext.getBean(name, field.getType());
                }
                if (val == null && autowiredAnn != null) {
                    val = applicationContext.getBean(field.getType());
                }
            }
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
        if (controller instanceof RSController) {
            ((RSController) controller).receiveArguments(args);
        }
    }

    public static void showView(String path, String title, StageStyle style,
                                final Modality mode, Color winColor, Boolean useShadow,
                                Map<String, Object> args, ApplicationContext applicationContext) {
        Stage newStage = new Stage();
        Parent root = load(path, args, applicationContext);
        if (useShadow != null && useShadow) {
            root = new RSBorderlessPane(newStage, root, useShadow, false);
        }
        Scene scene = null;
        scene = new Scene(root, winColor);
        newStage.setScene(scene);
        newStage.initModality(mode);
        newStage.initOwner(GUIState.getStage());
        newStage.setTitle(title);
        newStage.initStyle(style);
        newStage.showAndWait();
    }


    public static void showBorderlessView(String path, String title, StageStyle style,
                                final Modality mode, Color winColor,
                                Map<String, Object> args, ApplicationContext applicationContext) {
        Stage newStage = new Stage();
        Parent root = load(path, args, applicationContext);
        root = new RSBorderlessPane(newStage, root, true, true);
        Scene scene = new Scene(root, winColor);
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
        return load(path, args, applicationContext);
    }

    public void showStage(final Modality mode, Color winColor, Boolean useShadow, Map<String, Object> args) {
        FXMLView fxmlView = getClass().getAnnotation(FXMLView.class);
        String path = fxmlView.value();
        String title = MapUtils.getString(args, "title");
        if (StringUtils.isEmpty(title)) {
            title = fxmlView.title();
        }
        String style = fxmlView.stageStyle();
        StageStyle stageStyle = StageStyle.valueOf(style.toUpperCase());
        showView(path, title, stageStyle, mode, winColor, useShadow, args, applicationContext);
    }

    public void showStage(Color winColor, Boolean useShadow, Map<String, Object> args) {
        FXMLView fxmlView = getClass().getAnnotation(FXMLView.class);
        String path = fxmlView.value();
        String title = fxmlView.title();
        String style = fxmlView.stageStyle();
        StageStyle stageStyle = StageStyle.valueOf(style.toUpperCase());
        showView(path, title, stageStyle, Modality.NONE, winColor, useShadow, args, applicationContext);
    }


    public void showBorderlessStage(Color winColor, Map<String, Object> args) {
        FXMLView fxmlView = getClass().getAnnotation(FXMLView.class);
        String path = fxmlView.value();
        String title = fxmlView.title();
        String style = fxmlView.stageStyle();
        StageStyle stageStyle = StageStyle.valueOf(style.toUpperCase());
        showBorderlessView(path, title, stageStyle, Modality.NONE, winColor, args, applicationContext);
    }


    public void showStage(final Modality mode, Map<String, Object> args) {
        FXMLView fxmlView = getClass().getAnnotation(FXMLView.class);
        String path = fxmlView.value();
        String title = fxmlView.title();
        String style = fxmlView.stageStyle();
        StageStyle stageStyle = StageStyle.valueOf(style.toUpperCase());
        showView(path, title, stageStyle, mode, null, null, args, applicationContext);
    }


    public void showBorderlessStage(final Modality mode, Map<String, Object> args) {
        FXMLView fxmlView = getClass().getAnnotation(FXMLView.class);
        String path = fxmlView.value();
        String title = fxmlView.title();
        String style = fxmlView.stageStyle();
        StageStyle stageStyle = StageStyle.valueOf(style.toUpperCase());
        showBorderlessView(path, title, stageStyle, mode, null, args, applicationContext);
    }

    public void showStage(Map<String, Object> args) {
        showStage(Modality.NONE, args);
    }


    public void showModalDialog(Map<String, Object> args) {
        showStage(Modality.APPLICATION_MODAL, Color.TRANSPARENT, true, args);
    }


}
