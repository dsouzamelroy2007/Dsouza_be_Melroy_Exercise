package com.ecore.roles.service.impl;

import com.ecore.roles.client.TeamClient;
import com.ecore.roles.client.model.Team;
import com.ecore.roles.exception.ResourceNotFoundException;
import com.ecore.roles.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TeamServiceImpl implements TeamService {

    private final TeamClient teamClient;

    @Autowired
    public TeamServiceImpl(TeamClient teamClient) {
        this.teamClient = teamClient;
    }

    public Team getTeam(UUID id) {
        Team team = teamClient.getTeam(id).getBody();
        if (team == null) {
            throw new ResourceNotFoundException(Team.class, id);
        }
        return team;
    }

    public List<Team> getTeams() {
        return teamClient.getTeams().getBody();
    }
}
