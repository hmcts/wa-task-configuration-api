package uk.gov.hmcts.reform.wataskconfigurationapi.domain.entities.roleassignment;

import lombok.Builder;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
@Builder
public final class RoleRequest {
    private final String assignerId;
    private final String process;
    private final String reference;
    private final boolean replaceExisting;

    private RoleRequest(String assignerId, String process, String reference, boolean replaceExisting) {
        this.assignerId = assignerId;
        this.process = process;
        this.reference = reference;
        this.replaceExisting = replaceExisting;
    }

    public String getAssignerId() {
        return assignerId;
    }

    public String getProcess() {
        return process;
    }

    public String getReference() {
        return reference;
    }

    public boolean isReplaceExisting() {
        return replaceExisting;
    }
}
