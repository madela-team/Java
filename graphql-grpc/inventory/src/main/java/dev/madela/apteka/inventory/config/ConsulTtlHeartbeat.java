package dev.madela.apteka.inventory.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.consul.serviceregistry.ConsulAutoServiceRegistration;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурационный класс для принудительного запуска регистрации сервиса в Consul.
 * <p>
 * В некоторых случаях (например, при использовании health-check типа TTL)
 * автоматическая регистрация может не происходить должным образом без явного вызова.
 * <p>
 * Данный компонент решает эту проблему, вызывая {@code registration.start()} после инициализации контекста.
 * <p>
 * Автор: MaDeLa
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class ConsulTtlHeartbeat {

    /**
     * Компонент, отвечающий за автоматическую регистрацию сервиса в Consul.
     * Инжектируется через Spring.
     */
    @Autowired
    private ConsulAutoServiceRegistration registration;

    /**
     * Метод вызывается после инициализации Spring-контекста.
     * Принудительно запускает регистрацию сервиса в Consul.
     */
    @PostConstruct
    public void forceRegister() {
        registration.start();
        System.out.println(">>> Consul registration started manually"); // лог для отладки
    }
}
