package uk.gov.hmcts.reform.wataskconfigurationapi.domain.entities.roleassignment;

import lombok.Builder;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@EqualsAndHashCode
@Builder
public final class QueryRequest {
    private final List<String> actorId;
    private final List<String> roleType;
    private final List<String> roleName;
    private final List<String> classification;
    private final List<String> grantType;
    private final LocalDateTime validAt;
    private final List<String> roleCategory;
    private final Map<String, List<String>> attributes;
    private final List<String> authorisations;

    public List<String> getActorId() {
        return actorId;
    }

    public List<String> getRoleType() {
        return roleType;
    }

    public List<String> getRoleName() {
        return roleName;
    }

    public List<String> getClassification() {
        return classification;
    }

    public List<String> getGrantType() {
        return grantType;
    }

    public LocalDateTime getValidAt() {
        return validAt;
    }

    public List<String> getRoleCategory() {
        return roleCategory;
    }

    public Map<String, List<String>> getAttributes() {
        return attributes;
    }

    public List<String> getAuthorisations() {
        return authorisations;
    }
}
