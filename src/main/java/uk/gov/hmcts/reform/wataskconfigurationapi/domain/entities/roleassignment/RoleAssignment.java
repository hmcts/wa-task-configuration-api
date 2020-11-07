package uk.gov.hmcts.reform.wataskconfigurationapi.domain.entities.roleassignment;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@EqualsAndHashCode
@Builder
public final class RoleAssignment extends Assignment {
    private final UUID id;
    private final LocalDateTime created;
    private final List<String> authorisations;

    private RoleAssignment(UUID id,
                           ActorIdType actorIdType,
                           String actorId,
                           RoleType roleType,
                           String roleName,
                           RoleCategory roleCategory,
                           Classification classification,
                           GrantType grantType,
                           Boolean readOnly,
                           LocalDateTime created,
                           Map<String, JsonNode> attributes,
                           List<String> authorisations) {
        super(actorIdType, actorId, roleType, roleName, roleCategory, classification, grantType, readOnly, attributes);
        this.id = id;
        this.created = created;
        this.authorisations = authorisations;
    }

    public UUID getId() {
        return id;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public List<String> getAuthorisations() {
        return authorisations;
    }

    @Override
    public String toString() {
        return "RoleAssignment{" +
            "id=" + id +
            ", created=" + created +
            ", authorisations=" + authorisations +
            ", actorIdType=" + actorIdType +
            ", actorId='" + actorId + '\'' +
            ", roleType=" + roleType +
            ", roleName='" + roleName + '\'' +
            ", roleCategory=" + roleCategory +
            ", classification=" + classification +
            ", grantType=" + grantType +
            ", readOnly=" + readOnly +
            ", attributes=" + attributes +
            '}';
    }
}
