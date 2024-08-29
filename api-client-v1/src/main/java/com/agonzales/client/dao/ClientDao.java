package com.agonzales.client.dao;

import com.agonzales.client.dao.repository.ClientRepository;
import com.agonzales.client.dto.domain.Client;
import com.agonzales.client.dto.domain.ClientStatus;
import com.agonzales.client.dto.domain.Gender;
import com.agonzales.client.dto.thridparty.db.ClientEntity;
import com.agonzales.client.dto.thridparty.db.PersonEntity;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ClientDao {

    private ClientRepository clientRepository;

    public List<Client> getAllClients() {
        return clientRepository.findAll().stream()
          .map(this::convertEntityToDomain)
          .collect(Collectors.toList());
    }

    public Optional<Client> getClientById(Integer id) {
        return clientRepository.findById(id)
          .map(this::convertEntityToDomain);
    }

    public Client createClient(Client client) {
        ClientEntity clientEntity = convertDomainToEntity(client);
        return convertEntityToDomain(clientRepository.save(clientEntity));
    }

    public Client updateClient(Client client) {
        ClientEntity clientEntity = convertDomainToEntity(client);
        return convertEntityToDomain(clientRepository.save(clientEntity));
    }

    public void deleteClient(Integer id) {
        clientRepository.updateStatusById(id, ClientStatus.INACTIVE.getStatus());
    }

    private Client convertEntityToDomain(ClientEntity clientEntity) {
        Client client = new Client();
        client.setId(clientEntity.getId());
        client.setPassword(clientEntity.getPassword());
        client.setStatus(ClientStatus.fromString(clientEntity.getStatus()));
        client.setPersonId(clientEntity.getPerson().getId());
        client.setName(clientEntity.getPerson().getName());
        client.setGender(Gender.fromString(clientEntity.getPerson().getGender()));
        client.setAge(clientEntity.getPerson().getAge());
        client.setAddress(clientEntity.getPerson().getAddress());
        client.setPhone(clientEntity.getPerson().getPhone());
        return client;
    }

    private ClientEntity convertDomainToEntity(Client client) {
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setId(client.getId());
        clientEntity.setPassword(client.getPassword());
        clientEntity.setStatus(client.getStatus().getStatus());
        PersonEntity personEntity = new PersonEntity();
        personEntity.setId(client.getPersonId());
        personEntity.setName(client.getName());
        personEntity.setGender(client.getGender().getValue());
        personEntity.setAge(client.getAge());
        personEntity.setAddress(client.getAddress());
        personEntity.setPhone(client.getPhone());
        clientEntity.setPerson(personEntity);
        return clientEntity;
    }
}
