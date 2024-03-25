package com.mvp.investservice.web.dto.stock;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class DividendResponse {
    private String figi;
    List<DividendDto> dividends;
}
