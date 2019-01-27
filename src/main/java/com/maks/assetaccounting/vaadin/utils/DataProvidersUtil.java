package com.maks.assetaccounting.vaadin.utils;

import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.QuerySortOrderBuilder;

import java.util.List;

public class DataProvidersUtil {

    private DataProvidersUtil() {
    }

    public static final List<QuerySortOrder> DEFAULT_SORT_ORDERS = new QuerySortOrderBuilder().thenAsc("id").build();
}
