package com.dtflys.redistart.controls;

import com.dtflys.redistart.model.RedisConnection;
import com.dtflys.redistart.model.RedisConnectionStatus;
import javafx.beans.binding.Bindings;
import javafx.scene.control.MenuItem;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;


public class RSConnectionMenuItem extends MenuItem {


    private final RedisConnection connection;

    public RSConnectionMenuItem(RedisConnection connection) {
        this.connection = connection;
        FontIcon icon = new FontIcon();
        icon.setIconLiteral("mdi-checkbox-blank-circle");
        icon.setIconSize(6);
        icon.setIconColor(RSColors.CONNECTION_DEFAULT_COLOR);
        setGraphic(icon);
        textProperty().bind(connection.getConnectionConfig().nameProperty());
        icon.iconColorProperty().bind(RSColors.connectionStatusColorBinding(connection));
    }


}
