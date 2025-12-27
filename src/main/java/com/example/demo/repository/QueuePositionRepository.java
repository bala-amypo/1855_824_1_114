package com.example.demo.repository;

import com.example.demo.entity.QueuePosition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QueuePositionRepository extends JpaRepository<QueuePosition, Long> {
    QueuePosition findByToken_Id(Long tokenId);
}