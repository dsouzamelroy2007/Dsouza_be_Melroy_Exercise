package com.ecore.roles.service;

import com.ecore.roles.client.TeamClient;
import com.ecore.roles.client.model.Team;
import com.ecore.roles.exception.ResourceNotFoundException;
import com.ecore.roles.service.impl.TeamServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static com.ecore.roles.utils.TestData.ORDINARY_CORAL_LYNX_TEAM;
import static com.ecore.roles.utils.TestData.ORDINARY_CORAL_LYNX_TEAM_UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {

    @InjectMocks
    private TeamServiceImpl teamService;
    @Mock
    private TeamClient teamClient;

    @Test
    void shouldGetTeamWhenTeamIdExists() {
        Team ordinaryCoralLynxTeam = ORDINARY_CORAL_LYNX_TEAM();

        when(teamClient.getTeam(ORDINARY_CORAL_LYNX_TEAM_UUID))
                .thenReturn(ResponseEntity
                        .status(HttpStatus.OK)
                        .body(ordinaryCoralLynxTeam));

        assertNotNull(teamService.getTeam(ORDINARY_CORAL_LYNX_TEAM_UUID));
    }

    @Test
    void shouldThrowExceptionWhenTeamIdDoesNotExists() {
        UUID teamId = UUID.fromString("7476a4bf-adfe-415c-941b-1739af07039b");

        when(teamClient.getTeam(any(UUID.class)))
                .thenReturn(ResponseEntity
                        .status(HttpStatus.OK)
                        .body(null));

        Exception exception =
                Assertions.assertThrows(ResourceNotFoundException.class, () -> teamService.getTeam(teamId));
        assertEquals("Team 7476a4bf-adfe-415c-941b-1739af07039b not found", exception.getMessage());
    }

    @Test
    void shouldGetAllTeams() {
        Team ordinaryCoralLynxTeam = ORDINARY_CORAL_LYNX_TEAM();

        when(teamClient.getTeams())
                .thenReturn(ResponseEntity
                        .status(HttpStatus.OK)
                        .body(List.of(ordinaryCoralLynxTeam)));

        List result = teamService.getTeams();
        assertNotNull(result);
        assertEquals(1, result.size());
    }
}
