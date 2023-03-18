package com.ecore.roles.api;

import com.ecore.roles.client.model.Team;
import com.ecore.roles.utils.RestAssuredHelper;
import com.ecore.roles.web.dto.TeamDto;
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
public class TeamApiTest {

    private final RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    @LocalServerPort
    private int port;

    @Autowired
    public TeamApiTest(RestTemplate restTemplate) {
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
                .get("/v1/team")
                .then())
                        .validate(404, "Not Found");
    }

    @Test
    void shouldGetAllTeams() {
        mockGetTeams(mockServer);
        TeamDto[] teams = getTeams()
                .extract().as(TeamDto[].class);

        assertThat(teams.length).isGreaterThanOrEqualTo(1);
    }

    @Test
    void shouldGetTeamById() {
        Team expectedTeam = ORDINARY_CORAL_LYNX_TEAM();
        mockGetTeamById(mockServer, ORDINARY_CORAL_LYNX_TEAM_UUID, expectedTeam);

        getTeam(ORDINARY_CORAL_LYNX_TEAM_UUID)
                .statusCode(200)
                .body("name", equalTo(expectedTeam.getName()));
    }

    @Test
    void shouldFailToGetTeamById() {
        mockGetTeamById(mockServer, UUID_1, null);

        getTeam(UUID_1)
                .validate(404, format("Team %s not found", UUID_1));
    }

}
