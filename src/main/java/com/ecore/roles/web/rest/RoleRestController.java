package com.ecore.roles.web.rest;

import com.ecore.roles.model.Role;
import com.ecore.roles.service.RoleService;
import com.ecore.roles.web.RoleApi;
import com.ecore.roles.web.dto.RoleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.ecore.roles.web.dto.RoleDto.fromModel;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/roles")
public class RoleRestController implements RoleApi {

    private final RoleService roleService;

    @Override
    @PutMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<RoleDto> createRole(
            @Valid @RequestBody RoleDto role) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(fromModel(roleService.createRole(role.toModel())));
    }

    @Override
    @GetMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<RoleDto>> getRoles() {

        List<Role> getRoles = roleService.getRoles();

        List<RoleDto> roleDtoList = new ArrayList<>();

        for (Role role : getRoles) {
            RoleDto roleDto = fromModel(role);
            roleDtoList.add(roleDto);
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(roleDtoList);
    }

    @Override
    @GetMapping(
            path = "/{roleId}",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<RoleDto> getRole(
            @PathVariable @NotNull UUID roleId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(fromModel(roleService.getRole(roleId)));
    }

    @Override
    @GetMapping(
            path = "/search",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<RoleDto>> getRolesByUserAndTeam(
            @RequestParam @NotNull UUID userId,
            @RequestParam @NotNull UUID teamId) {
        Collection<Role> roles = roleService.getRolesByUserIdAndTeamId(userId, teamId);
        List<RoleDto> result = roles
                .stream()
                .map(role -> fromModel(role))
                .collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

}
