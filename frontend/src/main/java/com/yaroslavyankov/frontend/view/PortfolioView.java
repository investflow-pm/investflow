package com.yaroslavyankov.frontend.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.yaroslavyankov.frontend.dto.BondDto;
import com.yaroslavyankov.frontend.dto.PortfolioResponse;
import com.yaroslavyankov.frontend.dto.PositionResponse;
import com.yaroslavyankov.frontend.dto.StockDto;
import com.yaroslavyankov.frontend.service.PortfolioService;
import com.yaroslavyankov.frontend.service.exception.GettingProtfolioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;

@Route(value = "portfolio", layout = MainView.class)
@PageTitle("Портфель")
public class PortfolioView extends VerticalLayout implements BeforeEnterObserver {

    private final PortfolioService portfolioService;

    public PortfolioView(@Autowired PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
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
            List<BondDto> bonds = getBonds(positions);

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
}
