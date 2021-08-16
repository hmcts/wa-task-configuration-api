package uk.gov.hmcts.reform.wataskconfigurationapi.auth.idam;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import uk.gov.hmcts.reform.wataskconfigurationapi.auth.idam.entities.Token;
import uk.gov.hmcts.reform.wataskconfigurationapi.auth.idam.entities.UserIdamTokenGeneratorInfo;
import uk.gov.hmcts.reform.wataskconfigurationapi.auth.idam.entities.UserInfo;
import uk.gov.hmcts.reform.wataskconfigurationapi.clients.IdamServiceApi;

@CacheConfig(cacheNames = {"idamDetails"})
public class IdamTokenGenerator {

    private final UserIdamTokenGeneratorInfo userIdamTokenGeneratorInfo;
    private final IdamServiceApi idamServiceApi;

    public IdamTokenGenerator(UserIdamTokenGeneratorInfo userIdamTokenGeneratorInfo,
                              IdamServiceApi idamServiceApi) {
        this.userIdamTokenGeneratorInfo = userIdamTokenGeneratorInfo;
        this.idamServiceApi = idamServiceApi;
    }

    public String generate() {
        return getUserBearerToken(
            userIdamTokenGeneratorInfo.getUserName(),
            userIdamTokenGeneratorInfo.getUserPassword()
        );
    }

    @Cacheable
    public String getUserBearerToken(String username, String password) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "password");
        map.add("redirect_uri", userIdamTokenGeneratorInfo.getIdamRedirectUrl());
        map.add("client_id", userIdamTokenGeneratorInfo.getIdamClientId());
        map.add("client_secret", userIdamTokenGeneratorInfo.getIdamClientSecret());
        map.add("username", username);
        map.add("password", password);
        map.add("scope", userIdamTokenGeneratorInfo.getIdamScope());
        Token tokenResponse = idamServiceApi.token(map);

        return "Bearer " + tokenResponse.getAccessToken();
    }

    @Cacheable
    public UserInfo getUserInfo(String bearerAccessToken) {
        return idamServiceApi.userInfo(bearerAccessToken);
    }
}
