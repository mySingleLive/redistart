package com.dtflys.redistart.controls.list;

import com.dtflys.redistart.model.key.RSKey;
import com.dtflys.redistart.model.key.RSKeyFindStatus;
import com.dtflys.redistart.model.key.RSKeySet;
import com.dtflys.redistart.model.key.RSLoadMore;
import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXSpinner;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectPropertyBase;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

public class RSKeyListCell extends JFXListCell<RSKey> {

    private HBox keyNameBox = new HBox();

    private ImageView typeIconView = new ImageView();

    private Label keyNameLabel = new Label();

    private Label ttlLabel = new Label();

//    private JFXSpinner spinner = new JFXSpinner();

    private HBox loadMoreBox = new HBox();

    private Label loadMoreLabel = new Label();

    private Region leftSpace = new Region();

    public RSKeyListCell() {

        typeIconView.setFitWidth(18);
        typeIconView.setFitHeight(14);

        keyNameLabel.getStyleClass().add("key-name-text");
        HBox.setMargin(keyNameLabel, new Insets(0, 10, 0, 6));

        keyNameBox.getChildren().addAll(typeIconView, keyNameLabel);

        loadMoreBox.setPrefWidth(USE_COMPUTED_SIZE);
        loadMoreBox.setPrefHeight(USE_COMPUTED_SIZE);

//        spinner.setPrefWidth(20);
//        spinner.setPrefHeight(20);

        leftSpace.setPrefWidth(120);
        loadMoreLabel.setPrefHeight(18);
        loadMoreLabel.setText("Load More");
        loadMoreLabel.getStyleClass().add("load-more-label");
        loadMoreBox.getStyleClass().add("load-more-box");
        loadMoreBox.getChildren().addAll(leftSpace, loadMoreLabel);
    }

    @Override
    protected void updateItem(RSKey item, boolean empty) {
        super.updateItem(item, empty);
        if (!empty) {
            if (item.getType() != null) {
                typeIconView.setImage(item.getType().getListIconImage());
            } else {
                typeIconView.setImage(null);
            }
//            ttlLabel.setText("ttl: " + item.getTtl());
//            this.setText(item.getKey());
//            this.setGraphic(typeIconView);

            if (item instanceof RSLoadMore) {
                setGraphic(loadMoreBox);
                RSKeySet keySet = ((RSLoadMore) item).getKeySet();
                ObjectPropertyBase<RSKeyFindStatus> statusObjectProperty = keySet.statusProperty();
                setMouseTransparent(false);
                loadMoreBox.mouseTransparentProperty().bind(Bindings.createBooleanBinding(
                        () -> statusObjectProperty.get() == RSKeyFindStatus.LOADING,
                        statusObjectProperty));
                if (!getStyleClass().contains("load-more-cell")) {
                    getStyleClass().add("load-more-cell");
                }
                loadMoreLabel.textProperty().unbind();
                loadMoreLabel.textProperty().bind(Bindings.createStringBinding(() -> {
                    switch (keySet.getStatus()) {
                        case SEARCHING:
                            return "Searching (cursor: " + keySet.getLastIndex() + ")";
                        case LOADING:
                            return "Loading...";
                        case SEARCH_PAGE_COMPLETED:
                            return "Search More";
                        default:
                            return "Load More";
                    }
                }, keySet.statusProperty()));

                switch (keySet.getStatus()) {
                    case SEARCHING:
                        leftSpace.setPrefWidth(60);
                        break;
                    case LOADING:
                    case SEARCH_PAGE_COMPLETED:
                        leftSpace.setPrefWidth(110);
                        break;
                    default:
                        leftSpace.setPrefWidth(120);
                        break;
                }
                setText("");
            } else {
                getStyleClass().remove("load-more-cell");
                if (item.getType() != null) {
                    typeIconView.setImage(item.getType().getListIconImage());
                } else {
                    typeIconView.setImage(null);
                }
                keyNameLabel.setText(item.getKey());
                this.setText("TTL: " + item.getTtl());
                this.setText("");
//                this.setText(item.getKey());
                this.setGraphic(keyNameBox);
            }
        }
    }

    private boolean initBeforeSuper(RSKey item, boolean empty) {
        if (item instanceof RSLoadMore) {
            return empty;
        }
        return empty;
    }

}
