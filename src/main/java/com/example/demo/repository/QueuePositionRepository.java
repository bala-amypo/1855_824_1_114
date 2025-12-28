package com.example.demo.repository;

import com.example.demo.entity.QueuePosition;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface QueuePositionRepository extends JpaRepository<QueuePosition, Long> {
    // FIX: Added this method required by the tests
    Optional<QueuePosition> findByToken_Id(Long tokenId);
}