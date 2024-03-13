package com.mvp.investservice.web.mapper;

import com.mvp.investservice.util.MoneyParser;
import com.mvp.investservice.web.dto.OperationResponse;
import org.springframework.stereotype.Component;
import ru.tinkoff.piapi.contract.v1.Operation;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class OperationMapper {


    public OperationResponse toDto(Operation operation, String assetName, int lot) {
        OperationResponse operationResponse = new OperationResponse();

        operationResponse.setOperationId(operation.getId());
        operationResponse.setOperationDate(LocalDateTime.ofInstant(Instant.ofEpochSecond(operation.getDate().getSeconds(), operation.getDate().getNanos()), ZoneId.systemDefault()));
        operationResponse.setOperationState(operation.getState().name());
        operationResponse.setCurrency(operation.getCurrency());
        operationResponse.setFigi(operation.getFigi());
        operationResponse.setAssetName(assetName);
        operationResponse.setInstrumentType(operation.getInstrumentType());
        operationResponse.setPayment(MoneyParser.moneyValueToBigDecimal(operation.getPayment()));
        operationResponse.setOperationType(operation.getOperationType().name());
        operationResponse.setPrice(MoneyParser.moneyValueToBigDecimal(operation.getPrice()));
        operationResponse.setLotPrice(operationResponse.getPrice().multiply(BigDecimal.valueOf(lot)));

        return operationResponse;
    }


}
