package com.dtflys.redistart.controls.pane;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class RSBorderlessPane extends HBox {

    private final Parent root;

    public RSBorderlessPane(Parent root, boolean useShadow, boolean hasTitleBar) {
        this.root = root;
        if (useShadow) {
            DropShadow dropshadow = new DropShadow();// 阴影向外
            dropshadow.setRadius(10);// 颜色蔓延的距离
            dropshadow.setOffsetX(0);// 水平方向，0则向左右两侧，正则向右，负则向左
            dropshadow.setOffsetY(0);// 垂直方向，0则向上下两侧，正则向下，负则向上
            dropshadow.setSpread(0.1);// 颜色变淡的程度
            dropshadow.setColor(Color.BLACK);// 设置颜色

            BorderPane borderPane = new BorderPane();
            borderPane.setEffect(dropshadow);// 绑定指定窗口控件
            this.getChildren().add(borderPane);

            if (hasTitleBar) {
                RSBorderlessTitleBar titleBar = new RSBorderlessTitleBar();
                borderPane.setTop(titleBar);
            }
            borderPane.setCenter(root);

            this.setAlignment(Pos.CENTER);// 设置对齐方式为居中
            this.setPadding(new Insets(dropshadow.getRadius()));// 设置要显示的阴影宽度为根控件与底层容器的四边距离
            this.getStylesheets().add("/css/global.css");
            this.getStyleClass().add("dialog-parent");

        }

    }
}
