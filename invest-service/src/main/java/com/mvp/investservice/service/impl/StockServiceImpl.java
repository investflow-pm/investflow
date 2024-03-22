package com.mvp.investservice.service.impl;

import com.mvp.investservice.domain.exception.AssetNotFoundException;
import com.mvp.investservice.domain.exception.BuyUnavailableException;
import com.mvp.investservice.domain.exception.ResourceNotFoundException;
import com.mvp.investservice.service.StockService;
import com.mvp.investservice.service.cache.CacheService;
import com.mvp.investservice.util.MoneyParser;
import com.mvp.investservice.util.SectorStockUtil;
import com.mvp.investservice.web.dto.OrderResponse;
import com.mvp.investservice.web.dto.PurchaseDto;
import com.mvp.investservice.web.dto.stock.StockDto;
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

    private final CacheService cacheService;
    private final InvestApi investApi;
    private final StockMapper stockMapper;

    @Override
    public List<StockDto>  getStocksByName(String name) {
        var stocksInfo = investApi.getInstrumentsService().findInstrumentSync(name)
                .stream().filter(b -> b.getInstrumentType().equalsIgnoreCase("share")).toList();
        if (stocksInfo.isEmpty()) {
            throw new ResourceNotFoundException("Не удалось найти акцию: " + name);
        }

<<<<<<< HEAD
        var tradableStocks = cacheService.getTradableStocksSync(investApi);
        List<String> stocksFigis = new ArrayList<>(stocksInfo.size());
        for (var stock : stocksInfo) {
            if (!tradableStocks.stream().filter(b -> b.getFigi().equalsIgnoreCase(stock.getFigi())).findFirst().isEmpty()) {
                stocksFigis.add(stock.getFigi());
            }
        }
=======
        List<Share> shares = investApi.getInstrumentsService()
                .getTradableSharesSync();
>>>>>>> 933555e6caa582353e1a48013aa5841435de7f24

        List<Share> stocks = new ArrayList<>();
        for (var figi : stocksFigis) {
            stocks.add(investApi.getInstrumentsService().getShareByFigiSync(figi));
        }

        return stockMapper.toDto(stocks);
    }

    @Override
<<<<<<< HEAD
    public List<StockDto> getStocks(Integer page, Integer count) {
        if (page < 1) {
            page = 1;
        }
        if (count < 1) {
            count = 10;
=======
    public List<StockDto> getStocks() {
        List<Share> shares = investApi.getInstrumentsService()
                .getTradableSharesSync().subList(0, 200);

        List<StockDto> stockDtos = new ArrayList<>();
        for (Share share : shares) {
            stockDtos.add(stockMapper.toDto(share));
>>>>>>> 933555e6caa582353e1a48013aa5841435de7f24
        }

        var tradableStocks = cacheService.getTradableStocksSync(investApi).subList((page - 1) * count, count - 1);

        List<StockDto> stocks = new ArrayList<>();
        for (var stock : tradableStocks) {
            stocks.add(stockMapper.toDto(stock));
        }

        return stocks;
    }

    @Override
<<<<<<< HEAD
    public List<StockDto> getStocksBySector(String sectorName, Integer count) {
        if (count <= 0) {
            count = 10;
=======
    public List<StockDto> getStocksBySector(String sectorName) {
        List<Share> shares = investApi.getInstrumentsService()
                .getTradableSharesSync().subList(0, 100);

        List<Share> sectorShares = shares.stream()
                .filter(e -> e.getSector().equals(sectorName))
                .toList();

        List<StockDto> stockDtos = new ArrayList<>();
        for (Share share : sectorShares) {
            stockDtos.add(stockMapper.toDto(share));
>>>>>>> 933555e6caa582353e1a48013aa5841435de7f24
        }

        var sector = SectorStockUtil.valueOfRussianName(sectorName);
        var tradableStocks = cacheService.getTradableStocksSync(investApi);

        List<Share> stocksBySector = new ArrayList<>();

        for (var i = 0; i < tradableStocks.size() && stocksBySector.size() <= count; i++) {
            var stock = tradableStocks.get(i);
            if (stock.getSector().equalsIgnoreCase(sector)) {
                stocksBySector.add(stock);
            }
        }

        List<StockDto> stocks = new ArrayList<>();
        for (var stock : stocksBySector) {
            stocks.add(stockMapper.toDto(stock));
        }

        return stocks;
    }

    @Override
    public OrderResponse<StockDto> buyStock(PurchaseDto purchaseDto) {
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

        return lastPrice.subtract(minPrice.multiply(BigDecimal.TEN.multiply(BigDecimal.TEN)));
    }
}