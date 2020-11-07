package uk.gov.hmcts.reform.wataskconfigurationapi.domain.entities.roleassignment;

public enum RoleName {
    TRIBUNAL_CASEWORKER("tribunal-caseworker");
    private final String value;

    public String getValue() {
        return value;
    }

    RoleName(String value) {
        this.value = value;
    }
}
