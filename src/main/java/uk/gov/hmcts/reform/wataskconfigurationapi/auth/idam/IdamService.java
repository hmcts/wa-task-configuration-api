package uk.gov.hmcts.reform.wataskconfigurationapi.auth.idam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.wataskconfigurationapi.auth.idam.entities.UserInfo;
import uk.gov.hmcts.reform.wataskconfigurationapi.clients.IdamServiceApi;

import static java.util.Objects.requireNonNull;

//fixme: this class does not seem to be used anywhere. Can it be cleaned up?
@Component
public class IdamService {

    private final IdamServiceApi idamServiceApi;

    @Autowired
    public IdamService(IdamServiceApi idamServiceApi) {
        this.idamServiceApi = idamServiceApi;
    }

    public UserInfo getUserInfo(String accessToken) {
        requireNonNull(accessToken, "access token must not be null");
        return idamServiceApi.userInfo(accessToken);
    }

    public String getUserId(String authToken) {
        UserInfo userInfo = getUserInfo(authToken);
        requireNonNull(userInfo.getUid(), "User id must not be null");
        return userInfo.getUid();
    }
}
