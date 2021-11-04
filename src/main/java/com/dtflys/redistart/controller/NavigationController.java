package com.dtflys.redistart.controller;

import com.dtflys.redistart.controls.list.RSKeyListCell;
import com.dtflys.redistart.controls.item.RedisConnectionItem;
import com.dtflys.redistart.controls.menu.RSKeyTypeMenuItem;
import com.dtflys.redistart.model.action.RSAction;
import com.dtflys.redistart.model.connection.RedisConnection;
import com.dtflys.redistart.model.database.RedisDatabase;
import com.dtflys.redistart.model.key.*;
import com.dtflys.redistart.model.lua.RSResultCode;
import com.dtflys.redistart.model.ttl.RSTtlOperator;
import com.dtflys.redistart.service.ConnectionService;
import com.dtflys.redistart.service.RediStartService;
import com.dtflys.redistart.utils.ControlUtils;
import com.dtflys.redistart.utils.Dialogs;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.controlsfx.control.textfield.CustomTextField;

import javax.annotation.Resource;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

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

    @FXML
    private HBox ttlConditionBox;

    @FXML
    private HBox ttlOpBox;

    private ContextMenu typesMenu = new ContextMenu();

    private ContextMenu ttlOpMenu = new ContextMenu();

    private ContextMenu addTypesMenu = new ContextMenu();

    @FXML
    private Label lbTTLOp;

    @FXML
    private JFXButton btnAddKey;

    private ObjectPropertyBase<RSTtlOperator> currentTTLOp = new SimpleObjectProperty<>();

    @FXML
    private CustomTextField txTTLCondition;

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
        keyListView.setCellFactory(rsKeyListView -> new RSKeyListCell().setOnMouseDoubleClick(key -> {
            System.out.println("双击 " + key.getKey());
            rediStartService.doAction(RSAction.SELECT_KEY_ON_NEW_TAB, key);
        }));
        keyListView.setCache(false);
        keyListView.setCacheShape(false);

        keyListView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                event.consume();
            }
        });

        ControlUtils.numberField(txTTLCondition, "-1");

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
            RSKeyTypeMenuItem tyItem = new RSKeyTypeMenuItem(true, keyType);
            CheckBox checkBox = tyItem.getCheckBox();
            tyItem.setOnAction(event -> {
                checkBox.setSelected(!checkBox.isSelected());
            });
            typesMenu.getItems().add(tyItem);

            RSKeyTypeMenuItem addTyItem = new RSKeyTypeMenuItem(false, keyType);
            addTyItem.setText(addTyItem.getText() +" key");
            addTyItem.setOnAction(event -> {
                Dialogs.applicationModal()
                        .title("添加Key")
                        .content("")
                        .width(320)
                        .height(60)
                        .showOkButton(false)
                        .showCancelButton(false)
                        .onInit(dController -> {
                            HBox contentBox = dController.getContentBox();
                            contentBox.getChildren().clear();
                            contentBox.setAlignment(Pos.CENTER_LEFT);
                            CustomTextField keyNameField = new CustomTextField();
                            keyNameField.setPrefWidth(Region.USE_COMPUTED_SIZE);
                            keyNameField.setPromptText("Key Name");
                            HBox iconBox = new HBox();
//                            ObjectPropertyBase<RSKeyType> currentType = new SimpleObjectProperty<>(keyType);
//                            ContextMenu dTypeMenu = new ContextMenu();
//                            allTypes.forEach(dKeyType -> {
//                                RSKeyTypeMenuItem dAddTypeItem = new RSKeyTypeMenuItem(false, dKeyType);
//                                dTypeMenu.getItems().add(dAddTypeItem);
//                            });

                            ImageView imgView = new ImageView(keyType.getListIconImage());
                            imgView.setFitWidth(18);
                            imgView.setFitHeight(14);
                            iconBox.setAlignment(Pos.CENTER);
                            HBox.setMargin(imgView, new Insets(0, 5, 0, 5));
                            iconBox.getChildren().add(imgView);
//                            iconBox.setOnMouseClicked(mouseEvent -> {
//                                dTypeMenu.show(iconBox, 0, 0);
//                            });
                            keyNameField.setLeft(iconBox);
                            keyNameField.setOnKeyPressed(keyEvent -> {
                                String text = keyNameField.getText();
                                if (keyEvent.getCode() == KeyCode.ENTER) {
                                    if (!text.isBlank()) {
                                        RedisConnection connection = connectionService.getSelectedConnection();
                                        if (connection == null) {
                                            return;
                                        }
                                        RedisDatabase database = connection.getSelectedDatabase();
                                        if (database == null) {
                                            return;
                                        }
                                        database.getKeySet().addNewKey(keyType, text, result -> {
                                            if (result.getCode() == RSResultCode.SUCCESS) {
                                                keyListView.scrollTo(result.getKey());
                                                keyListView.getSelectionModel().select(result.getKey());
                                                dController.close();
                                            } else {
                                            }
                                        });
                                    }
                                } else if (keyEvent.getCode() == KeyCode.ESCAPE) {
                                    dController.close();
                                }
                            });
                            HBox.setHgrow(keyNameField, Priority.ALWAYS);
                            HBox.setMargin(keyNameField, new Insets(0, 0, 0, 0));
                            contentBox.getChildren().add(keyNameField);
                        })
                        .show();
            });
            addTypesMenu.getItems().add(addTyItem);
        });

        for (RSTtlOperator op : RSTtlOperator.values()) {
            MenuItem item = new MenuItem();
            item.setText(op.getDisplayText());
            item.setOnAction(event -> {
                currentTTLOp.set(op);
            });
            ttlOpMenu.getItems().add(item);
        }


        currentTTLOp.addListener((observableValue, oldOp, newOp) -> {
            ttlConditionBox.getChildren().clear();
            ttlConditionBox.getChildren().add(ttlOpBox);
            if (newOp != RSTtlOperator.NONE) {
                ttlConditionBox.getChildren().add(txTTLCondition);
            }
        });
        currentTTLOp.set(RSTtlOperator.NONE);
        lbTTLOp.textProperty().bind(Bindings.createStringBinding(
                () -> currentTTLOp.get().getDisplayText(), currentTTLOp));


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
            keySet.getSearchCondition().getTypes();
            List<String> changedList = new ArrayList<>();
            for (RSKeyType type : searchOptionTypeMap.keySet()) {
                BooleanProperty booleanProperty = searchOptionTypeMap.get(type);
                if (booleanProperty.get()) {
                    changedList.add(type.name());
                }
            }
            keySet.getSearchCondition().setTypes(changedList);
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
        keySet.getSearchCondition().patternProperty().bind(txSearchField.textProperty());
        keySet.getSearchCondition().ttlOperatorProperty().bind(currentTTLOp);
        keySet.getSearchCondition().ttlProperty().bind(Bindings.createIntegerBinding(
                () -> {
                    try {
                        return Integer.parseInt(txTTLCondition.getText());
                    } catch (Throwable th) {
                        return -1;
                    }
                },
                txTTLCondition.textProperty()));
        AtomicInteger lastIndex = new AtomicInteger(-1);
        keySet.setOnBeforeAddResults(keyFindResult -> {
            ObservableList<RSKey> items = keyListView.getItems();
            if (items.size() > 0) {
                RSKey lastItem = items.get(items.size() - 1);
                if (lastItem instanceof RSLoadMore) {
                    items.remove(items.size() - 1);
                }
                if (keySet.getAddedKeyList().size() > 0) {
                    items.removeAll(keySet.getAddedKeyList());
                }
                lastIndex.set(items.size());
            } else {
                lastIndex.set(-1);
            }
        });
        MultipleSelectionModel keySelectionModel = keyListView.getSelectionModel();

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
                if (keySelectionModel.getSelectedItem() == null) {
                    keySelectionModel.select(0);
                }
            }
        });
        lbKeyStatus.textProperty().unbind();
        lbKeyStatus.textProperty().bind(createKeyStatusBinding(keySet, database));

        ChangeListener<RSKey> selectedKeyChangeListener = (observableValue, oldItem, newItem) -> {
            if (newItem instanceof RSLoadMore) {
                keySet.findNextPage();
            } else if (newItem != null) {
                queryKey(newItem);
                lbKeyTTL.setText("TTL: " + newItem.getTtl());
            }
        };
        keySelectionModel.selectedItemProperty().removeListener(selectedKeyChangeListener);
        keySelectionModel.selectedItemProperty().addListener(selectedKeyChangeListener);
    }


    public void queryKey(RSKey key) {
        rediStartService.setSelectedKey(key);
    }


    public void openNewTabWithQueryKey(RSKey key) {

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

    public void onTerminalAction(ActionEvent actionEvent) {

    }

    public void onTTLOperatorClick(MouseEvent mouseEvent) {
        ttlOpMenu.show(ttlOpBox, Side.BOTTOM, 0, 0);
    }

    public void onAddKeyClick(MouseEvent mouseEvent) {
        addTypesMenu.show(btnAddKey, Side.BOTTOM, 0, 0);
    }
}
