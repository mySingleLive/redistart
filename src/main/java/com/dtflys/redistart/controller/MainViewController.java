package com.dtflys.redistart.controller;

import com.dtflys.redistart.controls.RSColors;
import com.dtflys.redistart.controls.RSMovableListener;
import com.dtflys.redistart.controls.RSConnectionMenuItem;
import com.dtflys.redistart.model.RedisConnection;
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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
    private HBox sideBtnCollection;

    @FXML
    private Label lbSelectedConn;

    @FXML
    private FontIcon selectedConnIcon;

    @FXML
    private HBox selectedConnBox;

    @FXML
    private HBox titleBox;

//    @FXML
//    private MenuButton mbConnections;

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

    private Image maximizeImage;

    private Image maximizeRestoreImage;

    @Resource
    private ConnectionSettingView connectionSettingView;

    @Resource
    private KeysContentView keysContentView;

    private final RSConnectionManagerPage connectionManagerPage = new RSConnectionManagerPage();;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.movable(appTitleBarBox);
        ResizeUtils.addResizeListener(GUIState.getStage(), mainPane);
        maximizeImage = imgvMaximize.getImage();
        maximizeRestoreImage = new Image( "/image/icons_max_restore_32px.png");

        sideButtonsGroup.addAll(sideBtnConnection, sideBtnKeys, sideBtnCollection);


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
            });
        });

        selectedConnIcon.iconColorProperty().bind(
                RSColors.connectionStatusColorBinding(connectionService.getSelectedConnection()));

        connectionService.setOnAfterOpenConnection(connection -> {
            Platform.runLater(() -> {
                selectKeysContentPage(connection);
            });
        });

//        connectionService.selectedConnectionProperty().addListener((observableValue, connection, newConnection) -> {
//            cbConnections.getSelectionModel().select(newConnection);
//        });

/*
        mbConnections.opacityProperty().bind(Bindings.createDoubleBinding(() -> {
            if (connectionService.getOpenedConnections().size() == 0) {
                return 0.0D;
            }
            return 1.0D;
        }, connectionService.getOpenedConnections()));

        mbConnections.disableProperty().bind(Bindings.createBooleanBinding(() -> {
            if (connectionService.getOpenedConnections().size() == 0) {
                return true;
            }
            return false;
        }, connectionService.getOpenedConnections()));
*/

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
                case RSContentPage.COLLECTION_PAGE:
                    selectedSideButton = sideBtnCollection;
                    break;
            }
            if (selectedSideButton != null) {
                selectedSideButton.getStyleClass().add(LEFT_SIDE_BAR_BUTTON_SELECTED_STYLE_CLASS);
            }

            titleBox.getChildren().clear();
            if (newPage.isShowSelectionItem()) {
                titleBox.getChildren().add(selectedConnBox);
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

        // 监听鼠标移入侧边数据键按钮上时的事件
        selectedConnBox.setOnMouseClicked(event -> {
            if (!connectionContextMenu.isShowing()) {
                connectionContextMenu.show(selectedConnBox, Side.BOTTOM, 0, 2);
            }
        });

/*
        sideBtnConnection.setOnMouseEntered(event -> {
            if (connectionContextMenu.isShowing()) {
                connectionContextMenu.hide();
            }
        });

        sideBtnCollection.setOnMouseEntered(event -> {
            if (connectionContextMenu.isShowing()) {
                connectionContextMenu.hide();
            }
        });

        stackPane.setOnMouseEntered(event -> {
            if (connectionContextMenu.isShowing()) {
                connectionContextMenu.hide();
            }
        });
*/



        // 监听鼠标移出侧边数据键按钮上时的事件
/*
        sideBtnKeys.setOnMouseExited(event -> {
            if (connectionContextMenu.isShowing()) {
                connectionContextMenu.hide();
            }
        });
*/


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
