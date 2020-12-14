package uk.gov.hmcts.reform.wataskconfigurationapi.domain.entities.ccd;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.wataskconfigurationapi.clients.CcdDataServiceApi;
import uk.gov.hmcts.reform.wataskconfigurationapi.domain.entities.idam.IdamSystemTokenGenerator;

@Component
public class CcdDataService {
    private final CcdDataServiceApi ccdDataServiceApi;
    private final AuthTokenGenerator serviceAuthTokenGenerator;
    private final IdamSystemTokenGenerator systemTokenGenerator;

    public CcdDataService(
        CcdDataServiceApi ccdDataServiceApi,
        AuthTokenGenerator serviceAuthTokenGenerator,
        IdamSystemTokenGenerator systemTokenGenerator
    ) {
        this.ccdDataServiceApi = ccdDataServiceApi;
        this.serviceAuthTokenGenerator = serviceAuthTokenGenerator;
        this.systemTokenGenerator = systemTokenGenerator;
    }

    public String getCaseData(String caseId) {
        return ccdDataServiceApi.getCase(
            systemTokenGenerator.generate(),
            serviceAuthTokenGenerator.generate(),
            caseId
        );
    }
}
