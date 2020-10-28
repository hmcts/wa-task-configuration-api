package uk.gov.hmcts.reform.wataskconfigurationapi.thirdparty.ccd;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.wataskconfigurationapi.thirdparty.idam.IdamApi;
import uk.gov.hmcts.reform.wataskconfigurationapi.thirdparty.idam.IdamSystemTokenGenerator;
import uk.gov.hmcts.reform.wataskconfigurationapi.thirdparty.idam.Token;

import java.util.Map;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@RunWith(MockitoJUnitRunner.class)
public class CcdDataServiceTest {

    @Mock
    CcdClient ccdClient;

    @Mock
    IdamApi idamApi;

    @Mock
    AuthTokenGenerator authTokenGenerator;

    @Mock
    IdamSystemTokenGenerator idamSystemTokenGenerator;

    private CcdDataService ccdDataService;

    @Before
    public void setUp(){
        ccdDataService = new CcdDataService(ccdClient, authTokenGenerator, idamSystemTokenGenerator);
    }

    @Test
    public void should_get_case_data(){
        String ccdId = UUID.randomUUID().toString();
        String userToken = "user_token";
        String serviceToken = "service_token";

        String caseData = "{ "
            + "\"jurisdiction\": \"ia\", "
            + "\"case_type_id\": \"Asylum\", "
            + "\"security_classification\": \"PUBLIC\","
            + "\"data\": {}"
            + " }";

        when(idamSystemTokenGenerator.generate()).thenReturn(userToken);
        when(authTokenGenerator.generate()).thenReturn(serviceToken);

        when(ccdClient.getCase("Bearer " + userToken, serviceToken, ccdId)).thenReturn(caseData);

        String actualCaseData = ccdDataService.getCaseData(ccdId);

        assertEquals(actualCaseData, caseData);
    }
}
