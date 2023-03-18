package com.ecore.roles.web.rest;

import com.ecore.roles.service.TeamService;
import com.ecore.roles.web.TeamApi;
import com.ecore.roles.web.dto.TeamDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.ecore.roles.web.dto.TeamDto.fromModel;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/teams")
public class TeamRestController implements TeamApi {

    private final TeamService teamService;

    @Override
    @GetMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<TeamDto>> getTeams() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(teamService.getTeams().stream()
                        .map(TeamDto::fromModel)
                        .collect(Collectors.toList()));
    }

    @Override
    @GetMapping(
            path = "/{teamId}",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<TeamDto> getTeam(
            @NotNull @PathVariable UUID teamId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(fromModel(teamService.getTeam(teamId)));
    }

}
