package org.example.reports;

import ru.tinkoff.piapi.core.InvestApi;

import java.io.IOException;
import java.util.ArrayList;

public class CurrenciesReport extends Report{
    public CurrenciesReport (ArrayList<String> positions, InvestApi api) throws IOException {
        super();
        this.setSheetname("Currencies")
                .setFilename("report.xls")
                .setHeaders(new String[]{"тикер", "isin", "figi", "наименование", "валюта", "лот", "шаг цены"})
                .setDataFigi(positions, api);
        Report.createSheet();
    }
}
