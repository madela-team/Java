//package dev.madela.apteka.resolver;
//
//import com.netflix.graphql.dgs.DgsQueryExecutor;
//import com.netflix.graphql.dgs.test.EnableDgsMockMvcTest;
//import com.netflix.graphql.dgs.test.EnableDgsTest;
//import org.intellij.lang.annotations.Language;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.Import;
//import dev.madela.apteka.GrpcTestConfig;
//import dev.madela.apteka.config.GraphQLScalarConfiguration;
//import dev.madela.apteka.service.InventoryService;
//import dev.madela.apteka.model.PharmacyStock;
//import org.junit.jupiter.api.Test;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.anyString;
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
//class InventoryResolverTest {
//
//    @MockitoBean
//    private InventoryService inventoryService;
//
//    @Autowired
//    private DgsQueryExecutor dgsQueryExecutor;
//
//    @Test
//    void testStockByMedication() {
//        when(inventoryService.getStock(anyString()))
//                .thenReturn(List.of(
//                        PharmacyStock.builder()
//                                .pharmacyId("pharmacy-1")
//                                .medicationId("med-1")
//                                .quantity(25)
//                                .build()
//                ));
//
//        @Language("GraphQL") String query = """
//            query {
//                stockByMedication(medicationId: "med-1") {
//                    pharmacyId
//                    quantity
//                }
//            }
//        """;
//
//        List<Integer> quantities = dgsQueryExecutor.executeAndExtractJsonPath(
//            query, "data.stockByMedication[*].quantity"
//        );
//
//        assertThat(quantities).containsExactly(25);
//    }
//}
