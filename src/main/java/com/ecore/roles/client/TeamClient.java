package com.ecore.roles.client;

import com.ecore.roles.client.model.Team;
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
public class TeamClient {

    private final RestTemplate restTemplate;
    private final ClientConfigurationProperties clientConfigurationProperties;

    public ResponseEntity<Team> getTeam(UUID id) {
        return restTemplate.exchange(
                clientConfigurationProperties.getTeamsApiHost() + "/" + id,
                HttpMethod.GET,
                null,
                Team.class);
    }

    public ResponseEntity<List<Team>> getTeams() {
        return restTemplate.exchange(
                clientConfigurationProperties.getTeamsApiHost(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {});
    }
}
