package uk.gov.hmcts.reform.wataskconfigurationapi.services.configurators;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.wataskconfigurationapi.domain.entities.camunda.CamundaTask;
import uk.gov.hmcts.reform.wataskconfigurationapi.domain.entities.configuration.TaskToConfigure;
import uk.gov.hmcts.reform.wataskconfigurationapi.services.CaseConfigurationProviderService;

import java.util.Map;

import static java.util.Collections.emptyMap;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsSame.sameInstance;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.wataskconfigurationapi.domain.entities.camunda.enums.CamundaVariableDefinition.CASE_TYPE_ID;
import static uk.gov.hmcts.reform.wataskconfigurationapi.domain.entities.camunda.enums.CamundaVariableDefinition.SECURITY_CLASSIFICATION;

class CaseRelatedVariablesConfiguratorTest {

    private CamundaTask task;
    private CaseConfigurationProviderService caseConfigurationProviderService;
    private CaseRelatedVariablesConfigurator caseRelatedVariablesConfigurator;

    @BeforeEach
    void setUp() {
        task = new CamundaTask("id", "processInstanceId", "taskName");
        caseConfigurationProviderService = mock(CaseConfigurationProviderService.class);
        caseRelatedVariablesConfigurator = new CaseRelatedVariablesConfigurator(caseConfigurationProviderService);
    }

    @Test
    void should_throw_exception_when_case_id_is_null() {

        TaskToConfigure testTaskToConfigure = new TaskToConfigure(
            "taskId",
            null,
            "taskName",
            emptyMap()
        );

        assertThrows(NullPointerException.class, () -> {
            caseRelatedVariablesConfigurator.getConfigurationVariables(testTaskToConfigure);
        });
    }

    @Test
    void should_get_values_from_map_case_details_service() {
        String caseId = "ccd_id_123";

        TaskToConfigure testTaskToConfigure = new TaskToConfigure(
            "taskId",
            caseId,
            "taskName",
            emptyMap()
        );

        Map<String, Object> expectedValues = Map.of(
            SECURITY_CLASSIFICATION.value(), "PUBLIC",
            CASE_TYPE_ID.value(), "IA");

        when(caseConfigurationProviderService.getCaseRelatedConfiguration(caseId))
            .thenReturn(expectedValues);

        Map<String, Object> values = caseRelatedVariablesConfigurator
            .getConfigurationVariables(testTaskToConfigure);

        assertThat(values, sameInstance(expectedValues));
    }
}
