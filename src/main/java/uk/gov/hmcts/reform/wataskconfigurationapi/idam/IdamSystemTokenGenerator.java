package uk.gov.hmcts.reform.wataskconfigurationapi.idam;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.slf4j.LoggerFactory.getLogger;

@Component
public class IdamSystemTokenGenerator {
    private static final Logger LOG = getLogger(IdamSystemTokenGenerator.class);

    private final String systemUserName;
    private final String systemUserPass;
    private final String idamRedirectUrl;
    private final String systemUserScope;
    private final String idamClientId;
    private final String idamClientSecret;
    private final IdamApi idamApi;
    private final String test;


    public IdamSystemTokenGenerator(
        @Value("${idam.system.username}") String systemUserName,
        @Value("${idam.system.password}") String systemUserPass,
        @Value("${idam.redirectUrl}") String idamRedirectUrl,
        @Value("${idam.system.scope}") String systemUserScope,
        @Value("${spring.security.oauth2.client.registration.oidc.client-id}") String idamClientId,
        @Value("${spring.security.oauth2.client.registration.oidc.client-secret}") String idamClientSecret,
        @Value("${idam.s2s-auth.secret}") String test,
        IdamApi idamApi
    ) {
        this.systemUserName = systemUserName;
        this.systemUserPass = systemUserPass;
        this.idamRedirectUrl = idamRedirectUrl;
        this.systemUserScope = systemUserScope;
        this.idamClientId = idamClientId;
        this.idamClientSecret = idamClientSecret;
        this.idamApi = idamApi;
        this.test = test;
    }

    @SuppressWarnings({
        "PMD.SystemPrintln"
    })
    public String generate() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "password");
        map.add("redirect_uri", idamRedirectUrl);
        map.add("client_id", idamClientId);
        map.add("client_secret", idamClientSecret);
        map.add("username", systemUserName);
        map.add("password", systemUserPass);
        map.add("scope", systemUserScope);
        LOG.info("Trying to connect with \n" + map);
        System.out.println("Trying to connect with \n" + map);
        System.out.println("VAULT or default: " + test);
        Token tokenResponse = idamApi.token(map);

        return tokenResponse.getAccessToken();
    }

    public UserInfo getUserInfo(String accessToken) {
        return idamApi.userInfo(accessToken);
    }
}