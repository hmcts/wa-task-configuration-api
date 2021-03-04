package uk.gov.hmcts.reform.wataskconfigurationapi.util;

import com.launchdarkly.sdk.LDUser;
import com.launchdarkly.sdk.server.interfaces.LDClientInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LaunchDarklyFunctionalTestClient {

    @Autowired
    private LDClientInterface ldClient;

    public boolean getKey(String key) {

        ldClient = new LDClient(key);

        LDUser ldUser =  new LDUser.Builder("wa-task-configuration-api")
            .build();

        return ldClient.boolVariation("tester", ldUser, false);
    }
}