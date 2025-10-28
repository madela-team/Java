package dev.madela.apteka.order.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.consul.serviceregistry.ConsulAutoServiceRegistration;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурационный класс, обеспечивающий ручовой запуск регистрации сервиса в Consul.
 * <p>
 * Необходим при использовании {@code health-check-type: ttl}, так как Spring Boot
 * не всегда автоматически инициирует отправку heartbeat в Consul.
 * <p>
 * Без вызова {@code registration.start()} сервис может считаться незарегистрированным
 * или "unhealthy", особенно в Docker-окружении.
 * <p>
 * Автор: MaDeLa
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class ConsulTtlHeartbeat {

    /**
     * Компонент Spring Cloud, отвечающий за регистрацию сервиса в Consul.
     * Подключается автоматически на основе конфигурации в application.yaml.
     */
    @Autowired
    private ConsulAutoServiceRegistration registration;

    /**
     * Метод вызывается после инициализации Spring-контекста.
     * <p>
     * Явно запускает процесс регистрации сервиса в Consul.
     * Это особенно важно, если используется проверка типа TTL
     * и сервис должен самостоятельно отправлять heartbeat.
     */
    @PostConstruct
    public void forceRegister() {
        registration.start(); // Принудительная регистрация в Consul
        System.out.println(">>> Consul registration started manually"); // Вывод в консоль (для отладки)
    }
}
