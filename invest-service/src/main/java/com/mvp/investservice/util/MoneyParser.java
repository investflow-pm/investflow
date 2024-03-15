package com.mvp.investservice.util;

import ru.tinkoff.piapi.contract.v1.MoneyValue;

import java.math.BigDecimal;

import static ru.tinkoff.piapi.core.utils.MapperUtils.quotationToBigDecimal;

public class MoneyParser {

    public static BigDecimal moneyValueToBigDecimal(MoneyValue moneyValue) {
        if (moneyValue == null) {
            return null;
        }

        return mapUnitsAndNanos(moneyValue.getUnits(), moneyValue.getNano());
    }

    private static BigDecimal mapUnitsAndNanos(long units, int nano) {
        if (units == 0 && nano == 0) {
            return BigDecimal.ZERO;
        }

        return BigDecimal.valueOf(units).add(BigDecimal.valueOf(nano, 9));
    }
}
