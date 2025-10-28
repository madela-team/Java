//package dev.madela.apteka.resolver;
//
////import com.netflix.graphql.dgs.DgsQueryExecutor;
////import com.netflix.graphql.dgs.autoconfig.DgsAutoConfiguration;
//
//import com.netflix.graphql.dgs.DgsQueryExecutor;
//import com.netflix.graphql.dgs.test.EnableDgsMockMvcTest;
//import dev.madela.apteka.GrpcTestConfig;
//import dev.madela.apteka.config.GraphQLScalarConfiguration;
//import dev.madela.apteka.model.Medication;
//import dev.madela.apteka.service.MedicationService;
//import org.intellij.lang.annotations.Language;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//
//import java.util.List;
//
//import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
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
//class MedicationResolverTest {
//    @MockitoBean
//    private MedicationService medicationService;
//
//    @Autowired
//    private DgsQueryExecutor dgsQueryExecutor;
//
//    @Test
//    void testMedicationsQuery() {
//        when(medicationService.getMedications(any(), any()))
//            .thenReturn(List.of(Medication.builder()
//                            .id("1")
//                            .name("Парацетамол")
//                            .price(100.0)
//                    .build()
//            ));
//
//        @Language("GraphQL") String query = """
//            query {
//                medications(filter: { category: "Антибиотики" }) {
//                    id
//                    name
//                }
//            }
//        """;
//
//        List<String> names = dgsQueryExecutor.executeAndExtractJsonPath(
//            query, "data.medications[*].name"
//        );
//
//        assertThat(names).containsExactly("Парацетамол");
//    }
//}