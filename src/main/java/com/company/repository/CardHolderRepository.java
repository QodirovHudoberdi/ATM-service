package com.company.repository;

import com.company.entity.CardHolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardHolderRepository extends JpaRepository<CardHolder,Long> {
   Optional<CardHolder> findBySeriesNum(String seriesNum);
}
