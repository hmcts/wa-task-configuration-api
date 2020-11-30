package uk.gov.hmcts.reform.wataskconfigurationapi;

import static io.restassured.RestAssured.given;
import static org.apache.commons.lang3.StringUtils.defaultIfBlank;
import static org.hamcrest.Matchers.containsString;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class SmokeTest {

    private final String targetInstance =
        defaultIfBlank(
            System.getenv("TEST_URL"),
            "http://localhost:8091"
        );

    @Test
    void should_check_service_and_return_welcome_message() {

        RequestSpecification requestSpecification = new RequestSpecBuilder()
            .setBaseUri(targetInstance)
            .setRelaxedHTTPSValidation()
            .build();

        given(requestSpecification)
            .when()
            .get("/")
            .then()
            .assertThat()
            .statusCode(HttpStatus.OK.value())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(containsString("Welcome to wa-task-configuration-api"));
    }
}
