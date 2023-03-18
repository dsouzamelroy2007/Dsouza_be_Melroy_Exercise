package com.ecore.roles.service;

import com.ecore.roles.client.UserClient;
import com.ecore.roles.client.model.User;
import com.ecore.roles.exception.ResourceNotFoundException;
import com.ecore.roles.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static com.ecore.roles.utils.TestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserServiceImpl usersService;
    @Mock
    private UserClient userClient;

    @Test
    void shouldGetUserWhenUserIdExists() {
        User gianniUser = GIANNI_USER();

        when(userClient.getUser(UUID_1))
                .thenReturn(ResponseEntity
                        .status(HttpStatus.OK)
                        .body(gianniUser));

        assertNotNull(usersService.getUser(UUID_1));
    }

    @Test
    void shouldThrowExceptionWhenUserIdDoesNotExists() {
        UUID userId = UUID.fromString("7476a4bf-adfe-415c-941b-1739af07039b");

        when(userClient.getUser(any(UUID.class)))
                .thenReturn(ResponseEntity
                        .status(HttpStatus.OK)
                        .body(null));

        Exception exception =
                Assertions.assertThrows(ResourceNotFoundException.class, () -> usersService.getUser(userId));
        assertEquals("User 7476a4bf-adfe-415c-941b-1739af07039b not found", exception.getMessage());
    }

    @Test
    void shouldGetAllUsers() {
        User gianniUser = GIANNI_USER();

        when(userClient.getUsers())
                .thenReturn(ResponseEntity
                        .status(HttpStatus.OK)
                        .body(List.of(gianniUser)));

        List result = usersService.getUsers();
        assertNotNull(result);
        assertEquals(1, result.size());
    }
}
