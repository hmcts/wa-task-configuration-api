package uk.gov.hmcts.reform.wataskconfigurationapi.ccdmapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.wataskconfigurationapi.ccd.CaseDetails;
import uk.gov.hmcts.reform.wataskconfigurationapi.ccd.CcdDataService;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;
import static uk.gov.hmcts.reform.wataskconfigurationapi.ccdmapping.CamundaValue.jsonValue;

@Component
public class MapCaseDetailsService {
    private final CcdDataService ccdDataService;
    private final CamundaClient camundaClient;

    public MapCaseDetailsService(CcdDataService ccdDataService, CamundaClient camundaClient) {
        this.ccdDataService = ccdDataService;
        this.camundaClient = camundaClient;
    }

    public Map<String, Object> getMappedDetails(String ccdId) {
        String caseData = ccdDataService.getCaseData(ccdId);

        try {
            CaseDetails caseDetails = new ObjectMapper().readValue(caseData, CaseDetails.class);
            List<MapCaseDataDmnResult> mapCaseDataDmnResults = camundaClient.mapCaseData(
                caseDetails.getJurisdiction(),
                caseDetails.getCaseTypeId(),
                new DmnRequest<>(
                    new MapCaseDataDmnRequest(jsonValue(caseData))
                )
            );

            return mapCaseDataDmnResults.stream().collect(toMap(
                mapCaseDataDmnResult -> mapCaseDataDmnResult.getName().getValue(),
                mapCaseDataDmnResult -> mapCaseDataDmnResult.getValue().getValue()
            ));
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Cannot parse result from CCD for [" + ccdId + "]", e);
        }
    }
}
