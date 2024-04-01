package com.yaroslavyankov.frontend.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.yaroslavyankov.frontend.dto.PortfolioRequest;
import com.yaroslavyankov.frontend.dto.PortfolioResponse;
import com.yaroslavyankov.frontend.dto.PositionResponse;
import com.yaroslavyankov.frontend.props.InvestLinkProperties;
import com.yaroslavyankov.frontend.service.AccessDeniedException;
import com.yaroslavyankov.frontend.service.GettingProtfolioException;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.RoundingMode;

@Route(value = "workspace/general", layout = MainView.class)
@PageTitle("Общая информация")
public class GeneralInfoView extends VerticalLayout {

    private final RestTemplate restTemplate;

    private final InvestLinkProperties investLinkProperties;

    public GeneralInfoView(RestTemplate restTemplate, InvestLinkProperties investLinkProperties) {
        this.restTemplate = restTemplate;
        this.investLinkProperties = investLinkProperties;

        addClassName("general-info");

        String accessToken = (String) VaadinSession.getCurrent().getAttribute("accessToken");

        VerticalLayout layout = new VerticalLayout();

        HorizontalLayout layout1 = new HorizontalLayout();
        HorizontalLayout layout2 = new HorizontalLayout();
        HorizontalLayout layout3 = new HorizontalLayout();

        layout.getStyle().set("padding", "20");
        layout.setJustifyContentMode(JustifyContentMode.BETWEEN);
        layout.setAlignItems(Alignment.BASELINE);

        layout.getElement().getStyle().set("white-space", "normal");
        layout.getElement().getStyle().set("word-wrap", "break-word");

        TextField accountIdField = new TextField("Аккаунт ID");
        TextField expectedYieldField = new TextField("Ожидаемая доходtность портфеля");
        TextField totalSharesField = new TextField("Цена акций в портфеле");
        TextField totalBondsField = new TextField("Цена облигаций в портфеле");
        TextField totalCurrenciesField = new TextField("Валюта");
        TextField totalPortfolioField = new TextField("Сумма портфеля");
        TextField totalAssetCountField = new TextField("Количество активов");
        TextField totalSharesCountField = new TextField("Количество акций");
        TextField totalBondsCountField = new TextField("Количество облигаций");

        PortfolioResponse portfolio;
        try {
            portfolio
                    = getPortfolio("4a0edb00-16cb-4272-86ee-6cd746198803", accessToken);
            int sharesCount = getSharesCount(portfolio);
            int bondCount = getBondsCount(portfolio);

            accountIdField.setValue(portfolio.getAccountId());
            expectedYieldField.setValue(String.valueOf(portfolio.getExpectedYield().setScale(2, RoundingMode.HALF_UP) + " Р"));
            totalSharesField.setValue(String.valueOf(portfolio.getTotalAmountShares().setScale(2, RoundingMode.HALF_UP)));
            totalBondsField.setValue(String.valueOf(portfolio.getTotalAmountBonds().setScale(2, RoundingMode.HALF_UP)));
            totalCurrenciesField.setValue(String.valueOf(portfolio.getTotalAmountCurrencies().setScale(2, RoundingMode.HALF_UP)));
            totalPortfolioField.setValue(String.valueOf(portfolio.getTotalAmountPortfolio().setScale(2, RoundingMode.HALF_UP)));
            totalAssetCountField.setValue(String.valueOf(portfolio.getPositions().size()));
            totalSharesCountField.setValue(String.valueOf(sharesCount));
            totalBondsCountField.setValue(String.valueOf(bondCount));

            layout1.add(accountIdField, expectedYieldField, totalSharesField);
            layout2.add(totalBondsField, totalCurrenciesField, totalPortfolioField);
            layout3.add(totalAssetCountField, totalSharesCountField, totalBondsCountField);

            layout.add(layout1, layout2, layout3);

            add(layout);
        } catch (AccessDeniedException e) {
            UI.getCurrent().navigate("/login");
        } catch (GettingProtfolioException e) {
            Notification.show(e.getMessage());
        }
    }

    private int getSharesCount(PortfolioResponse portfolio) {
        int sharesAmount = 0;
        for (PositionResponse position : portfolio.getPositions()) {
            if (position.getInstrumentType().equals("share")) {
                sharesAmount += position.getQuantity();
            }
        }
        return sharesAmount;
    }

    private int getBondsCount(PortfolioResponse portfolio) {
        int bondsAmount = 0;
        for (PositionResponse position : portfolio.getPositions()) {
            if (position.getInstrumentType().equals("bond")) {
                bondsAmount += position.getQuantity();
            }
        }
        return bondsAmount;
    }

    private PortfolioResponse getPortfolio(String accountId, String accessToken) {

        PortfolioRequest portfolioRequest = new PortfolioRequest();
        portfolioRequest.setAccountId(accountId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<PortfolioRequest> request = new HttpEntity<>(portfolioRequest, headers);

        ResponseEntity<PortfolioResponse> response = null;
        try {
            response = restTemplate
                    .exchange(investLinkProperties.getPortfolioLink(), HttpMethod.POST, request, PortfolioResponse.class);

        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                throw new AccessDeniedException("Сессия истекла.");
            }
        }
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            throw new AccessDeniedException("Сессия истекла.");
        } else {
            throw new GettingProtfolioException("Не удалось получить портфель инвестиций.");
        }
    }
}
