package com.agonzales.client.dao.repository;

import com.agonzales.client.dto.domain.ClientStatus;
import com.agonzales.client.dto.thridparty.db.ClientEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ClientRepository extends JpaRepository<ClientEntity, Integer> {

    @Query("SELECT c FROM ClientEntity c WHERE c.status != 'Inactive'")
    List<ClientEntity> findAllActiveClients();

    @Modifying
    @Transactional
    @Query("UPDATE ClientEntity c SET c.status = :status WHERE c.id = :id")
    void updateStatusById(Integer id, String status);

}
