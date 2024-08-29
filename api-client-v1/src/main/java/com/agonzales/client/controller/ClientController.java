package com.agonzales.client.controller;

import com.agonzales.client.dto.controller.ClientCreateRequest;
import com.agonzales.client.dto.controller.ClientCreateResponse;
import com.agonzales.client.dto.controller.ClientResponse;
import com.agonzales.client.dto.controller.ClientUpdateRequest;
import com.agonzales.client.dto.domain.Client;
import com.agonzales.client.service.ClientService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/clientes")
public class ClientController {

    private final ClientService clientService;

    @GetMapping
    public List<ClientResponse> getAllClients() {
        return clientService.getAllClients().stream()
          .map(this::convertDomainToResponse)
          .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ClientResponse getClientById(@PathVariable Integer id) {
        return convertDomainToResponse(clientService.getClientById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClientCreateResponse createClient(@RequestBody ClientCreateRequest clientCreateRequest) {
        return convertDomainToCreateResponse(clientService.createClient(convertRequestToDomain(clientCreateRequest)));
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateClient(@PathVariable Integer id, @RequestBody ClientUpdateRequest clientUpdateRequest) {
        clientService.updateClient(convertRequestToDomain(id, clientUpdateRequest));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteClient(@PathVariable Integer id) {
        clientService.deleteClient(id);
    }

    private Client convertRequestToDomain(ClientCreateRequest clientCreateRequest) {
        Client client = new Client();
        client.setName(clientCreateRequest.getName());
        client.setGender(clientCreateRequest.getGender());
        client.setAge(clientCreateRequest.getAge());
        client.setAddress(clientCreateRequest.getAddress());
        client.setPhone(clientCreateRequest.getPhone());
        client.setPassword(clientCreateRequest.getPassword());
        client.setStatus(clientCreateRequest.getStatus());
        return client;
    }

    private ClientResponse convertDomainToResponse(Client client) {
        ClientResponse clientResponse = new ClientResponse();
        clientResponse.setId(client.getId());
        clientResponse.setPersonId(client.getPersonId());
        clientResponse.setName(client.getName());
        clientResponse.setGender(client.getGender());
        clientResponse.setAge(client.getAge());
        clientResponse.setAddress(client.getAddress());
        clientResponse.setPhone(client.getPhone());
        clientResponse.setPassword(client.getPassword());
        clientResponse.setStatus(client.getStatus().getStatus());
        return clientResponse;
    }

    private Client convertRequestToDomain(Integer id, ClientUpdateRequest clientUpdateRequest) {
        Client client = new Client();
        client.setId(id);
        client.setName(clientUpdateRequest.getName());
        client.setGender(clientUpdateRequest.getGender());
        client.setAge(clientUpdateRequest.getAge());
        client.setAddress(clientUpdateRequest.getAddress());
        client.setPhone(clientUpdateRequest.getPhone());
        client.setPassword(clientUpdateRequest.getPassword());
        client.setStatus(clientUpdateRequest.getStatus());
        return client;
    }

    private ClientCreateResponse convertDomainToCreateResponse(Client client) {
        ClientCreateResponse clientCreateResponse = new ClientCreateResponse();
        clientCreateResponse.setId(client.getId());
        return clientCreateResponse;
    }
}
