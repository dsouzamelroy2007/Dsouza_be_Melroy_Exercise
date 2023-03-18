package com.ecore.roles.web.rest;

import com.ecore.roles.client.model.Team;
import com.ecore.roles.client.model.User;
import com.ecore.roles.exception.InvalidArgumentException;
import com.ecore.roles.model.Membership;
import com.ecore.roles.service.MembershipService;
import com.ecore.roles.service.TeamService;
import com.ecore.roles.service.UserService;
import com.ecore.roles.web.MembershipApi;
import com.ecore.roles.web.dto.MembershipDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.*;

import static com.ecore.roles.util.TeamUtil.isUserATeamMember;
import static com.ecore.roles.web.dto.MembershipDto.fromModel;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/roles/memberships")
public class MembershipRestController implements MembershipApi {

    private final MembershipService membershipService;

    private final TeamService teamService;

    private final UserService userService;

    @Override
    @PutMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<MembershipDto> assignRoleToMembership(
            @NotNull @Valid @RequestBody MembershipDto membershipDto) {
        Team team = teamService.getTeam(membershipDto.getTeamId());
        User user = userService.getUser(membershipDto.getUserId());
        if (!isUserATeamMember(team, user.getId())) {
            throw new InvalidArgumentException(Membership.class,
                    "The provided user doesn't belong to the provided team.");
        }
        Membership membership = membershipService.assignRoleToMembership(membershipDto.toModel());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(fromModel(membership));
    }

    @Override
    @GetMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<MembershipDto>> getMembershipsByRole(
            @RequestParam UUID roleId) {
        List<Membership> memberships = membershipService.getMemberships(roleId);
        List<MembershipDto> newMembershipDto = new ArrayList<>();

        for (Membership membership : memberships) {
            MembershipDto membershipDto = fromModel(membership);
            newMembershipDto.add(membershipDto);
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(newMembershipDto);
    }

}
