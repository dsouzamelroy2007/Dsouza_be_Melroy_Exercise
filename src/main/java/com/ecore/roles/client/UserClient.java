package com.ecore.roles.client;

import com.ecore.roles.client.model.User;
import com.ecore.roles.configuration.ClientConfigurationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class UserClient {

    private final RestTemplate restTemplate;
    private final ClientConfigurationProperties clientConfigurationProperties;

    public ResponseEntity<User> getUser(UUID id) {
        return restTemplate.exchange(
                clientConfigurationProperties.getUsersApiHost() + "/" + id,
                HttpMethod.GET,
                null,
                User.class);
    }

    public ResponseEntity<List<User>> getUsers() {
        return restTemplate.exchange(
                clientConfigurationProperties.getUsersApiHost(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {});
    }
}
