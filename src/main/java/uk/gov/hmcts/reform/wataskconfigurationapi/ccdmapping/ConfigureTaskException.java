package uk.gov.hmcts.reform.wataskconfigurationapi.ccdmapping;

public class ConfigureTaskException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ConfigureTaskException(String message, Throwable cause) {
        super(message, cause);
    }
}
