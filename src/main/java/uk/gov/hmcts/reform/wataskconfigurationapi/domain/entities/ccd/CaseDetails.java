package uk.gov.hmcts.reform.wataskconfigurationapi.domain.entities.ccd;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Map;

@EqualsAndHashCode
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CaseDetails {

    private String jurisdiction;
    private String caseTypeId;
    private String securityClassification;
    private Map<String, Object> data;

    public CaseDetails() {
        super();
        //No-op constructor
    }

    public CaseDetails(String jurisdiction,
                       String caseTypeId,
                       String securityClassification,
                       Map<String, Object> data) {
        this.jurisdiction = jurisdiction;
        this.caseTypeId = caseTypeId;
        this.securityClassification = securityClassification;
        this.data = data;
    }

    public String getJurisdiction() {
        return jurisdiction;
    }

    public String getCaseTypeId() {
        return caseTypeId;
    }

    public String getSecurityClassification() {
        return securityClassification;
    }

    public Map<String, Object> getData() {
        return data;
    }

}
