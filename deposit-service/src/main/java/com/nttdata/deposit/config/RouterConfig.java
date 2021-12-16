package com.nttdata.deposit.config;

import com.nttdata.deposit.handler.DepositHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import org.springframework.web.reactive.function.server.RouterFunction;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouterConfig {
    @Bean
    public RouterFunction<ServerResponse> routes(DepositHandler handler){
        return route(POST("/deposit"), handler::create)
                .andRoute(GET("/deposit/{id}"), handler::findById);
    }
}
