package com.dtflys.redistart.controls;

import com.dtflys.redistart.model.key.RSKey;
import com.dtflys.redistart.model.key.RSKeyFindStatus;
import com.dtflys.redistart.model.key.RSKeySet;
import com.dtflys.redistart.model.key.RSLoadMore;
import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXSpinner;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectPropertyBase;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

public class RSKeyListCell extends JFXListCell<RSKey> {

    private ImageView typeIconView = new ImageView();

    private Label ttlLabel = new Label();

    private JFXSpinner spinner = new JFXSpinner();

    private HBox loadMoreBox = new HBox();

    private Label loadMoreLabel = new Label();

    private Region leftSpace = new Region();

    public RSKeyListCell() {
        typeIconView.setFitWidth(20);
        typeIconView.setFitHeight(20 * 0.75);

        loadMoreBox.setPrefWidth(USE_COMPUTED_SIZE);
        loadMoreBox.setPrefHeight(USE_COMPUTED_SIZE);

        spinner.setPrefWidth(40);
        spinner.setPrefHeight(20);

        leftSpace.setPrefWidth(120);
        loadMoreLabel.setPrefHeight(20);
        loadMoreLabel.setText("Load More");
        loadMoreLabel.getStyleClass().add("load-more-label");
        loadMoreBox.getStyleClass().add("load-more-box");
        loadMoreBox.getChildren().addAll(leftSpace, loadMoreLabel);
    }

    @Override
    protected void updateItem(RSKey item, boolean empty) {
        super.updateItem(item, initBeforeSuper(item, empty));
        if (!empty) {
            if (item instanceof RSLoadMore) {
                setGraphic(loadMoreBox);
                RSKeySet keySet = ((RSLoadMore) item).getKeySet();
                ObjectPropertyBase<RSKeyFindStatus> statusObjectProperty = keySet.statusProperty();
                spinner.opacityProperty().bind(Bindings.createIntegerBinding(() -> {
                    if (statusObjectProperty.get() == RSKeyFindStatus.LOADING) {
                        return 1;
                    }
                    return 0;
                }, statusObjectProperty));
                setMouseTransparent(false);
                loadMoreBox.mouseTransparentProperty().bind(Bindings.createBooleanBinding(
                        () -> statusObjectProperty.get() == RSKeyFindStatus.LOADING,
                        statusObjectProperty));
                getStyleClass().add("load-more-cell");


//                loadMoreBox.setOnMouseClicked(event -> {
//                });
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
//                        loadMoreLabel.setText("Searching (cursor: " + keySet.getLastIndex() + ")");
                        leftSpace.setPrefWidth(60);
                        break;
                    case LOADING:
                    case SEARCH_PAGE_COMPLETED:
//                        loadMoreLabel.setText("Search More");
                        leftSpace.setPrefWidth(110);
                        break;
                    default:
//                        loadMoreLabel.setText("Load More");
                        leftSpace.setPrefWidth(120);
                        break;
                }


                setText("");
            } else {
                getStyleClass().remove("load-more-cell");
                cellRippler.getStyleClass().remove("load-more-cell-rippler");
                cellRippler.setStyle("");
                if (item.getType() != null) {
                    typeIconView.setImage(item.getType().getListIconImage());
                } else {
                    typeIconView.setImage(null);
                }
                ttlLabel.setText("ttl: " + item.getTtl());
                this.setText(item.getKey());
                this.setGraphic(typeIconView);
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
