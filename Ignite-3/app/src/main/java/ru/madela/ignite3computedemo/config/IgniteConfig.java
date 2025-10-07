package ru.madela.ignite3computedemo.config;

import lombok.extern.log4j.Log4j2;
import org.apache.ignite.client.IgniteClient;
import org.apache.ignite.table.RecordView;
import org.apache.ignite.table.Table;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.madela.ignite3computedemo.model.TransactionModel;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Log4j2
@Configuration
public class IgniteConfig {

    @Value("${ignite.address}")
    private String igniteAddress;

    @Bean
    public IgniteClient igniteClient() {
        return IgniteClient.builder()
                .addresses(igniteAddress)
                .build();
    }

    @Bean
    public RecordView<TransactionModel> transactionRecordView(IgniteClient client) {
        client.catalog().createTable(TransactionModel.class);
        Table t = client.tables().table("tx");
        RecordView<TransactionModel> transactionModelRecordView = t.recordView(TransactionModel.class);
        fillTable(transactionModelRecordView);
        return transactionModelRecordView;
    }

    private void fillTable(RecordView<TransactionModel> transactionModelRecordView) {
        UUID uuidTrue = UUID.randomUUID();
        UUID uuidFalse = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        log.info("uuid for true: {}", uuidTrue);
        log.info("uuid for false: {}", uuidFalse);
        List<TransactionModel> batch = List.of(
                new TransactionModel()
                        .setId(UUID.randomUUID())
                        .setAccountId(uuidTrue)
                        .setAmount(new BigDecimal("12"))
                        .setDateTime(now),
                new TransactionModel()
                        .setId(UUID.randomUUID())
                        .setAccountId(uuidTrue)
                        .setAmount(new BigDecimal("13"))
                        .setDateTime(now.plusSeconds(2)),
                new TransactionModel()
                        .setId(UUID.randomUUID())
                        .setAccountId(uuidTrue)
                        .setAmount(new BigDecimal("14"))
                        .setDateTime(now.plusSeconds(4)),
                new TransactionModel()
                        .setId(UUID.randomUUID())
                        .setAccountId(uuidFalse)
                        .setAmount(new BigDecimal("12"))
                        .setDateTime(now),
                new TransactionModel()
                        .setId(UUID.randomUUID())
                        .setAccountId(uuidFalse)
                        .setAmount(new BigDecimal("13"))
                        .setDateTime(now.plusSeconds(31)),
                new TransactionModel()
                        .setId(UUID.randomUUID())
                        .setAccountId(uuidFalse)
                        .setAmount(new BigDecimal("14"))
                        .setDateTime(now.plusSeconds(62))
        );
        transactionModelRecordView.upsertAll(null, batch);
    }

}
