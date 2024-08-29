package com.agonzales.client.service;

import com.agonzales.client.config.kafka.ClientInfoMessage;
import com.agonzales.client.dao.ClientDao;
import com.agonzales.client.dto.domain.Client;
import com.agonzales.client.exception.ClientNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ClientServiceTest {

    @Mock
    private ClientDao clientDao;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private ClientService clientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllClients_returnsClientsList() {
        clientService.getAllClients();
        verify(clientDao).getAllClients();
    }

    @Test
    void getClientById_returnsClient_whenClientExists() {
        Client client = new Client();
        client.setId(1);
        when(clientDao.getClientById(1)).thenReturn(Optional.of(client));

        Client result = clientService.getClientById(1);
        assertNotNull(result);
        assertEquals(client.getId(), result.getId());
    }

    @Test
    void getClientById_throwsClientNotFoundException_whenClientDoesNotExist() {
        when(clientDao.getClientById(1)).thenReturn(Optional.empty());
        assertThrows(ClientNotFoundException.class, () -> clientService.getClientById(1));
    }

    @Test
    void createClient_savesAndReturnsClient() {
        Client client = new Client();
        when(clientDao.createClient(client)).thenReturn(client);

        Client result = clientService.createClient(client);
        assertEquals(client, result);
        verify(clientDao).createClient(client);
    }

    @Test
    void updateClient_updatesAndReturnsClient() {
        Client currentClient = new Client();
        currentClient.setId(1);
        currentClient.setName("John Doe");
        when(clientDao.getClientById(1)).thenReturn(Optional.of(currentClient));

        Client updatedClient = new Client();
        updatedClient.setId(1);
        updatedClient.setName("Jane Doe");

        when(clientDao.updateClient(updatedClient)).thenReturn(updatedClient);

        Client result = clientService.updateClient(updatedClient);
        assertEquals("Jane Doe", result.getName());
        verify(clientDao).updateClient(updatedClient);
    }

    @Test
    void deleteClient_deletesClient_whenClientExists() {
        Client client = new Client();
        client.setId(1);
        when(clientDao.getClientById(1)).thenReturn(Optional.of(client));

        clientService.deleteClient(1);
        verify(clientDao).deleteClient(1);
    }

    @Test
    void processRequest_sendsMessage_whenClientExists() throws JsonProcessingException {
        Client client = new Client();
        client.setId(1);
        client.setName("John Doe");
        when(clientDao.getClientById(1)).thenReturn(Optional.of(client));

        ClientInfoMessage clientInfoMessage = new ClientInfoMessage();
        clientInfoMessage.setClientId(1);
        when(objectMapper.readValue(any(String.class), eq(ClientInfoMessage.class)))
          .thenReturn(clientInfoMessage);
        when(objectMapper.writeValueAsString(any(ClientInfoMessage.class)))
          .thenReturn("mockedMessage");

        clientService.processRequest("mockedRequest");

        verify(kafkaTemplate).send(eq("client-info-response-topic"), eq("mockedMessage"));
        verify(objectMapper).writeValueAsString(any(ClientInfoMessage.class));
    }

    @Test
    void processRequest_sendsError_whenClientDoesNotExist() throws JsonProcessingException {
        when(clientDao.getClientById(1)).thenReturn(Optional.empty());

        ClientInfoMessage clientInfoMessage = new ClientInfoMessage();
        clientInfoMessage.setClientId(1);
        when(objectMapper.readValue(any(String.class), eq(ClientInfoMessage.class)))
          .thenReturn(clientInfoMessage);
        when(objectMapper.writeValueAsString(any(ClientInfoMessage.class)))
          .thenReturn("mockedErrorMessage");

        clientService.processRequest("mockedRequest");

        ArgumentCaptor<ClientInfoMessage> captor = ArgumentCaptor.forClass(ClientInfoMessage.class);
        verify(objectMapper).writeValueAsString(captor.capture());
        ClientInfoMessage capturedMessage = captor.getValue();

        assertTrue(capturedMessage.isError());
        assertNotNull(capturedMessage.getErrorDetail());

        verify(kafkaTemplate).send(eq("client-info-response-topic"), eq("mockedErrorMessage"));
    }
}