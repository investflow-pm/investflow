package com.yaroslavyankov.frontend.view.stock;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;
import com.yaroslavyankov.frontend.dto.StockDto;
import com.yaroslavyankov.frontend.service.StockService;
import com.yaroslavyankov.frontend.service.exception.GettingProtfolioException;
import com.yaroslavyankov.frontend.view.LoginView;
import com.yaroslavyankov.frontend.view.MainView;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

@Route(value = "stocks", layout = MainView.class)
@PageTitle("Акции")
public class StocksInfoView extends VerticalLayout implements BeforeEnterObserver {

    private final StockService stockService;

    public StocksInfoView(StockService stockService) {
        this.stockService = stockService;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {

        String accessToken = (String) VaadinSession.getCurrent().getAttribute("accessToken");

        setSizeFull();
        setMargin(false);
        setSpacing(false);

        try {
            List<StockDto> stocks = stockService.getStocks(accessToken);

            Grid<StockDto> grid = new Grid<>(StockDto.class, false);
            grid.addColumn(createEmployeeRenderer()).setHeader("Акция")
                    .setAutoWidth(true).setFlexGrow(0)
                    .setSortable(true);
            grid.addColumn(p -> p.getPrice().setScale(2, RoundingMode.HALF_UP) + " " + Currency.getInstance(p.getCurrency().toUpperCase(Locale.ROOT)).getSymbol()).setHeader("Цена")
                    .setSortable(true)
                    .setAutoWidth(true);
            grid.addColumn(StockDto::getSector).setHeader("Отрасль")
                    .setAutoWidth(true)
                    .setSortable(true);
            grid.addColumn(StockDto::getLots).setHeader("Лотность")
                    .setAutoWidth(true);

            grid.setItems(stocks);

            add(grid);
        } catch (HttpClientErrorException.Unauthorized e) {
            event.rerouteTo(LoginView.class);
            UI.getCurrent().navigate("/login");
        } catch (GettingProtfolioException e) {
            H2 exceptionTitle = new H2("Не удалось получить портфель по счёту");
            exceptionTitle.getStyle()
                    .set("margin", "0 auto");
        }
    }

    private static Renderer<StockDto> createEmployeeRenderer() {
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
}
