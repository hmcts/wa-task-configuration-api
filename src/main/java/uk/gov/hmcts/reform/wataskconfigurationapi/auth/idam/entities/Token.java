package uk.gov.hmcts.reform.wataskconfigurationapi.auth.idam.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.EqualsAndHashCode;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@EqualsAndHashCode
public class Token {

    private String accessToken;
    private String scope;

    private Token() {
        //No-op constructor for deserialization
    }

    public Token(String accessToken, String scope) {
        this.accessToken = accessToken;
        this.scope = scope;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getScope() {
        return scope;
    }
}
