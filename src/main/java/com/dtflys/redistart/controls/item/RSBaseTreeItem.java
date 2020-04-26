package com.dtflys.redistart.controls.item;

import com.jfoenix.controls.JFXSpinner;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import org.kordamp.ikonli.javafx.FontIcon;

public abstract class RSBaseTreeItem extends TreeItem<String> {
    private final RSItemType itemType;

    private HBox leftBox;
    private FontIcon icon;
    private JFXSpinner spinner;

    public abstract void doAction(TreeView treeView);

    public RSBaseTreeItem(String s, RSItemType itemType) {
        super(s);
        this.itemType = itemType;
        leftBox = new HBox();
        icon = new FontIcon();
        icon.setIconLiteral(itemType.getIconLiteral());
        icon.getStyleClass().add(itemType.getIconStyleClass());
        leftBox.getChildren().add(icon);
        spinner = new JFXSpinner();
        setGraphic(leftBox);
    }

    public void refresh(TreeView treeView) {
        switch (getItemStatus()) {
            case CLOSED:
                setExpanded(false);
                break;
            case WAITING:
                getLeftBox().getChildren().setAll(spinner);
                break;
            case EXPANDED:
                setExpanded(true);
                getLeftBox().getChildren().setAll(icon);
                break;
        }
        treeView.refresh();
    }


    public RSItemStatus getItemStatus() {
        if (this.isExpanded()) {
            return RSItemStatus.EXPANDED;
        }
        return RSItemStatus.CLOSED;
    }

    public JFXSpinner getSpinner() {
        return spinner;
    }

    public FontIcon getIcon() {
        return icon;
    }

    public RSItemType getItemType() {
        return this.itemType;
    }

    public HBox getLeftBox() {
        return leftBox;
    }
}
