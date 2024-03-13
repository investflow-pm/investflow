package org.example.tinkoff.accountInfo;

import org.example.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.tinkoff.piapi.core.InvestApi;
import ru.tinkoff.piapi.core.models.FuturePosition;
import ru.tinkoff.piapi.core.models.Money;
import ru.tinkoff.piapi.core.models.Positions;
import ru.tinkoff.piapi.core.models.SecurityPosition;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import static ru.tinkoff.piapi.core.utils.DateUtils.timestampToString;
import static ru.tinkoff.piapi.core.utils.MapperUtils.moneyValueToBigDecimal;

public class Account {
    static final Logger log = LoggerFactory.getLogger(Main.class);
    public static void getPositions(InvestApi api) {
        var accounts = api.getUserService().getAccountsSync();
        var mainAccount = accounts.get(0).getId();
        //Получаем и печатаем список позиций
        var positions = api.getOperationsService().getPositionsSync(mainAccount);
        getMoneyPositions(api);

        log.info("список заблокированных валютных позиций портфеля");
        var blockedList = positions.getBlocked();
        for (Money moneyValue : blockedList) {
            log.info("валюта: {}, количество: {}", moneyValue.getCurrency(), moneyValue);
        }
        getFuturesPosition(api);
    }
    public static ArrayList<String> getStocksPositions(Positions positions) {

        ArrayList<String> positionsList = new ArrayList<>();

       // log.info("список ценно-бумажных позиций портфеля");
        var securities = positions.getSecurities();

        for (SecurityPosition security : securities) {
            positionsList.add(security.getFigi());
        }
        return positionsList;
    }
    public static ArrayList<String> getMoneyPositions(InvestApi api) {
        var accounts = api.getUserService().getAccountsSync();
        var mainAccount = accounts.get(0).getId();
        //Получаем и печатаем список позиций
        var positions = api.getOperationsService().getPositionsSync(mainAccount);

        ArrayList<String> positionsList = new ArrayList<>();

//        log.info("список валютных позиций портфеля");
        var moneyList = positions.getMoney();
        for (Money moneyValue : moneyList) {
            positionsList.add(moneyValue.getCurrency());
        }
        return positionsList;
    }
    public static ArrayList<String> getFuturesPosition(InvestApi api) {
        int i = 0;
        var accounts = api.getUserService().getAccountsSync();
        var mainAccount = accounts.get(0).getId();
        //Получаем и печатаем список позиций
        var positions = api.getOperationsService().getPositionsSync(mainAccount);
        ArrayList<String> positionsList = new ArrayList<>();
//        log.info("список фьючерсов портфеля");
        var futuresList = positions.getFutures();
        for (FuturePosition security : futuresList) {
            positionsList.add(security.getFigi());
        }
        return positionsList;
    }

    public static void getOperations(InvestApi api) {
        var accounts = api.getUserService().getAccountsSync();
        var mainAccount = accounts.get(0).getId();
        //Получаем и печатаем список операций клиента
        var operations = api.getOperationsService()
                .getAllOperationsSync(mainAccount, Instant.now().minus(30, ChronoUnit.DAYS), Instant.now());
        for (int i = 0; i < Math.min(operations.size(), 5); i++) {
            var operation = operations.get(i);
            var date = timestampToString(operation.getDate());
            var state = operation.getState().name();
            var id = operation.getId();
            var payment = moneyValueToBigDecimal(operation.getPayment());
            var figi = operation.getFigi();
            log.info("операция с id: {}, дата: {}, статус: {}, платеж: {}, figi: {}", id, date, state, payment, api.getInstrumentsService().getInstrumentByFigiSync(figi).getName());
        }
    }

    private static void getOperationsByCursor(InvestApi api) {
        var accounts = api.getUserService().getAccountsSync();
        var mainAccount = accounts.get(0).getId();

        //Получаем и печатаем список операций клиента
        var operations = api.getOperationsService()
                .getOperationByCursorSync(mainAccount, Instant.now().minus(30, ChronoUnit.DAYS), Instant.now())
                .getItemsList();
        for (int i = 0; i < Math.min(operations.size(), 5); i++) {
            var operation = operations.get(i);
            var date = timestampToString(operation.getDate());
            var state = operation.getState().name();
            var id = operation.getId();
            var payment = moneyValueToBigDecimal(operation.getPayment());
            var figi = operation.getFigi();
            log.info("операция с id: {}, дата: {}, статус: {}, платеж: {}, figi: {}", id, date, state, payment, figi);
        }
    }
}
