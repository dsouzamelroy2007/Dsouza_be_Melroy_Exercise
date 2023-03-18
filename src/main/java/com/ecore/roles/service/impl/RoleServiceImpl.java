package com.ecore.roles.service.impl;

import com.ecore.roles.exception.ResourceExistsException;
import com.ecore.roles.exception.ResourceNotFoundException;
import com.ecore.roles.model.Membership;
import com.ecore.roles.model.Role;
import com.ecore.roles.repository.MembershipRepository;
import com.ecore.roles.repository.RoleRepository;
import com.ecore.roles.service.RoleService;
import com.ecore.roles.service.TeamService;
import com.ecore.roles.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Log4j2
@Service
public class RoleServiceImpl implements RoleService {

    public static final String DEFAULT_ROLE = "Developer";

    private final RoleRepository roleRepository;
    private final MembershipRepository membershipRepository;

    private final TeamService teamService;

    private final UserService userService;

    public RoleServiceImpl(
            RoleRepository roleRepository,
            MembershipRepository membershipRepository,
            TeamService teamService,
            UserService userService) {
        this.roleRepository = roleRepository;
        this.membershipRepository = membershipRepository;
        this.teamService = teamService;
        this.userService = userService;
    }

    @Override
    public Role createRole(Role r) {
        if (roleRepository.findByName(r.getName()).isPresent()) {
            throw new ResourceExistsException(Role.class);
        }
        return roleRepository.save(r);
    }

    @Override
    public Role getRole(UUID rid) {
        return roleRepository.findById(rid)
                .orElseThrow(() -> new ResourceNotFoundException(Role.class, rid));
    }

    @Override
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Collection<Role> getRolesByUserIdAndTeamId(UUID userId, UUID teamId) {
        isUserAndTeamExist(userId, teamId);
        List<Membership> memberships = membershipRepository.findAll();
        return memberships.stream()
                .filter(x -> x.getUserId().equals(userId) || x.getTeamId().equals(teamId))
                .map(x -> x.getRole())
                .collect(Collectors.toMap(Role::getId, r -> r, (r, s) -> r)).values();
    }

    private boolean isUserAndTeamExist(UUID userId, UUID teamId) {
        return teamService.getTeam(teamId) != null && userService.getUser(userId) != null;
    }

    private Role getDefaultRole() {
        return roleRepository.findByName(DEFAULT_ROLE)
                .orElseThrow(() -> new IllegalStateException("Default role is not configured"));
    }
}
