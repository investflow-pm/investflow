package com.yaroslavyankov.frontend.service;


import com.yaroslavyankov.frontend.dto.PortfolioRequest;
import com.yaroslavyankov.frontend.dto.PortfolioResponse;
import com.yaroslavyankov.frontend.props.InvestLinkProperties;
import com.yaroslavyankov.frontend.service.exception.AccessDeniedException;
import com.yaroslavyankov.frontend.service.exception.GettingProtfolioException;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class PortfolioService {

    private static RestTemplate restTemplate;

    private static InvestLinkProperties investLinkProperties;

    public static PortfolioResponse getPortfolio(String accountId, String accessToken) {

        PortfolioRequest portfolioRequest = new PortfolioRequest();
        portfolioRequest.setAccountId(accountId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<PortfolioRequest> request = new HttpEntity<>(portfolioRequest, headers);

        ResponseEntity<PortfolioResponse> response = restTemplate
                .exchange(investLinkProperties.getPortfolioLink(), HttpMethod.POST, request, PortfolioResponse.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            throw new AccessDeniedException("Сессия истекла.");
        } else {
            throw new GettingProtfolioException("Не удалось получить портфель инвестиций.");
        }
    }
}
