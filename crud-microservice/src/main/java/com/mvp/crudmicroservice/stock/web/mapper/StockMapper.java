package com.mvp.crudmicroservice.stock.web.mapper;

import com.mvp.crudmicroservice.Mappable;
import com.mvp.crudmicroservice.stock.domain.Stock;
import com.mvp.crudmicroservice.stock.web.dto.StockDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StockMapper extends Mappable<Stock, StockDto> {
}
