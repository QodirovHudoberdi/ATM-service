package com.company.repository;

import com.company.entity.BankNoteType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BanknoteTypeRepository extends JpaRepository<BankNoteType,Long> {
}
