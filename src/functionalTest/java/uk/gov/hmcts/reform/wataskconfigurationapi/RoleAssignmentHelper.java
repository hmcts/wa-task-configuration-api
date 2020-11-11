package uk.gov.hmcts.reform.wataskconfigurationapi;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class RoleAssignmentHelper {

    @Value("${targets.role-assignment}")
    protected String roleAssignmentUrl;

    @Autowired
    @Qualifier("ccdServiceAuthTokenGenerator")
    private AuthTokenGenerator ccdServiceAuthTokenGenerator;

    @Autowired
    private IdamSystemTokenGenerator systemTokenGenerator;

    public void setRoleAssignments(String caseId) throws IOException {
        String bearerUserToken = systemTokenGenerator.generate();
        UserInfo userInfo = systemTokenGenerator.getUserInfo(bearerUserToken);
        createRoleAssignmentInThisOrder(caseId, bearerUserToken, userInfo);
    }

    private void createRoleAssignmentInThisOrder(String caseId,
                                                 String bearerUserToken,
                                                 UserInfo userInfo) throws IOException {
        postRoleAssignment(caseId, bearerUserToken, userInfo, "set-rules-assignment-request.json");
        postRoleAssignment(caseId, bearerUserToken, userInfo, "assignment-request.json");
    }

    private void postRoleAssignment(String caseId,
                                    String bearerUserToken,
                                    UserInfo userInfo,
                                    String resourceFilename) throws IOException {
        log.info("*** caseId *** : " + caseId);
        log.info("*** bearerUserToken *** : " + bearerUserToken);
        log.info("*** userInfo *** : " + userInfo);
        log.info("*** resourceFilename *** : " + resourceFilename);

        given()
            .relaxedHTTPSValidation()
            .contentType(APPLICATION_JSON_VALUE)
            .header("ServiceAuthorization", ccdServiceAuthTokenGenerator.generate())
            .header("Authorization", bearerUserToken)
            .baseUri(roleAssignmentUrl)
            .basePath("/am/role-assignments")
            .body(getBody(caseId, userInfo, resourceFilename))
            .when()
            .post()
            .prettyPeek()
            .then()
            .statusCode(HttpStatus.CREATED_201);
    }

    @NotNull
    private String getBody(final String caseId,
                           final UserInfo userInfo,
                           final String resourceFilename) throws IOException {
        String assignmentRequestBody =
            FileUtils.readFileToString(ResourceUtils.getFile("classpath:" + resourceFilename));

        assignmentRequestBody = assignmentRequestBody.replace("{ACTOR_ID_PLACEHOLDER}", userInfo.getUid());
        assignmentRequestBody = assignmentRequestBody.replace("{CASE_ID_PLACEHOLDER}", caseId);
        assignmentRequestBody = assignmentRequestBody.replace("{ASSIGNER_ID_PLACEHOLDER}", userInfo.getUid());

        log.info("**** assignmentRequestBody **** : "  + assignmentRequestBody);
        return assignmentRequestBody;
    }

}
