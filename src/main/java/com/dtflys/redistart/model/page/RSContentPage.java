package com.dtflys.redistart.model.page;

import java.util.function.Consumer;

public abstract class RSContentPage<T> {

    public static final int CONNECTION_MANAGER_PAGE = 0;

    public static final int KEYS_CONTENT_PAGE = 1;

    public static final int DASHBOARD_PAGE = 2;

    public static final int ANALYSE_PAGE = 3;

    private final int pageType;

    private final boolean useConnectionSelection;

    private final boolean useDatabaseSelection;

    private Consumer<RSContentPage<T>> onInit;

    private Consumer<RSContentPage<T>> onSelect;

    public RSContentPage(int pageType, boolean useConnectionSelection, boolean useDatabaseSelection) {
        this.pageType = pageType;
        this.useConnectionSelection = useConnectionSelection;
        this.useDatabaseSelection = useDatabaseSelection;
    }

    public boolean isUseConnectionSelection() {
        return useConnectionSelection;
    }

    public boolean isUseDatabaseSelection() {
        return useDatabaseSelection;
    }

    public int getPageType() {
        return pageType;
    }

    public Consumer<RSContentPage<T>> getOnInit() {
        return onInit;
    }

    public void setOnInit(Consumer<RSContentPage<T>> onInit) {
        this.onInit = onInit;
    }

    public Consumer<RSContentPage<T>> getOnSelect() {
        return onSelect;
    }

    public void setOnSelect(Consumer<RSContentPage<T>> onSelect) {
        this.onSelect = onSelect;
    }
}
