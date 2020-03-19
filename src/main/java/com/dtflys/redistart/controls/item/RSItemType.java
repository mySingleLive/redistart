package com.dtflys.redistart.controls.item;

public enum RSItemType {
    REDIS_CONNECTION("redisConnIcon", "di-redis")
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
