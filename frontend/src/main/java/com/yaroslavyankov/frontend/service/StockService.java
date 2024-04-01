package com.yaroslavyankov.frontend.service;

import com.yaroslavyankov.frontend.dto.PortfolioRequest;
import com.yaroslavyankov.frontend.dto.StockDto;
import com.yaroslavyankov.frontend.props.StockLinkProperties;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockService {

    private final RestTemplate restTemplate;

    private final StockLinkProperties stockLinkProperties;

    @SneakyThrows
    public List<StockDto> getStocks(String accessToken) {
        int page = 1;
        int count = 300;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<PortfolioRequest> request = new HttpEntity<>(headers);

        UriComponentsBuilder componentsBuilder = UriComponentsBuilder
                .fromHttpUrl(stockLinkProperties.getAllStocksLink())
                .queryParam("page", page)
                .queryParam("count", count);

        ResponseEntity<List<StockDto>> response = restTemplate.exchange(
                componentsBuilder.toUriString(),
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<List<StockDto>>() {}
        );

        return response.getBody();
    }
}
