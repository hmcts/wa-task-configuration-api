package uk.gov.hmcts.reform.wataskconfigurationapi.domain.entities.roleassignment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings("PMD.MissingStaticMethodInNonInstantiatableClass")
public final class RoleAssignmentResource {
    private final List<RoleAssignment> roleAssignmentResponse;
    private final Object links;

    public RoleAssignmentResource(List<RoleAssignment> roleAssignmentResponse, Object links) {
        this.roleAssignmentResponse = roleAssignmentResponse;
        this.links = links;
    }

    public List<RoleAssignment> getRoleAssignmentResponse() {
        return roleAssignmentResponse;
    }

    public Object getLinks() {
        return links;
    }
}
