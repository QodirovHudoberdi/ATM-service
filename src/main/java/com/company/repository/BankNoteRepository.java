package com.company.repository;

import com.company.entity.BankNote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BankNoteRepository extends JpaRepository<BankNote , Long> {
    BankNote findByAmount(Integer amount);
    List<BankNote> findAllByCurrency_IdOrderByAmount(Long currencyId);
    List<BankNote> findAllByType_IdOrderByAmount(Long typeId);

}
