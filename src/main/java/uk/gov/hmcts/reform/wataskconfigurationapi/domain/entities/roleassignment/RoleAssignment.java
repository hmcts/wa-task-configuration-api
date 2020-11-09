package uk.gov.hmcts.reform.wataskconfigurationapi.domain.entities.roleassignment;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public final class RoleAssignment extends Assignment {
    private final String id;
    private final LocalDateTime created;
    private final List<String> authorisations;

    @Builder
    private RoleAssignment(String id,
                           ActorIdType actorIdType,
                           String actorId,
                           RoleType roleType,
                           RoleName roleName,
                           RoleCategory roleCategory,
                           Classification classification,
                           GrantType grantType,
                           Boolean readOnly,
                           LocalDateTime created,
                           Map<Attributes, String> attributes,
                           List<String> authorisations) {
        super(actorIdType, actorId, roleType, roleName, roleCategory, classification, grantType, readOnly, attributes);
        this.id = id;
        this.created = created;
        this.authorisations = authorisations;
    }



    public String getId() {
        return id;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public List<String> getAuthorisations() {
        return authorisations;
    }

}
