package uk.gov.hmcts.reform.wataskconfigurationapi.ccdmapping.variableextractors;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.hmcts.reform.wataskconfigurationapi.domain.entities.roleassignment.ActorIdType;
import uk.gov.hmcts.reform.wataskconfigurationapi.domain.entities.roleassignment.Attributes;
import uk.gov.hmcts.reform.wataskconfigurationapi.domain.entities.roleassignment.Classification;
import uk.gov.hmcts.reform.wataskconfigurationapi.domain.entities.roleassignment.GrantType;
import uk.gov.hmcts.reform.wataskconfigurationapi.domain.entities.roleassignment.QueryRequest;
import uk.gov.hmcts.reform.wataskconfigurationapi.domain.entities.roleassignment.RoleAssignment;
import uk.gov.hmcts.reform.wataskconfigurationapi.domain.entities.roleassignment.RoleCategory;
import uk.gov.hmcts.reform.wataskconfigurationapi.domain.entities.roleassignment.RoleName;
import uk.gov.hmcts.reform.wataskconfigurationapi.domain.entities.roleassignment.RoleType;
import uk.gov.hmcts.reform.wataskconfigurationapi.thirdparty.roleassignment.RoleAssignmentApi;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("integration")
public class RoleAssignmentApiTest {

    @Autowired
    private RoleAssignmentApi roleAssignmentApi;

    private static WireMockServer wireMockServer;

    @BeforeAll
    static void beforeAll() {
        wireMockServer = new WireMockServer(8091);
        wireMockServer.start();
    }

    @AfterAll
    static void afterAll() {
        wireMockServer.stop();
    }

    @Test
    void queryRoleAssignmentTest() {

        //todo: move this to a json resource file
        String body = "[\n"
            + "    {\n"
            + "        \"id\": \"4c704d91-de05-43e1-a9ee-9b8dc87c2c12\",\n"
            + "        \"actorIdType\": \"IDAM\",\n"
            + "        \"actorId\": \"4afa7d5c-02fa-4a82-82c2-0a9ad7467d30\",\n"
            + "        \"roleType\": \"CASE\",\n"
            + "        \"roleName\": \"tribunal-caseworker\",\n"
            + "        \"classification\": \"RESTRICTED\",\n"
            + "        \"grantType\": \"SPECIFIC\",\n"
            + "        \"roleCategory\": \"STAFF\",\n"
            + "        \"readOnly\": false,\n"
            + "        \"created\": \"2020-11-06T17:15:36.960886\",\n"
            + "        \"attributes\": {\n"
            + "            \"caseId\": \"1604584759556245\"\n"
            + "        },\n"
            + "        \"authorisations\": []\n"
            + "    }\n"
            + "]";

        wireMockServer.stubFor(post(urlEqualTo("/am/role-assignments/query")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader(
                    "Content-Type",
                    "application/vnd.uk.gov.hmcts.role-assignment-service.post-assignment-query-request+json; "
                        + "version=1.0;charset=UTF-8"
                )
                .withBody(body))
        );

        List<RoleAssignment> actualRoleAssignments = roleAssignmentApi.queryRoleAssignments(
            "user token",
            "s2s token",
            QueryRequest.builder().build()
        );

        RoleAssignment expectedRoleAssignment = RoleAssignment.builder()
            .id("4c704d91-de05-43e1-a9ee-9b8dc87c2c12")
            .actorIdType(ActorIdType.IDAM)
            .actorId("4afa7d5c-02fa-4a82-82c2-0a9ad7467d30")
            .roleType(RoleType.CASE)
            .roleName(RoleName.TRIBUNAL_CASEWORKER.getValue())
            .classification(Classification.RESTRICTED)
            .grantType(GrantType.SPECIFIC)
            .roleCategory(RoleCategory.STAFF)
            .readOnly(false)
            .created(LocalDateTime.parse("2020-11-06T17:15:36.960886"))
            .attributes(Map.of(Attributes.CASE_ID.getValue(), "1604584759556245"))
            .authorisations(Collections.emptyList())
            .build();

        assertThat(actualRoleAssignments.get(0)).isEqualTo(expectedRoleAssignment);
    }

}
