package uk.gov.hmcts.reform.wataskconfigurationapi.services.configurators;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.wataskconfigurationapi.domain.entities.configuration.TaskToConfigure;

import java.util.Map;

import static java.util.Collections.emptyMap;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static uk.gov.hmcts.reform.wataskconfigurationapi.domain.entities.camunda.enums.CamundaVariableDefinition.CASE_ID;
import static uk.gov.hmcts.reform.wataskconfigurationapi.domain.entities.camunda.enums.CamundaVariableDefinition.TASK_TYPE;
import static uk.gov.hmcts.reform.wataskconfigurationapi.domain.entities.camunda.enums.CamundaVariableDefinition.TITLE;

class RequiredProcessVariableConfiguratorTest {

    private RequiredVariablesConfigurator requiredVariablesConfigurator;

    @BeforeEach
    void setUp() {
        requiredVariablesConfigurator = new RequiredVariablesConfigurator();
    }

    @Test
    void should_add_variables() {

        TaskToConfigure testTaskToConfigure = new TaskToConfigure(
            "taskId",
            "CASE_ID_123",
            "taskName",
            Map.of("taskId", "aTaskId")
        );

        Map<String, Object> values = requiredVariablesConfigurator.getConfigurationVariables(testTaskToConfigure);

        assertThat(values.size(), is(3));
        assertThat(values.get(CASE_ID.value()), is("CASE_ID_123"));
        assertThat(values.get(TITLE.value()), is("taskName"));
        assertThat(values.get(TASK_TYPE.value()), is("aTaskId"));
    }

    @Test
    void should_throw_exception_when_case_id_is_null() {

        TaskToConfigure testTaskToConfigure = new TaskToConfigure(
            "taskId",
            null,
            "taskName",
            Map.of("taskId", "aTaskId")
        );

        Exception exception = assertThrows(NullPointerException.class, () -> {
            requiredVariablesConfigurator.getConfigurationVariables(testTaskToConfigure);
        });

        assertEquals(
            "Task with id 'taskId' cannot be configured it has not been setup correctly. "
            + "No 'caseId' process variable.",
            exception.getMessage()
        );
    }

    @Test
    void should_throw_exception_when_task_id_process_variable_is_null() {

        TaskToConfigure testTaskToConfigure = new TaskToConfigure(
            "taskId",
            "CASE_ID_123",
            "taskName",
            emptyMap()
        );

        Exception exception = assertThrows(NullPointerException.class, () -> {
            requiredVariablesConfigurator.getConfigurationVariables(testTaskToConfigure);
        });


        assertEquals(
            "Task with id 'taskId' cannot be configured it has not been setup correctly. "
            + "No 'taskId' process variable.",
            exception.getMessage()
        );
    }
}
