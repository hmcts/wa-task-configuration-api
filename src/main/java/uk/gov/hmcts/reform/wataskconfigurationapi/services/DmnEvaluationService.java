package uk.gov.hmcts.reform.wataskconfigurationapi.services;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.wataskconfigurationapi.clients.CamundaServiceApi;
import uk.gov.hmcts.reform.wataskconfigurationapi.domain.entities.camunda.DecisionTableRequest;
import uk.gov.hmcts.reform.wataskconfigurationapi.domain.entities.camunda.DecisionTableResult;
import uk.gov.hmcts.reform.wataskconfigurationapi.domain.entities.camunda.DmnRequest;

import java.util.List;
import java.util.Locale;

import static uk.gov.hmcts.reform.wataskconfigurationapi.domain.entities.DecisionTable.WA_TASK_CONFIGURATION;
import static uk.gov.hmcts.reform.wataskconfigurationapi.domain.entities.DecisionTable.WA_TASK_PERMISSIONS;
import static uk.gov.hmcts.reform.wataskconfigurationapi.domain.entities.camunda.CamundaValue.jsonValue;

@Slf4j
@Component
public class DmnEvaluationService {

    private final CamundaServiceApi camundaServiceApi;
    private final AuthTokenGenerator serviceAuthTokenGenerator;

    public DmnEvaluationService(CamundaServiceApi camundaServiceApi,
                                AuthTokenGenerator serviceAuthTokenGenerator) {
        this.camundaServiceApi = camundaServiceApi;
        this.serviceAuthTokenGenerator = serviceAuthTokenGenerator;
    }

    public List<DecisionTableResult> evaluateTaskPermissionsDmn(String jurisdiction,
                                                                String caseType,
                                                                String caseData) {
        String decisionTableKey = WA_TASK_PERMISSIONS.getTableKey(jurisdiction, caseType);
        return performEvaluateDmnAction(decisionTableKey, caseData, jurisdiction);
    }

    public List<DecisionTableResult> evaluateTaskConfigurationDmn(String jurisdiction,
                                                                  String caseType,
                                                                  String caseData) {
        String decisionTableKey = WA_TASK_CONFIGURATION.getTableKey(jurisdiction, caseType);
        return performEvaluateDmnAction(decisionTableKey, caseData, jurisdiction);
    }

    private List<DecisionTableResult> performEvaluateDmnAction(String decisionTableKey,
                                                               String caseData,
                                                               String jurisdiction) {
        try {
            return camundaServiceApi.evaluateDmnTable(
                serviceAuthTokenGenerator.generate(),
                decisionTableKey,
                jurisdiction.toLowerCase(Locale.ENGLISH),
                new DmnRequest<>(
                    new DecisionTableRequest(jsonValue(caseData))
                )
            );
        } catch (FeignException e) {
            log.error("Case Configuration : Could not evaluate from decision table '{}'", decisionTableKey);
            throw new IllegalStateException(
                String.format("Could not evaluate from decision table %s", decisionTableKey),
                e
            );
        }
    }

}
