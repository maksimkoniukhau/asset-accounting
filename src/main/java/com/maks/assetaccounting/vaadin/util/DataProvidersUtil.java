package com.maks.assetaccounting.vaadin.util;

import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.SortDirection;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;

public class DataProvidersUtil {

    private DataProvidersUtil() {
    }

    public static <T, F> List<Sort.Order> getListSortOrders(final Query<T, F> query) {
        return query.getSortOrders().stream()
                .map(sortOrder -> new Sort.Order(sortOrder.getDirection() == SortDirection.ASCENDING ?
                        Sort.Direction.ASC : Sort.Direction.DESC,
                        sortOrder.getSorted()))
                .collect(Collectors.toList());
    }
}
