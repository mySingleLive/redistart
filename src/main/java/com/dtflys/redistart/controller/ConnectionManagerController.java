package com.dtflys.redistart.controller;

import com.dtflys.redistart.model.RedisConnection;
import com.dtflys.redistart.model.RedisConnectionConfig;
import com.dtflys.redistart.service.ConnectionService;
import com.dtflys.redistart.view.ConnectionSettingView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.util.Callback;

import javax.annotation.Resource;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.ResourceBundle;

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

        connTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

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
        connectionSettingView.showStage(Modality.WINDOW_MODAL, Map.of(
                "modify", true,
                "connectionConfig", connection.getConnectionConfig(),
                "connectionService", connectionService));

    }

    public void onDeleteConnectionClick(MouseEvent mouseEvent) {

    }

    public void onImportConnectionClick(MouseEvent mouseEvent) {

    }

    public void onExportConnectionClick(MouseEvent mouseEvent) {

    }
}
