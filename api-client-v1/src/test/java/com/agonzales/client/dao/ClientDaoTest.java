package com.agonzales.client.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.agonzales.client.dao.repository.ClientRepository;
import com.agonzales.client.dto.domain.Client;
import com.agonzales.client.dto.domain.ClientStatus;
import com.agonzales.client.dto.domain.Gender;
import com.agonzales.client.dto.thridparty.db.ClientEntity;
import com.agonzales.client.dto.thridparty.db.PersonEntity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class ClientDaoTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientDao clientDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllClients_returnsListOfClients() {
        ClientEntity clientEntity1 = createMockClientEntity(1, "John Doe", "MALE", 30, "ACTIVE");
        ClientEntity clientEntity2 = createMockClientEntity(2, "Jane Doe", "FEMALE", 25, "ACTIVE");

        when(clientRepository.findAll()).thenReturn(Arrays.asList(clientEntity1, clientEntity2));

        List<Client> clients = clientDao.getAllClients();

        assertEquals(2, clients.size());
        assertEquals("John Doe", clients.get(0).getName());
        assertEquals("Jane Doe", clients.get(1).getName());
    }

    @Test
    void getClientById_returnsClient_whenClientExists() {
        ClientEntity clientEntity = createMockClientEntity(1, "John Doe", "MALE", 30, "ACTIVE");

        when(clientRepository.findById(1)).thenReturn(Optional.of(clientEntity));

        Optional<Client> client = clientDao.getClientById(1);

        assertTrue(client.isPresent());
        assertEquals("John Doe", client.get().getName());
    }

    @Test
    void getClientById_returnsEmpty_whenClientDoesNotExist() {
        when(clientRepository.findById(1)).thenReturn(Optional.empty());

        Optional<Client> client = clientDao.getClientById(1);

        assertFalse(client.isPresent());
    }

    @Test
    void createClient_savesAndReturnsClient() {
        Client client = createMockClient(1, "John Doe", Gender.MALE, 30, ClientStatus.ACTIVE);
        ClientEntity clientEntity = createMockClientEntity(1, "John Doe", "MALE", 30, "ACTIVE");

        when(clientRepository.save(any(ClientEntity.class))).thenReturn(clientEntity);

        Client result = clientDao.createClient(client);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        verify(clientRepository).save(any(ClientEntity.class));
    }

    @Test
    void updateClient_updatesAndReturnsClient() {
        Client client = createMockClient(1, "John Doe", Gender.MALE, 30, ClientStatus.ACTIVE);
        ClientEntity clientEntity = createMockClientEntity(1, "John Doe", "MALE", 30, "ACTIVE");

        when(clientRepository.save(any(ClientEntity.class))).thenReturn(clientEntity);

        Client result = clientDao.updateClient(client);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        verify(clientRepository).save(any(ClientEntity.class));
    }

    @Test
    void deleteClient_updatesClientStatusToInactive() {
        doNothing().when(clientRepository).updateStatusById(1, ClientStatus.INACTIVE.getStatus());

        clientDao.deleteClient(1);

        verify(clientRepository).updateStatusById(1, ClientStatus.INACTIVE.getStatus());
    }

    private Client createMockClient(Integer id, String name, Gender gender, int age, ClientStatus status) {
        Client client = new Client();
        client.setId(id);
        client.setName(name);
        client.setGender(gender);
        client.setAge(age);
        client.setStatus(status);
        client.setPersonId(id);
        return client;
    }

    private ClientEntity createMockClientEntity(Integer id, String name, String gender, int age, String status) {
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setId(id);
        clientEntity.setPassword("password");
        clientEntity.setStatus(status);

        PersonEntity personEntity = new PersonEntity();
        personEntity.setId(id);
        personEntity.setName(name);
        personEntity.setGender(gender);
        personEntity.setAge(age);
        personEntity.setAddress("123 Address");
        personEntity.setPhone("1234567890");

        clientEntity.setPerson(personEntity);
        return clientEntity;
    }
}
