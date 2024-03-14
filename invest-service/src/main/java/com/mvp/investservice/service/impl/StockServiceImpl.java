package com.mvp.investservice.service.impl;

import com.mvp.investservice.domain.exception.AssetNotFoundException;
import com.mvp.investservice.domain.exception.BuyUnavailableException;
import com.mvp.investservice.domain.exception.ResourceNotFoundException;
import com.mvp.investservice.service.StockService;
import com.mvp.investservice.util.MoneyParser;
import com.mvp.investservice.web.dto.OrderResponse;
import com.mvp.investservice.web.dto.PurchaseDto;
import com.mvp.investservice.web.dto.StockDto;
import com.mvp.investservice.web.mapper.StockMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.contract.v1.*;
import ru.tinkoff.piapi.core.InvestApi;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private final InvestApi investApi;

    private final StockMapper stockMapper;

    @Override
    public StockDto getStockByName(String name) {

        List<Share> shares = investApi.getInstrumentsService()
                .getAllSharesSync();

        Share share = shares.stream()
                .filter(e -> e.getName().contains(name))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("There is no Share with name " + name));

        return stockMapper.toDto(share);
    }

    @Override
    public List<StockDto> getStocks() {
        List<Share> shares = investApi.getInstrumentsService()
                .getAllSharesSync().subList(0, 100);

        List<StockDto> stockDtos = new ArrayList<>();
        for (Share share : shares) {
            stockDtos.add(stockMapper.toDto(share));
        }

        return stockDtos;
    }

    @Override
    public List<StockDto> getStocksBySector(String sectorName) {
        List<Share> shares = investApi.getInstrumentsService()
                .getAllSharesSync();

        List<Share> sectorShares = shares.stream()
                .filter(e -> e.getSector().equals(sectorName))
                .toList();

        List<StockDto> stockDtos = new ArrayList<>();
        for (Share share : sectorShares) {
            stockDtos.add(stockMapper.toDto(share));
        }

        return stockDtos;
    }

    // TODO
    //  1) Проверить проверку работы покупки акций,
    //  где лот из нескольких лотов (BBG000LNHHJ9)
    //  2) Написать ControllerAdvice для обработки ошибок

    @Override
    public OrderResponse<StockDto> buyStock(PurchaseDto purchaseDto) {

        // !!! Временно использовать в целях посмотреть, какими акциями можно торговать)

        List<Share> allSharesSync = investApi.getInstrumentsService()
                .getAllSharesSync();

        List<Share> shares
                = allSharesSync.stream()
                .filter(share -> share.getApiTradeAvailableFlag() && share.getCurrency().equals("rub")).toList();


        Share shareToBuy;
        try {
            shareToBuy = investApi.getInstrumentsService()
                    .getShareByFigiSync(purchaseDto.getFigi());
        } catch (Exception e) {
            throw new AssetNotFoundException(e.getMessage());
        }

        if (shareToBuy.getBuyAvailableFlag() && shareToBuy.getApiTradeAvailableFlag()) {
            String figi = shareToBuy.getFigi();
            BigDecimal price = getBigDecimalPrice(figi);
            Quotation resultPrice = getPurchasePrice(price);
            PostOrderResponse postOrderResponse;
            try {
                postOrderResponse = investApi.getOrdersService()
                        .postOrderSync(figi, purchaseDto.getLot(), resultPrice, OrderDirection.ORDER_DIRECTION_BUY, purchaseDto.getAccountId(), OrderType.valueOf(purchaseDto.getOrderType().name()), UUID.randomUUID().toString());
            } catch (Exception e) {
                throw new BuyUnavailableException(e.getMessage());
            }

            return generateOrderResponse(shareToBuy, postOrderResponse);
        } else {
            throw new BuyUnavailableException("В данный момент невозможно купить данную акцию");
        }
    }

    /**
     * Метод по генерации ответа по покупке акции
     * @param shareToBuy - акция, которая будет куплена пользователем
     * @param postOrderResponse - ответ от tinkoff api о выставленной заявке/покупке
     * @return
     */
    private OrderResponse<StockDto> generateOrderResponse(Share shareToBuy, PostOrderResponse postOrderResponse) {
        StockDto stockDto = stockMapper.toDto(shareToBuy);

        return setOrderResponseFields(postOrderResponse, stockDto);
    }

    private OrderResponse<StockDto> setOrderResponseFields(PostOrderResponse postOrderResponse, StockDto stockDto) {
        OrderResponse<StockDto> orderResponse = new OrderResponse<>();
        orderResponse.setOrderId(postOrderResponse.getOrderId());
        orderResponse.setExecutionStatus(postOrderResponse.getExecutionReportStatus().name());
        orderResponse.setLotRequested((int) postOrderResponse.getLotsRequested());
        orderResponse.setLotExecuted((int) postOrderResponse.getLotsExecuted());
        orderResponse.setInitialOrderPrice(MoneyParser.moneyValueToBigDecimal(postOrderResponse.getInitialOrderPrice()));
        orderResponse.setExecutedOrderPrice(MoneyParser.moneyValueToBigDecimal(postOrderResponse.getExecutedOrderPrice()));
        orderResponse.setTotalOrderPrice(MoneyParser.moneyValueToBigDecimal(postOrderResponse.getTotalOrderAmount()));
        orderResponse.setAsset(stockDto);
        return orderResponse;
    }

    /**
     * Получение цены акции по figi
     * для последущего выставления заявки на покупку
     *
     * @param price
     * @return Quotation (units - рубли, nanos - копейки)
     */
    private Quotation getPurchasePrice(BigDecimal price) {

        return Quotation.newBuilder()
                .setUnits(price != null ? price.longValue() : 0)
                .setNano(price != null ? price.remainder(BigDecimal.ONE)
                        .multiply(BigDecimal.valueOf(1000000000)).intValue() : 0)
                .build();
    }

    /**
     * Просмежуточное получение цены акции
     * в виде BigDecimal
     *
     * @param figi
     * @return
     */

    private BigDecimal getBigDecimalPrice(String figi) {
        Quotation shareLastPrice = investApi.getMarketDataService()
                .getLastPricesSync(List.of(figi)).get(0)
                .getPrice();
        Quotation minPriceIncrement = investApi.getInstrumentsService()
                .getInstrumentByFigiSync(figi)
                .getMinPriceIncrement();

        BigDecimal lastPrice
                = shareLastPrice.getUnits() == 0 && shareLastPrice.getNano() == 0 ? BigDecimal.ZERO : BigDecimal.valueOf(shareLastPrice.getUnits()).add(BigDecimal.valueOf(shareLastPrice.getNano(), 9));
        BigDecimal minPrice
                = minPriceIncrement.getUnits() == 0 && minPriceIncrement.getNano() == 0 ? BigDecimal.ZERO : BigDecimal.valueOf(minPriceIncrement.getUnits()).add(BigDecimal.valueOf(minPriceIncrement.getNano(), 9));

        BigDecimal price
                = lastPrice.subtract(minPrice.multiply(BigDecimal.TEN.multiply(BigDecimal.TEN)));
        return price;
    }
}