package uk.gov.hmcts.reform.wataskconfigurationapi;

import org.apache.commons.io.FileUtils;
import org.eclipse.jetty.http.HttpStatus;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.wataskconfigurationapi.thirdparty.idam.IdamSystemTokenGenerator;
import uk.gov.hmcts.reform.wataskconfigurationapi.thirdparty.idam.UserInfo;

import java.io.IOException;

import static net.serenitybdd.rest.SerenityRest.given;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Service
public class RoleAssignmentHelper {

    @Value("${targets.role-assignment}")
    protected String roleAssignmentUrl;

    @Autowired
    @Qualifier("ccdServiceAuthTokenGenerator")
    private AuthTokenGenerator ccdServiceAuthTokenGenerator;

    @Autowired
    private IdamSystemTokenGenerator systemTokenGenerator;

    public void postRoleAssignment(String caseId) throws IOException {
        String bearerUserToken = systemTokenGenerator.generate();
        UserInfo userInfo = systemTokenGenerator.getUserInfo(bearerUserToken);

        given()
            .relaxedHTTPSValidation()
            .contentType(APPLICATION_JSON_VALUE)
            .header("ServiceAuthorization", ccdServiceAuthTokenGenerator.generate())
            .header("Authorization", bearerUserToken)
            .baseUri(roleAssignmentUrl)
            .basePath("/am/role-assignments")
            .body(getBody(caseId, userInfo))
            .when()
            .post()
            .prettyPeek()
            .then()
            .statusCode(HttpStatus.CREATED_201);

    }

    @NotNull
    private String getBody(String caseId, UserInfo userInfo) throws IOException {
        String assignmentRequestBody =
            FileUtils.readFileToString(ResourceUtils.getFile("classpath:assignment-request.json"));

        assignmentRequestBody = assignmentRequestBody.replace("{ACTOR_ID_PLACEHOLDER}", userInfo.getUid());
        assignmentRequestBody = assignmentRequestBody.replace("{CASE_ID_PLACEHOLDER}", caseId);
        assignmentRequestBody = assignmentRequestBody.replace("{ASSIGNER_ID_PLACEHOLDER}", userInfo.getUid());
        return assignmentRequestBody;
    }

}
