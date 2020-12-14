package uk.gov.hmcts.reform.wataskconfigurationapi.services.configurators;

import uk.gov.hmcts.reform.wataskconfigurationapi.domain.entities.camunda.CamundaValue;
import uk.gov.hmcts.reform.wataskconfigurationapi.domain.entities.camunda.TaskResponse;

import java.util.Map;

public interface TaskConfigurator {
    Map<String, Object> getConfigurationVariables(TaskResponse task,
                                                  Map<String, CamundaValue<Object>> processVariables);
}
