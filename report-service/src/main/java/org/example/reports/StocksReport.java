package org.example.reports;

import ru.tinkoff.piapi.core.InvestApi;

import java.io.IOException;
import java.util.ArrayList;

public class StocksReport extends Report{
    public StocksReport(ArrayList<String> positions, InvestApi api) throws IOException {
        super();
        this.setSheetname("Stocks")
                .setFilename("report.xls")
                .setHeaders(new String[]{"figi", "наименование", "тикер", "валюта", "лот", "шаг цены"})
                .setDataFigi(positions, api);
        Report.createSheet();
    }
}
