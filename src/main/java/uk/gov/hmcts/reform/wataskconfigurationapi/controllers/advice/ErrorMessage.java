package uk.gov.hmcts.reform.wataskconfigurationapi.controllers.advice;


import org.springframework.http.HttpStatus;

import java.sql.Timestamp;
import java.util.Objects;

public class ErrorMessage {

    private final Timestamp timestamp;
    private final String error;
    private final int status;
    private final String message;

    public ErrorMessage(Exception ex, HttpStatus error, Timestamp timestamp) {
        Objects.requireNonNull(ex, "Exception must not be null");
        Objects.requireNonNull(error, "HttpStatus error must not be null");
        Objects.requireNonNull(timestamp, "Timestamp must not be null");
        this.timestamp = timestamp;
        this.error = error.getReasonPhrase();
        this.status = error.value();
        this.message = ex.getMessage();
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getError() {
        return error;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "ErrorMessage{"
               + "timestamp=" + timestamp
               + ", error='" + error + '\''
               + ", status=" + status
               + ", message='" + message + '\''
               + '}';
    }
}
