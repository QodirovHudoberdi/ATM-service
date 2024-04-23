package com.company.repository;

import com.company.entity.HistoryCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoryCardRepository extends JpaRepository<HistoryCard,Long> {
    List<HistoryCard> findAllByFromCard_CardNumberAndToCard_CardNumber(String fromCardNumber,String toCardNumber);
    List<HistoryCard>findAllByFromCard_CardNumber(String fromCArdNumber);
    List<HistoryCard>findAllByToCard_CardNumber(String fromCArdNumber);
    List<HistoryCard> findAllByFromCard_CardNumberOrToCard_CardNumber(String toCardNumber,String fromCardNumber);
}