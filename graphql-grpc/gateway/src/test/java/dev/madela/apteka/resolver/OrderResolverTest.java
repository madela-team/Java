//package dev.madela.apteka.resolver;
//
//import com.netflix.graphql.dgs.DgsQueryExecutor;
//import com.netflix.graphql.dgs.test.EnableDgsMockMvcTest;
//import dev.madela.apteka.GrpcTestConfig;
//import dev.madela.apteka.config.GraphQLScalarConfiguration;
//import dev.madela.apteka.model.Order;
//import dev.madela.apteka.model.OrderItem;
//import dev.madela.apteka.service.OrderService;
//import org.intellij.lang.annotations.Language;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//
//@SpringBootTest(
//        classes = {GrpcTestConfig.class, GraphQLScalarConfiguration.class},
//        properties = {
//                "grpc.server.enabled=false",
//                "spring.autoconfigure.exclude=org.springframework.grpc.server.autoconfigure.GrpcServerAutoConfiguration"
//        }
//)
//@EnableDgsMockMvcTest
//@ActiveProfiles("test")
//@Import({GraphQLScalarConfiguration.class, GrpcTestConfig.class})
//class OrderResolverTest {
//
//    @MockitoBean
//    private OrderService orderService;
//
//    @Autowired
//    private DgsQueryExecutor dgsQueryExecutor;
//
//    @Test
//    void testOrdersByUser() {
//        when(orderService.getOrdersByUser("user-1"))
//                .thenReturn(List.of(
//                        Order.builder()
//                                .id("order-1")
//                                .userId("user-1")
//                                .status("NEW")
//                                .build()
//                ));
//
//        @Language("GraphQL") String query = """
//            query {
//                orders(userId: "user-1") {
//                    id
//                    status
//                }
//            }
//        """;
//
//        List<String> statuses = dgsQueryExecutor.executeAndExtractJsonPath(
//                query, "data.orders[*].status"
//        );
//
//        assertThat(statuses).containsExactly("NEW");
//    }
//
//    @Test
//    void testCreateOrder() {
//        Order inputOrder = Order.builder()
//                .id("order-1")
//                .userId("user-1")
//                .status("NEW")
//                .createdAt(LocalDateTime.now())
//                .items(List.of(
//                        OrderItem.builder()
//                                .medicationId("med-1")
//                                .quantity(2)
//                                .build()
//                ))
//                .build();
//
//        when(orderService.createOrder(any())).thenReturn(inputOrder);
//
//        @Language("GraphQL") String mutation = """
//        mutation {
//            createOrder(input: {
//                userId: "user-1",
//                items: [{ medicationId: "med-1", quantity: 2 }],
//                deliveryAddress: "ул. Ленина, д.1"
//            }) {
//                id
//                status
//                items {
//                    medicationId
//                    quantity
//                }
//            }
//        }
//    """;
//
//        String status = dgsQueryExecutor.executeAndExtractJsonPath(
//                mutation, "data.createOrder.status"
//        );
//
//        assertThat(status).isEqualTo("NEW");
//    }
//}
