package uk.gov.hmcts.reform.wataskconfigurationapi.domain.entities.roleassignment;

import lombok.Builder;
import lombok.EqualsAndHashCode;

import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("PMD.MissingStaticMethodInNonInstantiatableClass")
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

    @Override
    public ActorIdType getActorIdType() {
        return actorIdType;
    }

    @Override
    public String getActorId() {
        return actorId;
    }

    @Override
    public RoleType getRoleType() {
        return roleType;
    }

    @Override
    public RoleName getRoleName() {
        return roleName;
    }

    @Override
    public RoleCategory getRoleCategory() {
        return roleCategory;
    }

    @Override
    public Classification getClassification() {
        return classification;
    }

    @Override
    public GrantType getGrantType() {
        return grantType;
    }

    @Override
    public Boolean getReadOnly() {
        return readOnly;
    }

    @Override
    public Map<Attributes, String> getAttributes() {
        return attributes;
    }
}
