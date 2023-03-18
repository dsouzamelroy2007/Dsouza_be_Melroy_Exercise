package com.ecore.roles.api;

import com.ecore.roles.client.model.User;
import com.ecore.roles.utils.RestAssuredHelper;
import com.ecore.roles.web.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static com.ecore.roles.utils.MockUtils.*;
import static com.ecore.roles.utils.RestAssuredHelper.*;
import static com.ecore.roles.utils.TestData.*;
import static io.restassured.RestAssured.when;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserApiTest {

    private final RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    @LocalServerPort
    private int port;

    @Autowired
    public UserApiTest(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.bindTo(restTemplate).ignoreExpectOrder(true).build();
        RestAssuredHelper.setUp(port);
    }

    @Test
    void shouldFailWhenPathDoesNotExist() {

        sendRequest(when()
                .get("/v1/user")
                .then())
                        .validate(404, "Not Found");
    }

    @Test
    void shouldGetAllUsers() {
        mockGetUsers(mockServer);
        UserDto[] users = getUsers()
                .extract().as(UserDto[].class);

        assertThat(users.length).isGreaterThanOrEqualTo(1);
    }

    @Test
    void shouldGetUserById() {
        User expectedUser = GIANNI_USER();
        mockGetUserById(mockServer, GIANNI_USER_UUID, expectedUser);

        getUser(GIANNI_USER_UUID)
                .statusCode(200)
                .body("displayName", equalTo(expectedUser.getDisplayName()));
    }

    @Test
    void shouldFailToGetUserById() {
        mockGetUserById(mockServer, UUID_1, null);

        getUser(UUID_1)
                .validate(404, format("User %s not found", UUID_1));
    }

}
