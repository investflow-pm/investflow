package com.yaroslavyankov.frontend.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.yaroslavyankov.frontend.dto.*;
import com.yaroslavyankov.frontend.service.OperationService;
import com.yaroslavyankov.frontend.service.PortfolioService;
import com.yaroslavyankov.frontend.service.exception.GettingProtfolioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

@Route(value = "portfolio", layout = MainView.class)
@PageTitle("Портфель")
public class PortfolioView extends VerticalLayout implements BeforeEnterObserver {

    private final PortfolioService portfolioService;
    private final OperationService operationService;

    public PortfolioView(@Autowired PortfolioService portfolioService,
                         @Autowired OperationService operationService) {
        this.portfolioService = portfolioService;
        this.operationService = operationService;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String accessToken = (String) VaadinSession.getCurrent().getAttribute("accessToken");

        // Configure layout
        setSizeFull();
        setMargin(false);
        setSpacing(false);

        try {
            PortfolioResponse portfolio = portfolioService.getPortfolio("a7c911bb-5b01-41bf-9db7-3767ac46385d", accessToken);

            List<PositionResponse> positions = portfolio.getPositions();

            List<StockDto> shares = getShares(positions);
            for (int i = 0; i < shares.size(); i++) {
                if (shares.get(i).getPrice() == null) { // check price
                    shares.get(i).setPrice(BigDecimal.ZERO);
                }

                // TODO: change
                // change lots to Quantity
                shares.get(i).setLots(positions.get(i).getQuantity());
            }

            List<BondDto> bonds = getBonds(positions);
            for (int i = 0; i < bonds.size(); i++) {
                if (bonds.get(i).getPrice() == null) { // check price
                    bonds.get(i).setPrice(BigDecimal.ZERO);
                }

                // TODO: change
                // change lots to Quantity
                bonds.get(i).setLots(positions.get(i).getQuantity());
            }

            // grid stock
            Grid<StockDto> gridStock = new Grid<>(StockDto.class, false);
            gridStock.addColumn(createEmployeeRendererStock()).setHeader("Акция")
                    .setAutoWidth(true).setFlexGrow(0)
                    .setSortable(true);
            gridStock.addColumn(p -> p.getPrice().setScale(2, RoundingMode.HALF_UP) + " " + Currency.getInstance(p.getCurrency().toUpperCase(Locale.ROOT)).getSymbol())
                    .setHeader("Текущая цена")
                    .setSortable(true)
                    .setAutoWidth(true);
            gridStock.addColumn(StockDto::getSector).setHeader("Отрасль")
                    .setAutoWidth(true)
                    .setSortable(true);
            gridStock.addColumn(StockDto::getLots).setHeader("Количество")
                    .setAutoWidth(true);

            gridStock.setItems(shares);
            add(gridStock);

            // grid bond
            Grid<BondDto> gridBond = new Grid<>(BondDto.class, false);
            gridBond.addColumn(createEmployeeRendererBond()).setHeader("Облигация")
                    .setAutoWidth(true).setFlexGrow(0)
                    .setSortable(true);
            gridBond.addColumn(p -> p.getPrice().setScale(2, RoundingMode.HALF_UP) + " " + Currency.getInstance(p.getCurrency().toUpperCase(Locale.ROOT)).getSymbol())
                    .setHeader("Текущая цена")
                    .setSortable(true)
                    .setAutoWidth(true);
            gridBond.addColumn(BondDto::getSector).setHeader("Отрасль")
                    .setAutoWidth(true)
                    .setSortable(true);
            gridBond.addColumn(BondDto::getLots).setHeader("Количество")
                    .setAutoWidth(true);

            gridBond.setItems(bonds);
            add(gridBond);

        } catch (HttpClientErrorException.Unauthorized e) {
            event.rerouteTo(LoginView.class);
            UI.getCurrent().navigate("/login");
        } catch (GettingProtfolioException e) {
            H2 exceptionTitle = new H2("Не удалось получить портфель по счёту");
            exceptionTitle.getStyle()
                    .set("margin", "0 auto");
        }
    }

    private List<StockDto> getShares(List<PositionResponse> positions) {
        List<StockDto> shares = new ArrayList<>();

        for (PositionResponse position : positions) {
            if (position.getInstrumentType().equals("share")) {
                StockDto stockDto = (StockDto) position.getAsset();
                shares.add(stockDto);
            }
        }

        return shares;
    }

    private List<BondDto> getBonds(List<PositionResponse> positions) {
        List<BondDto> bonds = new ArrayList<>();

        for (PositionResponse position : positions) {
            if (position.getInstrumentType().equals("bond")) {
                BondDto bondDto = (BondDto) position.getAsset();
                bonds.add(bondDto);
            }
        }

        return bonds;
    }

    private static Renderer<StockDto> createEmployeeRendererStock() {
        return LitRenderer.<StockDto> of(
                        "<vaadin-horizontal-layout style=\"align-items: center;\" theme=\"spacing\">"
                                + "<vaadin-avatar alt=\"User avatar\" style=\"width:40px;\" name=\"${item.ticker}\"><img style=\"max-width:100%;\" src=\"${item.pictureUrl}\"></vaadin-avatar>"
                                + "  <vaadin-vertical-layout style=\"line-height: var(--lumo-line-height-m);\">"
                                + "    <span> ${item.ticker} </span>"
                                + "    <span style=\"font-size: var(--lumo-font-size-s); color: var(--lumo-secondary-text-color);\">"
                                + "      ${item.name}" + "    </span>"
                                + "  </vaadin-vertical-layout>"
                                + "</vaadin-horizontal-layout>")
                .withProperty("pictureUrl", s -> s.getBrandLogo().getLogoUrl())
                .withProperty("ticker", StockDto::getTicker)
                .withProperty("name", StockDto::getName);
    }

    private static Renderer<BondDto> createEmployeeRendererBond() {
        return LitRenderer.<BondDto> of(
                        "<vaadin-horizontal-layout style=\"align-items: center;\" theme=\"spacing\">"
                                + "<vaadin-avatar alt=\"User avatar\" style=\"width:40px;\" name=\"${item.ticker}\"><img style=\"max-width:100%;\" src=\"${item.pictureUrl}\"></vaadin-avatar>"
                                + "  <vaadin-vertical-layout style=\"line-height: var(--lumo-line-height-m);\">"
                                + "    <span> ${item.ticker} </span>"
                                + "    <span style=\"font-size: var(--lumo-font-size-s); color: var(--lumo-secondary-text-color);\">"
                                + "      ${item.name}" + "    </span>"
                                + "  </vaadin-vertical-layout>"
                                + "</vaadin-horizontal-layout>")
                .withProperty("pictureUrl", s -> s.getBrandLogo().getLogoUrl())
                .withProperty("ticker", BondDto::getTicker)
                .withProperty("name", BondDto::getName);
    }
}
