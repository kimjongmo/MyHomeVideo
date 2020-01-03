package com.myhome.play.config;

import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;


@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(){
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory =
                new HttpComponentsClientHttpRequestFactory();

        clientHttpRequestFactory.setConnectTimeout(3*1000);
        clientHttpRequestFactory.setReadTimeout(3*1000);
        clientHttpRequestFactory.setHttpClient(HttpClientBuilder.create()
                .setMaxConnTotal(100)
                .build());
        return new RestTemplate(clientHttpRequestFactory);
    }
}
