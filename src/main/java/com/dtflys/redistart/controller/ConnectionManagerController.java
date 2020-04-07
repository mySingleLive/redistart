package com.dtflys.redistart.controller;

import com.dtflys.redistart.model.RedisConnection;
import com.dtflys.redistart.model.RedisConnectionConfig;
import com.dtflys.redistart.service.ConnectionService;
import com.dtflys.redistart.utils.ConfirmResult;
import com.dtflys.redistart.utils.DialogUtils;
import com.dtflys.redistart.view.ConnectionSettingView;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;
import de.felixroske.jfxsupport.FXMLController;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.util.Callback;

import javax.annotation.Resource;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Consumer;

@FXMLController
public class ConnectionManagerController implements Initializable {

    @FXML
    private HBox btbAddNewConnection;

    @FXML
    private HBox btnEditConnection;

    @FXML
    private HBox btnDeleteConnection;

    @FXML
    private HBox btnImportConnection;

    @FXML
    private HBox btnExportConnection;

    @FXML
    private TableView<RedisConnection> connTableView;

    @FXML
    private TableColumn<RedisConnection, String> colConnName;

    @FXML
    private TableColumn<RedisConnection, String> colConnAddress;

    @FXML
    private TableColumn<RedisConnection, String> colConnSSH;

    @FXML
    private TableColumn<RedisConnection, Date> colConnCreateTime;

    @Resource
    private ConnectionService connectionService;

    @Resource
    private ConnectionSettingView connectionSettingView;


    private final static Callback<TableColumn<RedisConnection, Date>, TableCell<RedisConnection, Date>> dateColumnCallback = (column) -> {
        TableCell<RedisConnection, Date> cell = new TableCell<>() {
            private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            @Override
            protected void updateItem(Date item, boolean empty) {
                super.updateItem(item, empty);
                if(empty) {
                    setText(null);
                }
                else {
                    if(item != null)
                        this.setText(format.format(item));
                }
            }
        };
        return cell;
    };



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connTableView.setPlaceholder(new Label("没有连接"));
        TableView.TableViewSelectionModel selectionModel =
                connTableView.getSelectionModel();
        ReadOnlyObjectProperty selectItemProperty = selectionModel.selectedItemProperty();
        btnEditConnection.disableProperty().bind(selectItemProperty.isNull());
        btnDeleteConnection.disableProperty().bind(selectItemProperty.isNull());
        btnExportConnection.disableProperty().bind(selectItemProperty.isNull());

        colConnName.setCellValueFactory(cellData -> cellData.getValue().getConnectionConfig().nameProperty());
        colConnAddress.setCellValueFactory(cellData -> Bindings.concat(
                cellData.getValue().getConnectionConfig().redisHostProperty(),
                ":",
                cellData.getValue().getConnectionConfig().redisPortProperty()
        ));
        colConnSSH.setCellValueFactory(cellData ->
                Bindings.createStringBinding(() ->
                        cellData.getValue().getConnectionConfig().isUseSSHProperty().get() ? "使用SSH" : "无SSH",
                        cellData.getValue().getConnectionConfig().isUseSSHProperty()));

        colConnCreateTime.setCellValueFactory(cellData ->
                cellData.getValue().getConnectionConfig().createTimeProperty());
        colConnCreateTime.setCellFactory(dateColumnCallback);

//        connTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        DialogController dialogControllers[] = new DialogController[1];
        Region leftRegion = new Region();
        leftRegion.setPrefWidth(35);
        JFXSpinner spinner = new JFXSpinner();
        spinner.setPrefWidth(35);
        spinner.setPrefHeight(35);
        Label label = new Label();
        HBox.setMargin(label, new Insets(0, 0, 0, 25));

        connectionService.setOnAfterAddConnection(connection -> {
            connection.getOnOpenConnectionFailed().addHandler(handler -> {
                Platform.runLater(() -> {
                    spinner.setOpacity(0);
                    label.setText("连接失败");
                });
            });

            connection.getOnAfterOpenConnection().addHandler(handler -> {
                Platform.runLater(() -> {
                    synchronized (dialogControllers) {
                        if (dialogControllers[0] != null) {
                            dialogControllers[0].close();
                        }
                    }
                });
            });
        });

        connTableView.setRowFactory(tbl -> {
            TableRow<RedisConnection> row = new TableRow<>();
            row.setOnMouseClicked(eventHandler -> {
                if (eventHandler.getClickCount() == 2 && !row.isEmpty()) {
                    RedisConnection connection = row.getItem();
                    System.out.println("-- " + connection.getConnectionConfig().getName());
                    DialogUtils.showModalDialog(Map.of(
                            "content", "",
                            "width", 260,
                            "height", 130,
                            "showOkButton", false,
                            "showCancelButton", false,
                            "onInit", (Consumer<DialogController>) dController -> {
                                HBox contentBox = dController.getContentBox();
                                contentBox.getChildren().clear();
                                contentBox.setAlignment(Pos.CENTER_LEFT);
                                spinner.setOpacity(1);
                                label.setText("正在连接...");
                                contentBox.getChildren().addAll(leftRegion, spinner, label);
                                synchronized (dialogControllers) {
                                    dialogControllers[0] = dController;
                                }
                                connection.openConnection();
                            }
                    ));
                }
            });
            return row;
        });

/*
        RedisConnectionConfig connectionConfig = new RedisConnectionConfig();
        connectionConfig.setName("Test 1");
        connectionConfig.setRedisHost("127.0.0.1");
        connectionConfig.setRedisPort(6379);
        connectionConfig.setRedisPassword("xxxxxx");
        connectionConfig.setCreateTime(new Date());

        RedisConnectionConfig connectionConfig2 = new RedisConnectionConfig();
        connectionConfig2.setName("Test 2");
        connectionConfig2.setRedisHost("127.0.0.1");
        connectionConfig2.setRedisPassword("yyyyyy");
        connectionConfig2.setRedisPort(6389);
        connectionConfig2.setCreateTime(new Date());

        connectionService.addConnection(connectionConfig);
        connectionService.addConnection(connectionConfig2);
*/
        connectionService.loadConnections();

        connTableView.setItems(connectionService.getConnections());

    }

    private void onAfterAddNewConnection(RedisConnection connection) {

    }

    private void refreshConnectionTableData() {

    }

    public void onAddNewConnectionClick(MouseEvent mouseEvent) {
        connectionSettingView.showBorderlessStage(Modality.WINDOW_MODAL, Map.of(
                "modify", false,
                "connectionService", connectionService));
    }

    public void onEditConnectionClick(MouseEvent mouseEvent) {
        RedisConnection connection = connTableView.getSelectionModel().getSelectedItem();
        connectionSettingView.showBorderlessStage(Modality.WINDOW_MODAL, Map.of(
                "modify", true,
                "connectionConfig", connection.getConnectionConfig()));

    }

    public void onDeleteConnectionClick(MouseEvent mouseEvent) {
        DialogUtils.showModalDialog(Map.of(
                "title", "Redistart",
                "content", "是否删除?",
                "onConfirm", (Consumer<ConfirmResult>) result -> {
                    if (ConfirmResult.OK == result) {
                        RedisConnection connection = connTableView.getSelectionModel().getSelectedItem();
                        connectionService.deleteConnection(connection);
                    }
                }));
    }

    public void onImportConnectionClick(MouseEvent mouseEvent) {

    }

    public void onExportConnectionClick(MouseEvent mouseEvent) {

    }
}
