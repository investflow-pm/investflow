package com.mvp.investservice.web.dto;

import lombok.Data;

import java.util.List;

@Data
public class OperationResponse {

    private List<Operation> operations;

}
