package uk.gov.hmcts.reform.wataskconfigurationapi;

import org.eclipse.jetty.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.wataskconfigurationapi.thirdparty.idam.IdamSystemTokenGenerator;
import uk.gov.hmcts.reform.wataskconfigurationapi.thirdparty.idam.UserInfo;

import static net.serenitybdd.rest.SerenityRest.given;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Service
public class RoleAssignmentHelper {

    @Autowired
    @Qualifier("ccdServiceAuthTokenGenerator")
    private AuthTokenGenerator ccdServiceAuthTokenGenerator;

    @Autowired
    private IdamSystemTokenGenerator systemTokenGenerator;

    public void postRoleAssignment(String ccdId) {
        String bearerUserToken = systemTokenGenerator.generate();
        UserInfo userInfo = systemTokenGenerator.getUserInfo(bearerUserToken);

        String roleAssignmentRequest = "{\n"
            + "    \"requestedRoles\": [\n"
            + "        {\n"
            + "            \"actorId\": \"" + userInfo.getUid() + "\",\n"
            + "            \"actorIdType\": \"IDAM\",\n"
            + "            \"attributes\": {\n"
            + "                \"caseId\": \"" + ccdId + "\"\n"
            + "            },\n"
            + "            \"classification\": \"RESTRICTED\",\n"
            + "            \"grantType\": \"SPECIFIC\",\n"
            + "            \"readOnly\": false,\n"
            + "            \"roleCategory\": \"STAFF\",\n"
            + "            \"roleName\": \"tribunal-caseworker\",\n"
            + "            \"roleType\": \"CASE\"\n"
            + "        }\n"
            + "    ],\n"
            + "    \"roleRequest\": {\n"
            + "        \"assignerId\": \"" + userInfo.getUid() + "\",\n"
            + "        \"process\": \"case-allocation\",\n"
            + "        \"reference\": \"" + ccdId + "/tribunal-caseworker\",\n"
            + "        \"replaceExisting\": true\n"
            + "    }\n"
            + "}";

        given()
            .relaxedHTTPSValidation()
            .contentType(APPLICATION_JSON_VALUE)
            .header("ServiceAuthorization", ccdServiceAuthTokenGenerator.generate())
            .header("Authorization", bearerUserToken)
            .baseUri("http://role-assignment")
            .basePath("/am/role-assignments")
            .body(roleAssignmentRequest)
            .when()
            .post()
            .then()
            .statusCode(HttpStatus.CREATED_201);

    }

}
