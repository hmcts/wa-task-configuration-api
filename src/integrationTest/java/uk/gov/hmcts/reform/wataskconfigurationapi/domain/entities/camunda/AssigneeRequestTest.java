package uk.gov.hmcts.reform.wataskconfigurationapi.domain.entities.camunda;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@JsonTest
class AssigneeRequestTest {

    @Autowired
    private JacksonTester<AssigneeRequest> jacksonTester;

    @Test
    public void testSerializeAssigneeRequest() throws Exception {
        AssigneeRequest assigneeRequest = new AssigneeRequest("37d4eab7-e14c-404e-8cd1-55cd06b2fc06");

        JsonContent<AssigneeRequest> assigneeRequestJsonContent = jacksonTester.write(assigneeRequest);

        System.out.println(assigneeRequestJsonContent.toString());

        assertThat(assigneeRequestJsonContent).isEqualToJson("{\"userId\": \"37d4eab7-e14c-404e-8cd1-55cd06b2fc06\"}");
    }

}
