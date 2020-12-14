package uk.gov.hmcts.reform.wataskconfigurationapi.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.wataskconfigurationapi.clients.CamundaServiceApi;
import uk.gov.hmcts.reform.wataskconfigurationapi.domain.entities.camunda.DecisionTableRequest;
import uk.gov.hmcts.reform.wataskconfigurationapi.domain.entities.camunda.DecisionTableResult;
import uk.gov.hmcts.reform.wataskconfigurationapi.domain.entities.camunda.DmnRequest;
import uk.gov.hmcts.reform.wataskconfigurationapi.domain.entities.ccd.CcdDataService;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;
import static uk.gov.hmcts.reform.wataskconfigurationapi.domain.entities.camunda.CamundaValue.jsonValue;

@Component
public class MapCaseDetailsService {
    public static final String WA_TASK_CONFIGURATION_DECISION_TABLE_NAME = "wa-task-configuration";
    private final CcdDataService ccdDataService;
    private final CamundaServiceApi camundaServiceApi;
    private final PermissionsService permissionsService;
    private final AuthTokenGenerator serviceAuthTokenGenerator;

    public MapCaseDetailsService(CcdDataService ccdDataService,
                                 CamundaServiceApi camundaServiceApi,
                                 PermissionsService permissionsService,
                                 AuthTokenGenerator serviceAuthTokenGenerator) {
        this.ccdDataService = ccdDataService;
        this.camundaServiceApi = camundaServiceApi;
        this.permissionsService = permissionsService;
        this.serviceAuthTokenGenerator = serviceAuthTokenGenerator;
    }

    public Map<String, Object> getMappedDetails(String caseId) {
        String caseData = ccdDataService.getCaseData(caseId);

        try {
            CaseDetails caseDetails = new ObjectMapper().readValue(caseData, CaseDetails.class);

            String jurisdiction = caseDetails.getJurisdiction();
            String caseType = caseDetails.getCaseTypeId();

            List<DecisionTableResult> decisionTableResults = camundaServiceApi.evaluateDmnTable(
                serviceAuthTokenGenerator.generate(),
                WA_TASK_CONFIGURATION_DECISION_TABLE_NAME,
                jurisdiction.toLowerCase(Locale.getDefault()),
                caseType.toLowerCase(Locale.getDefault()),
                new DmnRequest<>(
                    new DecisionTableRequest(jsonValue(caseData))
                )
            );

            List<DecisionTableResult> permissionsDmnResults =
                permissionsService.getMappedDetails(jurisdiction, caseType, caseData);

            Map<String, Object> mappedCaseDetails =
                Stream.concat(decisionTableResults.stream(), permissionsDmnResults.stream())
                    .collect(toMap(
                        mapCaseDataDmnResult -> mapCaseDataDmnResult.getName().getValue(),
                        mapCaseDataDmnResult -> mapCaseDataDmnResult.getValue().getValue()
                    ));

            HashMap<String, Object> allMappedDetails = new HashMap<>(mappedCaseDetails);
            allMappedDetails.put("securityClassification", caseDetails.getSecurityClassification().toString());
            allMappedDetails.put("caseType", caseDetails.getCaseTypeId());
            return allMappedDetails;
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Cannot parse result from CCD for [" + caseId + "]", e);
        }
    }
}