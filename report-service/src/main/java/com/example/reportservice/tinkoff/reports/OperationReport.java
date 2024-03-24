package com.example.reportservice.tinkoff.reports;

import com.example.reportservice.model.operation.OperationResponse;

import java.io.IOException;
public class OperationReport extends Report{
    private static int i = 0;
    public OperationReport (OperationResponse operationResponse) throws IOException {
        super();
        this.setSheetname("Operations" + i++)
                .setFilename("report.xls")
                .setHeaders(new String[]{"Id", "Тип инструмента", "Наименование", "figi", "Валюта", "Тип Операции",
                        "Операция", "Количество", "Оплачено", "Цена инструмента", "Цена Лота", "Дата"})
                .setOperations(operationResponse);
        Report.createSheet();
    }
}
