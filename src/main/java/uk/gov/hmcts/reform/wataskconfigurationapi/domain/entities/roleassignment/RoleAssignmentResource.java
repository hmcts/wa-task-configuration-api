package uk.gov.hmcts.reform.wataskconfigurationapi.domain.entities.roleassignment;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuppressWarnings("PMD.MissingStaticMethodInNonInstantiatableClass")
public final class RoleAssignmentResource {
    private final List<RoleAssignment> roleAssignmentResponse;

    public RoleAssignmentResource(List<RoleAssignment> roleAssignmentResponse) {
        this.roleAssignmentResponse = roleAssignmentResponse;
    }

    public List<RoleAssignment> getRoleAssignmentResponse() {
        return roleAssignmentResponse;
    }
}
