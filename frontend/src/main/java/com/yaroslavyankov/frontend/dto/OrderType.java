package com.yaroslavyankov.frontend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.aspectj.weaver.ast.Or;

import java.util.ArrayList;
import java.util.List;

@Schema(description = "Сущность типа заявки")
public enum OrderType {
    ORDER_TYPE_LIMIT("Лимитная"),
    ORDER_TYPE_MARKET("Рыночная"),
    ORDER_TYPE_BESTPRICE("Лучшая цена");

    private final String russianName;

    OrderType(String russianName) {
        this.russianName = russianName;
    }

    public String getRussianValue() {
        return russianName;
    }

    public static List<String> getRussianValues() {
        var list = new ArrayList<String>();

        for (var i : OrderType.values()) {
            list.add(i.russianName);
        }

        return list;
    }

    public static OrderType getValue(String russianName) {
        for (var i : OrderType.values()) {
            if (i.russianName.equalsIgnoreCase(russianName)) {
                return i;
            }
        }

        return ORDER_TYPE_LIMIT;
    }
}
