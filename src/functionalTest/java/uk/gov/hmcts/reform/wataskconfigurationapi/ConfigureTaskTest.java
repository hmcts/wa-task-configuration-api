package uk.gov.hmcts.reform.wataskconfigurationapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.ccd.client.CoreCaseDataApi;
import uk.gov.hmcts.reform.ccd.client.model.CaseDataContent;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.ccd.client.model.Event;
import uk.gov.hmcts.reform.ccd.client.model.StartEventResponse;
import uk.gov.hmcts.reform.wataskconfigurationapi.controllers.ConfigureTaskRequest;
import uk.gov.hmcts.reform.wataskconfigurationapi.thirdparty.idam.IdamSystemTokenGenerator;
import uk.gov.hmcts.reform.wataskconfigurationapi.thirdparty.idam.UserInfo;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static uk.gov.hmcts.reform.wataskconfigurationapi.CreateTaskMessageBuilder.createBasicMessageForTask;
import static uk.gov.hmcts.reform.wataskconfigurationapi.CreatorObjectMapper.asCamundaJsonString;
import static uk.gov.hmcts.reform.wataskconfigurationapi.CreatorObjectMapper.asJsonString;

public class ConfigureTaskTest extends BaseFunctionalTest {

    @Autowired
    @Qualifier("ccdServiceAuthTokenGenerator")
    private AuthTokenGenerator ccdServiceAuthTokenGenerator;

    @Autowired
    @Qualifier("camundaServiceAuthTokenGenerator")
    private AuthTokenGenerator camundaServiceAuthTokenGenerator;

    @Autowired
    private IdamSystemTokenGenerator systemTokenGenerator;

    @Autowired
    private CoreCaseDataApi coreCaseDataApi;

    @Autowired
    private RoleAssignmentHelper roleAssignmentHelper;

    @Test
    public void canConfigureATask() throws IOException {
        String ccdId = createCcdCase();

        roleAssignmentHelper.postRoleAssignment(ccdId);

        CreateTaskMessage createTaskMessage = createBasicMessageForTask()
            .withCcdId(ccdId)
            .build();

        String taskId = createTask(createTaskMessage);

        given()
            .relaxedHTTPSValidation()
            .contentType(APPLICATION_JSON_VALUE)
            .basePath("/configureTask")
            .body(asJsonString(new ConfigureTaskRequest(taskId)))
            .when()
            .post()
            .then()
            .statusCode(HttpStatus.OK_200);

        given()
            .contentType(APPLICATION_JSON_VALUE)
            .header(SERVICE_AUTHORIZATION, camundaServiceAuthTokenGenerator.generate())
            .baseUri(camundaUrl)
            .basePath("/task/" + taskId + "/localVariables")
            .when()
            .get()
            .then()
            .body("caseName.value", is("Bob Smith"))
            .body("appealType.value", is("protection"))
            .body("region.value", is("1"))
            .body("location.value", is("765324"))
            .body("locationName.value", is("Taylor House"))
            .body("taskState.value", is("configured"))
            .body("ccdId.value", is(createTaskMessage.getCcdId()))
            .body("securityClassification.value", is("PUBLIC"))
            .body("caseType.value", is("Asylum"))
            .body("title.value", is("task name"))
            .body("tribunal-caseworker.value", is("Read,Refer,Own,Manage,Cancel"))
            .body("senior-tribunal-caseworker.value", is("Read,Refer,Own,Manage,Cancel"))

        ;
    }

    private String createTask(CreateTaskMessage createTaskMessage) {
        given()
            .contentType(APPLICATION_JSON_VALUE)
            .header(SERVICE_AUTHORIZATION, camundaServiceAuthTokenGenerator.generate())
            .baseUri(camundaUrl)
            .basePath("/message")
            .body(asCamundaJsonString(createTaskMessage))
            .when()
            .post()
            .then()
            .statusCode(HttpStatus.NO_CONTENT_204);

        Object taskName = createTaskMessage.getProcessVariables().get("name").getValue();
        return given()
            .contentType(APPLICATION_JSON_VALUE)
            .header(SERVICE_AUTHORIZATION, camundaServiceAuthTokenGenerator.generate())
            .baseUri(camundaUrl)
            .basePath("/task")
            .param("processVariables", "ccdId_eq_" + createTaskMessage.getCcdId())
            .when()
            .get()
            .then()
            .body("size()", is(1))
            .body("[0].name", is(taskName))
            .extract()
            .path("[0].id");
    }

    private String createCcdCase() throws IOException {
        String userToken = systemTokenGenerator.generate();
        UserInfo userInfo = systemTokenGenerator.getUserInfo(userToken);
        String serviceToken = ccdServiceAuthTokenGenerator.generate();
        StartEventResponse startCase = coreCaseDataApi.startForCaseworker(
            userToken,
            serviceToken,
            userInfo.getUid(),
            "IA",
            "Asylum",
            "startAppeal"
        );
        String caseData = new String(
            (Objects.requireNonNull(Thread.currentThread().getContextClassLoader()
                                        .getResourceAsStream("case_data.json"))).readAllBytes()
        );
        Map data = new ObjectMapper().readValue(caseData, Map.class);
        CaseDataContent caseDataContent = CaseDataContent.builder()
            .eventToken(startCase.getToken())
            .event(Event.builder()
                       .id(startCase.getEventId())
                       .summary("summary")
                       .description("description")
                       .build())
            .data(data)
            .build();
        CaseDetails caseDetails = coreCaseDataApi.submitForCaseworker(
            userToken,
            serviceToken,
            userInfo.getUid(),
            "IA",
            "Asylum",
            true,
            caseDataContent
        );

        System.out.println("Created case [" + caseDetails.getId() + "]");

        StartEventResponse submitCase = coreCaseDataApi.startEventForCaseWorker(
            userToken,
            serviceToken,
            userInfo.getUid(),
            "IA",
            "Asylum",
            caseDetails.getId().toString(),
            "submitAppeal"
        );
        CaseDataContent submitCaseDataContent = CaseDataContent.builder()
            .eventToken(submitCase.getToken())
            .event(Event.builder()
                       .id(submitCase.getEventId())
                       .summary("summary")
                       .description("description")
                       .build())
            .data(data)
            .build();
        coreCaseDataApi.submitEventForCaseWorker(
            userToken,
            serviceToken,
            userInfo.getUid(),
            "IA",
            "Asylum",
            caseDetails.getId().toString(),
            true,
            submitCaseDataContent
        );

        return caseDetails.getId().toString();
    }

}
