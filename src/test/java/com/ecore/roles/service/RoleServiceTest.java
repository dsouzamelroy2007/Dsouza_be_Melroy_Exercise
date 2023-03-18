package com.ecore.roles.service;

import com.ecore.roles.client.model.User;
import com.ecore.roles.exception.ResourceNotFoundException;
import com.ecore.roles.model.Membership;
import com.ecore.roles.model.Role;
import com.ecore.roles.repository.MembershipRepository;
import com.ecore.roles.repository.RoleRepository;
import com.ecore.roles.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.ecore.roles.utils.TestData.*;
import static java.lang.String.format;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @InjectMocks
    private RoleServiceImpl roleService;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private MembershipRepository membershipRepository;

    @Mock
    private TeamService teamService;

    @Mock
    private UserService userService;

    @Test
    public void shouldCreateRole() {
        Role developerRole = DEVELOPER_ROLE();

        when(roleRepository.save(developerRole)).thenReturn(developerRole);

        Role role = roleService.createRole(developerRole);
        assertNotNull(role);
        assertEquals(developerRole, role);
    }

    @Test
    public void shouldFailToCreateRoleWhenRoleIsNull() {
        assertThrows(NullPointerException.class,
                () -> roleService.createRole(null));
    }

    @Test
    public void shouldReturnRoleWhenRoleIdExists() {
        Role developerRole = DEVELOPER_ROLE();

        when(roleRepository.findById(developerRole.getId())).thenReturn(Optional.of(developerRole));

        Role role = roleService.getRole(developerRole.getId());
        assertNotNull(role);
        assertEquals(developerRole, role);
    }

    @Test
    public void shouldFailToGetRoleWhenRoleIdDoesNotExist() {
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> roleService.getRole(UUID_1));

        assertEquals(format("Role %s not found", UUID_1), exception.getMessage());
    }

    @Test
    public void shouldFailToGetRolesWhenUserIddDoesNotExist() {
        when(teamService.getTeam(any(UUID.class)))
                .thenReturn(ORDINARY_CORAL_LYNX_TEAM());
        doThrow(ResourceNotFoundException.class)
                .when(userService)
                .getUser(any(UUID.class));

        assertThrows(ResourceNotFoundException.class,
                () -> roleService.getRolesByUserIdAndTeamId(UUID_1, ORDINARY_CORAL_LYNX_TEAM_UUID));
    }

    @Test
    public void shouldFailToGetRoleWhenTeamIdDoesNotExist() {
        doThrow(ResourceNotFoundException.class)
                .when(teamService)
                .getTeam(any(UUID.class));

        assertThrows(ResourceNotFoundException.class,
                () -> roleService.getRolesByUserIdAndTeamId(UUID_1, ORDINARY_CORAL_LYNX_TEAM_UUID));
    }

    @Test
    public void shouldGetRoleWhenTeamIdAndUserIdExistAndSameTeam() {
        when(teamService.getTeam(any(UUID.class)))
                .thenReturn(ORDINARY_CORAL_LYNX_TEAM());
        when(userService.getUser(any(UUID.class)))
                .thenReturn(GIANNI_USER());
        when(membershipRepository.findAll())
                .thenReturn(List.of(DEFAULT_MEMBERSHIP()));

        Collection<Role> roles =
                roleService.getRolesByUserIdAndTeamId(GIANNI_USER_UUID, ORDINARY_CORAL_LYNX_TEAM_UUID);
        assertEquals(1, roles.size());
        assertTrue(roles.stream().filter(role -> role.getName().equals("Developer")).findAny().isPresent());
    }

    @Test
    public void shouldGetRoleWhenTeamIdAndUserIdExistAndDifferentTeam() {
        UUID userId = UUID_4;
        User user = User.builder().id(userId).build();
        Membership membership = Membership.builder().teamId(UUID_1).userId(userId)
                .role(Role.builder().name("Product Owner").build()).build();

        when(teamService.getTeam(any(UUID.class)))
                .thenReturn(ORDINARY_CORAL_LYNX_TEAM());
        when(userService.getUser(any(UUID.class)))
                .thenReturn(user);
        when(membershipRepository.findAll())
                .thenReturn(List.of(DEFAULT_MEMBERSHIP(), membership));

        Collection<Role> roles = roleService.getRolesByUserIdAndTeamId(userId, ORDINARY_CORAL_LYNX_TEAM_UUID);

        assertEquals(2, roles.size());
    }
}
