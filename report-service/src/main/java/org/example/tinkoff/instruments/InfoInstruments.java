package org.example.tinkoff.instruments;

import org.example.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.tinkoff.piapi.core.InvestApi;

import static ru.tinkoff.piapi.core.utils.MapperUtils.quotationToBigDecimal;

public class InfoInstruments {
    static final Logger log = LoggerFactory.getLogger(Main.class);
    private static InvestApi api = null;
    public InfoInstruments(InvestApi api) {
        this.api = api;
    }

    public static void printInfoInstrument(String figi) {
        var instrument = api.getInstrumentsService().getInstrumentByFigiSync(figi);
        log.info(
                "\nинструмент figi: {},\n лотность: {},\n текущий режим торгов: {},\n тикер: {},\n валюта: {},\n цена: {},\n признак доступности торгов " +
                        "через api : {}",
                instrument.getFigi(),
                getLot(figi),
                instrument.getTradingStatus().name(),
                getTicker(figi),
                getCurency(figi),
                getPrice(figi),
                instrument.getApiTradeAvailableFlag());
    }
    public static int getLot(String figi){
        return api.getInstrumentsService().getInstrumentByFigiSync(figi).getLot();
    }
    public static String getTicker(String figi) {
        return api.getInstrumentsService().getInstrumentByFigiSync(figi).getTicker();
    }
    public static String getPrice(String figi) {
        return quotationToBigDecimal(api.getMarketDataService().getOrderBookSync(api.getInstrumentsService().getInstrumentByFigiSync(figi).getFigi(), 10).getLastPrice()).toString();
    }
    public static String getCurency(String figi) {
        return api.getInstrumentsService().getInstrumentByFigiSync(figi).getCurrency();
    }
    public static String getName(String figi) {
        return api.getInstrumentsService().getInstrumentByFigiSync(figi).getName();
    }
}
