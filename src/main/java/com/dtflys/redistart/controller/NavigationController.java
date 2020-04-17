package com.dtflys.redistart.controller;

import com.dtflys.redistart.controls.RSKeyListCell;
import com.dtflys.redistart.controls.item.RedisConnectionItem;
import com.dtflys.redistart.model.connection.RedisConnection;
import com.dtflys.redistart.model.database.RedisDatabase;
import com.dtflys.redistart.model.key.RSKey;
import com.dtflys.redistart.model.key.RSKeySet;
import com.dtflys.redistart.model.key.RSLoadMore;
import com.dtflys.redistart.service.ConnectionService;
import com.dtflys.redistart.utils.ControlUtils;
import com.jfoenix.controls.JFXListView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import org.controlsfx.control.StatusBar;
import org.controlsfx.control.textfield.CustomTextField;

import javax.annotation.Resource;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@FXMLController
public class NavigationController implements Initializable {

    @Resource
    private ConnectionService connectionService;

    @FXML
    private CustomTextField txSearchField;

    @FXML
    private JFXListView<RSKey> keyListView;

    @FXML
    private HBox keyStatusBar;

    @FXML
    private Label lbKeyStatus;

    private Map<RedisConnection, RedisConnectionItem> connectionItemMap = new HashMap<>();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connectionService.setOnAfterAddConnection(this::addRedisConnectionItem);
        keyListView.setCellFactory(rsKeyListView -> new RSKeyListCell());
        keyListView.setCache(false);
        keyListView.setCacheShape(false);

        if (connectionService.getSelectedConnection() != null) {
            connectionService.getSelectedConnection().selectedDatabaseProperty().addListener((observableValue1, redisDatabase, newDatabase) -> {
                Platform.runLater(() -> {
                    RSKeySet keySet = newDatabase.getKeySet();
                    bindData(keySet, connectionService.getSelectedConnection().selectedDatabaseProperty().get());
                });
            });
            if (connectionService.getSelectedConnection().getSelectedDatabase() != null) {
                RSKeySet keySet = connectionService.getSelectedConnection().getSelectedDatabase().getKeySet();
                bindData(keySet, connectionService.getSelectedConnection().getSelectedDatabase());
            }
        }

    }

    private void bindData(RSKeySet keySet, RedisDatabase database) {
        RSLoadMore loadMore = new RSLoadMore(keySet);
        keyListView.setItems(keySet.getKeyList());
        txSearchField.setText("");
        txSearchField.textProperty().unbind();
        keySet.getSearchInfo().patternProperty().bind(txSearchField.textProperty());
        AtomicInteger lastIndex = new AtomicInteger(-1);
        keySet.setOnBeforeAddResults(keyFindResult -> {
            ObservableList<RSKey> items = keyListView.getItems();
            if (items.size() > 0) {
                RSKey lastItem = items.get(items.size() - 1);
                if (lastItem instanceof RSLoadMore) {
                    items.remove(items.size() - 1);
                    keyListView.refresh();
                }
                lastIndex.set(items.size());
            } else {
                lastIndex.set(-1);
            }
        });
        keySet.setOnLoadCompleted(keyFindResult -> {
            ObservableList<RSKey> items = keyListView.getItems();
            if (keyFindResult.hasMoreKeys()) {
                items.add(loadMore);
            }
            int index = lastIndex.get();
            int count = (int) Math.floor(keyListView.getHeight() / 40);
            if (index > count) {
                keyListView.scrollTo(index - count);
            }
        });
        lbKeyStatus.textProperty().bind(createKeyStatusBinding(keySet, database));

        ChangeListener changeListener = (observableValue, oldItem, newItem) -> {
            if (newItem instanceof RSLoadMore) {

                keySet.findNextPage();
            }
        };
        keyListView.getSelectionModel().selectedItemProperty().removeListener(changeListener);
        keyListView.getSelectionModel().selectedItemProperty().addListener(changeListener);

    }

    private StringBinding createKeyStatusBinding(RSKeySet keySet, RedisDatabase database) {
        return Bindings.createStringBinding(() -> {
            int size = keySet.getKeyList().size();
            return "Keys: " + size + " / " + database.getSize();
        }, keySet.getKeyList());
    }

    private void addRedisConnectionItem(RedisConnection connection) {
        RedisConnectionItem connItem = new RedisConnectionItem(connection);

    }


    public void onSearchCancel(MouseEvent mouseEvent) {
        txSearchField.setText("");
    }
}
