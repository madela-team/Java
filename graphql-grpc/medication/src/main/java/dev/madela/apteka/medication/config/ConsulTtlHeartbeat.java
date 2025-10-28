package dev.madela.apteka.medication.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.consul.serviceregistry.ConsulAutoServiceRegistration;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурационный класс, отвечающий за принудительный запуск регистрации сервиса в Consul.
 * <p>
 * Используется в случаях, когда включена регистрация через TTL (Time-To-Live),
 * и необходимо вручную инициировать процесс регистрации после старта приложения.
 * Это особенно важно при использовании health-check'ов с типом `ttl` в Consul.
 * <p>
 * Автор: MaDeLa
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class ConsulTtlHeartbeat {

    /**
     * Компонент Spring Cloud, отвечающий за регистрацию сервиса в Consul.
     * Автоматически конфигурируется на основе application.yaml и запускается вручную.
     */
    @Autowired
    private ConsulAutoServiceRegistration registration;

    /**
     * Метод, вызываемый после инициализации Spring-контекста.
     * <p>
     * Запускает процесс регистрации сервиса в Consul вручную.
     * Это необходимо при использовании health-check'ов с типом TTL,
     * чтобы гарантировать отправку heartbeat сразу после старта.
     */
    @PostConstruct
    public void forceRegister() {
        registration.start(); // Запуск регистрации сервиса вручную
        System.out.println(">>> Consul registration started manually"); // В учебных целях — вывод в консоль
    }
}
