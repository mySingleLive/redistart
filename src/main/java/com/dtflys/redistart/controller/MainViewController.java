package com.dtflys.redistart.controller;

import com.dtflys.redistart.controls.RSColors;
import com.dtflys.redistart.controls.RSMovableListener;
import com.dtflys.redistart.controls.menu.RSConnectionMenuItem;
import com.dtflys.redistart.model.connection.RedisConnection;
import com.dtflys.redistart.model.database.RedisDatabase;
import com.dtflys.redistart.model.page.RSConnectionManagerPage;
import com.dtflys.redistart.model.page.RSContentPage;
import com.dtflys.redistart.model.page.RSKeysContentPage;
import com.dtflys.redistart.service.ConnectionService;
import com.dtflys.redistart.service.RediStartService;
import com.dtflys.redistart.utils.ResizeUtils;
import com.dtflys.redistart.view.ConnectionSettingView;
import com.dtflys.redistart.view.KeysContentView;
import de.felixroske.jfxsupport.FXMLController;
import de.felixroske.jfxsupport.GUIState;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

import javax.annotation.Resource;
import java.net.URL;
import java.util.*;

@FXMLController
public class MainViewController extends RSMovableListener implements Initializable {

    private final static String LEFT_SIDE_BAR_BUTTON_SELECTED_STYLE_CLASS = "left-side-tab-selected";

    @Resource
    private RediStartService rediStartService;

    @Resource
    private ConnectionService connectionService;

    @FXML
    private VBox appTitleBarBox;

    @FXML
    private Label lbAppTitle;

    @FXML
    private HBox sideBtnConnection;

    @FXML
    private HBox sideBtnKeys;

    @FXML
    private HBox sideBtnDashboard;

    @FXML
    private HBox sideBtnAnalyse;

    @FXML
    private Label lbSelectedConn;

    @FXML
    private FontIcon selectedConnIcon;

    @FXML
    private HBox selectedConnBox;

    @FXML
    private Label lbSelectedDB;

    @FXML
    private HBox selectedDBBox;

    @FXML
    private HBox titleBox;

    private ObservableList<HBox> sideButtonsGroup = FXCollections.observableList(new LinkedList<>());

    @FXML
    private BorderPane mainPane;

    @FXML
    private MenuBar menuBar;

    @FXML
    private VBox connManagerBox;

    @FXML
    private StackPane stackPane;

    @FXML
    private HBox appMaximizeBox;

    @FXML
    private ImageView imgvMaximize;

    private ContextMenu connectionContextMenu = new ContextMenu();

    private ContextMenu databaseContextMenu = new ContextMenu();

    private Image maximizeImage;

    private Image maximizeRestoreImage;

    @Resource
    private ConnectionSettingView connectionSettingView;

    @Resource
    private KeysContentView keysContentView;

    private final RSConnectionManagerPage connectionManagerPage = new RSConnectionManagerPage();;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ResizeUtils.addResizeListener(GUIState.getStage(), mainPane);
        super.movable(appTitleBarBox);
        maximizeImage = imgvMaximize.getImage();
        maximizeRestoreImage = new Image( "/image/icons_max_restore_32px.png");

        sideButtonsGroup.addAll(sideBtnConnection, sideBtnKeys, sideBtnDashboard, sideBtnAnalyse);

        // Select current connection
        connectionService.selectedConnectionProperty().addListener((observableValue, connection, newConnection) -> {
            Platform.runLater(() -> {
                if (newConnection == null) {
                    lbSelectedConn.setText("没有打开的连接");
                }
                RSContentPage page = rediStartService.getSelectedPage();
                if (page instanceof RSKeysContentPage) {
                    selectKeysContentPage(newConnection);
                }
                lbSelectedConn.setText(newConnection.getConnectionConfig().getName());
                selectedConnIcon.iconColorProperty().unbind();
                selectedConnIcon.iconColorProperty().bind(
                        RSColors.connectionStatusColorBinding(connectionService.getSelectedConnection()));

                List<RedisDatabase> databaseList = newConnection.getDatabaseList();
                databaseContextMenu.getItems().clear();
                for (RedisDatabase database : databaseList) {
                    MenuItem item = new MenuItem();
                    item.setText(database.getDisplayName());
                    FontIcon icon = new FontIcon();
                    icon.setIconLiteral("mdi-database");
                    icon.setIconColor(Color.valueOf("#d0c0c0"));
                    icon.setIconSize(14);
                    item.setGraphic(icon);
                    item.setOnAction(event -> {
                        newConnection.setSelectedDatabase(database);
                    });
                    databaseContextMenu.getItems().add(item);
                }

                lbSelectedDB.textProperty().unbind();
                lbSelectedDB.textProperty().bind(Bindings.createStringBinding(
                        () -> newConnection.getSelectedDatabase().getDisplayName(),
                        newConnection.selectedDatabaseProperty()));
            });
        });

        selectedConnIcon.iconColorProperty().bind(
                RSColors.connectionStatusColorBinding(connectionService.getSelectedConnection()));

        connectionService.setOnAfterOpenConnection(connection -> {
            Platform.runLater(() -> {
                selectKeysContentPage(connection);
            });
        });


        // 监听切换主内容页面事件
        // 设置侧边栏按钮选中时的颜色
        rediStartService.selectedPageProperty().addListener((observableValue, oldPage, newPage) -> {
            if (newPage == null) {
                return;
            }
            clearSideBarButtonsStyle();
            HBox selectedSideButton = null;
            switch (newPage.getPageType()) {
                case RSContentPage.CONNECTION_MANAGER_PAGE:
                    selectedSideButton = sideBtnConnection;
                    break;
                case RSContentPage.KEYS_CONTENT_PAGE:
                    selectedSideButton = sideBtnKeys;
                    break;
                case RSContentPage.DASHBOARD_PAGE:
                    selectedSideButton = sideBtnDashboard;
                    break;
                case RSContentPage.ANALYSE_PAGE:
                    selectedSideButton = sideBtnAnalyse;
                    break;
            }
            if (selectedSideButton != null) {
                selectedSideButton.getStyleClass().add(LEFT_SIDE_BAR_BUTTON_SELECTED_STYLE_CLASS);
            }

            titleBox.getChildren().clear();
            if (newPage.isShowSelectionItem()) {
                titleBox.getChildren().addAll(selectedConnBox, selectedDBBox);
            }
            titleBox.getChildren().add(lbAppTitle);
        });

        // 初始化连接管理页面
        // 添加当选择连接管理页面时的事件
        connectionManagerPage.setOnSelect(page -> {
            Platform.runLater(() -> {
                connManagerBox.toFront();
            });
        });

        // 设置当前主内容页面为链接管理页面
        rediStartService.setSelectedPage(connectionManagerPage);


        // 当有新打开的连接时，更新侧边数据键按钮上的侧边菜单
        connectionService.getOpenedConnections().addListener((ListChangeListener<RedisConnection>) change -> {
            Platform.runLater(() -> {
                while (change.next()) {
                    if (change.wasAdded() || change.wasRemoved() || change.wasReplaced() || change.wasUpdated()) {
                        connectionContextMenu.getItems().clear();
                        ObservableList<RedisConnection> connections = connectionService.getOpenedConnections();
                        for (RedisConnection connection : connections) {
                            RSConnectionMenuItem item = new RSConnectionMenuItem(connection);
                            item.setOnAction(event -> {
                                connectionService.setSelectedConnection(connection);
                            });
                            connectionContextMenu.getItems().add(item);
                        }
                    }
                }
            });
        });

        selectedConnBox.setOnMouseClicked(event -> {
            if (!connectionContextMenu.isShowing()) {
                connectionContextMenu.show(selectedConnBox, Side.BOTTOM, 0, 0);
            }
        });

        selectedDBBox.setOnMouseClicked(event -> {
            if (!databaseContextMenu.isShowing()) {
                databaseContextMenu.show(selectedDBBox, Side.BOTTOM, 0, 0);
            }
        });


        // 监听鼠标移出侧边数据键按钮上时的事件


    }

    private void clearSideBarButtonsStyle() {
        for (HBox sideBtn : sideButtonsGroup) {
            sideBtn.getStyleClass().remove(LEFT_SIDE_BAR_BUTTON_SELECTED_STYLE_CLASS);
        }
    }


    public void onAddConnectionItemAction(ActionEvent actionEvent) {
        connectionSettingView.showBorderlessStage(Modality.WINDOW_MODAL, Map.of(
                "modify", false,
                "connectionService", connectionService));
    }


    private void toggleMaximizeWindow() {
        Stage stage = getStage();
        if (stage.isMaximized()) {
            getStage().setMaximized(false);
            imgvMaximize.setImage(maximizeImage);
        } else {
            getStage().setMaximized(true);
            imgvMaximize.setImage(maximizeRestoreImage);
        }
    }

    public RSKeysContentPage createKeysContentPage(RedisConnection connection) {
        System.out.println("打开连接: " + connection.getConnectionConfig().getName());
        RSKeysContentPage keysContentPage = new RSKeysContentPage(connection);
        Parent root = keysContentView.loadAsParent(Map.of());
        stackPane.getChildren().add(root);
        keysContentPage.setOnSelect(page -> {
            Platform.runLater(() -> {
                root.toFront();
            });
        });
        return keysContentPage;
    }

    public void onAppCloseClicked(MouseEvent mouseEvent) {
        Platform.exit();
    }

    public void onAppMaximizeClicked(MouseEvent mouseEvent) {
        toggleMaximizeWindow();
    }

    public void onAppMinimizeClicked(MouseEvent mouseEvent) {
        getStage().setIconified(true);
    }

    public void onTitleBarClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            toggleMaximizeWindow();
        }
    }

    public void onSelectConnectionTab(MouseEvent mouseEvent) {
        rediStartService.setSelectedPage(connectionManagerPage);
    }

    private void selectKeysContentPage(RedisConnection connection) {
        if (connection != null) {
            RSKeysContentPage page = rediStartService.getKeysContentPageMap().get(connection);
            if (page == null) {
                page = createKeysContentPage(connection);
            }
            connectionService.setSelectedConnection(connection);
            rediStartService.setSelectedPage(page);
        }
    }

    public void onSelectKeysTab(MouseEvent mouseEvent) {
        RedisConnection connection = connectionService.getSelectedConnection();
        selectKeysContentPage(connection);
    }

    public void onSelectCollectionTab(MouseEvent mouseEvent) {

    }

    public void onSelectSettingsTab(MouseEvent mouseEvent) {

    }
}
