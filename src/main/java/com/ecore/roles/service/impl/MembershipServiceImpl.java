package com.ecore.roles.service.impl;

import com.ecore.roles.client.model.Team;
import com.ecore.roles.client.model.User;
import com.ecore.roles.exception.InvalidArgumentException;
import com.ecore.roles.exception.ResourceExistsException;
import com.ecore.roles.exception.ResourceNotFoundException;
import com.ecore.roles.model.Membership;
import com.ecore.roles.model.Role;
import com.ecore.roles.repository.MembershipRepository;
import com.ecore.roles.repository.RoleRepository;
import com.ecore.roles.service.MembershipService;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static java.util.Optional.ofNullable;

@Log4j2
@Service
public class MembershipServiceImpl implements MembershipService {

    private final MembershipRepository membershipRepository;
    private final RoleRepository roleRepository;

    public MembershipServiceImpl(
            MembershipRepository membershipRepository,
            RoleRepository roleRepository) {
        this.membershipRepository = membershipRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public Membership assignRoleToMembership(@NonNull Membership membership) {

        UUID roleId = ofNullable(membership.getRole()).map(Role::getId)
                .orElseThrow(() -> new InvalidArgumentException(Role.class));
        UUID teamId = ofNullable(membership.getTeamId())
                .orElseThrow(() -> new InvalidArgumentException(Team.class));
        UUID userId = ofNullable(membership.getUserId())
                .orElseThrow(() -> new InvalidArgumentException(User.class));
        if (membershipRepository.findByUserIdAndTeamId(userId, teamId)
                .isPresent()) {
            throw new ResourceExistsException(Membership.class);
        }

        roleRepository.findById(roleId).orElseThrow(() -> new ResourceNotFoundException(Role.class, roleId));
        return membershipRepository.save(membership);
    }

    @Override
    public List<Membership> getMemberships(@NonNull UUID roleId) {
        return membershipRepository.findByRoleId(roleId);
    }
}
