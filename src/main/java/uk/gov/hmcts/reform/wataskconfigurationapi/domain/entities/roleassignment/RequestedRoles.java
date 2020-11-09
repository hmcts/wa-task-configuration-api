package uk.gov.hmcts.reform.wataskconfigurationapi.domain.entities.roleassignment;

import lombok.Builder;
import lombok.EqualsAndHashCode;

import java.util.Map;

@EqualsAndHashCode(callSuper = true)
public final class RequestedRoles extends Assignment {

    @Builder
    private RequestedRoles(ActorIdType actorIdType,
                           String actorId,
                           RoleType roleType,
                           RoleName roleName,
                           RoleCategory roleCategory,
                           Classification classification,
                           GrantType grantType,
                           Boolean readOnly,
                           Map<Attributes, String> attributes) {
        super(actorIdType, actorId, roleType, roleName, roleCategory, classification, grantType, readOnly, attributes);
    }

    public ActorIdType getActorIdType() {
        return actorIdType;
    }

    public String getActorId() {
        return actorId;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public RoleName getRoleName() {
        return roleName;
    }

    public RoleCategory getRoleCategory() {
        return roleCategory;
    }

    public Classification getClassification() {
        return classification;
    }

    public GrantType getGrantType() {
        return grantType;
    }

    public Boolean getReadOnly() {
        return readOnly;
    }

    public Map<Attributes, String> getAttributes() {
        return attributes;
    }
}
