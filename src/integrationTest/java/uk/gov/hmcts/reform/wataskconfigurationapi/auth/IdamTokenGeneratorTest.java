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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("integration")
class IdamTokenGeneratorTest {

    @MockBean
    private IdamServiceApi idamServiceApi;

    @Autowired
    private IdamTokenGenerator systemUserIdamToken;

    @Test
    void getUserInfo() {
        when(idamServiceApi.userInfo(anyString())).thenReturn(UserInfo.builder()
                                                                  .uid("some user id")
                                                                  .build());

        UserInfo actualUserInfo = systemUserIdamToken.getUserInfo("some Bearer token");
        systemUserIdamToken.getUserInfo("some Bearer token");
        systemUserIdamToken.getUserInfo("some Bearer token");
        systemUserIdamToken.getUserInfo("some Bearer token2");
        systemUserIdamToken.getUserInfo("some Bearer token2");
        systemUserIdamToken.getUserInfo("some Bearer token2");
        systemUserIdamToken.getUserInfo("some Bearer token2");

        assertThat(actualUserInfo).isEqualTo(UserInfo.builder()
                                                 .uid("some user id")
                                                 .build());

        verify(idamServiceApi, times(1)).userInfo("some Bearer token");
        verify(idamServiceApi, times(1)).userInfo("some Bearer token2");
    }

    @Test
    void generate() {
        when(idamServiceApi.token(anyMap()))
            .thenReturn(new Token("some user token", "some scope"));

        systemUserIdamToken.getUserBearerToken(
            "some username",
            "some user password"
        );
        systemUserIdamToken.getUserBearerToken(
            "some username",
            "some user password"
        );
        systemUserIdamToken.getUserBearerToken(
            "some username",
            "some user password"
        );
        systemUserIdamToken.getUserBearerToken(
            "some username",
            "some user password"
        );

        systemUserIdamToken.getUserBearerToken(
            "some other username",
            "some other user password"
        );
        systemUserIdamToken.getUserBearerToken(
            "some other username",
            "some other user password"
        );

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "password");
        map.add("redirect_uri", "http://localhost:3451/oauth2redirect");
        map.add("client_id", "wa");
        map.add("client_secret", "something");
        map.add("username", "some username");
        map.add("password", "some user password");
        map.add("scope", "openid profile roles");

        verify(idamServiceApi).token(map);

        MultiValueMap<String, String> map2 = new LinkedMultiValueMap<>();
        map2.add("grant_type", "password");
        map2.add("redirect_uri", "http://localhost:3451/oauth2redirect");
        map2.add("client_id", "wa");
        map2.add("client_secret", "something");
        map2.add("username", "some other username");
        map2.add("password", "some other user password");
        map2.add("scope", "openid profile roles");

        verify(idamServiceApi).token(map2);
    }
}
