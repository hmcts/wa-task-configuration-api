package uk.gov.hmcts.reform.wataskconfigurationapi.domain.entities.roleassignment;

public enum Attributes {
    CASE_ID("caseId");
    private final String value;

    public String getValue() {
        return value;
    }

    Attributes(String value) {
        this.value = value;
    }
}
