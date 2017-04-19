package com.formento.apigateway;

import com.formento.apigateway.filter.LogPreFilter;
import com.formento.apigateway.filter.RedirectPreFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableZuulProxy
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    @Bean
    public LogPreFilter logPreFilter() {
        return new LogPreFilter();
    }

    @Bean
    public RedirectPreFilter redirectPreFilter() {
        return new RedirectPreFilter();
    }

}
