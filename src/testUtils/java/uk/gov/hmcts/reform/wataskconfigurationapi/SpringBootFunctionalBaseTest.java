package uk.gov.hmcts.reform.wataskconfigurationapi;

import net.serenitybdd.junit.spring.integration.SpringIntegrationSerenityRunner;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.hmcts.reform.wataskconfigurationapi.config.RestApiActions;
import uk.gov.hmcts.reform.wataskconfigurationapi.services.AuthorizationHeadersProvider;

import java.time.format.DateTimeFormatter;

import static com.fasterxml.jackson.databind.PropertyNamingStrategy.LOWER_CAMEL_CASE;
import static com.fasterxml.jackson.databind.PropertyNamingStrategy.SNAKE_CASE;
import static java.time.format.DateTimeFormatter.ofPattern;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static uk.gov.hmcts.reform.wataskconfigurationapi.config.ServiceTokenGeneratorConfiguration.SERVICE_AUTHORIZATION;

@RunWith(SpringIntegrationSerenityRunner.class)
@SpringBootTest
@ActiveProfiles("functional")
public abstract class SpringBootFunctionalBaseTest {
    public static final DateTimeFormatter CAMUNDA_DATA_TIME_FORMATTER = ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    @Value("${targets.instance}")
    protected String testUrl;
    @Value("${targets.camunda}")
    protected String camundaUrl;

    protected RestApiActions restApiActions;
    protected RestApiActions camundaApiActions;

    @Autowired
    protected AuthorizationHeadersProvider authorizationHeadersProvider;

    @Before
    public void setUp() throws Exception {

        restApiActions = new RestApiActions(testUrl, SNAKE_CASE).setUp();
        camundaApiActions = new RestApiActions(camundaUrl, LOWER_CAMEL_CASE).setUp();

    }

    public void cleanUp(String taskId, String token) {
        given()
            .header(SERVICE_AUTHORIZATION, token)
            .contentType(APPLICATION_JSON_VALUE)
            .baseUri(camundaUrl)
            .basePath("/task/" + taskId + "/complete")
            .when()
            .post();

        given()
            .header(SERVICE_AUTHORIZATION, token)
            .contentType(APPLICATION_JSON_VALUE)
            .accept(APPLICATION_JSON_VALUE)
            .baseUri(camundaUrl)
            .when()
            .get("/history/task?taskId=" + taskId)
            .then()
            .body("[0].deleteReason", is("completed"));
    }

}
