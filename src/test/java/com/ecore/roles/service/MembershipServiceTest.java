package com.ecore.roles.service;

import com.ecore.roles.exception.InvalidArgumentException;
import com.ecore.roles.exception.ResourceExistsException;
import com.ecore.roles.model.Membership;
import com.ecore.roles.repository.MembershipRepository;
import com.ecore.roles.repository.RoleRepository;
import com.ecore.roles.service.impl.MembershipServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.ecore.roles.utils.TestData.DEFAULT_MEMBERSHIP;
import static com.ecore.roles.utils.TestData.DEVELOPER_ROLE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MembershipServiceTest {

    @InjectMocks
    private MembershipServiceImpl membershipService;
    @Mock
    private MembershipRepository membershipRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private UserService userService;
    @Mock
    private TeamService teamService;

    @Test
    public void shouldCreateMembership() {
        Membership expectedMembership = DEFAULT_MEMBERSHIP();
        when(roleRepository.findById(expectedMembership.getRole().getId()))
                .thenReturn(Optional.ofNullable(DEVELOPER_ROLE()));
        when(membershipRepository.findByUserIdAndTeamId(expectedMembership.getUserId(),
                expectedMembership.getTeamId()))
                        .thenReturn(Optional.empty());
        when(membershipRepository
                .save(expectedMembership))
                        .thenReturn(expectedMembership);

        Membership actualMembership = membershipService.assignRoleToMembership(expectedMembership);
        assertNotNull(actualMembership);
        assertEquals(actualMembership, expectedMembership);
        verify(roleRepository).findById(expectedMembership.getRole().getId());
    }

    @Test
    public void shouldFailToCreateMembershipWhenMembershipsIsNull() {
        assertThrows(NullPointerException.class,
                () -> membershipService.assignRoleToMembership(null));
    }

    @Test
    public void shouldFailToCreateMembershipWhenItExists() {
        Membership expectedMembership = DEFAULT_MEMBERSHIP();

        when(membershipRepository.findByUserIdAndTeamId(expectedMembership.getUserId(),
                expectedMembership.getTeamId()))
                        .thenReturn(Optional.of(expectedMembership));

        ResourceExistsException exception = assertThrows(ResourceExistsException.class,
                () -> membershipService.assignRoleToMembership(expectedMembership));
        assertEquals("Membership already exists", exception.getMessage());
        verify(roleRepository, times(0)).getById(any());
        verify(userService, times(0)).getUser(any());
        verify(teamService, times(0)).getTeam(any());
    }

    @Test
    public void shouldFailToCreateMembershipWhenItHasInvalidRole() {
        Membership expectedMembership = DEFAULT_MEMBERSHIP();
        expectedMembership.setRole(null);

        InvalidArgumentException exception = assertThrows(InvalidArgumentException.class,
                () -> membershipService.assignRoleToMembership(expectedMembership));

        assertEquals("Invalid 'Role' object", exception.getMessage());
        verify(membershipRepository, times(0)).findByUserIdAndTeamId(any(), any());
        verify(roleRepository, times(0)).getById(any());
        verify(userService, times(0)).getUser(any());
        verify(teamService, times(0)).getTeam(any());
    }

    @Test
    public void shouldFailToCreateMembershipWhenItHasInvalidTeam() {
        Membership expectedMembership = DEFAULT_MEMBERSHIP();
        expectedMembership.setTeamId(null);

        InvalidArgumentException exception = assertThrows(InvalidArgumentException.class,
                () -> membershipService.assignRoleToMembership(expectedMembership));

        assertEquals("Invalid 'Team' object", exception.getMessage());
        verify(membershipRepository, times(0)).findByUserIdAndTeamId(any(), any());
        verify(roleRepository, times(0)).getById(any());
        verify(userService, times(0)).getUser(any());
        verify(teamService, times(0)).getTeam(any());
    }

    @Test
    public void shouldFailToCreateMembershipWhenItHasInvalidUser() {
        Membership expectedMembership = DEFAULT_MEMBERSHIP();
        expectedMembership.setUserId(null);

        InvalidArgumentException exception = assertThrows(InvalidArgumentException.class,
                () -> membershipService.assignRoleToMembership(expectedMembership));

        assertEquals("Invalid 'User' object", exception.getMessage());
        verify(membershipRepository, times(0)).findByUserIdAndTeamId(any(), any());
        verify(roleRepository, times(0)).getById(any());
        verify(userService, times(0)).getUser(any());
        verify(teamService, times(0)).getTeam(any());
    }

    @Test
    public void shouldFailToGetMembershipsWhenRoleIdIsNull() {
        assertThrows(NullPointerException.class,
                () -> membershipService.getMemberships(null));
    }

}
