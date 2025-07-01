package com.example.subscribe.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupportInquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String question;
    private String answer;

    private LocalDateTime createdAt;
    private LocalDateTime answeredAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
