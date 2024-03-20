package com.mvp.investservice.service.impl;

import com.mvp.investservice.domain.exception.AssetNotFoundException;
import com.mvp.investservice.domain.exception.BuyUnavailableException;
import com.mvp.investservice.domain.exception.ResourceNotFoundException;
import com.mvp.investservice.service.BondService;
import com.mvp.investservice.util.MoneyParser;
import com.mvp.investservice.util.SectorBondUtil;
import com.mvp.investservice.web.dto.OrderResponse;
import com.mvp.investservice.web.dto.PurchaseDto;
import com.mvp.investservice.web.dto.bond.BondDto;
import com.mvp.investservice.web.mapper.BondMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.contract.v1.*;
import ru.tinkoff.piapi.core.InvestApi;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BondServiceImpl implements BondService {

    private final InvestApi investApi;
    private final BondMapper bondMapper;

    @Override
    public List<BondDto> getBondsByName(String name) {
        var bondsInfo = investApi.getInstrumentsService().findInstrumentSync(name)
                .stream().filter(b -> b.getInstrumentType().equalsIgnoreCase("bond")).toList();
        if (bondsInfo.isEmpty()) {
            throw new ResourceNotFoundException("Не удалось найти облигацию: " + name);
        }

        var tradableBonds = investApi.getInstrumentsService().getTradableBondsSync(); // TODO: cash
        List<String> bondFigis = new ArrayList<>(bondsInfo.size());
        for (var bond : bondsInfo) {
            if (!tradableBonds.stream().filter(b -> b.getFigi().equalsIgnoreCase(bond.getFigi())).findFirst().isEmpty()) {
                bondFigis.add(bond.getFigi());
            }
        }

        List<Bond> bonds = new ArrayList<>();
        for (var figi : bondFigis) {
            bonds.add(investApi.getInstrumentsService().getBondByFigiSync(figi));
        }

        return bondMapper.toDto(bonds);
    }

    @Override
    public List<BondDto> getBonds(Integer page, Integer count) {
        var tradableBonds = investApi.getInstrumentsService()
                .getTradableBondsSync().subList((page - 1) * count, count - 1); // TODO: cash

        List<BondDto> bonds = new ArrayList<>();
        for (var bond : tradableBonds) {
            bonds.add(bondMapper.toDto(bond));
        }

        return bonds;
    }

    @Override
    public List<BondDto> getBondsBySector(String sectorName, Integer count) {
        if (count <= 0) {
            count = 10;
        }

        var sector = SectorBondUtil.valueOfRussianName(sectorName);
        var tradableBonds = investApi.getInstrumentsService().getTradableBondsSync();

        List<Bond> bondsBySector = new ArrayList<>();

        for (var i = 0; i < tradableBonds.size() && bondsBySector.size() <= count; i++) {
            var bond = tradableBonds.get(i);
            if (bond.getSector().equalsIgnoreCase(sector)) {
                bondsBySector.add(bond);
            }
        }

        List<BondDto> bonds = new ArrayList<>();
        for (var bond : bondsBySector) {
            bonds.add(bondMapper.toDto(bond));
        }

        return bonds;
    }

    @Override
    public OrderResponse<BondDto> buyBond(PurchaseDto purchaseDto) {
        Bond purchasedBond = null;
        try {
            purchasedBond = investApi.getInstrumentsService()
                    .getBondByFigiSync(purchasedBond.getFigi());
        } catch (Exception e) {
            throw new AssetNotFoundException(e.getMessage());
        }

        if (purchasedBond.getBuyAvailableFlag() && purchasedBond.getApiTradeAvailableFlag()) {
            var figi = purchasedBond.getFigi();
            var price = getBigDecimalPrice(figi);
            var resultPrice = getPurchasePrice(price);
            try {
                var postOrderResponse = investApi.getOrdersService()
                        .postOrderSync(figi, purchaseDto.getLot(), resultPrice, OrderDirection.ORDER_DIRECTION_BUY,
                                purchaseDto.getAccountId(), OrderType.valueOf(purchaseDto.getOrderType().name()),
                                UUID.randomUUID().toString());

                return generateOrderResponse(purchasedBond, postOrderResponse);
            } catch (Exception e) {
                throw new BuyUnavailableException(e.getMessage());
            }
        } else {
            throw new BuyUnavailableException("В данный момент невозможно купить данную облигацию");
        }
    }

    /**
     * Метод по генерации ответа по покупке акции
     * @param purchasedBond - акция, которая будет куплена пользователем
     * @param postOrderResponse - ответ от tinkoff api о выставленной заявке/покупке
     * @return
     */
    private OrderResponse<BondDto> generateOrderResponse(Bond purchasedBond, PostOrderResponse postOrderResponse) {
        BondDto bondDto = bondMapper.toDto(purchasedBond);

        return setOrderResponseFields(postOrderResponse, bondDto);
    }

    private OrderResponse<BondDto> setOrderResponseFields(PostOrderResponse postOrderResponse, BondDto bondDto) {
        OrderResponse<BondDto> orderResponse = new OrderResponse<>();
        orderResponse.setOrderId(postOrderResponse.getOrderId());
        orderResponse.setExecutionStatus(postOrderResponse.getExecutionReportStatus().name());
        orderResponse.setLotRequested((int) postOrderResponse.getLotsRequested());
        orderResponse.setLotExecuted((int) postOrderResponse.getLotsExecuted());
        orderResponse.setInitialOrderPrice(MoneyParser.moneyValueToBigDecimal(postOrderResponse.getInitialOrderPrice()));
        orderResponse.setExecutedOrderPrice(MoneyParser.moneyValueToBigDecimal(postOrderResponse.getExecutedOrderPrice()));
        orderResponse.setTotalOrderPrice(MoneyParser.moneyValueToBigDecimal(postOrderResponse.getTotalOrderAmount()));
        orderResponse.setAsset(bondDto);
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
