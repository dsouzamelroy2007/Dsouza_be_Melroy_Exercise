package com.ecore.roles.web;

import com.ecore.roles.web.dto.MembershipDto;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface MembershipApi {

    ResponseEntity<MembershipDto> assignRoleToMembership(
            MembershipDto membership);

    ResponseEntity<List<MembershipDto>> getMembershipsByRole(
            UUID roleId);

}
