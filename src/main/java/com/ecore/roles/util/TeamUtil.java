package com.ecore.roles.util;

import com.ecore.roles.client.model.Team;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class TeamUtil {

    public static boolean isUserATeamMember(Team team, UUID userId) {
        Set<UUID> teamMembers = new HashSet<>(team.getTeamMemberIds());
        teamMembers.add(team.getTeamLeadId());
        return teamMembers.contains(userId);
    }
}
