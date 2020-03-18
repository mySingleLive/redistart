package com.dtflys.redistart.controller;

import com.jfoenix.controls.JFXTreeView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseEvent;
import org.controlsfx.control.textfield.CustomTextField;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@FXMLController
public class NavigationController implements Initializable {

    @FXML
    private CustomTextField txSearchField;

    @FXML
    private JFXTreeView treeView;

    private TreeItem<String> rootItem;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rootItem = new TreeItem<>("root");

        TreeItem<String> test1 = new TreeItem<>("Test 1");
        test1.getChildren().addAll(
                new TreeItem<>("isFirstOrder:CHN2078:M1002100095"),
                new TreeItem<>("POINT_EXCHANGE:SHOP_CART:2061193"),
                new TreeItem<>("POINT_EXCHANGE:SHOP_CART:PACK_ID"));
        TreeItem<String> test2 = new TreeItem<>("Test 2");
        rootItem.getChildren().addAll(test1, test2);

        treeView.setRoot(rootItem);
    }

    public void onSearchCancel(MouseEvent mouseEvent) {
        txSearchField.setText("");
    }
}
