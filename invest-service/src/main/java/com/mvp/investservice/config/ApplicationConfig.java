package com.mvp.investservice.config;

import com.mvp.investservice.service.props.InvestProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import ru.tinkoff.piapi.core.InvestApi;

@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class ApplicationConfig {

    private final InvestProperties investProperties;

//    t.AH0I6SMNFaFJhnATn36NMf7Rlv06sItlSlid1U45zRF4Uks0OargwwuMxNe_fBOvwptnmD5-4cFQgJKTfEVe1w
    @Bean
    public InvestApi investApi() {
        return InvestApi.createSandbox(investProperties.getToken());
    }
}
