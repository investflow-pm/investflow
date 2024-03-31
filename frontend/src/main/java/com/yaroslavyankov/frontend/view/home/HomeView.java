package com.yaroslavyankov.frontend.view.home;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.yaroslavyankov.frontend.dto.Asset;
import com.yaroslavyankov.frontend.dto.PortfolioResponse;
import com.yaroslavyankov.frontend.dto.PositionResponse;
import com.yaroslavyankov.frontend.service.PortfolioService;
import com.yaroslavyankov.frontend.service.exception.AccessDeniedException;
import com.yaroslavyankov.frontend.service.exception.GettingProtfolioException;
import com.yaroslavyankov.frontend.util.SectorCounter;
import com.yaroslavyankov.frontend.view.GridLayout;
import com.yaroslavyankov.frontend.view.MainView;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

@Route(value = "home", layout = MainView.class)
@PageTitle("Главная")
public class HomeView extends VerticalLayout {

    public HomeView(@Autowired PortfolioService portfolioService) {

        String accessToken = (String) VaadinSession.getCurrent().getAttribute("accessToken");

        // Configure layout
        setSizeFull();
        setMargin(false);
        setSpacing(false);

        try {
            PortfolioResponse portfolio = portfolioService.getPortfolio("a7c911bb-5b01-41bf-9db7-3767ac46385d", accessToken);

            PortfolioWidget portfolioWidget = getPortfolioWidget(portfolio);
            SectorChartWidget sectorChartWidget = getSectorChartWidget(portfolio);
            InfoWidget widget3 = new InfoWidget("Widget 3");
            InfoWidget widget4 = new InfoWidget("Widget 4");

            // Configure grid layout
            GridLayout gridLayout = new GridLayout();
            gridLayout.setSizeFull();

            gridLayout.add(portfolioWidget);
            gridLayout.add(widget3);
            gridLayout.add(sectorChartWidget);
            gridLayout.add(widget4);

            // Add grid layout to the view
            add(gridLayout);

        } catch (AccessDeniedException e) {
            UI.getCurrent().navigate("/login");
        } catch (GettingProtfolioException e) {
            H2 exceptionTitle = new H2("Не удалось получить портфель по счёту");
            exceptionTitle.getStyle()
                    .set("margin", "0 auto");
        }
    }

    private PortfolioWidget getPortfolioWidget(PortfolioResponse portfolio) {
        BigDecimal expectedYield = portfolio.getExpectedYield().setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalAmountPortfolio = portfolio.getTotalAmountPortfolio().setScale(1, RoundingMode.HALF_UP);
        int positionCount = portfolio.getPositions().size();

        PortfolioWidgetBody body = new PortfolioWidgetBody();
        body.setTotalAmountPortfolio(totalAmountPortfolio);
        body.setExpectedYield(expectedYield);
        body.setPositionCount(positionCount);

        return new PortfolioWidget("Ваш счёт", body);
    }

    private SectorChartWidget getSectorChartWidget(PortfolioResponse portfolioResponse) {
        List<PositionResponse> positions = portfolioResponse.getPositions();
        List<Asset> assets = positions.stream()
                .map(PositionResponse::getAsset).toList();

        Map<String, Long> sectorMap = SectorCounter.countSectors(assets, Asset::getSector);

        return new SectorChartWidget("Аналитика", sectorMap);
    }


}
