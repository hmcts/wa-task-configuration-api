package uk.gov.hmcts.reform.wataskconfigurationapi.domain.entities.roleassignment;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@JsonTest
class QueryRequestTest {

    @Autowired
    private JacksonTester<QueryRequest> actualQueryRequestJson;

    @Test
    void testSerialiseQueryRequest() throws Exception {
        QueryRequest query = QueryRequest.builder()
            .roleType(Collections.singletonList(RoleType.CASE.name()))
            .roleName(Collections.singletonList(RoleName.TRIBUNAL_CASEWORKER.getValue()))
            .validAt(LocalDateTime.of(2020, 10, 6, 17, 0, 0))
            .attributes(Map.of(Attributes.CASE_ID.getValue(), Collections.singletonList("1604584759556245")))
            .build();

        JsonContent<QueryRequest> queryRequestJsonContent = this.actualQueryRequestJson.write(query);
        System.out.println(queryRequestJsonContent);
        assertThat(queryRequestJsonContent).isEqualToJson("queryRequest.json");
    }
}
