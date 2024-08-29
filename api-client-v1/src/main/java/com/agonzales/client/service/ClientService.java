package com.agonzales.client.service;

import com.agonzales.client.config.kafka.ClientInfoMessage;
import com.agonzales.client.dao.ClientDao;
import com.agonzales.client.dto.domain.Client;
import com.agonzales.client.exception.ClientNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class ClientService {

    private static final String CLIENT_INFO_GROUP_ID = "client-info-group-id";
    private static final String CLIENT_INFO_RESPONSE_TOPIC = "client-info-response-topic";
    private static final String CLIENT_INFO_REQUEST_TOPIC = "client-info-request-topic";

    private ClientDao                     clientDao;
    private KafkaTemplate<String, String> kafkaTemplate;
    private ObjectMapper objectMapper;

    public List<Client> getAllClients() {
        return clientDao.getAllClients();
    }

    public Client getClientById(Integer id) {
        return clientDao.getClientById(id)
          .orElseThrow(() -> new ClientNotFoundException("Client not found"));
    }

    public Client createClient(Client client) {
        return clientDao.createClient(client);
    }

    public Client updateClient(Client client) {
        Client currentClient = getClientById(client.getId());

        client.setName(client.getName() != null ? client.getName() : currentClient.getName());
        client.setGender(client.getGender() != null ? client.getGender() : currentClient.getGender());
        client.setAge(client.getAge() != null ? client.getAge() : currentClient.getAge());
        client.setAddress(client.getAddress() != null ? client.getAddress() : currentClient.getAddress());
        client.setPhone(client.getPhone() != null ? client.getPhone() : currentClient.getPhone());
        client.setPassword(client.getPassword() != null ? client.getPassword() : currentClient.getPassword());
        client.setStatus(client.getStatus() != null ? client.getStatus() : currentClient.getStatus());
        client.setPersonId(currentClient.getPersonId());

        return clientDao.updateClient(client);
    }

    public void deleteClient(Integer id) {
        getClientById(id);
        clientDao.deleteClient(id);
    }

    @KafkaListener(topics = CLIENT_INFO_REQUEST_TOPIC, groupId = CLIENT_INFO_GROUP_ID)
    public void processRequest(String message) {

        ClientInfoMessage clientInfoMessage = null;
        try {
            clientInfoMessage = objectMapper.readValue(message, ClientInfoMessage.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        try {
            Client client = getClientById(clientInfoMessage.getClientId());
            clientInfoMessage.setName(client.getName());
        } catch (ClientNotFoundException e) {
            clientInfoMessage.setError(true);
            clientInfoMessage.setErrorDetail(e.getMessage());
        }

        try {
            kafkaTemplate.send(CLIENT_INFO_RESPONSE_TOPIC, objectMapper.writeValueAsString(clientInfoMessage));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

