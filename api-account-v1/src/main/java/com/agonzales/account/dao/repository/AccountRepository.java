package com.agonzales.account.dao.repository;

import com.agonzales.account.dto.thridparty.db.AccountEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<AccountEntity, Integer> {

    @Query("SELECT c FROM AccountEntity c WHERE c.status != 'INACTIVO'")
    List<AccountEntity> findAllActiveClients();

    @Modifying
    @Transactional
    @Query("UPDATE AccountEntity c SET c.status = :status WHERE c.id = :id")
    void updateStatusById(Integer id, String status);

    List<AccountEntity> findAllByClientId(Integer clientId);

    Optional<AccountEntity> findByAccountNumber(String accountNumber);

    boolean existsByAccountNumber(String accountNumber);
}
