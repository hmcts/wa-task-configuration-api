package uk.gov.hmcts.reform.wataskconfigurationapi.domain.entities.roleassignment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;

class QueryRequestTest {

    @Test
    void serialiseQueryRequest() throws JsonProcessingException {
        QueryRequest query = QueryRequest.builder()
            .roleType(Collections.singletonList(RoleType.CASE.name()))
            .roleName(Collections.singletonList(RoleName.TRIBUNAL_CASEWORKER.getValue()))
            .validAt(LocalDateTime.now())
            .attributes(Collections.singletonMap(Attributes.CASE_ID.getValue(), Collections.singletonList("112")))
            .build();

        ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.writeValueAsString(query));

    }
}
