package com.dtflys.redistart.utils;

import de.felixroske.jfxsupport.AbstractFxmlView;
import de.felixroske.jfxsupport.FXMLView;
import de.felixroske.jfxsupport.GUIState;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
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

    private static Parent load(String path, Map<String, Object> args) {
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

    public static void showView(String path, String title, StageStyle style, final Modality mode, Color winColor, Boolean useShadow, Map<String, Object> args) {
        Parent root = load(path, args);
        if (useShadow != null && useShadow) {
            DropShadow dropshadow = new DropShadow();// 阴影向外
            dropshadow.setRadius(10);// 颜色蔓延的距离
            dropshadow.setOffsetX(1);// 水平方向，0则向左右两侧，正则向右，负则向左
            dropshadow.setOffsetY(1.5);// 垂直方向，0则向上下两侧，正则向下，负则向上
            dropshadow.setSpread(0.1);// 颜色变淡的程度
            dropshadow.setColor(Color.BLACK);// 设置颜色
            root.setEffect(dropshadow);// 绑定指定窗口控件

            HBox parentBox = new HBox();// 创建最底层的面板
            parentBox.setAlignment(Pos.CENTER);// 设置对齐方式为居中
            parentBox.setPadding(new Insets(dropshadow.getRadius()));// 设置要显示的阴影宽度为根控件与底层容器的四边距离
            parentBox.getChildren().add(root);// 添加根控件到底层容器中
            parentBox.getStylesheets().add("/css/global.css");
            parentBox.getStyleClass().add("dialog-parent");
            root = parentBox;
        }
        Scene scene = null;
        scene = new Scene(root, winColor);
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

    public void showStage(final Modality mode, Color winColor, Boolean useShadow, Map<String, Object> args) {
        FXMLView fxmlView = getClass().getAnnotation(FXMLView.class);
        String path = fxmlView.value();
        String title = MapUtils.getString(args, "title");
        if (StringUtils.isEmpty(title)) {
            title = fxmlView.title();
        }
        String style = fxmlView.stageStyle();
        StageStyle stageStyle = StageStyle.valueOf(style.toUpperCase());
        showView(path, title, stageStyle, mode, winColor, useShadow, args);
    }

    public void showStage(Color winColor, Boolean useShadow, Map<String, Object> args) {
        FXMLView fxmlView = getClass().getAnnotation(FXMLView.class);
        String path = fxmlView.value();
        String title = fxmlView.title();
        String style = fxmlView.stageStyle();
        StageStyle stageStyle = StageStyle.valueOf(style.toUpperCase());
        showView(path, title, stageStyle, Modality.NONE, winColor, useShadow, args);
    }

    public void showStage(final Modality mode, Map<String, Object> args) {
        FXMLView fxmlView = getClass().getAnnotation(FXMLView.class);
        String path = fxmlView.value();
        String title = fxmlView.title();
        String style = fxmlView.stageStyle();
        StageStyle stageStyle = StageStyle.valueOf(style.toUpperCase());
        showView(path, title, stageStyle, mode, null, null, args);
    }


    public void showStage(Map<String, Object> args) {
        showStage(Modality.NONE, args);
    }


    public void showModalDialog(Map<String, Object> args) {
        showStage(Modality.APPLICATION_MODAL, Color.TRANSPARENT, true, args);
    }


}
