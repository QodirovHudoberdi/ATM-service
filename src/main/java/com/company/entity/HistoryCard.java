package com.company.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class HistoryCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "toCard_id")
    private Card toCard;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fromCard_id")
    private Card fromCard;
    @Column(name = "date")
    private LocalDateTime localDateTime=LocalDateTime.now();
    @Column(name = "amount")
    private Float amount;
    @Column
    private Float commission;
}
