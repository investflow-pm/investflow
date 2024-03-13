package com.mvp.investservice.service.impl;

import com.mvp.investservice.service.OperationService;
import com.mvp.investservice.web.dto.AccountDto;
import com.mvp.investservice.web.dto.OperationResponse;
import com.mvp.investservice.web.mapper.OperationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.contract.v1.AssetFull;
import ru.tinkoff.piapi.contract.v1.Instrument;
import ru.tinkoff.piapi.contract.v1.Operation;
import ru.tinkoff.piapi.core.InvestApi;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class OperationServiceImpl implements OperationService {

    private final InvestApi investApi;

    private final OperationMapper operationMapper;

    @Override
    public List<OperationResponse> getAllOperations(AccountDto accountDto) {
        List<Operation> allOperations = investApi.getOperationsService()
                .getAllOperationsSync(accountDto.getInvestAccountId(), Instant.now().minus(1, ChronoUnit.DAYS), Instant.now());

        List<OperationResponse> operations = new ArrayList<>();
        for (Operation operation : allOperations) {
            Instrument asset = investApi.getInstrumentsService()
                    .getInstrumentByFigiSync(operation.getFigi());
            String assetName = asset.getName();
            int lot = asset.getLot();
            operations.add(operationMapper.toDto(operation, assetName, lot));
        }

        return operations;
    }
}
