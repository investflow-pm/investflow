package org.example.tinkoff.instruments;

import org.example.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.tinkoff.piapi.contract.v1.OrderDirection;
import ru.tinkoff.piapi.contract.v1.OrderType;
import ru.tinkoff.piapi.core.InvestApi;
import ru.tinkoff.piapi.core.models.Quantity;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class Order {
    static final Logger log = LoggerFactory.getLogger(Main.class);
    public static String ordersServiceBuy(InvestApi api, String figi, int quantity) {
//        выставляем заявку
        var accounts = api.getUserService().getAccountsSync();
        var mainAccount = accounts.get(0).getId();
        var lastPrice = api.getMarketDataService().getLastPricesSync(List.of(figi)).get(0).getPrice();
        var minPriceIncrement = api.getInstrumentsService().getInstrumentByFigiSync(figi).getMinPriceIncrement();
        var price = Quantity.ofQuotation(lastPrice)
                .subtract(
                        Quantity.ofQuotation(minPriceIncrement)
                                .mapValue(minPriceBifDecimal -> minPriceBifDecimal.multiply(BigDecimal.TEN.multiply(BigDecimal.TEN))))
                .toQuotation();
        //выставляем заявку на покупку по лимитной цене
        var orderId = api.getOrdersService()
                .postOrderSync(figi, quantity, price, OrderDirection.ORDER_DIRECTION_BUY, mainAccount, OrderType.ORDER_TYPE_LIMIT,
                        UUID.randomUUID().toString()).getOrderId();
        // получаем список актиных заявок
        var orders = api.getOrdersService().getOrdersSync(mainAccount);
        if(checkOrder(api, orderId))
            log.info("заявка с id: {} есть в списке активных заявок", orderId);
        else
            log.info("заявки с id: {} нет в списке активных заявок", orderId);

        // отмена заявки
//        api.getOrdersService().cancelOrder(mainAccount, orderId);
        return orderId;
    }
    public static String orderServiceSold(InvestApi api, String figi,  int quantity) {
        var account = api.getUserService().getAccountsSync();
        var mainAccount = api.getUserService().getAccountsSync().get(0).getId();
        var lastPrice = api.getMarketDataService().getLastPricesSync(List.of(figi)).get(0).getPrice();
        var minPrice = api.getInstrumentsService().getInstrumentByFigiSync(figi).getMinPriceIncrement();
        var price = Quantity.ofQuotation(lastPrice)
                .subtract(
                        Quantity.ofQuotation(minPrice)
                                .mapValue(minPriceBifDecimal -> minPriceBifDecimal.multiply(BigDecimal.TEN.multiply(BigDecimal.TEN))))
                .toQuotation();
        var orderId = api.getOrdersService()
                .postOrderSync(figi, quantity, price, OrderDirection.ORDER_DIRECTION_SELL, mainAccount, OrderType.ORDER_TYPE_LIMIT,
                        UUID.randomUUID().toString()).getOrderId();

        if (checkOrder(api, orderId))
            log.info("заявка с id: {} есть в списке активных заявок", orderId);
        else
            log.info("заявки с id: {} нет в списке активных заявок", orderId);
        return orderId;
    }
    public static void cancelOrder(InvestApi api, String orderId) {
        var mainAccount = api.getUserService().getAccountsSync().get(0).getId();
        api.getOrdersService().cancelOrder(mainAccount, orderId);
        if(checkOrder(api, orderId))
            log.info("заявка с id: {} есть в списке активных заявок", orderId);
        else
            log.info("заявки с id: {} удалена из списка активных заявок", orderId);
    }
    public static boolean checkOrder(InvestApi api, String orderId) {
        var mainAccount = api.getUserService().getAccountsSync().get(0).getId();
        var orders = api.getOrdersService().getOrdersSync(mainAccount);
        return orders.stream().anyMatch(el -> orderId.equals(el.getOrderId()));
    }
}
