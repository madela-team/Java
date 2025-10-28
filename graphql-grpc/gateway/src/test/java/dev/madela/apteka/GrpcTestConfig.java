package dev.madela.apteka;

import dev.madela.apteka.proto.inventory.InventoryServiceGrpc;
import dev.madela.apteka.proto.medication.MedicationServiceGrpc;
import dev.madela.apteka.proto.order.OrderServiceGrpc;
import dev.madela.apteka.proto.user.UserServiceGrpc;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.io.IOException;
import java.net.ServerSocket;

@TestConfiguration
public class GrpcTestConfig {

    @DynamicPropertySource
    static void registerGrpcPort(DynamicPropertyRegistry registry) {
        registry.add("grpc.server.port", GrpcTestConfig::findAvailablePort);
    }

    private static int findAvailablePort() {
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        } catch (IOException e) {
            throw new IllegalStateException("Unable to find available port", e);
        }
    }

    @Bean
    public InventoryServiceGrpc.InventoryServiceBlockingStub inventoryStub() {
        return Mockito.mock(InventoryServiceGrpc.InventoryServiceBlockingStub.class);
    }
    @Bean
    public MedicationServiceGrpc.MedicationServiceBlockingStub medicationStub() {
        return Mockito.mock(MedicationServiceGrpc.MedicationServiceBlockingStub.class);
    }
    @Bean
    public OrderServiceGrpc.OrderServiceBlockingStub orderStub() {
        return Mockito.mock(OrderServiceGrpc.OrderServiceBlockingStub.class);
    }
    @Bean
    public UserServiceGrpc.UserServiceBlockingStub userStub() {
        return Mockito.mock(UserServiceGrpc.UserServiceBlockingStub.class);
    }
}
