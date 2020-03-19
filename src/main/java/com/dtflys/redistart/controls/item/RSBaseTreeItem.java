package com.dtflys.redistart.controls.item;

import javafx.scene.control.TreeItem;
import org.kordamp.ikonli.javafx.FontIcon;

public abstract class RSBaseTreeItem extends TreeItem<String> {
    private final RSItemType itemType;
    protected FontIcon icon;

    public RSBaseTreeItem(String s, RSItemType itemType) {
        super(s);
        this.itemType = itemType;
        icon = new FontIcon();
        icon.setIconLiteral(itemType.getIconLiteral());
        icon.getStyleClass().add(itemType.getIconStyleClass());
        setGraphic(icon);
    }

    public RSItemType getItemType() {
        return this.itemType;
    }

}
