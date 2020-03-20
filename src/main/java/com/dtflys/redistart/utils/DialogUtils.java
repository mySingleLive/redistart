package com.dtflys.redistart.utils;

import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class DialogUtils {

    public static void showDialog(Modality modality, Map<String, Object> args) {
        String title = MapUtils.getString(args, "title");
        if (StringUtils.isBlank(title)) {
            title = AppVersion.APP_NAME;
        }
        StageStyle stageStyle = StageStyle.TRANSPARENT;
        Color winColor = Color.TRANSPARENT;
        args = new HashMap<>(args);
        args.put("title", title);
        AbstractRSView.showView("/fxml/Dialog.fxml", title, stageStyle, modality, winColor, true, args);
    }

    public static void showModalDialog(Map<String, Object> args) {
        showDialog(Modality.APPLICATION_MODAL, args);
    }

    public static void alertError(Map<String, Object> args) {

    }

}
