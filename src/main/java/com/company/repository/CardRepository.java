package com.company.repository;

import com.company.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card,Long> {
  List<Card> getAllByCardHolder_Pinfl(String pinfl);
  Card findByCardNumber(String cardNumber);
}
