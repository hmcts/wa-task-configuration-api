package uk.gov.hmcts.reform.wataskconfigurationapi.domain.entities.roleassignment;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import lombok.EqualsAndHashCode;

import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Builder
public class RequestedRoles extends Assignment {

    private RequestedRoles(ActorIdType actorIdType,
                           String actorId,
                           RoleType roleType,
                           String roleName,
                           RoleCategory roleCategory,
                           Classification classification,
                           GrantType grantType,
                           Boolean readOnly,
                           Map<String, JsonNode> attributes) {
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

    public String getRoleName() {
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

    public Map<String, JsonNode> getAttributes() {
        return attributes;
    }
}
