package dev.madela.reminder.repo;

import dev.madela.reminder.entity.ReminderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReminderRepository extends JpaRepository<ReminderEntity, Long> {
    List<ReminderEntity> findByUserId(Long userId);
}
