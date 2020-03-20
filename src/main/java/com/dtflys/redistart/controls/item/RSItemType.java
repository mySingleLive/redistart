package com.dtflys.redistart.controls.item;

public enum RSItemType {
    REDIS_CONNECTION("redis-conn-icon", "di-redis"),
    REDIS_DATABASE("redis-db-icon", "icm-database")
    ;

    private final String iconStyleClass;
    private final String iconLiteral;

    RSItemType(String iconStyleClass, String iconLiteral) {
        this.iconLiteral = iconLiteral;
        this.iconStyleClass = iconStyleClass;
    }

    public String getIconStyleClass() {
        return iconStyleClass;
    }

    public String getIconLiteral() {
        return iconLiteral;
    }
}
