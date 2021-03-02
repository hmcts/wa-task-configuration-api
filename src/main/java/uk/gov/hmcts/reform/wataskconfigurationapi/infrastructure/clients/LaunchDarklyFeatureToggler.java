package uk.gov.hmcts.reform.wataskconfigurationapi.infrastructure.clients;

import com.launchdarkly.client.LDClientInterface;
import com.launchdarkly.client.LDUser;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.wataskconfigurationapi.domain.entities.UserDetails;
import uk.gov.hmcts.reform.wataskconfigurationapi.domain.service.FeatureToggler;

@Service
public class LaunchDarklyFeatureToggler implements FeatureToggler {

    private LDClientInterface ldClient;
    private UserDetails userDetails;

    public LaunchDarklyFeatureToggler(LDClientInterface ldClient,
                                      UserDetails userDetails) {
        this.ldClient = ldClient;
        this.userDetails = userDetails;
    }

    public boolean getValue(String key, Boolean defaultValue) {

        return ldClient.boolVariation(
            key,
            new LDUser.Builder(userDetails.getId())
                .firstName(userDetails.getForename())
                .lastName(userDetails.getSurname())
                .email(userDetails.getEmailAddress())
                .build(),
            defaultValue
        );
    }

}
