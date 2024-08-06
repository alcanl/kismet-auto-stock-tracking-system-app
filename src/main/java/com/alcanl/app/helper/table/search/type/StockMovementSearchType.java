package com.alcanl.app.helper.table.search.type;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Accessors(prefix = "ms_")
public enum StockMovementSearchType {
    PRODUCT, USER, DATE, PRODUCT_AND_DATE, PRODUCT_AND_USER, USER_AND_DATE, ALL, NONE;

    @Setter
    @Getter
    private static String ms_userName;

    @Setter
    @Getter
    private static String ms_productId;

    @Setter
    @Getter
    private static LocalDate ms_startDate;

    @Setter
    @Getter
    private static LocalDate ms_endDate;

}
