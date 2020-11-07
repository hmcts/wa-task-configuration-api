package uk.gov.hmcts.reform.wataskconfigurationapi.domain.entities.roleassignment;

import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode
public final class AssignmentRequest {
    private final RoleRequest roleRequest;
    private final List<RequestedRoles> requestedRoles;

    public AssignmentRequest(RoleRequest roleRequest, List<RequestedRoles> requestedRoles) {
        this.roleRequest = roleRequest;
        this.requestedRoles = requestedRoles;
    }

    public RoleRequest getRoleRequest() {
        return roleRequest;
    }

    public List<RequestedRoles> getRequestedRoles() {
        return requestedRoles;
    }
}
