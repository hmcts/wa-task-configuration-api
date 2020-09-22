package uk.gov.hmcts.reform.wataskconfigurationapi;

import com.fasterxml.jackson.annotation.JsonIgnore;
import uk.gov.hmcts.reform.wataskconfigurationapi.ccdmapping.CamundaValue;

import java.util.Map;

public class CreateTaskMessage {
    private final String messageName;
    private final Map<String, CamundaValue<?>> processVariables;

    public CreateTaskMessage(String messageName, Map<String, CamundaValue<?>> processVariables) {
        this.messageName = messageName;
        this.processVariables = processVariables;
    }

    public String getMessageName() {
        return messageName;
    }

    public Map<String, CamundaValue<?>> getProcessVariables() {
        return processVariables;
    }

    @JsonIgnore
    public String getCcdId() {
        CamundaValue<?> ccdId = processVariables.get("ccdId");
        return String.valueOf(ccdId.getValue());
    }
}