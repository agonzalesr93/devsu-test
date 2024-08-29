package com.agonzales.account.dao.repository;

import com.agonzales.account.dto.thridparty.db.TransactionEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Integer>, PagingAndSortingRepository<TransactionEntity, Integer> {

    List<TransactionEntity> findByAccountIdOrderByIdDesc(Integer accountId);

    Optional<TransactionEntity> findByAccountIdAndId(Integer accountId, Integer id);

    @Query("SELECT c FROM TransactionEntity c WHERE c.account.id IN :accountIds AND c.date BETWEEN :dateFrom AND :dateTo ORDER BY c.date DESC")
    Page<TransactionEntity> findByFilters(List<Integer> accountIds, LocalDateTime dateFrom, LocalDateTime dateTo, Pageable pageable);
}
