package uk.gov.hmcts.reform.wataskconfigurationapi.domain.entities.roleassignment;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.ObjectContent;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

@RunWith(SpringRunner.class)
@JsonTest
class RoleAssignmentTest {

    @Autowired
    private JacksonTester<RoleAssignment> jacksonTester;

    @Test
    public void testDeserializeRoleAssignment() throws Exception {
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

        ObjectContent<RoleAssignment> actualRoleAssignment = jacksonTester.read("roleAssignment.json");

        actualRoleAssignment.assertThat().isEqualTo(expectedRoleAssignment);
    }
}
