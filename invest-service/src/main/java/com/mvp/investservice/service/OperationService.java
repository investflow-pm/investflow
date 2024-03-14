package com.mvp.investservice.service;

import com.mvp.investservice.web.dto.AccountDto;
import com.mvp.investservice.web.dto.OperationResponse;

import java.util.List;

public interface OperationService {

    List<OperationResponse> getAllOperations(AccountDto accountDto);
}
