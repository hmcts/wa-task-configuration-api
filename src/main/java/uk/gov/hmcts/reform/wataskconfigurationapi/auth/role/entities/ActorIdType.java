package uk.gov.hmcts.reform.wataskconfigurationapi.auth.role.entities;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum ActorIdType {
    IDAM, CASEPARTY, @JsonEnumDefaultValue UNKNOWN
}
