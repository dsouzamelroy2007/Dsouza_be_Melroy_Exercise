package com.ecore.roles.web.rest;

import com.ecore.roles.service.UserService;
import com.ecore.roles.web.UserApi;
import com.ecore.roles.web.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.ecore.roles.web.dto.UserDto.fromModel;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/users")
public class UserRestController implements UserApi {

    private final UserService userService;

    @Override
    @GetMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<UserDto>> getUsers() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getUsers().stream()
                        .map(UserDto::fromModel)
                        .collect(Collectors.toList()));
    }

    @Override
    @GetMapping(
            path = "/{userId}",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<UserDto> getUser(
            @NotNull @PathVariable UUID userId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(fromModel(userService.getUser(userId)));
    }
}
