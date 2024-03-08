package politetransaction.store.gateway.security;

import java.security.Provider.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.integration.IntegrationProperties.RSocket.Server;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

public class AuthenticationFilter implements GlobalFilter {

    @Autowired
    private final RouterValidator routerValidator;

    @Autowired
    private WebClient webClient;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        final ServerHttpRequest request = exchange.getRequest();

        // verificar se a rota eh segura
        if (!routerValidator.isSecured.test(request)) {
            return chain.filter(exchange);
        }
        if (isAuthMissing(request)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
        final String[] parts = request.getHeaders().get("Authorization").get(0).split(" ");
        if (parts.length != 2 || !"Bearer".equals(parts[0])) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid authorization header");
        }

        // TODO: verificar se o token eh valido, senao retornar 401
        // TODO: verificar se o cabecalho de autorizacao esta presente
        // TODO: atualizar o cabecalho de autorizacao
        return null;
    }

    private boolean isAuthMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey("Authorization");
    }

    public AuthenticationFilter(RouterValidator routerValidator) {
        this.routerValidator = routerValidator;
    }

}
