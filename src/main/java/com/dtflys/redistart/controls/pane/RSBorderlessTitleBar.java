package com.dtflys.redistart.controls.pane;

import com.dtflys.redistart.controls.RSMovableListener;
import com.dtflys.redistart.utils.AppVersion;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

public class RSBorderlessTitleBar extends VBox {

    private RSMovableListener movableListener;

    private final Stage stage;

    private HBox titleBox;

    private Label titleLabel;

    private HBox closeBox;

//    private ImageView closeImageView;

    private final int HEIGHT = 20;


    public RSBorderlessTitleBar(Stage stage) {
        this.stage = stage;
        this.movableListener = new RSMovableListener(this.stage);
        this.setPrefWidth(USE_COMPUTED_SIZE);
        this.setAlignment(Pos.CENTER);
        this.getStyleClass().add("dialog-title-bar");
        this.setPadding(new Insets(0, 0, 0, 0));

        GridPane gridPane = new GridPane();
        gridPane.setPrefHeight(HEIGHT);
        gridPane.setPrefWidth(USE_COMPUTED_SIZE);
        gridPane.setMinWidth(USE_COMPUTED_SIZE);
        gridPane.setMaxWidth(USE_COMPUTED_SIZE);

        closeBox = new HBox();
//        Image closeImg = new Image( "/image/icons_window_close_32px.png");
//        closeImageView = new ImageView(closeImg);
//        closeImageView.setFitWidth(HEIGHT);
//        closeImageView.setFitWidth(HEIGHT);
        FontIcon closeButtonIcon = new FontIcon("icm-cross");
        closeButtonIcon.setIconSize(16);
        closeBox.setAlignment(Pos.CENTER);
        closeBox.getStyleClass().add("dialog-close-button");
        closeBox.getChildren().add(closeButtonIcon);
        closeBox.setPrefWidth(HEIGHT);

        titleLabel = new Label();
        titleLabel.setText(AppVersion.APP_NAME);
        HBox.setMargin(titleLabel, new Insets(0, 0, 0, 10));
        titleBox = new HBox();
        titleBox.setPrefHeight(HEIGHT);
        titleBox.setPrefWidth(USE_COMPUTED_SIZE);
        titleBox.setMinWidth(USE_COMPUTED_SIZE);
        titleBox.setAlignment(Pos.CENTER_LEFT);
        titleBox.getChildren().add(titleLabel);

        HBox buttonBarBox = new HBox();
        buttonBarBox.setPrefHeight(HEIGHT);
        buttonBarBox.setPrefWidth(USE_COMPUTED_SIZE);
        buttonBarBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBarBox.getChildren().add(closeBox);

        buttonBarBox.setOnMouseClicked(eventHandler -> {
            stage.close();
        });

        RowConstraints row = new RowConstraints();
        row.setPrefHeight(USE_COMPUTED_SIZE);
        gridPane.getRowConstraints().add(row);
        ColumnConstraints col0 = new ColumnConstraints();
        col0.setPrefWidth(USE_COMPUTED_SIZE);
        col0.setMaxWidth(USE_COMPUTED_SIZE);
        col0.setMinWidth(USE_COMPUTED_SIZE);
        col0.setHgrow(Priority.ALWAYS);
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPrefWidth(HEIGHT);
        col1.setMaxWidth(HEIGHT);
        col1.setPrefWidth(USE_COMPUTED_SIZE);
        col1.setHgrow(Priority.SOMETIMES);

        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPrefWidth(8);
        col2.setMaxWidth(8);
        col2.setPrefWidth(8);
        col2.setHgrow(Priority.SOMETIMES);

        gridPane.getColumnConstraints().setAll(col0, col1, col2);
        gridPane.setPrefWidth(USE_COMPUTED_SIZE);
        gridPane.setPrefHeight(USE_COMPUTED_SIZE);

        GridPane.setHalignment(titleLabel, HPos.LEFT);
        gridPane.add(titleBox, 0, 0);
        GridPane.setHalignment(buttonBarBox, HPos.RIGHT);
        gridPane.add(buttonBarBox, 1, 0);


//        HBox.setHgrow(gridPane, Priority.ALWAYS);

        this.getChildren().add(gridPane);

        movableListener.movable(this);
    }
}
