package com.mvp.investservice.service.impl;

import com.mvp.investservice.domain.exception.AssetNotFoundException;
import com.mvp.investservice.domain.exception.BuyUnavailableException;
import com.mvp.investservice.domain.exception.ResourceNotFoundException;
import com.mvp.investservice.domain.exception.SellUnavailableException;
import com.mvp.investservice.service.FondService;
import com.mvp.investservice.service.cache.CacheService;
import com.mvp.investservice.util.MoneyParser;
import com.mvp.investservice.web.dto.OrderResponse;
import com.mvp.investservice.web.dto.PurchaseDto;
import com.mvp.investservice.web.dto.fond.FondDto;
import com.mvp.investservice.web.mapper.FondMapper;
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
public class FondServiceImpl implements FondService {
    private final CacheService cacheService;
    private final InvestApi investApi;
    private final FondMapper fondMapper;

    @Override
    public List<FondDto> getFondByName(String name) {
        var fondsInfo = investApi.getInstrumentsService().findInstrumentSync(name)
                .stream().filter(b -> b.getInstrumentType().equalsIgnoreCase("fond")).toList();
        if (fondsInfo.isEmpty()) {
            throw new ResourceNotFoundException("Не удалось найти облигацию: " + name);
        }

        var tradableFonds = cacheService.getTradableFondsSync(investApi);
        List<String> fondFigis = new ArrayList<>(fondsInfo.size());
        for (var fond : fondsInfo) {
            if (!tradableFonds.stream().filter(b -> b.getFigi().equalsIgnoreCase(fond.getFigi())).findFirst().isEmpty()) {
                fondFigis.add(fond.getFigi());
            }
        }
       
        List<Share> fonds = new ArrayList<>();
        for (var figi : fondFigis) {
            fonds.add(investApi.getInstrumentsService().getShareByFigiSync(figi));
        }

        return fondMapper.toDto(fonds);
    }

    @Override
    public List<FondDto> getFonds(Integer page, Integer count) {
        if (page < 1) {
            page = 1;
        }
        if (count < 1) {
            count = 10;
        }

        var tradableFonds = cacheService.getTradableFondsSync(investApi).subList((page - 1) * count, count - 1);

        List<FondDto> fonds = new ArrayList<>();
        for (var fond : tradableFonds) {
            fonds.add(fondMapper.toDto(fond));
        }

        return fonds;
    }

//    @Override
//    public List<FondDto> getFondsBySector(String sectorName, Integer count) {
//        if (count <= 0) {
//            count = 10;
//        }
//
//        var sector = SectorFondUtil.valueOfRussianName(sectorName);
//        var tradableFonds = cacheService.getTradableFondsSync(investApi);
//
//        List<Share> fondsBySector = new ArrayList<>();
//
//        for (var i = 0; i < tradableFonds.size() && fondsBySector.size() <= count; i++) {
//            var fond = tradableFonds.get(i);
//            if (fond.getSector().equalsIgnoreCase(sector)) {
//                fondsBySector.add(fond);
//            }
//        }
//
//        List<FondDto> fonds = new ArrayList<>();
//        for (var fond : fondsBySector) {
//            fonds.add(fondMapper.toDto(fond));
//        }
//
//        return fonds;
//    }

    @Override
    public OrderResponse<FondDto> buyFond(PurchaseDto purchaseDto) {
        Share purchasedFond = null;
        try {
            purchasedFond = investApi.getInstrumentsService()
                    .getShareByFigiSync(purchasedFond.getFigi());
        } catch (Exception e) {
            throw new AssetNotFoundException(e.getMessage());
        }

        if (purchasedFond.getBuyAvailableFlag() && purchasedFond.getApiTradeAvailableFlag()) {
            var figi = purchasedFond.getFigi();
            var price = getBigDecimalPrice(figi);
            var resultPrice = getPurchasePrice(price);
            try {
                var postOrderResponse = investApi.getOrdersService()
                        .postOrderSync(figi, purchaseDto.getLot(), resultPrice, OrderDirection.ORDER_DIRECTION_BUY,
                                purchaseDto.getAccountId(), OrderType.valueOf(purchaseDto.getOrderType().name()),
                                UUID.randomUUID().toString());

                return generateOrderResponse(purchasedFond, postOrderResponse);
            } catch (Exception e) {
                throw new BuyUnavailableException(e.getMessage());
            }
        } else {
            throw new BuyUnavailableException("В данный момент невозможно купить данную облигацию");
        }
    }
    @Override
    public OrderResponse<FondDto> sellFond(PurchaseDto purchaseDto) {
        Share purchasedFond = null;
        try {
            purchasedFond = investApi.getInstrumentsService()
                    .getShareByFigiSync(purchasedFond.getFigi());
        } catch (Exception e) {
            throw new AssetNotFoundException(e.getMessage());
        }

        if (purchasedFond.getBuyAvailableFlag() && purchasedFond.getApiTradeAvailableFlag()) {
            var figi = purchasedFond.getFigi();
            var price = getBigDecimalPrice(figi);
            var resultPrice = getPurchasePrice(price);
            try {
                var postOrderResponse = investApi.getOrdersService()
                        .postOrderSync(figi, purchaseDto.getLot(), resultPrice, OrderDirection.ORDER_DIRECTION_SELL,
                                purchaseDto.getAccountId(), OrderType.valueOf(purchaseDto.getOrderType().name()),
                                UUID.randomUUID().toString());

                return generateOrderResponse(purchasedFond, postOrderResponse);
            } catch (Exception e) {
                throw new SellUnavailableException(e.getMessage());
            }
        } else {
            throw new SellUnavailableException("В данный момент невозможно купить данную облигацию");
        }
    }
    /**
     * Метод по генерации ответа по покупке акции
     * @param purchasedFond - акция, которая будет куплена пользователем
     * @param postOrderResponse - ответ от tinkoff api о выставленной заявке/покупке
     * @return
     */
    private OrderResponse<FondDto> generateOrderResponse(Share purchasedFond, PostOrderResponse postOrderResponse) {
        FondDto fondDto = fondMapper.toDto(purchasedFond);

        return setOrderResponseFields(postOrderResponse, fondDto);
    }

    private OrderResponse<FondDto> setOrderResponseFields(PostOrderResponse postOrderResponse, FondDto fondDto) {
        OrderResponse<FondDto> orderResponse = new OrderResponse<>();
        orderResponse.setOrderId(postOrderResponse.getOrderId());
        orderResponse.setExecutionStatus(postOrderResponse.getExecutionReportStatus().name());
        orderResponse.setLotRequested((int) postOrderResponse.getLotsRequested());
        orderResponse.setLotExecuted((int) postOrderResponse.getLotsExecuted());
        orderResponse.setInitialOrderPrice(MoneyParser.moneyValueToBigDecimal(postOrderResponse.getInitialOrderPrice()));
        orderResponse.setExecutedOrderPrice(MoneyParser.moneyValueToBigDecimal(postOrderResponse.getExecutedOrderPrice()));
        orderResponse.setTotalOrderPrice(MoneyParser.moneyValueToBigDecimal(postOrderResponse.getTotalOrderAmount()));
        orderResponse.setAsset(fondDto);
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
     * @return lastPrice
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
