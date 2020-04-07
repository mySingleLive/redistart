package com.dtflys.redistart.controls;

import com.dtflys.redistart.model.RedisConnection;
import com.dtflys.redistart.model.RedisConnectionStatus;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.scene.paint.Color;

public class RSColors {

    public final static Color CONNECTION_DEFAULT_COLOR = Color.valueOf("#aea6a6");

    public final static Color CONNECTION_CONNECTING_COLOR = Color.valueOf("#5bd558");

    public final static Color CONNECTION_DISCONNECTING_COLOR = Color.valueOf("#b52525");


    public static ObjectBinding connectionStatusColorBinding(RedisConnection connection) {
        if (connection == null) {
            return Bindings.createObjectBinding(() -> CONNECTION_DEFAULT_COLOR);
        }
        return Bindings.createObjectBinding(() -> {
            if (connection == null) {
                return CONNECTION_DEFAULT_COLOR;
            }
            RedisConnectionStatus status = connection.statusProperty().get();
            switch (status) {
                case CONNECTING:
                    return CONNECTION_DEFAULT_COLOR;
                case CLOSED:
                    return CONNECTION_DISCONNECTING_COLOR;
                default:
                    return CONNECTION_CONNECTING_COLOR;
            }
        }, connection.statusProperty());
    }
}
