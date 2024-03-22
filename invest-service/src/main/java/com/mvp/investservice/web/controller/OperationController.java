package com.mvp.investservice.web.controller;

import com.mvp.investservice.service.OperationService;
import com.mvp.investservice.web.dto.AccountDto;
import com.mvp.investservice.web.dto.Operation;
import com.mvp.investservice.web.dto.OperationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/invest/operations")
@RequiredArgsConstructor
public class OperationController {

    private final OperationService operationService;

    @PostMapping
    public OperationResponse getAllOperations(@RequestBody AccountDto accountDto) {
        return operationService.getAllOperations(accountDto);
    }
}
