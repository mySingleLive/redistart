package com.dtflys.redistart.utils;

import com.dtflys.redistart.controller.DialogController;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.StageStyle;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class RSDialog {

    private final Modality modality;

    private String title = AppVersion.APP_NAME;

    private String content;

    private Integer width;

    private Integer height;

    private Boolean showOkButton = true;

    private Boolean showCancelButton = true;

    private StageStyle stageStyle = StageStyle.TRANSPARENT;

    private Color winColor = Color.TRANSPARENT;

    private Consumer<DialogController> onInit;

    private Consumer<ConfirmResult> onConfirm;

    public RSDialog(Modality modality) {
        this.modality = modality;
    }

    public String title() {
        return title;
    }

    public RSDialog title(String title) {
        this.title = title;
        return this;
    }

    public String content() {
        return content;
    }

    public RSDialog content(String content) {
        this.content = content;
        return this;
    }

    public Integer width() {
        return width;
    }

    public RSDialog width(Integer width) {
        this.width = width;
        return this;
    }

    public Integer height() {
        return height;
    }

    public RSDialog height(Integer height) {
        this.height = height;
        return this;
    }

    public Boolean isShowOkButton() {
        return showOkButton;
    }

    public RSDialog showOkButton(Boolean showOkButton) {
        this.showOkButton = showOkButton;
        return this;
    }

    public Boolean isShowCancelButton() {
        return showCancelButton;
    }

    public RSDialog showCancelButton(Boolean showCancelButton) {
        this.showCancelButton = showCancelButton;
        return this;
    }

    public StageStyle stageStyle() {
        return stageStyle;
    }

    public RSDialog stageStyle(StageStyle stageStyle) {
        this.stageStyle = stageStyle;
        return this;
    }

    public Color winColor() {
        return winColor;
    }

    public RSDialog winColor(Color winColor) {
        this.winColor = winColor;
        return this;
    }

    public Consumer<DialogController> onInit() {
        return onInit;
    }

    public RSDialog onInit(Consumer<DialogController> onInit) {
        this.onInit = onInit;
        return this;
    }

    public Consumer<ConfirmResult> onConfirm() {
        return onConfirm;
    }

    public RSDialog onConfirm(Consumer<ConfirmResult> onConfirm) {
        this.onConfirm = onConfirm;
        return this;
    }

    public Map<String, Object> toMap() {
        Field[] fields = this.getClass().getDeclaredFields();
        Map<String, Object> map = new HashMap<>();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(this);
                if (value != null) {
                    map.put(field.getName(), value);
                }
            } catch (IllegalAccessException e) {
            }
        }
        return map;
    }

    public RSDialog show() {
        Map<String, Object> args = toMap();
        AbstractRSView.showView("/fxml/Dialog.fxml", title, stageStyle, modality, winColor, true, args, null);
        return this;
    }
}
