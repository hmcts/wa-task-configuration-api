package uk.gov.hmcts.reform.wataskconfigurationapi.auth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import uk.gov.hmcts.reform.wataskconfigurationapi.auth.idam.IdamTokenGenerator;
import uk.gov.hmcts.reform.wataskconfigurationapi.auth.idam.entities.Token;
import uk.gov.hmcts.reform.wataskconfigurationapi.auth.idam.entities.UserInfo;
import uk.gov.hmcts.reform.wataskconfigurationapi.clients.IdamServiceApi;

import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(properties = "IA_IDAM_REDIRECT_URI=http://localhost:3002/oauth2/callback")
@ActiveProfiles("integration")
class IdamTokenGeneratorTest {

    private final String bearerAccessToken1 = "some bearer access token";
    @MockBean
    private IdamServiceApi idamServiceApi;

    @Autowired
    private IdamTokenGenerator systemUserIdamToken;

    @Test
    void getUserInfo() {
        when(idamServiceApi.userInfo(anyString())).thenReturn(UserInfo.builder()
                                                                  .uid("some user id")
                                                                  .build());

        systemUserIdamToken.getUserInfo(bearerAccessToken1);
        systemUserIdamToken.getUserInfo(bearerAccessToken1);
        systemUserIdamToken.getUserInfo(bearerAccessToken1);

        String bearerAccessToken2 = "some other bearer access token";
        systemUserIdamToken.getUserInfo(bearerAccessToken2);
        systemUserIdamToken.getUserInfo(bearerAccessToken2);
        systemUserIdamToken.getUserInfo(bearerAccessToken2);

        verify(idamServiceApi, times(1)).userInfo(bearerAccessToken1);
        verify(idamServiceApi, times(1)).userInfo(bearerAccessToken2);
    }

    @Test
    void generate() {
        when(idamServiceApi.token(anyMap())).thenReturn(new Token(bearerAccessToken1, "some scope"));

        String someUsername = "some username";
        String someUserPassword = "some user password";
        systemUserIdamToken.getUserBearerToken(someUsername, someUserPassword);
        systemUserIdamToken.getUserBearerToken(someUsername, someUserPassword);
        systemUserIdamToken.getUserBearerToken(someUsername, someUserPassword);
        systemUserIdamToken.getUserBearerToken(someUsername, someUserPassword);

        String someOtherUsername = "some other username";
        String someOtherUserPassword = "some other user password";
        systemUserIdamToken.getUserBearerToken(someOtherUsername, someOtherUserPassword);
        systemUserIdamToken.getUserBearerToken(someOtherUsername, someOtherUserPassword);


        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "password");
        map.add("redirect_uri", "http://localhost:3002/oauth2/callback");
        map.add("client_id", "wa");
        map.add("client_secret", "something");
        map.add("username", someUsername);
        map.add("password", someUserPassword);
        map.add("scope", "openid profile roles");
        verify(idamServiceApi).token(map);

        MultiValueMap<String, String> map2 = new LinkedMultiValueMap<>();
        map2.add("grant_type", "password");
        map2.add("redirect_uri", "http://localhost:3002/oauth2/callback");
        map2.add("client_id", "wa");
        map2.add("client_secret", "something");
        map2.add("username", someOtherUsername);
        map2.add("password", someOtherUserPassword);
        map2.add("scope", "openid profile roles");
        verify(idamServiceApi).token(map2);
    }
}
