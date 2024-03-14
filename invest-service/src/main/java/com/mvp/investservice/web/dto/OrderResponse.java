package com.mvp.investservice.web.dto;

import lombok.Data;

@Data
public class OrderResponse<A> {

    private String orderId;

    private A asset;

    private String price;
}
