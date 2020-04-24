package com.dtflys.redistart.controller;

import com.dtflys.redistart.controls.list.RSKeyListCell;
import com.dtflys.redistart.controls.item.RedisConnectionItem;
import com.dtflys.redistart.controls.menu.RSKeyTypeMenuItem;
import com.dtflys.redistart.model.connection.RedisConnection;
import com.dtflys.redistart.model.database.RedisDatabase;
import com.dtflys.redistart.model.key.*;
import com.dtflys.redistart.service.ConnectionService;
import com.dtflys.redistart.service.RediStartService;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXListView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.controlsfx.control.textfield.CustomTextField;

import javax.annotation.Resource;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@FXMLController
public class NavigationController implements Initializable {

    @Resource
    private ConnectionService connectionService;

    @Resource
    private RediStartService rediStartService;

    @FXML
    private CustomTextField txSearchField;

    @FXML
    private VBox advancedSearchBox;

    @FXML
    private HBox navigationToolbar;

    @FXML
    private Label lbTypes;

    @FXML
    private HBox typesBox;

    private ContextMenu typesMenu = new ContextMenu();

    @FXML
    private JFXListView<RSKey> keyListView;

    @FXML
    private HBox keyStatusBar;

    @FXML
    private Label lbKeyTTL;

    @FXML
    private Label lbKeyStatus;

    private Map<RedisConnection, RedisConnectionItem> connectionItemMap = new HashMap<>();

    private final int KEY_LIST_CELL_HEIGHT = 36;

    private final Map<RSKeyType, BooleanProperty> searchOptionTypeMap = new LinkedHashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connectionService.setOnAfterAddConnection(this::addRedisConnectionItem);
        keyListView.setCellFactory(rsKeyListView -> new RSKeyListCell());
        keyListView.setCache(false);
        keyListView.setCacheShape(false);

        List<RSKeyType> allTypes = RSKeyType.toList();
        allTypes.forEach(type -> {
            searchOptionTypeMap.put(type, new SimpleBooleanProperty(false));
        });

        BooleanProperty[] typeBooleanProperties = new BooleanProperty[allTypes.size()];
        for (int i = 0; i < allTypes.size(); i++) {
            BooleanProperty typeBooleanProperty = searchOptionTypeMap.get(allTypes.get(i));
            typeBooleanProperties[i] = typeBooleanProperty;
        }

        lbTypes.textProperty().bind(
                Bindings.createStringBinding(() -> {
                    List<String> selectedTypes = new ArrayList<>();
                    for (RSKeyType type : searchOptionTypeMap.keySet()) {
                        if (searchOptionTypeMap.get(type).get()) {
                            selectedTypes.add(type.name());
                        }
                    }
                    if (selectedTypes.isEmpty()) {
                        return "none";
                    }
                    return String.join(", ", selectedTypes);
                },
                typeBooleanProperties));

        allTypes.forEach(keyType -> {
            RSKeyTypeMenuItem item = new RSKeyTypeMenuItem(keyType);
            JFXCheckBox checkBox = item.getCheckBox();
            item.setOnAction(event -> {
                checkBox.setSelected(!checkBox.isSelected());
            });
            typesMenu.getItems().add(item);
        });



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

    private ChangeListener<Boolean> lastTypeChangeListener = null;


    private void bindData(RSKeySet keySet, RedisDatabase database) {
        RSLoadMore loadMore = new RSLoadMore(keySet);
        keyListView.setItems(keySet.getKeyList());
        txSearchField.setText("");
        txSearchField.textProperty().unbind();

        ChangeListener<Boolean> searchTypesChangeListener = (observableValue, oldVal, newVal) -> {
            keySet.getSearchInfo().getTypes();
            List<String> changedList = new ArrayList<>();
            for (RSKeyType type : searchOptionTypeMap.keySet()) {
                BooleanProperty booleanProperty = searchOptionTypeMap.get(type);
                if (booleanProperty.get()) {
                    changedList.add(type.name());
                }
            }
            keySet.getSearchInfo().setTypes(changedList);
        };

        for (MenuItem item : typesMenu.getItems()) {
            RSKeyTypeMenuItem keyTypeMenuItem = (RSKeyTypeMenuItem) item;
            keyTypeMenuItem.unbind();
        }

        for (RSKeyType type : searchOptionTypeMap.keySet()) {
            BooleanProperty booleanProperty = searchOptionTypeMap.get(type);
            if (lastTypeChangeListener != null) {
                booleanProperty.removeListener(lastTypeChangeListener);
            }
            booleanProperty.addListener(searchTypesChangeListener);
            lastTypeChangeListener = searchTypesChangeListener;
        }

        List<RSKeyType> allTypes = RSKeyType.toList();
        allTypes.forEach(type -> {
            searchOptionTypeMap.get(type).set(true);
        });

        for (MenuItem item : typesMenu.getItems()) {
            RSKeyTypeMenuItem keyTypeMenuItem = (RSKeyTypeMenuItem) item;
            keyTypeMenuItem.bind(searchOptionTypeMap.get(keyTypeMenuItem.getKeyType()));
        }

        keySet.getSearchInfo().patternProperty().bind(txSearchField.textProperty());
        AtomicInteger lastIndex = new AtomicInteger(-1);
        keySet.setOnBeforeAddResults(keyFindResult -> {
            ObservableList<RSKey> items = keyListView.getItems();
            if (items.size() > 0) {
                RSKey lastItem = items.get(items.size() - 1);
                if (lastItem instanceof RSLoadMore) {
                    items.remove(items.size() - 1);
                }
                lastIndex.set(items.size());
            } else {
                lastIndex.set(-1);
            }
        });
        MultipleSelectionModel selectionModel = keyListView.getSelectionModel();

        keySet.setOnLoadCompleted(keyFindResult -> {
            ObservableList<RSKey> items = keyListView.getItems();
            if (keyFindResult.hasMoreKeys()) {
                items.add(loadMore);
            }
            int index = lastIndex.get();
            int count = (int) Math.floor(keyListView.getHeight() / KEY_LIST_CELL_HEIGHT);
            if (keySet.getStatus() == RSKeyFindStatus.LOAD_PAGE_COMPLETED
                    || keySet.getStatus() == RSKeyFindStatus.SEARCH_PAGE_COMPLETED) {
                if (index > count) {
                    keyListView.scrollTo(index - count);
                }
                if (selectionModel.getSelectedItem() == null) {
                    selectionModel.select(0);
                }
            }
        });
        lbKeyStatus.textProperty().unbind();
        lbKeyStatus.textProperty().bind(createKeyStatusBinding(keySet, database));



        ChangeListener<RSKey> selectedKeyChangeListener = (observableValue, oldItem, newItem) -> {
            if (newItem instanceof RSLoadMore) {
                keySet.findNextPage();
            } else if (newItem != null) {
                rediStartService.setSelectedKey(newItem);
                lbKeyTTL.setText("TTL: " + newItem.getTtl());
            }
        };
        selectionModel.selectedItemProperty().removeListener(selectedKeyChangeListener);
        selectionModel.selectedItemProperty().addListener(selectedKeyChangeListener);

    }

    private StringBinding createKeyStatusBinding(RSKeySet keySet, RedisDatabase database) {
        return Bindings.createStringBinding(() -> {
            int size = keySet.getKeyList().size();
            return "Keys: " + size + " / " + database.getSize();
        }, keySet.getKeyList());
    }

    private void addRedisConnectionItem(RedisConnection connection) {
//        RedisConnectionItem connItem = new RedisConnectionItem(connection);
    }


    public void onSearchCancel(MouseEvent mouseEvent) {
        txSearchField.setText("");
    }

    public void onKeysRefresh(ActionEvent actionEvent) {
        if (connectionService.getSelectedConnection() != null) {
            if (connectionService.getSelectedConnection().getSelectedDatabase() != null) {
                RSKeySet keySet = connectionService.getSelectedConnection().getSelectedDatabase().getKeySet();
                keySet.refreshData();
            }
        }
    }

    public void onSelectedTypesClick(MouseEvent mouseEvent) {
        typesMenu.show(typesBox, Side.BOTTOM, 0, 0);
    }
}
