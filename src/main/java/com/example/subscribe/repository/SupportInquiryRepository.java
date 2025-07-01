package com.example.subscribe.repository;

import com.example.subscribe.entity.SupportInquiry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupportInquiryRepository extends JpaRepository<SupportInquiry, Long> {
    List<SupportInquiry> findByUserId(Long userId);

    long countByAnswerIsNull();

    List<SupportInquiry> findByAnswerIsNull();

}
