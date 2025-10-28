package dev.madela.apteka.config;

import dev.madela.apteka.proto.inventory.InventoryServiceGrpc;
import dev.madela.apteka.proto.medication.MedicationServiceGrpc;
import dev.madela.apteka.proto.order.OrderServiceGrpc;
import dev.madela.apteka.proto.user.UserServiceGrpc;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация gRPC-клиентов (stubs) для взаимодействия с микросервисами через Consul.
 *
 * Каждый бин создаёт блокирующий gRPC-клиент (BlockingStub) и подключается по сервисному имени,
 * зарегистрированному в Consul (см. spring.application.name каждого микросервиса).
 *
 * Автор: MaDeLa
 */
@Configuration
@ConditionalOnProperty(
        name = "spring.cloud.consul.discovery.enabled",
        havingValue = "true",
        matchIfMissing = true
)
/*
 * Аннотация @ConditionalOnProperty позволяет включать конфигурацию только если discovery включён.
 * Это нужно для изоляции: если Consul выключен (например, в тестах) — бин не создаётся.
 */
public class GrpcConfig {

    /**
     * gRPC Stub для inventory-service.
     * Используется для получения и обновления остатков препаратов в аптеках.
     */
    @Bean
    public InventoryServiceGrpc.InventoryServiceBlockingStub inventoryStub(GrpcConsulChannelFactory channelFactory) {
        // Имя должно совпадать с spring.application.name у inventory-сервиса
        var channel = channelFactory.getChannel("inventory-service");
        return InventoryServiceGrpc.newBlockingStub(channel);
    }

    /**
     * gRPC Stub для order-service.
     * Служит для создания, получения и отмены заказов.
     */
    @Bean
    public OrderServiceGrpc.OrderServiceBlockingStub orderStub(GrpcConsulChannelFactory channelFactory) {
        var channel = channelFactory.getChannel("order-service");
        return OrderServiceGrpc.newBlockingStub(channel);
    }

    /**
     * gRPC Stub для medication-service.
     * Обрабатывает информацию о медикаментах, фильтрацию, обновление цен, удаление и т.д.
     */
    @Bean
    public MedicationServiceGrpc.MedicationServiceBlockingStub medicationStub(GrpcConsulChannelFactory channelFactory) {
        var channel = channelFactory.getChannel("medication-service");
        return MedicationServiceGrpc.newBlockingStub(channel);
    }

    /**
     * gRPC Stub для user-service.
     * Управляет пользователями, регистрацией, удалением, фильтрацией и т.д.
     */
    @Bean
    public UserServiceGrpc.UserServiceBlockingStub userStub(GrpcConsulChannelFactory channelFactory) {
        var channel = channelFactory.getChannel("user-service");
        return UserServiceGrpc.newBlockingStub(channel);
    }
}
