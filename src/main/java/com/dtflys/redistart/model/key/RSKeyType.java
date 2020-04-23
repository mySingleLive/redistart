package com.dtflys.redistart.model.key;

import javafx.scene.image.Image;

import java.util.List;

public enum RSKeyType {
    string("/image/list-icon-str.png"),
    list("/image/list-icon-list.png"),
    hash("/image/list-icon-hash.png"),
    set("/image/list-icon-set.png"),
    zset("/image/list-icon-zset.png");

    private final String listIcon;

    private final Image listIconImage;

    RSKeyType(String listIcon) {
        this.listIcon = listIcon;
        listIconImage = new Image(listIcon);
    }

    public Image getListIconImage() {
        return listIconImage;
    }

    public static RSKeyType findByName(String name) {
        return RSKeyType.valueOf(name.toUpperCase());
    }

    public static List<RSKeyType> toList() {
        return List.of(RSKeyType.values());
    }

}
