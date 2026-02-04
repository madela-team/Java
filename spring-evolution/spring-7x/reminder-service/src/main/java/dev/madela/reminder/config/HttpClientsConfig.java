package dev.madela.reminder.config;

import dev.madela.reminder.client.UserHttpClient;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class HttpClientsConfig {

    @Bean
    UserHttpClient userHttpClient(@Value("${app.userServiceBaseUrl}") String baseUrl) {
        RestClient restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .requestInterceptor((request, body, execution) -> {
                    // Берём X-API-Version из входящего запроса reminder-service
                    String apiVersion = null;
                    var attrs = RequestContextHolder.getRequestAttributes();
                    if (attrs instanceof ServletRequestAttributes sra) {
                        HttpServletRequest incoming = sra.getRequest();
                        apiVersion = incoming.getHeader("X-API-Version");
                    }

                    // Если вдруг нет — можно либо упасть, либо дефолтнуть на 1
                    if (apiVersion == null || apiVersion.isBlank()) {
                        apiVersion = "1";
                    }
                    request.getHeaders().set("X-API-Version", apiVersion);
                    return execution.execute(request, body);
                })
                .build();

        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient))
                .build();

        return factory.createClient(UserHttpClient.class);
    }
}
