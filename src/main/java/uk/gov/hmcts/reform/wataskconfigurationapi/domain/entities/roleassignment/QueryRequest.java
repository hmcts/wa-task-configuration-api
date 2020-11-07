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
}
