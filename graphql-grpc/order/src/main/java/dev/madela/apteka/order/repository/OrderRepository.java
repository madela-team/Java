package dev.madela.apteka.order.repository;

import dev.madela.apteka.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findByUserIdOrderByCreatedAtDesc(String userId);

    @Query("SELECT COALESCE(SUM(oi.quantity), 0) " +
            "FROM Order o JOIN o.items oi " +
            "WHERE o.userId = :userId AND o.status != 'CANCELLED'")
    Integer sumQuantityByUserId(@Param("userId") String userId);
}
