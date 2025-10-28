package dev.madela.apteka.config;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Фабрика для создания и кэширования gRPC-каналов на основе информации из Consul.
 *
 * Используется совместно с Spring Cloud Discovery (через DiscoveryClient).
 * Под капотом обращается к Consul, чтобы найти IP и порт нужного сервиса.
 *
 * Автор: MaDeLa
 */
@Log4j2
@Component
@ConditionalOnProperty(name = "spring.cloud.consul.discovery.enabled", havingValue = "true", matchIfMissing = true)
/*
 * Эта аннотация гарантирует, что компонент будет создан только если discovery включён.
 * Это предотвращает ошибки в окружениях, где Consul не используется (например, unit-тесты).
 */
public class GrpcConsulChannelFactory {

    private final DiscoveryClient discoveryClient;

    // Потокобезопасный кэш каналов, чтобы не пересоздавать gRPC-соединение каждый раз
    private final ConcurrentHashMap<String, ManagedChannel> channels = new ConcurrentHashMap<>();

    public GrpcConsulChannelFactory(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    /**
     * Получает (или создаёт) gRPC-канал до сервиса по его имени.
     *
     * @param serviceName имя сервиса в Consul (должно совпадать с spring.application.name у удалённого сервиса)
     * @return ManagedChannel — готовый к использованию канал
     */
    public ManagedChannel getChannel(String serviceName) {
        return channels.computeIfAbsent(serviceName, name -> {
            // Получаем список инстансов (может быть несколько — например, при масштабировании)
            var instances = discoveryClient.getInstances(name);

            if (instances.isEmpty()) {
                log.error("No instances found in Consul for service: {}", name);
                throw new IllegalStateException("No instance for " + name);
            } else {
                log.debug("Found {} instances for service {}", instances.size(), name);
            }

            var instance = instances.get(0); // !!! Простой выбор первого. В перспективе — round-robin или random.
            log.debug(instance.getInstanceId() + ", " + instance.getHost() + ", " + instance.getPort());

            // Создаём канал без TLS (plaintext), подходит для внутренних сервисов
            return ManagedChannelBuilder
                    .forAddress(instance.getHost(), instance.getPort())
                    .usePlaintext() // Важно: без TLS (можно заменить на useTransportSecurity() для прод-сред)
                    .build();
        });
    }
}
