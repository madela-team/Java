package ru.madela.ignite3computedemo.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.ignite.catalog.annotations.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Table(
        value = "tx",
        zone = @Zone(
                value = "zone_tx",
                replicas = 1,
                partitions = 16,
                storageProfiles = "default"
        ),
        colocateBy = { @ColumnRef("account_id") },
        indexes = {
                @Index(
                        value = "ix_tx_acc_dt",
                        columns = { @ColumnRef("account_id"), @ColumnRef("dt") }
                )
        }
)
@Accessors(chain = true)
public class TransactionModel {

    @Id
    private UUID id;

    @Id
    @Column("account_id")
    private UUID accountId;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(value = "dt", nullable = false)
    private LocalDateTime dateTime;
}
