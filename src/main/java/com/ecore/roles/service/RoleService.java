package com.ecore.roles.service;

import com.ecore.roles.model.Role;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface RoleService {

    Role createRole(Role role);

    Role getRole(UUID id);

    List<Role> getRoles();

    Collection<Role> getRolesByUserIdAndTeamId(UUID userId, UUID teamId);
}
