package uk.gov.hmcts.reform.wataskconfigurationapi.services;

import feign.FeignException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.wataskconfigurationapi.clients.CamundaServiceApi;
import uk.gov.hmcts.reform.wataskconfigurationapi.domain.entities.camunda.AddLocalVariableRequest;
import uk.gov.hmcts.reform.wataskconfigurationapi.domain.entities.camunda.CamundaValue;
import uk.gov.hmcts.reform.wataskconfigurationapi.domain.entities.camunda.TaskResponse;
import uk.gov.hmcts.reform.wataskconfigurationapi.exceptions.ConfigureTaskException;
import uk.gov.hmcts.reform.wataskconfigurationapi.services.configurators.TaskConfigurator;

import java.util.Collections;
import java.util.HashMap;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ConfigureTaskServiceTest {

    private CamundaServiceApi camundaServiceApi;
    private ConfigureTaskService configureTaskService;
    private TaskConfigurator taskVariableExtractor;

    private static final String BEARER_SERVICE_TOKEN = "Bearer service token";

    @BeforeEach
    void setup() {
        camundaServiceApi = mock(CamundaServiceApi.class);
        taskVariableExtractor = mock(TaskConfigurator.class);
        AuthTokenGenerator authTokenGenerator = mock(AuthTokenGenerator.class);
        configureTaskService = new ConfigureTaskService(
            camundaServiceApi,
            Collections.singletonList(taskVariableExtractor),
            authTokenGenerator
        );

        when(authTokenGenerator.generate()).thenReturn(BEARER_SERVICE_TOKEN);
    }

    @Test
    void canConfigureATaskWithVariables() {
        String taskId = "taskId";
        String processInstanceId = "processInstanceId";
        TaskResponse taskResponse = new TaskResponse("id", processInstanceId, "taskName");
        when(camundaServiceApi.getTask(BEARER_SERVICE_TOKEN, taskId)).thenReturn(taskResponse);

        HashMap<String, CamundaValue<Object>> processVariables = new HashMap<>();
        String caseId = "someCcdValue";
        processVariables.put("caseId", new CamundaValue<>(caseId, "String"));
        when(camundaServiceApi.getProcessVariables(BEARER_SERVICE_TOKEN, processInstanceId))
            .thenReturn(processVariables);

        HashMap<String, Object> mappedValues = new HashMap<>();
        mappedValues.put("key1", "value1");
        mappedValues.put("key2", "value2");
        when(taskVariableExtractor.getConfigurationVariables(taskResponse, processVariables)).thenReturn(mappedValues);

        configureTaskService.configureTask(taskId);

        HashMap<String, CamundaValue<String>> modifications = new HashMap<>();
        modifications.put("key1", CamundaValue.stringValue("value1"));
        modifications.put("key2", CamundaValue.stringValue("value2"));
        verify(camundaServiceApi).addLocalVariablesToTask(
            BEARER_SERVICE_TOKEN,
            taskId,
            new AddLocalVariableRequest(modifications)
        );
    }

    @Test
    void canConfigureATaskWithNoExtraVariables() {
        String taskId = "taskId";
        String processInstanceId = "processInstanceId";
        TaskResponse taskResponse = new TaskResponse("id", processInstanceId, "taskName");
        when(camundaServiceApi.getTask(BEARER_SERVICE_TOKEN, taskId)).thenReturn(taskResponse);
        HashMap<String, CamundaValue<Object>> processVariables = new HashMap<>();
        String caseId = "someCcdValue";
        processVariables.put("caseId", new CamundaValue<>(caseId, "String"));
        when(camundaServiceApi.getProcessVariables(BEARER_SERVICE_TOKEN, processInstanceId))
            .thenReturn(processVariables);
        HashMap<String, Object> mappedValues = new HashMap<>();
        when(taskVariableExtractor.getConfigurationVariables(taskResponse, processVariables)).thenReturn(mappedValues);

        configureTaskService.configureTask(taskId);

        HashMap<String, CamundaValue<String>> modifications = new HashMap<>();
        verify(camundaServiceApi).addLocalVariablesToTask(
            BEARER_SERVICE_TOKEN,
            taskId,
            new AddLocalVariableRequest(modifications)
        );
    }

    @Test
    void tryToConfigureATaskThatDoesNotExist() {
        String taskIdThatDoesNotExist = "doesNotExist";
        FeignException.NotFound notFound = mock(FeignException.NotFound.class);
        when(camundaServiceApi.getTask(BEARER_SERVICE_TOKEN, taskIdThatDoesNotExist)).thenThrow(notFound);

        Assertions.assertThrows(ConfigureTaskException.class, () -> {
            configureTaskService.configureTask(taskIdThatDoesNotExist);
        });
    }
}
