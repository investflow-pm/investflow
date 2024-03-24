package com.example.reportservice.model.operation;

import lombok.Data;

import java.util.List;

@Data
public class OperationResponse {
    private List<Operation> operations;
}
