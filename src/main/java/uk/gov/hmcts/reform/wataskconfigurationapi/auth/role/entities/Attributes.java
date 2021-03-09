package uk.gov.hmcts.reform.wataskconfigurationapi.auth.role.entities;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Attributes {
    CASE_ID("caseId"),
    JURISDICTION("jurisdiction"),
    CASE_TYPE("caseType");

    private final String value;

    @JsonValue
    public String getValue() {
        return value;
    }

    Attributes(String value) {
        this.value = value;
    }
}
