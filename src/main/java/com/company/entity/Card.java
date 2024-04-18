package com.company.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @Column(name = "card_holder_id")
    private CardHolder cardHolder;
    @Column(name = "expired_date")
    private LocalDate localDate=LocalDate.now().plusYears(5);
    @Column(name = "balance")
    private Float balance;
    @Column (name = "is_active")
    private Boolean isActive =true;
    @Column (name = "card_number")
    @Size(max = 16,min = 16)
    private String cardNumber;
    @Column (name = "card_type")
    private String cardType;

}
