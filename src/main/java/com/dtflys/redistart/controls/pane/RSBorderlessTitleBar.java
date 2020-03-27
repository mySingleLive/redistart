package com.dtflys.redistart.controls.pane;

import com.dtflys.redistart.utils.AppVersion;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;


public class RSBorderlessTitleBar extends HBox {

    private HBox titleBox;

    private Label titleLabel;

    private HBox closeBox;

    private ImageView closeImageView;

    public RSBorderlessTitleBar() {
        this.setPrefWidth(USE_COMPUTED_SIZE);
        this.getStyleClass().add("dialog-title-bar");
        this.setPadding(new Insets(0, 0, 0, 0));

        GridPane gridPane = new GridPane();
        gridPane.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, new Insets(0, 0, 0, 0))));


        closeBox = new HBox();
        Image closeImg = new Image( "/image/icons_window_close_32px.png");
        closeImageView = new ImageView(closeImg);
        closeImageView.setFitWidth(35);
        closeImageView.setFitWidth(35);
        closeBox.setAlignment(Pos.CENTER);
        closeBox.getStyleClass().add("window-close-button");
        closeBox.getChildren().add(closeImageView);

        titleLabel = new Label();
        titleLabel.setText(AppVersion.APP_NAME);
        HBox.setMargin(titleLabel, new Insets(0, 0, 0, 5));
        titleBox = new HBox();
        titleBox.setPrefHeight(35);
        titleBox.setPrefWidth(300);
        titleBox.setAlignment(Pos.CENTER_LEFT);
        titleBox.getChildren().add(titleLabel);

        HBox buttonBarBox = new HBox();
        buttonBarBox.setPrefHeight(35);
        buttonBarBox.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, new Insets(0, 0, 0, 0))));
        buttonBarBox.setPrefWidth(USE_COMPUTED_SIZE);
        buttonBarBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBarBox.getChildren().add(closeBox);

        GridPane.setHalignment(titleLabel, HPos.LEFT);
        gridPane.add(titleBox, 0, 0);
        Region centerRegion = new Region();
        centerRegion.setPrefWidth(USE_COMPUTED_SIZE);
        gridPane.add(centerRegion, 1, 0);
        GridPane.setHalignment(buttonBarBox, HPos.RIGHT);
        gridPane.add(buttonBarBox, 2, 0);


        RowConstraints row = new RowConstraints();
        row.setPrefHeight(USE_COMPUTED_SIZE);
        gridPane.getRowConstraints().add(row);
        ColumnConstraints col0 = new ColumnConstraints();
        col0.setPrefWidth(200);
        col0.setMaxWidth(200);
        col0.setPrefWidth(USE_COMPUTED_SIZE);
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPrefWidth(USE_COMPUTED_SIZE);
        col1.setMaxWidth(USE_COMPUTED_SIZE);
        col1.setPrefWidth(USE_COMPUTED_SIZE);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPrefWidth(135);
        col2.setMaxWidth(135);
        col2.setPrefWidth(USE_COMPUTED_SIZE);

        gridPane.getColumnConstraints().setAll(col0, col1, col2);
        gridPane.setPrefWidth(USE_COMPUTED_SIZE);
        gridPane.setPrefHeight(USE_COMPUTED_SIZE);

//        HBox.setHgrow(gridPane, Priority.ALWAYS);

        this.getChildren().add(gridPane);

    }
}
