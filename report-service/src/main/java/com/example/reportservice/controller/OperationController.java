package com.example.reportservice.controller;

import com.example.reportservice.model.dto.AccountDto;
import com.example.reportservice.model.operation.OperationResponse;
import com.example.reportservice.tinkoff.reports.OperationReport;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/report/operation")
@AllArgsConstructor
public class OperationController {
    private final RestTemplate restTemplate;

    @PostMapping
    public OperationResponse getOperations(@RequestBody AccountDto accountDto) throws IOException {
        OperationResponse response = restTemplate
                .postForObject("http://localhost:9098/api/v1/invest/operations", accountDto,OperationResponse.class);
        operations(response);
        return response;
    }
    public void operations(OperationResponse operationResponse) throws IOException {
        OperationReport operationReport = new OperationReport(operationResponse);
    }
}
