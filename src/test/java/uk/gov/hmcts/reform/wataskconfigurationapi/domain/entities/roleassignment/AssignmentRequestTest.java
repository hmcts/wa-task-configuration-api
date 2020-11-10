package uk.gov.hmcts.reform.wataskconfigurationapi.domain.entities.roleassignment;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AssignmentRequestTest {

    @Test
    void pojoTest() {
        List<RequestedRoles> requestedRolesList = Collections.singletonList(RequestedRoles.builder().build());
        RoleRequest roleRequest = RoleRequest.builder().build();

        AssignmentRequest assignmentRequest = new AssignmentRequest(roleRequest, requestedRolesList);

        assertThat(assignmentRequest).isNotNull();
        assertThat(assignmentRequest.getRequestedRoles()).isEqualTo(requestedRolesList);
        assertThat(assignmentRequest.getRoleRequest()).isEqualTo(roleRequest);

        assertThat(new AssignmentRequest(roleRequest, requestedRolesList)).isEqualTo(assignmentRequest);
    }
}
